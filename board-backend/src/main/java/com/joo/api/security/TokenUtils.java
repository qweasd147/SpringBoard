package com.joo.api.security;

import com.joo.api.security.custom.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 차후 상수 값이나 그 외 값들이 spring과 연관 관계가 있을 수 있어서
 * static이 아닌 spring에서 관리하게 함
 */
@Component
public class TokenUtils {

    private static final Logger logger = LoggerFactory.getLogger(TokenUtils.class);

    private final static String secret = "wngudWkdWkd";
    public final static Integer expiration = 86400;        //기간. 단위 초 => 하루

    public enum TOKEN_STATUS {
        ENABLED, EXPIRED, INVALID
    }

    /**
     * 외부(naver, kakao 등)에서 사용 될 token
     */
    private final static String THIRDPARTY_TOKEN = "thirdpartyToken";

    public String getUsernameFromToken(String token) {
        return this.getUsernameFromToken(this.getClaimsFromToken(token));
    }

    public String getUsernameFromToken(final Claims claims) {
        String username;
        try {
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public String getPasswordFromToken(String token) {
        return this.getPasswordFromToken(this.getClaimsFromToken(token));
    }

    public String getPasswordFromToken(final Claims claims) {
        String password;
        try {
            password = (String) claims.get("password");
        } catch (Exception e) {
            password = null;
        }
        return password;
    }

    public String getThirdPartyTokenFromToken(String token) {
        return this.getThirdPartyTokenFromToken(this.getClaimsFromToken(token));
    }

    public String getThirdPartyTokenFromToken(final Claims claims) {
        String thirdpartyToken;
        try {
            thirdpartyToken = (String) claims.get(THIRDPARTY_TOKEN);
        } catch (Exception e) {
            thirdpartyToken = null;
        }
        return thirdpartyToken;
    }

    /**
     * 토큰에서 생성 날짜를 파싱하여 반환한다.
     * @param token
     * @return
     */
    public Date getCreatedDateFromToken(String token) {
        return this.getCreatedDateFromToken(this.getClaimsFromToken(token));
    }

    /**
     * 토큰에서 생성 날짜를 파싱하여 반환한다.
     * @param claims
     * @return
     */
    public Date getCreatedDateFromToken(final Claims claims) {
        Date created;
        try {
            created = new Date((Long) claims.get("created"));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    /**
     * 토큰에서 만료날짜를 가져온다.
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        return this.getCreatedDateFromToken(this.getClaimsFromToken(token));
    }

    /**
     * 토큰에서 만료날짜를 가져온다.
     * @param claims
     * @return
     */
    public Date getExpirationDateFromToken(final Claims claims) {
        return claims.getExpiration();
    }

    public String getAudienceFromToken(String token) {
        return this.getAudienceFromToken(this.getClaimsFromToken(token));
    }

    public String getAudienceFromToken(final Claims claims) {
        String audience;
        try {
            audience = (String) claims.get("audience");
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }

    /**
     * 토큰을 파싱 할수 있는 객체(Claims)로 만든다.
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(this.secret)
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 현재 날짜를 만든다.
     * @return
     */
    private Date createCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 만료일 날짜 객체를 만든다.
     * @return
     */
    private Date createExpirationDate() {
        return new Date(System.currentTimeMillis() + this.expiration * 1000);
    }

    /**
     * 토큰이 만료되었는지 검사한다.
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        final Claims claims = this.getClaimsFromToken(token);

        final Date expireDate = claims.getExpiration();
        final Date createDate = new Date((Long) claims.get("created"));

        // 만기일 < 생성일 or 현재시간 < 만기일  ==> 토큰 만료
        return expireDate.before(createDate) || this.createCurrentDate().before(expireDate);
    }

    private Boolean isCreatedBeforeLastPasswordReset(
            Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    /**
     * 토큰을 생성한다.
     * @param userDetails
     * @return
     */
    public String createToken(CustomUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userDetails.getUsername());
        claims.put("created", this.createCurrentDate());
        claims.put(THIRDPARTY_TOKEN, userDetails.getThirdPartyToken());
        //claims.put("password", userDetails.getPassword());
        return this.createToken(claims);
    }

    private String createToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims)
                .setExpiration(this.createExpirationDate())
                .signWith(SignatureAlgorithm.HS512, this.secret).compact();
    }

    public Boolean isTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = this.getCreatedDateFromToken(token);
        return (!(this.isCreatedBeforeLastPasswordReset(created,
                lastPasswordReset)) && !(this.isTokenExpired(token)));
    }

    public String createRefreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = this.getClaimsFromToken(token);

            claims.put("created", this.createCurrentDate());
            claims.setId(UUID.randomUUID().toString());
            claims.setExpiration(new Date(System.currentTimeMillis() + this.expiration * 30 * 1000));   //30일

            refreshedToken = Jwts.builder().setClaims(claims)
                    .signWith(SignatureAlgorithm.HS512, this.secret).compact();
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 토큰의 유효기간을 넘겨버린다.
     * @param token
     * @return
     */
    public String deleteToken(String token) {
        String deletedToken;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            claims.put("created", new Date(System.currentTimeMillis() + this.expiration * 2000));
            deletedToken = this.createToken(claims);
        } catch (Exception e) {
            deletedToken = null;
        }
        return deletedToken;
    }

    /**
     * 토큰이 사용 가능한지 검사한다.
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails) {

        TOKEN_STATUS tokenStatus = getTokenStatus(token, userDetails);
        return tokenStatus == TOKEN_STATUS.ENABLED;
    }

    /**
     * 토큰 상태값을 가져온다.
     * @param token
     * @param userDetails
     * @return
     */
    public TOKEN_STATUS getTokenStatus(String token, UserDetails userDetails){
        if(token == null || userDetails == null)    return TOKEN_STATUS.INVALID;

        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;

        final Claims claims = this.getClaimsFromToken(token);
        final String userNameFromToken = this.getUsernameFromToken(claims);
        //final String password = this.getPasswordFromToken(claims);

        if(StringUtils.isEmpty(userNameFromToken))  return TOKEN_STATUS.INVALID;

        if(this.isTokenExpired(token))  return TOKEN_STATUS.EXPIRED;
        if(userNameFromToken.equals(customUserDetails.getUsername()))   return TOKEN_STATUS.ENABLED;        //비밀번호도 할꺼면 여기서 userDetails랑 토큰에서 뽑아서 검사해야함

        return TOKEN_STATUS.INVALID;
    }
}

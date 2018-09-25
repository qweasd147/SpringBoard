package com.joo.api.security;

import com.joo.api.security.custom.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 차후 상수 값이나 그 외 값들이 spring과 연관 관계가 있을 수 있어서
 * static이 아닌 spring에서 관리하게 함
 */
@Component
public class TokenUtils {

    private static final Logger logger = LoggerFactory.getLogger(TokenUtils.class);

    private final static String secret = "wngudWkdWkd";
    public final static Integer expiration = 86400;        //기간. 단위 초 => 하루

    /**
     * 외부(naver, kakao 등)에서 사용 될 token
     */
    private final static String THIRDPARTY_TOKEN = "thirdpartyToken";

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public String getPasswordFromToken(String token) {
        String password;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            password = (String) claims.get("password");
        } catch (Exception e) {
            password = null;
        }
        return password;
    }

    public String getThirdPartyTokenFromToken(String token) {
        String thirdpartyToken;
        try {
            final Claims claims = this.getClaimsFromToken(token);
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
        Date created;
        try {
            final Claims claims = this.getClaimsFromToken(token);
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
        Date expiration;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = this.getClaimsFromToken(token);
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
        final Date expiration = this.getExpirationDateFromToken(token);
        return expiration.before(this.createCurrentDate());
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

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = this.getCreatedDateFromToken(token);
        return (!(this.isCreatedBeforeLastPasswordReset(created,
                lastPasswordReset)) && !(this.isTokenExpired(token)));
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            claims.put("created", this.createCurrentDate());
            refreshedToken = this.createToken(claims);
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

    public Boolean validateToken(String token, UserDetails userDetails) {
        CustomUserDetails user = (CustomUserDetails) userDetails;
        final String username = this.getUsernameFromToken(token);
        //final String password = this.getPasswordFromToken(token);
        return (username.equals(user.getUsername())
                && !(this.isTokenExpired(token))
                //&& password.equals(user.getPassword())    비밀번호 직접 관리 안함
        );
    }
}

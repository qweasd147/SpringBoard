package com.joo.security;

import com.joo.model.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static com.joo.security.TokenUtils.TOKEN_STATUS;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(TokenFilter.class);

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    private final String tokenHeader;
    private final String TOKEN_PREFIX = "Bearer ";

    public TokenFilter(@Value("jwt.header")String tokenHeader) {
        this.tokenHeader = tokenHeader;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {

        String authHeader = request.getHeader(tokenHeader);
        String token = null;

        if(!StringUtils.isEmpty(authHeader) && authHeader.startsWith(TOKEN_PREFIX)){
            token = authHeader.substring(TOKEN_PREFIX.length());
        }else{
            logger.debug("인증 헤더가 없음");
        }

        if(token != null && SecurityContextHolder.getContext().getAuthentication() == null){
            //토큰 정보가 있지만 spring context에 올려져 있지 않을 시 올려놓는다.
            setAuthInfo(request, response, token);
        }else {
            logger.debug("사용자 정보 없음");
        }
        filterChain.doFilter(request, response);
    }

    public void setAuthInfo(HttpServletRequest request, HttpServletResponse response, String token) throws IOException {

        String userName = tokenUtils.getUsernameFromToken(token);
        String thirdPartyToken = tokenUtils.getThirdPartyTokenFromToken(token);

        Objects.requireNonNull(userName, "not found user name from token. "+token);
        if(Objects.isNull(thirdPartyToken)) logger.debug("not found third party token from token. "+token);

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(userName);
        TOKEN_STATUS tokenStatus = tokenUtils.getTokenStatus(token, userDetails);

        Objects.requireNonNull(tokenStatus, "not found token status from token. "+token);

        UserDto userDto = userDetails.toBuilder()
                .thirdPartyToken(thirdPartyToken)
                .state(tokenStatus.getUserStateFromTokenStatus())
                .build();

        userDetails = new CustomUserDetails(userDto);

        if(tokenStatus == TOKEN_STATUS.ENABLED){
            logger.debug("pass validate");
            //토큰 유효성을 체크한다.
            UsernamePasswordAuthenticationToken authToken
                    = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            //사용자 정보를 spring context에 올려 놓는다.
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else if(tokenStatus == TOKEN_STATUS.EXPIRED){
            response.sendError(401);
        } else{
            logger.debug("validate 통과 실패! "+userDetails.toString());
        }
    }
}

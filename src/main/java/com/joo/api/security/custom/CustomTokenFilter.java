package com.joo.api.security.custom;

import com.joo.api.security.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomTokenFilter extends OncePerRequestFilter{

    private static final Logger logger = LoggerFactory.getLogger(CustomTokenFilter.class);

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    private final String tokenHeader;

    public CustomTokenFilter(@Value("#{appProperty['jwt.header']}")String tokenHeader) {
        this.tokenHeader = tokenHeader;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {

        String token = request.getHeader(tokenHeader);
        String userName = tokenUtils.getUsernameFromToken(token);

        if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
            //토큰 정보가 있지만 spring memory에는 올려져 있지 않을 시 올려놓는다.
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            if(tokenUtils.validateToken(token, userDetails)){
                logger.debug("pass validate");
                //토큰 유효성을 체크한다.
                UsernamePasswordAuthenticationToken authToken
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                //사용자 정보를 spring context에 올려 놓는다.
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }else{
                logger.debug("validate 통과 실패! "+userDetails.toString());
            }
        }else {
            logger.debug("사용자 정보 없음");
        }
        filterChain.doFilter(request, response);
    }
}

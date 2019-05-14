package com.joo.security.oauth.client;

import com.joo.model.dto.UserDto;
import com.joo.model.entity.UserEntity;
import com.joo.security.CustomUserDetails;
import com.joo.security.TokenUtils;
import com.joo.service.UserService;
import com.joo.utils.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler {

    @Value("${security.token.expiration}")
    private Integer expiration;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${redirect.domain}")
    private String redirectDomainAfterLogin;

    private final UserService userService;
    private final TokenUtils tokenUtils;

    public AuthenticationSuccessHandler handle(OAuth2RestTemplate template, ClientResourceDetails clientDetails){

        //TODO : token 만들때랑 user를 DB에 입력할때 선형으로 요구됨
        //user insert 시 context에서 create user name이 요구됨

        //user name을 context에 올려 놓을 때
        //user insert 시 생성되는 index(user key)가 함께 올려놓는게 차후 버그 예방하는데 좋음

        return (request, response, authentication) -> {
            Map<String, Object> userDetailsMap = (Map<String, Object>) ((OAuth2Authentication) authentication).getUserAuthentication().getDetails();

            String accessToken = template.getAccessToken().getValue();
            UserDto userDto = clientDetails.makeUserDto(userDetailsMap, accessToken);

            CustomUserDetails userDetails = new CustomUserDetails(userDto);
            UsernamePasswordAuthenticationToken authToken
                    = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            //사용자 정보를 spring context에 올려 놓는다.
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            //insert user in DB
            UserEntity userEntityFromDB = userService.insertIfNotExist(userDto);
            userDto = UserDto.of(userEntityFromDB);

            userDetails = new CustomUserDetails(userDto);
            String jwtStr = tokenUtils.createToken(userDetails);

            /*
            response.setHeader("Authorization", jwtToken);
            response.setStatus(HttpStatus.NO_CONTENT.value());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            */

            //CookieUtils.setCookie(tokenHeader, jwtToken, TOKEN_EXPIRATION);
            Cookie cookie = CookieUtils.makeCookie(tokenHeader, jwtStr, "/", expiration);

            response.addCookie(cookie);
            response.sendRedirect(redirectDomainAfterLogin);
        };
    }
}

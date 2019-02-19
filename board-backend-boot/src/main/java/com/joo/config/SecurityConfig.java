package com.joo.config;

import com.joo.model.dto.UserDto;
import com.joo.security.CustomUserDetails;
import com.joo.security.EntryPointHandler;
import com.joo.security.TokenFilter;
import com.joo.security.TokenUtils;
import com.joo.security.oauth.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.*;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private EntryPointHandler entryPointHandler;

    @Autowired
    private TokenFilter tokenFilter;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    ClientResourceHandler clientResourceHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //토큰 방식으로도 충분하므로 csrf 공격은 생각안해도됨(disable)
        http.csrf().disable()
                .headers().cacheControl().disable().and()
                .exceptionHandling().authenticationEntryPoint(this.entryPointHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()   //사용자의 쿠키에 세션을 저장하지 않겠다
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()                     //Preflight 요청은 따로 제한 x
                .antMatchers("/api/authen/logout/").authenticated()                  //로그아웃은 인증된 사용자여야함
                .antMatchers("/api/authen/login/page/**").permitAll()                //외부 로그인 요청페이지
                .antMatchers("/api/authen/login/**/callback").permitAll()           //로그인 처리 로직. 보안관련은 내부에서 검사함
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/**").authenticated()
                .anyRequest().denyAll();

        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(oauth2Filter(), BasicAuthenticationFilter.class);
        http.cors().configurationSource(request -> getCorsConfigurationSource());
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    private Filter oauth2Filter() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = clientResourceHandler.oauth2Filters();
        filter.setFilters(filters);
        return filter;
    }

    @Bean
    CorsConfiguration getCorsConfigurationSource() {
        final List<String> allowedHeaders = Arrays.asList(
                HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS
                , HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS
                , HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN
                , HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS
                , HttpHeaders.ORIGIN
                , HttpHeaders.CACHE_CONTROL
                , HttpHeaders.CONTENT_TYPE
                , HttpHeaders.AUTHORIZATION);

        final List<String> allowedMethods = Arrays.asList(
                HttpMethod.GET.name()
                , HttpMethod.POST.name()
                , HttpMethod.PUT.name()
                , HttpMethod.DELETE.name()
                , HttpMethod.PATCH.name()
                , HttpMethod.OPTIONS.name()
        );

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(allowedHeaders);
        configuration.setAllowedMethods(allowedMethods);
        configuration.setMaxAge(3600L);

        return configuration;
    }
}
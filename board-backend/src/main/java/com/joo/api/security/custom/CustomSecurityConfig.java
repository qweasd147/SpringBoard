package com.joo.api.security.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private CustomEntryPointHandler customEntryPointHandler;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomTokenFilter customTokenFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(shaPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web){
        web.ignoring()
                .antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //토큰 방식으로도 충분하므로 csrf 공격은 생각안해도됨(disable)
        http.csrf().disable()
                .headers().cacheControl().disable().and()
                .exceptionHandling().authenticationEntryPoint(this.customEntryPointHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()   //사용자의 쿠키에 세션을 저장하지 않겠다
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()                     //Preflight 요청은 따로 제한 x
                .antMatchers("/api/authen/logout/").authenticated()                  //로그아웃은 인증된 사용자여야함
                .antMatchers("/api/authen/login/**/callback").permitAll()           //로그인 처리 로직. 보안관련은 내부에서 검사함
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/**").authenticated()
                .anyRequest().denyAll();

        http.addFilterBefore(customTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.cors().configurationSource(request -> getCorsConfigurationSource());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public ShaPasswordEncoder shaPasswordEncoder() {
        return new ShaPasswordEncoder();
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

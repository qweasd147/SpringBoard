# Spring Board

Spring + react를 사용해서 기초 게시판 제작

# 1 backend
oracle + spring + spring security + 외부 로그인 시스템(OAuth 2.0 서드파티 로그인 사용. kakao, google, naver)

## 1.1 spring security 주요 설정

```java
@Configuration
@EnableWebSecurity
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private CustomTokenFilter customTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //...
        http.addFilterBefore(customTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.cors().configurationSource(request -> getCorsConfigurationSource());
        //...
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

        return configuration;
    }
}
```
일단 요청 url별 인증 & 권한 체크 설정 부분은 생략.

`CustomTokenFilter`클래스는 `OncePerRequestFilter`의 구현체 클래스로 모든 요청이 들어오면 `request`의 헤더를 검사하여

토큰을 추출 & 파싱하여 `SecurityContext`에 올려놓고 다른곳에서 토큰 정보를 자유롭게 사용할 수가 있다. 물론 `OncePerRequestFilter`

에서 토큰의 유효성 검사로 함께 처리해준다.

## 1.2 http 헤더, 메소드 제한

# 2 frontend
기존 React로 만든거에서 살짝 수정해서 그냥 사용
* https://github.com/qweasd147/ReactBoard

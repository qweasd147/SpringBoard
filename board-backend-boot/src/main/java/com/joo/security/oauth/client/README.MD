# 참고사항

### AuthorizationCodeAccessTokenProvider class

`CustomAuthorizationCodeAccessTokenProvider` 클래스의 존재 이유인데, token을 만들려고 API를 요청 시

`getParametersForTokenRequest` 메소드에서 `client_id`값을 넘겨주지 않는다. 그래서 `CustomAuthorizationCodeAccessTokenProvider`

클래스를 만들어 `client_id`를 담아서 보내준다. google은 요청 시 문제가 없는데 kakao로 토큰 요청 시 `client_id` 없다고 문제가 생긴다.


참고사항

* 토큰 요청 시 필요한 파라미터들 : https://tools.ietf.org/html/rfc6749#section-4.1.3
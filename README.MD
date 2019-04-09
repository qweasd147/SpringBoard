# Spring Board

Spring + react를 사용해서 기초 게시판 제작.

# 1 backend
똑같은 API를 순수 spring(board-backend)과 spring boot(board-backend-boot)를 나누어서 개발

## 1.1 사용기술
기본적 공통기술은 RDBMS + spring security + 외부 로그인 시스템(OAuth 2.0 서드파티 인증 사용. kakao, google, naver) 사용

### 1.1.1 board-backend
spring, oracle, mybatis, junit, docker
### 1.1.2 board-backend-boot
spring-boot, mariaDB, JPA, junit, docker

# 1.2 spring-security 주요 설정
[spring-security 주요 설정](https://github.com/qweasd147/SpringBoard/docs/spring-security.MD)

# 2 frontend
기존 React로 만든거에서 살짝 수정해서 그냥 사용
* https://github.com/qweasd147/ReactBoard

스프링 시큐리티 구조
Authentication
실질적으로 인증정보를 담고 있는 객체

SecurityContext
Authentication을 보관하는 역할
SecurityContext를 통해 Authentication 객체를 꺼내올 수 있음

SecurityContextHolder
시큐리티가 최종적으로 제공하는 객체

UsernamePasswordAuthenticationToke`
Authentication을 implements한 AbstractAuthenticationToken의 하위 클래스
User의 ID가 Principal 역할을 하고, Password가 Credential의 역할을 수행
UsernamePasswordAuthenticationToken의 첫 번째 생성자는 객체를 생성, 두번째 생성자는 인증이 완료된 객체를 생성

AuthenticationProvider
실제 인증에 대한 부분 처리
인증 전의 Authentication 객체를 받아 -> 인증이 완료된 객체를 반환
AuthenticationProvider 인터페이스를 구현하여 Custom해 AuthenticationManager에 등록

AuthenticationManager
SpringSecurity의 인증 부분을 처리
AuthenticationManager에 등록된 AuthenticationProvider에 의해 처리
AuthenticationProvider를 List로 갖으며, AuthenticationManager는 for문을 통해 모든 provider를 조회하면서 authenticate 처리
인증이 성공하면 두번째 생성자를 이용 -> 인증이 성공한 객체(isAuthenticated-true)를 생성하여 Security Context에 저장
인증 상태 유지를 위해 세션에 보관, 인증이 실패한 경우 AuthenticationException 발생


스프링 시큐리티 구조의 처리 과정

1. 사용자가 로그인 정보와 함께 인증 요청을 한다
2. AuthenticationFilter가 요청을 가로채고, 가로챈 정보를 통해 UsernamePasswordAuthenticationToken의 인증용 객체를 생성한다.

3. AuthenticationManager의 구현체인 ProviderManager에게 생성한 UsernamePasswordToken 객체를 전달한다.
4. AuthenticationManager는 등록된 AuthenticationProvider(들)을 조회하여 인증을 요구한다.
5. 실제 DB에서 사용자 인증정보를 가져오는 UserDetailsService에 사용자 정보를 넘겨준다.
6. 넘겨받은 사용자 정보를 통해 DB에서 찾은 사용자 정보인 UserDetails 객체를 만든다.
7. AuthenticationProvider(들)은 UserDetails를 넘겨받고 사용자 정보를 비교한다.
8. 인증이 완료되면 권한 등의 사용자 정보를 담은 Authentication 객체를 반환한다.
9. 다시 최초의 AuthenticationFilter에 Authentication 객체가 반환된다.
10. Authenticaton 객체를 SecurityContext에 저장한다.


레퍼런스
https://it-hhhj2.tistory.com/71 [Spring Security 기본 개념과 동작구조 이해]
https://dev-coco.tistory.com/174[Spring Security의 구조(Architecture) 및 처리 과정 알아보기]
https://velog.io/@franc/%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0-%ED%9D%90%EB%A6%84%EA%B3%BC-%EA%B5%AC%EC%A1%B0[스프링 시큐리티 흐름과 구조]
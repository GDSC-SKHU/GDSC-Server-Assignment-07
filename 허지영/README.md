스프링 시큐리티는 보안(인증과 권한, 인가 등)을 담당하는 스프링 하위 프레임워크이다.
스프링 시큐리티에는 ‘인증’과 ‘인가’에 관한 영역이 있다.
인증은 해당 사용자가 본인이 맞는지 확인하는 과정이고, 인가는 해당 사용자가 요청하는 자원을 실행할 수 있는 권한이 있는가를 확인하는 과정이다. 
간단히 말해 인증은 누구인지 증명하는 것이고 인가는 권한이 있는지 확인하는 것이다.
스프링 시큐리티는 보안과 관련해서 체계적으로 많은 옵션을 제공해주기 때문에, 개발자 입장에서 일일이 보안 관련 로직을 작성하지 않아도 된다는 장점이 있다.

스프링 시큐리티는 세션-쿠키방식으로 인증한다.
AuthenticationFilter 에서부터 user DB까지 타고 들어가고, 만약 DB에 있는 유저라면 UserDetails 로 꺼내서 유저의 session 생성한다. 
스프링 시큐리티의 인메모리 세션저장소인 SecurityContextHolder 에 저장하고 유저에게 session ID와 함께 응답을 내려준다. 
이후 요청에서는 요청쿠키에서 JSESSIONID 검증 후 유효하면 Authentication를 쥐어주는 과정을 거친다.

흐름은 다음과 같다.
1. 사용자가 로그인 정보와 함께 인증 요청을 한다.(Http Request)
2. AuthenticationFilter가 요청을 가로채고, 가로챈 정보를 통해 UsernamePasswordAuthenticationToken의 인증용 객체를 생성한다.
3. AuthenticationManager의 구현체인 ProviderManager에게 생성한 UsernamePasswordToken 객체를 전달한다.
4. AuthenticationManager는 등록된 AuthenticationProvider(들)을 조회하여 인증을 요구한다.
5. 실제 DB에서 사용자 인증정보를 가져오는 UserDetailsService에 사용자 정보를 넘겨준다.
6. 넘겨받은 사용자 정보를 통해 DB에서 찾은 사용자 정보인 UserDetails 객체를 만든다.
7. AuthenticationProvider(들)은 UserDetails를 넘겨받고 사용자 정보를 비교한다.
8. 인증이 완료되면 권한 등의 사용자 정보를 담은 Authentication 객체를 반환한다.
9. 다시 최초의 AuthenticationFilter에 Authentication 객체가 반환된다.
10. Authenticaton 객체를 SecurityContext에 저장한다.

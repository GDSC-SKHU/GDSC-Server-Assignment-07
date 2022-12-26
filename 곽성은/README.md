스프링 시큐리티의 구조와 흐름 설명
## Spring Security
**Spring Security** : Spring 기반의 어플리케이션의 보안(인증과 권한, 인가 등)을 담당하는 스프링 하위 프레임워크

Spring Security는 '인증'과 '권한'에 대한 부분을 Filter 흐름에 따라 처리

- **인증(Authentication)?**: 사용자가 본인이 맞는지 확인하는 과정
- **인가(Authorization)?**: 사용자가 요청하는 자원을 실행할 수 있는 권한이 있는가 확인하는 과정

인증과 인가를 위해 Principal을 아이디로, Credential을 비밀번호로 사용하는 **Credential 기반의 인증 방식 사용**

Principal(접근 주체) : 보호받는 Resource에 접근하는 대상
Credential(비밀번호) : Resource에 접근하는 대상의 비밀번호

---
### Spring Security의 처리 과정 정리
1. 사용자가 로그인 정보와 함께 인증 요청(Http Request)
2.  AuthenticationFilter가 요청을 가로채고, 가로챈 정보를 통해 UsernamePasswordAuthenticationToken의 인증용 객체를 생성
3. AuthenticationManager의 구현체인 ProviderManager에게 생성한 UsernamePasswordToken 객체를 전달
4. AuthenticationManager는 등록된 AuthenticationProvider(들)을 조회하여 인증 요구
5. 실제 DB에서 사용자 인증정보를 가져오는 UserDetailsService에 사용자 정보를 넘겨줌
6. 넘겨받은 사용자 정보를 통해 DB에서 찾은 사용자 정보인 UserDetails 객체를 만듦
7. AuthenticationProvider(들)은 UserDetails를 넘겨받고 사용자 정보를 비교
8. 인증이 완료되면 권한 등의 사용자 정보를 담은 Authentication 객체를 반환
9. 다시 최초의 AuthenticationFilter에 Authentication 객체가 반환
10. Authenticaton 객체를 SecurityContext에 저장
---
### Spring Security 구조 주요 모듈
- **Authentication**
  - 현재 접근하는 주체의 정보와 권한을 담는 인터페이스
  - Authentication 객체는 SecurityContext에 저장
- **SecurityContext** 
  - Authentication을 보관하는 역할
  - SecurityContext를 통해 Authentication 객체를 꺼내올 수 있음
- **SecurityContextHolder**  
  - 보안 주체의 세부 정보를 포함하여 응용프로그램의 현재 보안 컨텍스트에 대한 세부 정보가 저장
- **UsernamePasswordAuthenticationToken**
  - Authentication을 implements한 AbstractAuthenticationToken의 하위 클래스
  - User의 ID가 Principal 역할을 하고, Password가 Credential의 역할을 수행
- **AuthenticationProvider**
  - 실제 인증에 대한 부분을 처리, 인증 전의 Authentication 객체를 받아서 인증이 완료된 객체를 반환
  - AuthenticationProvider를 작성하고 AuthenticationManager에 등록
- **AuthenticationManager**
  - SpringSecurity의 인증 부분을 처리
  - 실질적으로는 AuthenticationManager에등록된 AuthenticationProvider에 의해 처리

<아이디를 기반으로 DB에서 데이터 조회, 암호화 방식, 토큰 전달>
- UserDetails : 인증에 성공하여 생성된 UserDetails 객체는 Authentication객체를 구현한 UsernamePasswordAuthenticationToken을 생성하기 위해 사용
- UserDetailsService : UserRepository를 주입받아 DB와 연결 처리
- Password Encoding : AuthenticationManagerBuilder.userDetailsService().passwordEncoder()를 통해 패스워드 암호화에 사용될 PasswordEncoder 구현체 지정
- GrantedAuthority : GrantedAuthority는 현재 사용자(Principal)가 가지고 있는 권한을 의미하며, ROLE_ADMIN이나 ROLE_USER와 같이 ROLE_*의 형태로 사용, GrantedAuthority 객체는 UserDetailsService에 의해 불러올 수 있고, 특정 자원에 대한 권한이 있는지를 검사하여 접근 허용 여부를 결정
# 22-23 Server Assignment 7

#### Spring Security란?
> -Spring 기반의 어플리케이션의 보안을 담당하는 Spring 하위 프레임워크  
  -많은 보안 관련 옵션을 체계적으로 제공 -> 개발자가 일일이 보안 관련 코드를 작성할 필요 없음

#### Spring Security 처리 과정
![SpringSecurityArchitecture](https://blog.kakaocdn.net/dn/UOabX/btqEJBBNixH/PGDv64FTKaBSLzMiiXkA3K/img.png)

1. 사용자가 로그인 정보와 함께 인증 요청 (HTTP Request)  
2. AuthenticationFilter가 요청을 가로채고, 가로챈 정보로 (인증용)UsernamePasswordAuthenticationToken 발급  
3. AuthenticationManager의 구현체인 ProviderManager에게 생성한 (인증용)UsernamePasswordToken 객체를 전달  
4. AuthenticationManager는 등록된 AuthenticationProvider(들)을 조회하여 인증 요구  
5. 실제 DB에서 사용자 인증정보를 가져오는 UserDetailsService에 사용자 정보를 넘김  
6. 넘겨받은 사용자 정보를 통해 DB에서 찾은 사용자 정보인 UserDetails 객체 생성  
7. AuthenticationProvider(들)은 UserDetails를 넘겨받고 사용자 정보를 비교  
   (입력받은 비밀번호를 암호화하여 DB의 비밀번호와 매칭되는 경우 인증 완료)  
8. 인증이 완료되면 권한 등의 사용자 정보를 담은 (인증이 성공된)UsernamePasswordAuthenticationToken 객체를 ProviderManager로 반환  
9. 다시 최초의 AuthenticationFilter에 (인증이 성공된)UsernamePasswordAuthenticationToken 객체 반환  
10. (인증이 성공된)UsernamePasswordAuthenticationToken 객체를 SecurityContext에 저장  

#### 구조
Authentication
> -현재 접근하는 주체의 정보와 권한을 담는 인터페이스  
  -인증 후 최종 인증 결과를 담아 SecurityContext에 보관됨

UserDetails
> -Authentication 객체를 구현한 UsernamePasswordAuthenticationToken 객체를 생성하기 위해 사용됨

UserDetailsService
> -유저의 정보를 불러오기 위해서 구현해야 하는 인터페이스  
  -기본 오버라이드 메소드 : loadUserByUsername, 유저의 정보를 불러와서 UserDetails로 리턴

AuthenticationProvider
> -AuthenticationProvider에서는 실제 인증에 대한 부분을 처리  
  -인증 전의 Authentication 객체를 받아서 인증이 완료된 Authentication 객체를 반환하는 역할
  
AuthenticationManager
> -실제 인증 과정에 대한 로직을 가지고 있는 AuthenticationProvider를 가지는 인터페이스

ProviderManager
> -AuthenticationManager를 implements한 클래스  
  -실제 인증 과정에 대한 로직을 가지고 있는 AuthenticationProvider를 List로 가지고 있으며,  
   &nbsp;&nbsp;for문을 통해 모든 provider를 조회하면서 authenticate 처리  
   &nbsp;&nbsp;(=순차적으로 AuthenticationProvider들에게 전달하여 실제 인증 과정을 수행함)

SecurityContextHolder
> -SecurityContext 객체를 보관하고 있는 wrapper 클래스

SecurityContext
> -Authentication을 보관하는 역할을 하며, SecurityContext를 통해 Authentication을 저장하거나 꺼내올 수 있음





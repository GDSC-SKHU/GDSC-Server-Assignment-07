
# 스프링 시큐리티란?

- 어플리케이션의 보안 (인증 및 권한)을 담당하는 프레임워크
- Spring Security는 기본적으로 인증 절차를 거친 후에 인가 절차를 진행하며, 인가 과정에서 해당 리소스에 접근 권한이 있는지 확인하게 된다.
- Spring Security에서는 이러한 인증과 인가를 위해 Principal을 아이디로, Credential을 비밀번호로 사용하는 Credential 기반의 인증 방식을 사용한다.
- 인증 (Authentication) → 인증 성공 후 → 인가 (Authorization)
- 스프링 시큐리티를 사용하지 않으면
    - 자체적으로 세션을 체크해야 한다.
    - redirect을 일일이 설정해주어야 한다.

## 구조&흐름

- Request → Application Filters → Authentication Filters → UsernamePasswordAuthentication Filter
1. **Http Request 수신**
    - Spring Security는 필터로 동작을 한다.
    - 요청이 들어오면, 인증과 권한을 위한 필터들을 통하게 된다.
    - 유저가 인증을 요청할때 필터는 인증 메커니즘과 모델을 기반으로한 필터들을 통과한다.
    - 예
        - HTTP 기본 인증을 요청하면 BasicAuthenticationFilter를 통과한다.
        - HTTP Digest 인증을 요청하면 DigestAuthenticationFilter를 통과한다.
        - 로그인 폼에 의해 요청된 인증은 UseerPasswordAuthenticationFilter를 통과한다.
        - x509 인증을 요청하면 X509AuthenticationFilter를 통과한다.


2. **유저 자격을 기반으로 인증토큰(AuthenticationToken) 만들기**
    - username과 password를 요청으로 추출하여 유저 자격을 기반으로 인증 객체를 생성한다.
        - 대부분의 인증메커니즘은 username과 password를 기반으로 한다.
    - username과 password는 UsernamePassworkdAutehnticationToekn을 만드는데 사용된다.


3. **Filter를 통해 AuthenticationToken을 AuthenticationManager에 위임한다.**
    - UsernamePasswordAuthenticationToken오브젝트가 생성된 후, AuthenticationManager의 인증 메소드를 호출한다.
    - AuthenticationManager는 인터페이스로 정의되어있다.
        - 실제 구현은 ProviderManager에서 한다.
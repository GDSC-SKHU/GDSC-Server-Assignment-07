# 22-23 Server Assignment 7
---
## Spring Security
### 개념
+ Spring Security는 Spring 기반의 애플리케이션의 보안을 담당하는 스프링 하위 프레임워크로 인증(Authentication-본인 확인)과 인가(Authorization-권한 확인)를 **filter의 흐름**에 따라 처리한다.
+ Spring Security에서는 이러한 인증과 인가를 위해 Principal을 아이디로, Credential을 비밀번호로 사용하는 Credential 기반의 인증 방식을 사용한다.
    >Principal(접근 주체): 보호받는 Resource에 접근하는 대상
    >Credential(비밀번호): Resource에 접근하는 대상의 비밀번호

#### 의존성 추가 (gradle 방식)
```java
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity5'
```
의존성을 추가하면 특정 url에 접속을 할 때 인증을 받음

### 구조
<img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbeDENY%2FbtrBs0cquNc%2FPkwRQzgyzhoy1ecQrlQOJk%2Fimg.png"  width="650" height="450"/>

1. 사용자가 로그인 정보와 함께 인증 요청을 한다.(Http Request)
2.  AuthenticationFilter가 요청을 가로채고, 가로챈 정보를 통해 UsernamePasswordAuthenticationToken의 인증용 객체를 생성한다.
3. AuthenticationManager의 구현체인 ProviderManager에게 생성한 UsernamePasswordToken 객체를 전달한다.
4. AuthenticationManager는 등록된 AuthenticationProvider(들)을 조회하여 인증을 요구한다.
5. 실제 DB에서 사용자 인증정보를 가져오는 UserDetailsService에 사용자 정보를 넘겨준다.
6. 넘겨받은 사용자 정보를 통해 DB에서 찾은 사용자 정보인 UserDetails 객체를 만든다.
7. AuthenticationProvider(들)은 UserDetails를 넘겨받고 사용자 정보를 비교한다.
8. 인증이 완료되면 권한 등의 사용자 정보를 담은 Authentication 객체를 반환한다.
9. 다시 최초의 AuthenticationFilter에 Authentication 객체가 반환된다.
10. Authenticaton 객체를 SecurityContext에 저장한다.

➔ 최종적으로 SecurityContextHolder는 세션 영역에 있는 SecurityContext에 Authentication 객체를 저장한다. 사용자 정보를 저장한다는 것은 Spring Security가 전통적인 세션-쿠키 기반의 인증 방식을 사용한다는 것을 의미한다.

#### SecurityConfig
<img src="https://velog.velcdn.com/images%2Fseongwon97%2Fpost%2F67421279-a801-459b-a73a-055dfdd1b579%2Fimage.png"  width="900" height="400"/>

사용자 정의 보안 설정 클래스로 Spring Security의 WebSecurityConfigurerAdapter 클래스가 실행되고, 이 클래스의 내부적으로 생성되는 HTTPSecurity 클래스에서 제공되는 인증/인가 API를 이용해 사용자 정의 보안 기능을 구현한다.


### 주요 모듈
+ **Authentication**
현재 접근하는 주체의 정보와 권한을 담는 인터페이스이다. Authentication 객체는 SecurityContext에 저장되며, SecurityContextHolder를 통해 SecurityContext에 접근하고, SecurityContext를 통해 Authentication에 접근할 수 있다.
SecurityContextHolder는 인증을 보관하는 보관소이며 Authentication을 갖고 있는 SecurityContext를 보관한다.
```java
public interface Authentication extends Principal, Serializable {
    // 현재 사용자의 권한 목록을 가져옴
    Collection<? extends GrantedAuthority> getAuthorities();
    
    // credentials을 가져옴
    Object getCredentials();
    
    Object getDetails();
    
    // Principal 객체를 가져옴.
    Object getPrincipal();
    
    // 인증 여부를 가져옴
    boolean isAuthenticated();
    
    // 인증 여부를 설정함
    void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException;
}
```
+ **AuthenticationManager**
인증에 대한 부분은 AuthenticationManager를 통해서 처리하게 되는데, 실질적으로는 AuthenticationManager에등록된 AuthenticationProvider에 의해 처리된다. 인증에 성공하면 두번째 생성자를 이용해 객체를 생성하여 SecurityContext에 저장한다. 
```java
public interface AuthenticationManager {
 
	Authentication authenticate(Authentication authentication) throws AuthenticationException;
 
}
```
+ **AuthenticationProvider**
AuthenticationProvider에서는 실제 인증에 대한 부분을 처리하는데, 인증 전의 Authentication 객체를 받아서 인증이 완료된 객체를 반환하는 역할을 한다. 아래와 같은 인터페이스를 구현해 Custom한 AuthenticationProvider를 작성하고 AuthenticationManager에 등록하면 된다.
```java
public interface AuthenticationProvider {
 
	Authentication authenticate(Authentication authentication) throws AuthenticationException;
 
	boolean supports(Class<?> authentication);
 
}
```
+ **ProviderManager**
AuthenticationManager를 implements한 ProviderManager는 AuthenticationProvider를 구성하는 목록을 갖는다.
+ **UserDetailsService**
UserDetailsService는 UserDetails 객체를 반환하는 하나의 메소드만을 가지고 있는데, 일반적으로 이를 implements한 클래스에 UserRepository를 주입받아 DB와 연결하여 처리한다.
```java
public interface UserDetailsService {
 
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
 
}
```
+ **UsernamePasswordAuthenticationToken**
UsernamePasswordAuthenticationToken은 Authentication을 implements한 AbstractAuthenticationToken의 하위 클래스로, User의 ID가 Principal 역할을 하고, Password가 Credential의 역할을 한다. 
UsernamePasswordAuthenticationToken의 첫 번째 생성자는 인증 전의 객체를 생성하고, 두번째는 인증이 완료된 객체를 생성한다.
```java
public abstract class AbstractAuthenticationToken implements Authentication, CredentialsContainer {
}
 
public class UsernamePasswordAuthenticationToken extends AbstractAuthenticationToken {
 
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
 
	// 주로 사용자의 ID에 해당
	private final Object principal;
 
	// 주로 사용자의 PW에 해당
	private Object credentials;
 
	// 인증 완료 전의 객체 생성
	public UsernamePasswordAuthenticationToken(Object principal, Object credentials) {
		super(null);
		this.principal = principal;
		this.credentials = credentials;
		setAuthenticated(false);
	}
 
	// 인증 완료 후의 객체 생성
	public UsernamePasswordAuthenticationToken(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		super.setAuthenticated(true); // must use super, as we override
	}
}
```
+ **UserDetails**
인증에 성공하여 생성된 UserDetails 객체는 Authentication객체를 구현한 UsernamePasswordAuthenticationToken을 생성하기 위해 사용된다. UserDetails 인터페이스의 경우 직접 개발한 UserVO 모델에 UserDetails를 implements하여 이를 처리하거나 UserDetailsVO에 UserDetails를 implements하여 처리할 수 있다.

```java
public interface UserDetails extends Serializable {
 
	// 권한 목록
	Collection<? extends GrantedAuthority> getAuthorities();
 
	String getPassword();
 
	String getUsername();
 
	// 계정 만료 여부
	boolean isAccountNonExpired();
 
	// 계정 잠김 여부
	boolean isAccountNonLocked();
 
	// 비밀번호 만료 여부
	boolean isCredentialsNonExpired();
 
	// 사용자 활성화 여부
	boolean isEnabled();
 
}
```
+ **SecurityContextHolder**
SecurityContextHolder는 보안 주체의 세부 정보를 포함하여 응용프로그램의 현재 보안 컨텍스트에 대한 세부 정보가 저장된다.
+ **SecurityContext**
Authentication을 보관하는 역할을 하며, SecurityContext를 통해 Authentication을 저장하거나 꺼내올 수 있다.
+ **GrantedAuthority**
GrantedAuthority는 현재 사용자(Principal)가 가지고 있는 권한을 의미하며, ROLE_ADMIN이나 ROLE_USER와 같이 ROLE_*의 형태로 사용한다. GrantedAuthority 객체는 UserDetailsService에 의해 불러올 수 있고, 특정 자원에 대한 권한이 있는지를 검사하여 접근 허용 여부를 결정한다.

### Filter
<img src="https://velog.velcdn.com/images%2Fseongwon97%2Fpost%2F900e612a-9e5a-4c08-a93f-444521f124d7%2Ffig-2-spring-big-picture.png"  width="900" height="400"/>
Spring Security는 메인 filter chain 사이에 DelegatingFilterProxy 필터를 넣고, 그 아래 SecurityFilterchain 그룹을 등록하여 url에 따라 적용되는 filter chain을 다르게 하는 방법을 사용한다. configure(HttpSecurity http)를 override하여 filter를 세팅한다.

```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    // HttpSecurity를 설정하는 것이 Filter들을 Setting하는 것
        http.authorizeRequests((requests) ->
                requests.antMatchers("/").permitAll()
                        .anyRequest().authenticated()
        );
        http.formLogin(login->
                login.defaultSuccessUrl("/", false));
       http.httpBasic();
    }
```

### Form Login
Spring Security에서 제공하는 인증방식으로, 사용자가 요청한 URL이 인증이 필요할 경우 서버는 login 페이지를 리턴한다. 사용자가 로그인 요청을 하면 post mapping으로 데이터가 전송되고, 서버는 해당 로그인 정보를 확인해서 존재한다면 세션과 토큰을 생성한다.
+ loginPage("/"): 인증이 필요할 때 이동하는 페이지 설정
+ defaultSuccessUrl("/"): 인증이 성공하였을 때 default로 이동하는 URL
+ failureUrl("/"): 인증이 실패하였을 때 이동하는 페이지
+ usernameParameter("id"), passwordParameter("pwd"): spring security의 기본값 username, password의 파라미터값을 변경
+ loginProcessingUrl("/login_proc"): 폼 태그의 action url을 설정. default값은 default login
+ successHandler(): 로그인이 성공했을 때 success handler를 호출. 파라미터로는 AuthenticationSuccessHandler 인터페이스를 구현한 것을 전달
+ failureHandler(): 로그인이 실패하였을 때 failure handler를 호출
+ permitAll(): 해당 경로는 인증을 받지 않아도 누구나 접근 가능

### Logout
logout request를 받았을 때, server의 세션을 무효화하고 쿠키 정보, 인증토큰과 인증토큰이 저장된 Security Context의 객체를 삭제해야 한다.

```java
    protected void configure(HttpSecurity http) throws Exception {
        http.logout() // 로그아웃 처리
                .logoutUrl("/logout") // 로그아웃 처리 URL
	            .logoutSuccessUrl("/login") // 로그아웃 성공 후 이동페이지
                .deleteCookies("JSESSIONID", "remember - me") // 로그아웃 후 해당 쿠키 삭제
                .addLogoutHandler(logoutHandler()) // 로그아웃 핸들러
                .logoutSuccessHandler(logoutSuccessHandler()) // 로그아웃 성공 후 핸들러
    }
```
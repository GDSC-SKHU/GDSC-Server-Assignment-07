# 22-23 Server Assignment 7

* * *

## Spring Security

### ✔️ 개념
  *  Spring 기반의 애플리케이션의 보안(인증과 권한, 인가 등)을 담당하는 스프링 하위 프레임워크.
   Spring Security는 '인증'과 '권한'에 대한 부분을 Filter 흐름에 따라 처리함.
  * 인증(Authorizatoin) : 해당 사용자가 본인이 맞는지를 확인하는 절차
  * 인가(Authorization) : 인증된 사용자가 요청한 자원에 접근 가능한지를 결정하는 절차
  * 기본적으로 인증을 거친 후 인가 절차를 진행함.
  * 인증과 인가를 위해 Principal를 아이디로, Credential를 비밀번호로 사용하는 방식을 이용함.
### ✔️ 흐름
<img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcsQBDx%2Fbtq01prLmWg%2F6Cisvky0EntbOiRn5nS2a1%2Fimg.pnghttps://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcsQBDx%2Fbtq01prLmWg%2F6Cisvky0EntbOiRn5nS2a1%2Fimg.png" alt="Security"/> </br>

1. 사용자가 로그인 정보와 함께 **인증 요청**함. (Http Request) -> filter 통과 (filter의 종류는 다양)
2. AuthenticationFilter가 요청을 가로채고, 가로챈 정보를 통해 **UsernamePasswordAuthenticationToken**의 인증용 객체를 생성함.
   * Authentication: 현재 접근한 주체의 정보와 권한을 담는 인터페이스
   * UsernamePasswordAuthenticationToken : Authentication을 implements한 AbstractAuthenticationToken의 하위 클래스로, User의 ID가 Principal 역할을 하고, Password가 Credential의 역할을 한다. 
      UsernamePasswordAuthenticationToken의 첫 번째 생성자는 인증 전의 객체를 생성하고, 두번째는 인증이 완료된 객체를 생성한다.
3. **AuthenticationManager**의 구현체인 ProviderManager에게 생성한 UsernamePasswordToken 객체를 전달한다.
   * 실직적으로는 AuthenticationManager에 등록된 AuthenticationProvider에 의해 처리된다. 인증에 성공하면 두번째 생성자를 이용해 객체를 생성하여 SecurityContext에 저장한다. 
   * ProviderManager : AuthenticationProvider를 구성하는 목록을 갖는다. 
4. AuthenticationManager는 등록된 **AuthenticationProvider**(들)을 조회하여 인증을 요구한다.
   * 실제 인증에 대한 부분을 처리하는데, 인증 전의 Authentication 객체를 받아서 인증이 완료된 객체를 반환하는 역할을 한다.
5. 실제 DB에서 사용자 인증정보를 가져오는 **UserDetailsService**에 사용자 정보를 넘겨준다.
   * UserDetailsService : UserDetails 객체를 반환하는 하나의 메소드만을 가지고 있는데, 일반적으로 이를 implements한 클래스에 UserRepository를 주입받아 DB와 연결하여 처리한다.
6. 넘겨받은 사용자 정보를 통해 DB에서 찾은 사용자 정보인 **UserDetails** 객체를 만든다.
    * 인증에 성공하여 생성된 UserDetails 객체는 Authentication 객체를 구현한 UsernamePasswordAuthenticationToken을 생성하기 위해 사용된다.
7. AuthenticationProvider(들)은 UserDetails를 넘겨받고 사용자 정보를 비교한다.
8. 인증이 완료되면 권한 등의 사용자 정보를 담은 Authentication 객체를 반환한다.
9. 다시 최초의 AuthenticationFilter에 Authentication 객체가 반환된다.
10. Authenticaton 객체를 SecurityContext에 저장한다.
    * SecurityContextHolder : 보안 주체의 세부 정보를 포함하여 응용프로그램의 현재 보안 컨텍스트에 대한 세부 정보가 저장된다.
    * SecurityContext : Authentication을 보관하는 역할을 하며, SecurityContext를 통해 Authentication을 저장하거나 꺼내올 수 있다.


### ✔️ Spring Security 모듈

* Authentication
```java
public interface Authentication extends Principal, Serializable {
    // 현재 사용자의 권한 목록을 가져옴
    Collection<? extends GrantedAuthority> getAuthorities();
    
    // credentials(주로 비밀번호)을 가져옴
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

* UsernamePasswordAuthenticationToken
```java
public class UsernamePasswordAuthenticationToken extends AbstractAuthenticationToken {
    // 주로 사용자의 ID에 해당함
    private final Object principal;
    // 주로 사용자의 PW에 해당함
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


public abstract class AbstractAuthenticationToken implements Authentication, CredentialsContainer {
}

```

* AuthenticationProvider
```java
public interface AuthenticationProvider {

	// 인증 전의 Authenticaion 객체를 받아서 인증된 Authentication 객체를 반환
    Authentication authenticate(Authentication var1) throws AuthenticationException;

    boolean supports(Class<?> var1);
    
}
```

* Authentication Manager
```java
public interface AuthenticationManager {
	Authentication authenticate(Authentication authentication) 
		throws AuthenticationException;
}
```

* UserDetails
```java
public interface UserDetails extends Serializable {

    Collection<? extends GrantedAuthority> getAuthorities();

    String getPassword();

    String getUsername();

    boolean isAccountNonExpired();

    boolean isAccountNonLocked();

    boolean isCredentialsNonExpired();

    boolean isEnabled();
    
}
```

* UserDetailsService
```java
public interface UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
```


### Reference
* https://mangkyu.tistory.com/76
* https://dev-coco.tistory.com/174
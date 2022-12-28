package com.gdsc.security.repository;

import com.gdsc.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//public interface 이름 extends JpaRepository <엔티티 ID 유형> <-- 이와 같은 형식으로 선언함.
//JpaRepository -> 인터페이스에 미리 검색 메소드를 정의 해 두는 것으로, 메소드를 호출하는 것만으로 스마트한 데이터 검색을 할 수 있게 되는 것임.
public interface UserRepository extends JpaRepository<User, Long> {

    //NPE(NullPointException)를 피하기 위해서는 Null 여부를 검사해야함. 변수가 많은 경우 코드가 복잡해지고 번거로워짐.
    //Optional<T>은 null이 올 수 있는 값을 감싸는 Wapper class 임.
    Optional<User> findByUsername(String username);
}

package org.example2.Dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

// 클라이언트는 DAO 대신 이 인터페이스를 사용해 서비스 프록시의 트랜잭션 경계를 거친다.
// 6.7.1: 타입 수준의 기본 속성은 REQUIRED, 읽기/쓰기이며 메소드의 선언이 우선한다.
@Transactional
public interface UserService {
    void add(User user);

    // DAO 메소드와 1:1로 대응하는 CRUD 메소드지만 add()처럼 별도의 로직을 가질 수 있다.
    @Transactional(readOnly = true)
    User get(String id);

    @Transactional(readOnly = true)
    List<User> getAll();
    void deleteAll();
    void update(User user);

    void upgradeLevels();
}

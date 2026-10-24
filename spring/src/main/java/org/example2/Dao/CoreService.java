package org.example2.Dao;

import java.util.List;

// UserService와 다른 인터페이스에도 ProxyFactoryBean으로 같은 트랜잭션 어드바이스를 재사용한다.
public interface CoreService {
    void add(String name);

    void addAll(List<String> names);

    List<String> getAll();

    void deleteAll();
}

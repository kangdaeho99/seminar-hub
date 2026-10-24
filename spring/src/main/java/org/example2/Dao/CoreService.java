package org.example2.Dao;

import java.util.List;

// UserService와 다른 인터페이스에도 같은 TxProxyFactoryBean을 적용하는 예제다.
public interface CoreService {
    void add(String name);

    void addAll(List<String> names);

    List<String> getAll();

    void deleteAll();
}

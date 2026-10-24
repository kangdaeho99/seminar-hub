package org.example2.Dao;

import java.util.List;

public interface CoreDao {
    void add(String name);

    List<String> getAll();

    void deleteAll();
}

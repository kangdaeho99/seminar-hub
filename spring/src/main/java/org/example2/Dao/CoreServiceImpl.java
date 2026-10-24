package org.example2.Dao;

import java.util.List;

public class CoreServiceImpl implements CoreService {
    private CoreDao coreDao;

    public void setCoreDao(CoreDao coreDao) {
        this.coreDao = coreDao;
    }

    @Override
    public void add(String name) {
        coreDao.add(name);
    }

    @Override
    public void addAll(List<String> names) {
        // 여러 DAO 호출을 하나로 묶는 트랜잭션 경계는 외부 프록시가 담당한다.
        for (String name : names) {
            coreDao.add(name);
        }
    }

    @Override
    public List<String> getAll() {
        return coreDao.getAll();
    }

    @Override
    public void deleteAll() {
        coreDao.deleteAll();
    }
}

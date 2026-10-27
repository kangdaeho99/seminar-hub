package org.example2.Service;

import java.util.List;

import org.example2.Dao.CoreDao;
import org.example2.Dao.CoreService;
import org.example2.Dao.CoreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:test-applicationContext.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CoreServiceTest {
    @Autowired
    @Qualifier("coreService")
    CoreService coreService;

    @Autowired
    CoreDao coreDao;

    @BeforeEach
    public void setUp() {
        coreDao.deleteAll();
    }

    @Test
    public void addAllCommitsEveryItem() {
        coreService.add("alpha");
        coreService.addAll(List.of("beta", "gamma"));

        // 프록시 호출이 끝난 뒤 별도 DB 연결에서도 커밋된 데이터를 조회할 수 있다.
        assertEquals(List.of("alpha", "beta", "gamma"), coreDao.getAll());
        assertEquals(coreDao.getAll(), coreService.getAll());
        assertFalse(TransactionSynchronizationManager.isActualTransactionActive());

        coreService.deleteAll();
        assertEquals(List.of(), coreDao.getAll());
    }

    @Test
    public void addAllRollsBackEveryNewItemOnDuplicate() {
        coreDao.add("existing");

        // 세 번째 항목의 중복 오류로 앞서 저장한 두 항목도 함께 롤백된다.
        assertThrows(DuplicateKeyException.class,
                () -> coreService.addAll(List.of("alpha", "beta", "existing")));

        assertEquals(List.of("existing"), coreDao.getAll());
        assertFalse(TransactionSynchronizationManager.isActualTransactionActive());
    }

    @Test
    public void targetWithoutProxyLeavesEarlierInsertCommitted() {
        // 같은 구현체라도 타깃을 직접 호출하면 일괄 저장을 묶는 트랜잭션이 없다.
        // 컨테이너에 등록하면 자동 프록시가 생성되므로 이 비교용 타깃은 직접 만든다.
        CoreServiceImpl coreServiceTarget = new CoreServiceImpl();
        coreServiceTarget.setCoreDao(coreDao);
        assertThrows(DuplicateKeyException.class,
                () -> coreServiceTarget.addAll(List.of("alpha", "alpha")));

        assertEquals(List.of("alpha"), coreDao.getAll());
    }
}

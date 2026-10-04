package org.example2.Service;

import java.lang.reflect.Proxy;

import org.example2.Dao.TransactionHandler;
import org.example2.Dao.User;
import org.example2.Dao.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.PlatformTransactionManager;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class TransactionHandlerTest {
    private final UserService target = mock(UserService.class);
    private final PlatformTransactionManager transactionManager = mock(PlatformTransactionManager.class);

    @Test
    void delegatesUnmatchedMethodWithoutTransaction() {
        User user = new User();
        UserService proxy = createProxy();

        proxy.add(user);

        verify(target).add(user);
        verifyNoInteractions(transactionManager);
    }

    @Test
    void propagatesOriginalExceptionFromUnmatchedMethod() {
        User user = new User();
        IllegalArgumentException failure = new IllegalArgumentException("Invalid user");
        doThrow(failure).when(target).add(user);
        UserService proxy = createProxy();

        assertSame(failure, assertThrows(IllegalArgumentException.class, () -> proxy.add(user)));
        verifyNoInteractions(transactionManager);
    }

    private UserService createProxy() {
        TransactionHandler handler = new TransactionHandler();
        handler.setTarget(target);
        handler.setTransactionManager(transactionManager);
        handler.setPattern("upgradeLevels");
        return (UserService) Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class<?>[]{UserService.class}, handler);
    }
}

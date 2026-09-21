package com.seminarhub.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.seminarhub.data.DataTestApplication;
import com.seminarhub.global.domain.base.AuditMetadata;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        classes = DataTestApplication.class,
        properties = {
            "spring.datasource.url=jdbc:h2:mem:data-contract;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;NON_KEYWORDS=MEMBER",
            "spring.datasource.driver-class-name=org.h2.Driver",
            "spring.jpa.hibernate.ddl-auto=create-drop"
        })
class DataLayerContractTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void everyEntityHasIndependentRepositoryQueryAndServiceLayers() throws ReflectiveOperationException {
        assertThat(entityManager.getMetamodel().getEntities()).hasSize(11);

        for (EntityType<?> entityType : entityManager.getMetamodel().getEntities()) {
            Class<?> entity = entityType.getJavaType();
            String entityName = entity.getSimpleName();
            String domainPackage = entity.getPackageName().substring(0, entity.getPackageName().lastIndexOf(".domain"));

            Class<?> repository = Class.forName(domainPackage + ".repository." + entityName + "Repository");
            Class<?> custom = Class.forName(domainPackage + ".repository.querydsl." + entityName + "RepositoryCustom");
            Class<?> implementation = Class.forName(domainPackage + ".repository.querydsl." + entityName + "RepositoryImpl");
            Class<?> searchQuery = Class.forName(domainPackage + ".service." + entityName + "SearchQuery");
            Class<?> service = Class.forName(domainPackage + ".service." + entityName + "Service");

            assertThat(AuditMetadata.class).isAssignableFrom(entity);
            assertThat(repository.getInterfaces()).contains(custom);
            assertThat(custom.getDeclaredMethods()).hasSize(3);
            assertThat(implementation.getInterfaces()).contains(custom);
            assertThat(searchQuery.isRecord()).isTrue();
            assertThat(searchQuery.getDeclaredMethod("empty")).isNotNull();
            assertThat(methodNames(service)).contains(
                    "save", "findById", "findByIds", "findAll", "findByCursor", "update", "delete", "deleteAll");
            assertThat(methodNames(entity)).contains("update", "delete");
        }
    }

    private String[] methodNames(Class<?> type) {
        return Arrays.stream(type.getDeclaredMethods()).map(Method::getName).toArray(String[]::new);
    }
}

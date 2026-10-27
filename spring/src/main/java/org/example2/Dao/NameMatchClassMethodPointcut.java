package org.example2.Dao;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.util.PatternMatchUtils;

public class NameMatchClassMethodPointcut extends NameMatchMethodPointcut {

    public void setMappedClassName(String mappedClassName) {
        // 모든 클래스를 허용하는 기본 필터를 클래스 이름 패턴으로 비교하는 필터로 교체한다.
        setClassFilter(new SimpleClassFilter(mappedClassName));
    }

    private static class SimpleClassFilter implements ClassFilter {
        private final String mappedName;

        private SimpleClassFilter(String mappedName) {
            this.mappedName = mappedName;
        }

        @Override
        public boolean matches(Class<?> clazz) {
            // 패키지를 제외한 클래스 이름에 대해 정확한 이름과 HelloT*, *Service, *Service* 등을 비교한다.
            return PatternMatchUtils.simpleMatch(mappedName, clazz.getSimpleName());
        }
    }
}

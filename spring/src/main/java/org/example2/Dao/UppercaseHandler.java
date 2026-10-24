package org.example2.Dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {
    // Hello target;

    // public UppercaseHandler(Hello target){
    //     this.target = target;
    // }

    // @Override
    // public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
    //     String ret = (String) method.invoke(target, args);
    //     return ret.toUpperCase();
    // }

    Object target; // 어떤 종류의 인터페이스를 구현한 타깃에도 적용 가능하도록 Object 타입으로 수정
    public UppercaseHandler(Object target){
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = method.invoke(target, args);
        if( ret instanceof String && method.getName().startsWith("say")) { // 리턴타입과 메소드 이름이 일치하는 경우에만 부가기능을 적용한다.
            return ((String)ret).toUpperCase();
        } else {
            return ret; // 조건이 일치하지 않으면 타깃 오브제긑의 호출 결과를 그대로 리턴한다. 
        }
    }
}

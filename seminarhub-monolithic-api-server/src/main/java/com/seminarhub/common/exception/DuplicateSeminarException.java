package com.seminarhub.common.exception;

import javassist.CannotCompileException;

/**
 * [ 2023-08-21 daeho.kang ]
 * Description : SeminarHub에서 사용하는 DuplicateSeminarException Custom Excpeion 입니다.
 * 기본적으로 Java에서 제공하는 Exception 을 사용하지만, 해당 Exception 이 존재하지 않을시 새로운 에러객체를 만들어 진행합니다.
 * ( Exception 이 없을시, Exception 패키지에 새로 해당 클래스를 만듭니다. )
 * 중복된 세미나일시 예외처리합니다.
 */
public class DuplicateSeminarException extends CannotCompileException {
    /** default serialVersionUID */
    private static final long serialVersionUID = 1L;

    public DuplicateSeminarException(String msg) {
        super(msg);
    }
}

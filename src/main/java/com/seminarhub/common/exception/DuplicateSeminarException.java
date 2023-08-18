package com.seminarhub.common.exception;

import javassist.CannotCompileException;

public class DuplicateSeminarException extends CannotCompileException {
    /** default serialVersionUID */
    private static final long serialVersionUID = 1L;

    public DuplicateSeminarException(String msg) {
        super(msg);
    }
}

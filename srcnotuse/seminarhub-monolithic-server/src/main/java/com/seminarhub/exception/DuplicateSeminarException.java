package com.seminarhub.exception;

public class DuplicateSeminarException extends RuntimeException {
    /** default serialVersionUID */
    private static final long serialVersionUID = 1L;

    public DuplicateSeminarException(String msg) {
        super(msg);
    }
}

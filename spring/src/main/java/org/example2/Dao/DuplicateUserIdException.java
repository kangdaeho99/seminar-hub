package org.example2.Dao;

public class DuplicateUserIdException extends RuntimeException {    
    public DuplicateUserIdException(Throwable cause) {
        super(cause);
    }
}

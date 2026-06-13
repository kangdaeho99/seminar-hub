package com.seminarhub.feign.exception;

import feign.FeignException;

/**
 * [2023-08-30 daeho.kang]
 * Description: Custom exception class representing errors that occur when a server-side error is encountered during interactions with a Feign client.
 * Extends the RuntimeException class and provides a constructor for specifying error messages.
 */
public class FeignServerErrorException extends RuntimeException {
    /** default serialVersionUID */
    private static final long serialVersionUID = 1L;

    public FeignServerErrorException(String message) {
        super(message);
    }
}

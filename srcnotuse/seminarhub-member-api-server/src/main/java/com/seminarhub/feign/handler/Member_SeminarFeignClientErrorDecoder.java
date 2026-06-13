package com.seminarhub.feign.handler;

import com.seminarhub.feign.exception.FeignClientErrorException;
import com.seminarhub.feign.exception.FeignServerErrorException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

/**
 * [2023-08-30 daeho.kang]
 * Description: Custom error decoder class for handling errors in the Member_SeminarFeignClient.
 * This class implements the ErrorDecoder interface provided by Feign and is responsible for decoding and handling different types of errors.
 * It logs error details and constructs specific exceptions based on the error status codes.
 */
@Log4j2
public class Member_SeminarFeignClientErrorDecoder implements ErrorDecoder {

    private ErrorDecoder errorDecoder = new Default();


    /**
     * [ 2023-08-30 daeho.kang ]
     * Description : Decodes the error response and constructs an appropriate exception based on the error status code.
     *
     * @return An exception representing the decoded error.
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("{} 요청이 성공하지 못했습니다. status: {} requestUrl: {}, requestBody: {}, responseBody: {}",
                response.status(), methodKey, response.request().url(), response.request().body(), response.body());

        int status = response.status();


        if (status >= 400 && status <= 499) {
//            return new FeignClientErrorException("Client Error - " + response.reason());
            throw new RetryableException(response.status(), "server", response.request().httpMethod(), null, response.request());
        } else if (status >= 500 && status <= 599) {
            return new FeignServerErrorException("Server Error - " + response.reason());
        }

        return errorDecoder.decode(methodKey, response);
    }
}

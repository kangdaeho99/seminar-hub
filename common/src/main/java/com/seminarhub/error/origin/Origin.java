package com.seminarhub.error.origin;

import com.seminarhub.dto.ErrorResponse;
import org.springframework.lang.Nullable;

/** {@link ErrorResponse}의 에러 출처, 코드, 메시지를 제공한다. */
public interface Origin {

    OriginType getOrigin();

    int getCode();

    @Nullable
    String getMessage();
}

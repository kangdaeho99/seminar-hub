package com.seminarhub.global.resolver;

import com.seminarhub.error.BadRequestException;
import com.seminarhub.global.annotation.CursorDefault;
import com.seminarhub.global.dto.CursorRequest;
import com.seminarhub.global.dto.Direction;
import java.util.Locale;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CursorRequestArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CursorDefault.class)
                && CursorRequest.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public CursorRequest resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        CursorDefault defaults = parameter.getParameterAnnotation(CursorDefault.class);
        if (defaults == null) throw new IllegalStateException("CursorDefault annotation is required.");
        return CursorRequest.of(
                parseSize(webRequest.getParameter("size"), defaults.size()),
                parseCursor(webRequest.getParameter("cursor")),
                parseDirection(webRequest.getParameter("direction"), defaults.direction()));
    }

    private int parseSize(String value, int defaultValue) {
        if (!StringUtils.hasText(value)) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new BadRequestException("size는 정수여야 합니다.", exception);
        }
    }

    private Long parseCursor(String value) {
        if (!StringUtils.hasText(value)) return null;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException exception) {
            throw new BadRequestException("cursor는 정수여야 합니다.", exception);
        }
    }

    private Direction parseDirection(String value, Direction defaultValue) {
        if (!StringUtils.hasText(value)) return defaultValue;
        try {
            return Direction.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("direction은 ASC 또는 DESC여야 합니다.", exception);
        }
    }
}

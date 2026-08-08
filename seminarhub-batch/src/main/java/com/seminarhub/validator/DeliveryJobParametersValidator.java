package com.seminarhub.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

// ==========================================
// Job 파라미터 커스텀 검증기 분리
// ==========================================
public class DeliveryJobParametersValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        // 1. 파라미터 객체 자체가 없는 경우 방어
        if (parameters == null) {
            throw new JobParametersInvalidException("JobParameters가 존재하지 않습니다.");
        }

        // 2. 파라미터 값 추출
        Object startAt = parameters.getParameter("startAt");
        Object endAt = parameters.getParameter("endAt");

        // 3. 필수 키 누락 검증 및 상세 에러 메시지 부여
        if (startAt == null) {
            throw new JobParametersInvalidException("필수 파라미터 누락: startAt이 필요합니다.");
        }

        if (endAt == null) {
            throw new JobParametersInvalidException("필수 파라미터 누락: endAt이 필요합니다.");
        }

    }
}

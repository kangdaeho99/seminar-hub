package com.seminarhub.batch.delivery.validator;

import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersValidator;

import java.time.LocalDateTime;

// ==========================================
// Job 파라미터 커스텀 검증기 분리
// ==========================================
public class DeliveryJobParametersValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws InvalidJobParametersException {
        // 1. 파라미터 객체 자체가 없는 경우 방어
        if (parameters == null) {
            throw new InvalidJobParametersException("JobParameters가 존재하지 않습니다.");
        }

        // 2. 파라미터 값 추출 (타입 세이프 Getter)
        LocalDateTime startAt = parameters.getLocalDateTime("startAt");
        LocalDateTime endAt = parameters.getLocalDateTime("endAt");

        // 3. 필수 키 누락 검증 및 상세 에러 메시지 부여
        if (startAt == null) {
            throw new InvalidJobParametersException("필수 파라미터 누락: startAt이 필요합니다.");
        }

        if (endAt == null) {
            throw new InvalidJobParametersException("필수 파라미터 누락: endAt이 필요합니다.");
        }
    }
}

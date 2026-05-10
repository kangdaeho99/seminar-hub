package com.seminarhub.service;

import com.seminarhub.dto.SettlementApiResponse;
import com.seminarhub.dto.SettlementOperation;
import com.seminarhub.dto.SettlementStrategy;
import com.seminarhub.dto.SettlementDateUpdateRequest;
import com.seminarhub.entity.MemberSeminarSettlementDate;
import com.seminarhub.exception.SettlementDateNotFoundException;
import com.seminarhub.exception.TransactionInterruptedException;
import com.seminarhub.repository.MemberSeminarSettlementDateRepository;
import com.seminarhub.repository.SettlementAggregationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementService {

    private static final long SLEEP_MILLIS = 5000L;

    private final MemberSeminarSettlementDateRepository settlementDateRepository;
    private final SettlementAggregationRepository settlementAggregationRepository;

    @Transactional
    public SettlementApiResponse updateWithPessimisticLock(SettlementDateUpdateRequest request) {
        validateUpdateRequest(request);
        LocalDateTime startedAt = LocalDateTime.now();
        log.info("[PESSIMISTIC][UPDATE] start settlementDateId={}, targetDate={}", request.settlementDateId(), request.targetDate());

        MemberSeminarSettlementDate settlementDate = settlementDateRepository.findByIdForUpdate(request.settlementDateId())
                .orElseThrow(() -> new SettlementDateNotFoundException(request.settlementDateId()));

        LocalDate previousDate = settlementDate.getDate();
        log.info("[PESSIMISTIC][UPDATE] row locked settlementDateId={}, previousDate={}", request.settlementDateId(), previousDate);

        sleepInsideTransaction("PESSIMISTIC_UPDATE");

        settlementDate.updateDate(request.targetDate());
        log.info("[PESSIMISTIC][UPDATE] date updated settlementDateId={}, resultDate={}", request.settlementDateId(), settlementDate.getDate());

        return SettlementApiResponse.builder()
                .strategy(SettlementStrategy.PESSIMISTIC_WRITE_READ)
                .operation(SettlementOperation.UPDATE)
                .settlementDateId(request.settlementDateId())
                .targetDate(request.targetDate())
                .previousDate(previousDate)
                .resultDate(settlementDate.getDate())
                .threadName(Thread.currentThread().getName())
                .transactionStartAt(startedAt)
                .transactionEndAt(LocalDateTime.now())
                .sleepMillis(SLEEP_MILLIS)
                .lockMode("PESSIMISTIC_WRITE")
                .message("Updated settlement date with pessimistic write lock.")
                .build();
    }

    @Transactional
    public SettlementApiResponse aggregateWithPessimisticLock(LocalDate startAt, LocalDate endAt) {
        validateDateRange(startAt, endAt);
        LocalDateTime transactionStartedAt = LocalDateTime.now();
        log.info("[PESSIMISTIC][AGGREGATE] start startAt={}, endAt={}", startAt, endAt);

        List<MemberSeminarSettlementDate> lockedRows = settlementDateRepository.findAllByDateBetweenForShareLock(startAt, endAt);
        log.info("[PESSIMISTIC][AGGREGATE] rows locked startAt={}, endAt={}, lockedRowCount={}", startAt, endAt, lockedRows.size());

        sleepInsideTransaction("PESSIMISTIC_AGGREGATE");

        Long totalSeminarPrice = settlementAggregationRepository.sumSeminarPriceBySettlementDateBetween(startAt, endAt);
        log.info("[PESSIMISTIC][AGGREGATE] sum completed startAt={}, endAt={}, totalSeminarPrice={}", startAt, endAt, totalSeminarPrice);

        return SettlementApiResponse.builder()
                .strategy(SettlementStrategy.PESSIMISTIC_WRITE_READ)
                .operation(SettlementOperation.AGGREGATE)
                .startAt(startAt)
                .endAt(endAt)
                .totalSeminarPrice(totalSeminarPrice)
                .threadName(Thread.currentThread().getName())
                .transactionStartAt(transactionStartedAt)
                .transactionEndAt(LocalDateTime.now())
                .sleepMillis(SLEEP_MILLIS)
                .lockMode("PESSIMISTIC_READ")
                .lockedRowCount(lockedRows.size())
                .message("Aggregated seminar prices after applying pessimistic read lock on settlement rows.")
                .build();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public SettlementApiResponse updateWithReadCommitted(SettlementDateUpdateRequest request) {
        return updateWithIsolation(request, SettlementStrategy.READ_COMMITTED, Isolation.READ_COMMITTED);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public SettlementApiResponse aggregateWithReadCommitted(LocalDate startAt, LocalDate endAt) {
        return aggregateWithIsolation(startAt, endAt, SettlementStrategy.READ_COMMITTED, Isolation.READ_COMMITTED);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public SettlementApiResponse updateWithRepeatableRead(SettlementDateUpdateRequest request) {
        return updateWithIsolation(request, SettlementStrategy.REPEATABLE_READ, Isolation.REPEATABLE_READ);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public SettlementApiResponse aggregateWithRepeatableRead(LocalDate startAt, LocalDate endAt) {
        return aggregateWithIsolation(startAt, endAt, SettlementStrategy.REPEATABLE_READ, Isolation.REPEATABLE_READ);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public SettlementApiResponse updateWithSerializable(SettlementDateUpdateRequest request) {
        return updateWithIsolation(request, SettlementStrategy.SERIALIZABLE, Isolation.SERIALIZABLE);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public SettlementApiResponse aggregateWithSerializable(LocalDate startAt, LocalDate endAt) {
        return aggregateWithIsolation(startAt, endAt, SettlementStrategy.SERIALIZABLE, Isolation.SERIALIZABLE);
    }

    private SettlementApiResponse updateWithIsolation(
            SettlementDateUpdateRequest request,
            SettlementStrategy strategy,
            Isolation isolation
    ) {
        validateUpdateRequest(request);
        LocalDateTime startedAt = LocalDateTime.now();
        log.info("[{}][UPDATE] start settlementDateId={}, targetDate={}, isolation={}",
                strategy, request.settlementDateId(), request.targetDate(), isolation);

        MemberSeminarSettlementDate settlementDate = settlementDateRepository.findByIdWithoutLock(request.settlementDateId())
                .orElseThrow(() -> new SettlementDateNotFoundException(request.settlementDateId()));

        LocalDate previousDate = settlementDate.getDate();
        log.info("[{}][UPDATE] row loaded settlementDateId={}, previousDate={}", strategy, request.settlementDateId(), previousDate);

        sleepInsideTransaction(strategy + "_UPDATE");

        settlementDate.updateDate(request.targetDate());
        log.info("[{}][UPDATE] date updated settlementDateId={}, resultDate={}", strategy, request.settlementDateId(), settlementDate.getDate());

        return SettlementApiResponse.builder()
                .strategy(strategy)
                .operation(SettlementOperation.UPDATE)
                .settlementDateId(request.settlementDateId())
                .targetDate(request.targetDate())
                .previousDate(previousDate)
                .resultDate(settlementDate.getDate())
                .threadName(Thread.currentThread().getName())
                .transactionStartAt(startedAt)
                .transactionEndAt(LocalDateTime.now())
                .sleepMillis(SLEEP_MILLIS)
                .isolationLevel(isolation.name())
                .message("Updated settlement date with transaction isolation " + isolation.name() + ".")
                .build();
    }

    private SettlementApiResponse aggregateWithIsolation(
            LocalDate startAt,
            LocalDate endAt,
            SettlementStrategy strategy,
            Isolation isolation
    ) {
        validateDateRange(startAt, endAt);
        LocalDateTime transactionStartedAt = LocalDateTime.now();
        log.info("[{}][AGGREGATE] start startAt={}, endAt={}, isolation={}", strategy, startAt, endAt, isolation);

        sleepInsideTransaction(strategy + "_AGGREGATE");

        Long totalSeminarPrice = settlementAggregationRepository.sumSeminarPriceBySettlementDateBetween(startAt, endAt);
        log.info("[{}][AGGREGATE] sum completed startAt={}, endAt={}, totalSeminarPrice={}", strategy, startAt, endAt, totalSeminarPrice);

        return SettlementApiResponse.builder()
                .strategy(strategy)
                .operation(SettlementOperation.AGGREGATE)
                .startAt(startAt)
                .endAt(endAt)
                .totalSeminarPrice(totalSeminarPrice)
                .threadName(Thread.currentThread().getName())
                .transactionStartAt(transactionStartedAt)
                .transactionEndAt(LocalDateTime.now())
                .sleepMillis(SLEEP_MILLIS)
                .isolationLevel(isolation.name())
                .message("Aggregated seminar prices with transaction isolation " + isolation.name() + ".")
                .build();
    }

    private void sleepInsideTransaction(String context) {
        log.info("[{}] sleeping inside transaction for {} ms", context, SLEEP_MILLIS);
        try {
            Thread.sleep(SLEEP_MILLIS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new TransactionInterruptedException("Transaction interrupted while waiting for concurrency observation.", exception);
        }
    }

    private void validateUpdateRequest(SettlementDateUpdateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Update request must not be null.");
        }
        if (request.settlementDateId() == null) {
            throw new IllegalArgumentException("settlementDateId must not be null.");
        }
        validateDate(request.targetDate(), "targetDate");
    }

    private void validateDateRange(LocalDate startAt, LocalDate endAt) {
        validateDate(startAt, "startAt");
        validateDate(endAt, "endAt");
        if (startAt.isAfter(endAt)) {
            throw new IllegalArgumentException("startAt must be before or equal to endAt.");
        }
    }

    private void validateDate(LocalDate date, String fieldName) {
        if (date == null) {
            throw new IllegalArgumentException(fieldName + " must not be null.");
        }
    }
}

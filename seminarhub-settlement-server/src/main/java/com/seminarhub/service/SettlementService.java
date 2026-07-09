package com.seminarhub.service;

import com.seminarhub.dto.SettlementDateUpdateRequest;
import com.seminarhub.dto.SettlementRecord;
import com.seminarhub.dto.SettlementRecordProjection;
import com.seminarhub.entity.MemberSeminar;
import com.seminarhub.entity.Settlement;
import com.seminarhub.entity.SettlementItem;
import com.seminarhub.exception.SettlementDateNotFoundException;
import com.seminarhub.repository.MemberSeminarRepository;
import com.seminarhub.repository.MemberSeminarSettlementDateRepository;
import com.seminarhub.repository.SettlementAggregationRepository;
import com.seminarhub.repository.SettlementItemRepository;
import com.seminarhub.repository.SettlementRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import org.hibernate.engine.spi.ActionQueue;
import org.hibernate.internal.SessionImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.EntityManager;
import org.hibernate.engine.spi.ActionQueue;
import org.hibernate.engine.spi.PersistenceContext;
import org.hibernate.internal.SessionImpl;
import lombok.extern.slf4j.Slf4j;
import com.seminarhub.repository.SettlementItemJdbcRepository;
import com.seminarhub.repository.MemberSeminarSettlementDateJdbcRepository;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.CannotAcquireLockException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementService {

    private final MemberSeminarSettlementDateRepository settlementDateRepository;
    private final SettlementAggregationRepository settlementAggregationRepository;
    private final SettlementRepository settlementRepository;
    private final SettlementItemRepository settlementItemRepository;
    private final MemberSeminarRepository memberSeminarRepository;

    // 영속성 컨텍스트를 꺼내기 위해 추가
    private final EntityManager entityManager;
    private final SettlementItemJdbcRepository settlementItemJdbcRepository;
    private final MemberSeminarSettlementDateJdbcRepository memberSeminarSettlementDateJdbcRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateWithReadCommitted(SettlementDateUpdateRequest request) {
        settlementDateRepository.updateDateByMemberSeminarIdIfNotSettled(request.memberSeminarId(), request.targetDate());
    }


    // private void processSettlement(LocalDate startAt, LocalDate endAt, List<SettlementRecord> records) {
    //     if (records == null || records.isEmpty()) return;

    //     long totalAmount = records.stream().mapToLong(SettlementRecord::price).sum();

    //     Settlement settlement = Settlement.builder()
    //             .startDate(startAt)
    //             .endDate(endAt)
    //             .amount(BigDecimal.valueOf(totalAmount))
    //             .settlement_status(com.seminarhub.enums.SettlementStatus.COMPLETED)
    //             .build();

    //     settlementRepository.save(settlement);

    //     List<SettlementItem> items = records.stream().map(record -> {
    //         MemberSeminar ms = memberSeminarRepository.getReferenceById(record.memberSeminarId());
    //         return SettlementItem.builder()
    //                 .settlement(settlement)
    //                 .memberSeminar(ms)
    //                 .amount(BigDecimal.valueOf(record.price()))
    //                 .build();
    //     }).toList();

    //     // =================================================================
    //     // [디버깅] saveAll() 실행 전 영속성 컨텍스트 상태 확인
    //     // =================================================================
    //     SessionImpl session = entityManager.unwrap(SessionImpl.class);
    //     PersistenceContext pc = session.getPersistenceContext();
    //     ActionQueue actionQueue = session.getActionQueue();

    //     log.info("▶ [saveAll 실행 전] 1차 캐시 관리 객체 수: {}", pc.getNumberOfManagedEntities());
    //     log.info("▶ [saveAll 실행 전] ActionQueue 대기 중인 Insert 쿼리 수: {}", actionQueue.numberOfInsertions());

    //     // 실제 저장 로직 실행
    //     settlementItemRepository.saveAll(items);

    //     // =================================================================
    //     // [디버깅] saveAll() 실행 후 영속성 컨텍스트 상태 확인
    //     // =================================================================
    //     log.info("▶ [saveAll 실행 후] 1차 캐시 관리 객체 수: {}", pc.getNumberOfManagedEntities());
    //     log.info("▶ [saveAll 실행 후] ActionQueue 대기 중인 Insert 쿼리 수: {}", actionQueue.numberOfInsertions());
    // }

    private void processSettlement(LocalDate startAt, LocalDate endAt, List<SettlementRecord> records) {
        if (records == null || records.isEmpty()) return;

        long totalAmount = records.stream().mapToLong(SettlementRecord::price).sum();

        Settlement settlement = Settlement.builder()
                .startDate(startAt)
                .endDate(endAt)
                .amount(BigDecimal.valueOf(totalAmount))
                .settlement_status(com.seminarhub.enums.SettlementStatus.COMPLETED)
                .build();

        settlementRepository.save(settlement);

        SessionImpl session = entityManager.unwrap(SessionImpl.class);
        PersistenceContext pc = session.getPersistenceContext();
        ActionQueue actionQueue = session.getActionQueue();

        log.info("▶ [batchUpdate 실행 전] 1차 캐시 관리 객체 수: {}", pc.getNumberOfManagedEntities());
        log.info("▶ [batchUpdate 실행 전] ActionQueue 대기 중인 Insert 쿼리 수: {}", actionQueue.numberOfInsertions());

        memberSeminarSettlementDateJdbcRepository.updateMemberSeminarSettlementDateToTriggerLock(records);

        settlementItemJdbcRepository.insertSettlementItems(settlement.getId(), records);

        log.info("▶ [batchUpdate 실행 후] 1차 캐시 관리 객체 수: {}", pc.getNumberOfManagedEntities());
        log.info("▶ [batchUpdate 실행 후] ActionQueue 대기 중인 Insert 쿼리 수: {}", actionQueue.numberOfInsertions());
    }

    
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void aggregateWithReadCommitted(LocalDate startAt, LocalDate endAt) {
        List<SettlementRecordProjection> projections = settlementAggregationRepository.findAggregateTarget(startAt, endAt);
        processSettlement(startAt, endAt, mapToSettlementRecords(projections));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void aggregateWithReadCommittedPessimisticWrite(LocalDate startAt, LocalDate endAt) {
        List<SettlementRecordProjection> projections = settlementAggregationRepository.findAggregateTargetForUpdate(startAt, endAt);
        processSettlement(startAt, endAt, mapToSettlementRecords(projections));
    }

    @Retryable(
        retryFor = { CannotAcquireLockException.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000) // 매 재시도마다 고정적으로 1초 대기
    )
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void aggregateWithRepeatableRead(LocalDate startAt, LocalDate endAt) {
        List<SettlementRecordProjection> projections = settlementAggregationRepository.findAggregateTarget(startAt, endAt);
        processSettlement(startAt, endAt, mapToSettlementRecords(projections));
    }

//     @Transactional(isolation = Isolation.REPEATABLE_READ)
//     public void aggregateWithRepeatableRead(LocalDate startAt, LocalDate endAt) {
//     try {
//         List<SettlementRecordProjection> projections = settlementAggregationRepository.findAggregateTarget(startAt, endAt);
//         processSettlement(startAt, endAt, mapToSettlementRecords(projections));
//     } catch (Exception e) {
//         System.out.println("발생한 예외 타입: " + e.getClass().getName());
//         throw e;
//     }
// }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void aggregateWithSerializable(LocalDate startAt, LocalDate endAt) {
        List<SettlementRecordProjection> projections = settlementAggregationRepository.findAggregateTarget(startAt, endAt);
        processSettlement(startAt, endAt, mapToSettlementRecords(projections));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void aggregateWithRepeatableReadPessimisticWrite(LocalDate startAt, LocalDate endAt) {
        List<SettlementRecordProjection> projections = settlementAggregationRepository.findAggregateTargetForUpdate(startAt, endAt);
        processSettlement(startAt, endAt, mapToSettlementRecords(projections));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void aggregateWithSerializablePessimisticWrite(LocalDate startAt, LocalDate endAt) {
        List<SettlementRecordProjection> projections = settlementAggregationRepository.findAggregateTargetForUpdate(startAt, endAt);
        processSettlement(startAt, endAt, mapToSettlementRecords(projections));
    }
    private List<SettlementRecord> mapToSettlementRecords(List<SettlementRecordProjection> projections) {
        return projections.stream()
                .map(p -> new SettlementRecord(
                        p.getMemberSeminarId(),
                        p.getPrice(),
                        p.getSettlementDateId()
                )).toList();
    }
}
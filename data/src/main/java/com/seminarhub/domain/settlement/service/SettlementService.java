package com.seminarhub.domain.settlement.service;

import com.seminarhub.dto.SettlementRecord;
import com.seminarhub.dto.SettlementRecordProjection;
import com.seminarhub.entity.Settlement;
import com.seminarhub.enums.SettlementStatus;
import com.seminarhub.global.dto.CursorRequest;
import com.seminarhub.repository.MemberSeminarSettlementDateJdbcRepository;
import com.seminarhub.repository.MemberSeminarSettlementDateRepository;
import com.seminarhub.repository.SettlementAggregationRepository;
import com.seminarhub.repository.SettlementItemJdbcRepository;
import com.seminarhub.repository.SettlementRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.spi.ActionQueue;
import org.hibernate.engine.spi.PersistenceContext;
import org.hibernate.internal.SessionImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettlementService {

    private final MemberSeminarSettlementDateRepository settlementDateRepository;
    private final SettlementAggregationRepository settlementAggregationRepository;
    private final SettlementRepository settlementRepository;
    private final EntityManager entityManager;
    private final SettlementItemJdbcRepository settlementItemJdbcRepository;
    private final MemberSeminarSettlementDateJdbcRepository memberSeminarSettlementDateJdbcRepository;

    public List<Settlement> findAll(SettlementSearchQuery query) {
        return settlementRepository.search(query);
    }

    public Page<Settlement> findAll(SettlementSearchQuery query, Pageable pageable) {
        return settlementRepository.search(query, pageable);
    }

    public List<Settlement> findByCursor(SettlementSearchQuery query, CursorRequest cursorRequest) {
        return settlementRepository.search(query, cursorRequest);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateWithReadCommitted(Long memberSeminarId, LocalDate targetDate) {
        settlementDateRepository.updateDateByMemberSeminarIdIfNotSettled(memberSeminarId, targetDate);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void aggregateWithReadCommitted(LocalDate startAt, LocalDate endAt) {
        processSettlement(startAt, endAt, settlementAggregationRepository.findAggregateTarget(startAt, endAt));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void aggregateWithReadCommittedPessimisticWrite(LocalDate startAt, LocalDate endAt) {
        processSettlement(startAt, endAt, settlementAggregationRepository.findAggregateTargetForUpdate(startAt, endAt));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void aggregateWithRepeatableRead(LocalDate startAt, LocalDate endAt) {
        processSettlement(startAt, endAt, settlementAggregationRepository.findAggregateTarget(startAt, endAt));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void aggregateWithSerializable(LocalDate startAt, LocalDate endAt) {
        processSettlement(startAt, endAt, settlementAggregationRepository.findAggregateTarget(startAt, endAt));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void aggregateWithRepeatableReadPessimisticWrite(LocalDate startAt, LocalDate endAt) {
        processSettlement(startAt, endAt, settlementAggregationRepository.findAggregateTargetForUpdate(startAt, endAt));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void aggregateWithSerializablePessimisticWrite(LocalDate startAt, LocalDate endAt) {
        processSettlement(startAt, endAt, settlementAggregationRepository.findAggregateTargetForUpdate(startAt, endAt));
    }

    private void processSettlement(
            LocalDate startAt, LocalDate endAt, List<SettlementRecordProjection> projections) {
        List<SettlementRecord> records = projections.stream()
                .map(p -> new SettlementRecord(p.getMemberSeminarId(), p.getPrice(), p.getSettlementDateId()))
                .toList();
        if (records.isEmpty()) return;

        long totalAmount = records.stream().mapToLong(SettlementRecord::price).sum();
        Settlement settlement = Settlement.builder()
                .startDate(startAt)
                .endDate(endAt)
                .amount(BigDecimal.valueOf(totalAmount))
                .settlement_status(SettlementStatus.COMPLETED)
                .build();
        settlementRepository.save(settlement);

        SessionImpl session = entityManager.unwrap(SessionImpl.class);
        PersistenceContext persistenceContext = session.getPersistenceContext();
        ActionQueue actionQueue = session.getActionQueue();
        log.info("▶ [batchUpdate 실행 전] 1차 캐시 관리 객체 수: {}", persistenceContext.getNumberOfManagedEntities());
        log.info("▶ [batchUpdate 실행 전] ActionQueue 대기 중인 Insert 쿼리 수: {}", actionQueue.numberOfInsertions());

        memberSeminarSettlementDateJdbcRepository.updateMemberSeminarSettlementDateToTriggerLock(records);
        settlementItemJdbcRepository.insertSettlementItems(settlement.getId(), records);

        log.info("▶ [batchUpdate 실행 후] 1차 캐시 관리 객체 수: {}", persistenceContext.getNumberOfManagedEntities());
        log.info("▶ [batchUpdate 실행 후] ActionQueue 대기 중인 Insert 쿼리 수: {}", actionQueue.numberOfInsertions());
    }
}

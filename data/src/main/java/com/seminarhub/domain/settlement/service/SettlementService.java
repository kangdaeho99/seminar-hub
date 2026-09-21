package com.seminarhub.domain.settlement.service;

import com.seminarhub.domain.settlement.domain.Settlement;
import com.seminarhub.domain.settlement.enums.SettlementStatus;
import com.seminarhub.domain.settlement.repository.MemberSeminarSettlementDateJdbcRepository;
import com.seminarhub.domain.settlement.repository.SettlementAggregationRepository;
import com.seminarhub.domain.settlement.repository.SettlementItemJdbcRepository;
import com.seminarhub.domain.settlement.repository.SettlementRecord;
import com.seminarhub.domain.settlement.repository.SettlementRecordProjection;
import com.seminarhub.domain.settlement.repository.SettlementRepository;
import com.seminarhub.domain.seminar.repository.MemberSeminarSettlementDateRepository;
import com.seminarhub.global.dto.CursorRequest;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

    @Transactional
    public Settlement save(Settlement settlement) {
        return settlementRepository.save(settlement);
    }

    public Optional<Settlement> findById(Long id) {
        return settlementRepository.findByIdAndDeletedAtIsNull(id);
    }

    public List<Settlement> findByIds(List<Long> ids) {
        return settlementRepository.findAllByIdInAndDeletedAtIsNull(ids);
    }

    @Transactional
    public Settlement update(
            Settlement settlement,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal amount,
            SettlementStatus settlementStatus) {
        settlement.update(startDate, endDate, amount, settlementStatus);
        return settlement;
    }

    @Transactional
    public Settlement delete(Settlement settlement) {
        settlement.delete();
        return settlement;
    }

    @Transactional
    public List<Settlement> deleteAll(List<Settlement> settlements) {
        settlements.forEach(Settlement::delete);
        return settlements;
    }

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
                .settlementStatus(SettlementStatus.COMPLETED)
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

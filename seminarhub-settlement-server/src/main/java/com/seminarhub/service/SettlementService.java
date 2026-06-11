package com.seminarhub.service;

import com.seminarhub.dto.SettlementDateUpdateRequest;
import com.seminarhub.dto.SettlementRecord;
import com.seminarhub.dto.SettlementRecordProjection;
import com.seminarhub.entity.Member_Seminar;
import com.seminarhub.entity.Settlement;
import com.seminarhub.entity.SettlementItem;
import com.seminarhub.exception.SettlementDateNotFoundException;
import com.seminarhub.repository.MemberSeminarRepository;
import com.seminarhub.repository.MemberSeminarSettlementDateRepository;
import com.seminarhub.repository.SettlementAggregationRepository;
import com.seminarhub.repository.SettlementItemRepository;
import com.seminarhub.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final MemberSeminarSettlementDateRepository settlementDateRepository;
    private final SettlementAggregationRepository settlementAggregationRepository;
    private final SettlementRepository settlementRepository;
    private final SettlementItemRepository settlementItemRepository;
    private final MemberSeminarRepository memberSeminarRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateWithReadCommitted(SettlementDateUpdateRequest request) {
        settlementDateRepository.findByIdWithoutLock(request.settlementDateId())
                .orElseThrow(() -> new SettlementDateNotFoundException(request.settlementDateId()));

        settlementDateRepository.updateDateIfNotSettled(request.settlementDateId(), request.targetDate());
    }

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

        List<SettlementItem> items = records.stream().map(record -> {
            Member_Seminar ms = memberSeminarRepository.getReferenceById(record.memberSeminarNo());
            return SettlementItem.builder()
                    .settlement(settlement)
                    .memberSeminar(ms)
                    .amount(BigDecimal.valueOf(record.price()))
                    .build();
        }).toList();

        settlementItemRepository.saveAll(items);
    }

    private List<SettlementRecord> mapToSettlementRecords(List<SettlementRecordProjection> projections) {
        return projections.stream()
                .map(p -> new SettlementRecord(
                        p.getMemberSeminarNo(),
                        p.getSeminarPrice(),
                        p.getSettlementDateId()
                )).toList();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void aggregateWithReadCommitted(LocalDate startAt, LocalDate endAt) {
        List<SettlementRecord> records = settlementAggregationRepository.findAllBySettlementDateBetween(startAt, endAt);
        processSettlement(startAt, endAt, records);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void aggregateWithReadCommittedPessimisticWrite(LocalDate startAt, LocalDate endAt) {
        List<SettlementRecordProjection> projections = settlementDateRepository.findAllByDateBetweenForExclusiveLockNative(startAt, endAt);
        processSettlement(startAt, endAt, mapToSettlementRecords(projections));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void aggregateWithRepeatableRead(LocalDate startAt, LocalDate endAt) {
        List<SettlementRecord> records = settlementAggregationRepository.findAllBySettlementDateBetween(startAt, endAt);
        processSettlement(startAt, endAt, records);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void aggregateWithSerializable(LocalDate startAt, LocalDate endAt) {
        List<SettlementRecord> records = settlementAggregationRepository.findAllBySettlementDateBetween(startAt, endAt);
        processSettlement(startAt, endAt, records);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void aggregateWithRepeatableReadPessimisticWrite(LocalDate startAt, LocalDate endAt) {
        List<SettlementRecordProjection> projections = settlementDateRepository.findAllByDateBetweenForExclusiveLockNative(startAt, endAt);
        processSettlement(startAt, endAt, mapToSettlementRecords(projections));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void aggregateWithSerializablePessimisticWrite(LocalDate startAt, LocalDate endAt) {
        List<SettlementRecordProjection> projections = settlementDateRepository.findAllByDateBetweenForExclusiveLockNative(startAt, endAt);
        processSettlement(startAt, endAt, mapToSettlementRecords(projections));
    }
}
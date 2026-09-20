package com.seminarhub.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.seminarhub.domain.settlement.service.SettlementService;
import com.seminarhub.dto.SettlementCreateRequest;
import com.seminarhub.dto.SettlementUpdateRequest;
import com.seminarhub.entity.Settlement;
import com.seminarhub.enums.SettlementStatus;
import com.seminarhub.error.BadRequestException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SettlementUseCaseCrudTest {

    private SettlementService service;
    private SettlementUseCase useCase;

    @BeforeEach
    void setUp() {
        service = mock(SettlementService.class);
        useCase = new SettlementUseCase(service);
    }

    @Test
    void createsAndPartiallyUpdatesSettlement() {
        Settlement created = settlement(1L, LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30));
        when(service.save(any())).thenReturn(created);
        when(service.findById(1L)).thenReturn(Optional.of(created));
        when(service.update(any(), any(), any(), any(), any())).thenAnswer(invocation -> invocation.getArgument(0));

        Settlement result = useCase.create(new SettlementCreateRequest(
                LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 9, 30),
                BigDecimal.TEN,
                SettlementStatus.READY));
        assertEquals(1L, result.getId());

        useCase.update(1L, new SettlementUpdateRequest(
                null, LocalDate.of(2026, 10, 1), BigDecimal.valueOf(20), SettlementStatus.COMPLETED));
        verify(service).update(
                created, null, LocalDate.of(2026, 10, 1), BigDecimal.valueOf(20), SettlementStatus.COMPLETED);
    }

    @Test
    void rejectsAnInvalidEffectiveDateRange() {
        Settlement existing = settlement(1L, LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30));
        when(service.findById(1L)).thenReturn(Optional.of(existing));

        assertThrows(BadRequestException.class, () -> useCase.update(
                1L,
                new SettlementUpdateRequest(LocalDate.of(2026, 10, 1), null, null, null)));
    }

    @Test
    void preservesFirstInputOrderAndSkipsMissingBatchIds() {
        Settlement first = settlement(1L, LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30));
        Settlement third = settlement(3L, LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30));
        when(service.findByIds(List.of(3L, 1L, 2L))).thenReturn(List.of(first, third));

        List<Settlement> result = useCase.readAll(List.of(3L, 1L, 3L, 2L));

        assertEquals(List.of(3L, 1L), result.stream().map(Settlement::getId).toList());
    }

    @Test
    void rejectsMissingSingleSettlementAndSoftDeletesBatch() {
        when(service.findById(99L)).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> useCase.read(99L));

        Settlement first = settlement(1L, LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30));
        when(service.findByIds(List.of(1L))).thenReturn(List.of(first));
        when(service.deleteAll(List.of(first))).thenReturn(List.of(first));
        assertEquals(List.of(first), useCase.deleteAll(List.of(1L, 1L)));
    }

    private Settlement settlement(Long id, LocalDate startDate, LocalDate endDate) {
        return Settlement.builder()
                .id(id)
                .startDate(startDate)
                .endDate(endDate)
                .amount(BigDecimal.TEN)
                .settlement_status(SettlementStatus.READY)
                .build();
    }
}

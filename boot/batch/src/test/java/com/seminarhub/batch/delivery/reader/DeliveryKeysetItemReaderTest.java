package com.seminarhub.batch.delivery.reader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.seminarhub.domain.delivery.domain.Delivery;
import com.seminarhub.domain.delivery.service.DeliveryService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class DeliveryKeysetItemReaderTest {

    @Test
    void advancesTheKeysetAcrossPagesAndStopsWhenNoItemsRemain() {
        DeliveryService service = mock(DeliveryService.class);
        LocalDateTime startAt = LocalDateTime.of(2026, 9, 1, 0, 0);
        LocalDateTime endAt = LocalDateTime.of(2026, 9, 30, 23, 59);
        Delivery first = Delivery.builder().id(1L).build();
        Delivery second = Delivery.builder().id(2L).build();
        Delivery third = Delivery.builder().id(3L).build();
        when(service.findNextBatch(startAt, endAt, 0L, 2)).thenReturn(List.of(first, second));
        when(service.findNextBatch(startAt, endAt, 2L, 2)).thenReturn(List.of(third));
        when(service.findNextBatch(startAt, endAt, 3L, 2)).thenReturn(List.of());

        DeliveryKeysetItemReader reader = new DeliveryKeysetItemReader(service, startAt, endAt, 2);

        assertThat(reader.read()).isSameAs(first);
        assertThat(reader.read()).isSameAs(second);
        assertThat(reader.read()).isSameAs(third);
        assertThat(reader.read()).isNull();
    }
}

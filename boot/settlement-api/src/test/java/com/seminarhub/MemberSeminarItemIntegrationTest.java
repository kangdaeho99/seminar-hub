package com.seminarhub;

import static org.assertj.core.api.Assertions.assertThat;

import com.seminarhub.domain.delivery.domain.Delivery;
import com.seminarhub.domain.delivery.enums.DeliveryStatus;
import com.seminarhub.domain.delivery.repository.DeliveryRepository;
import com.seminarhub.domain.member.domain.Member;
import com.seminarhub.domain.member.repository.MemberRepository;
import com.seminarhub.domain.seminar.domain.MemberSeminar;
import com.seminarhub.domain.seminar.domain.MemberSeminarItem;
import com.seminarhub.domain.seminar.domain.MemberSeminarSettlementDate;
import com.seminarhub.domain.seminar.domain.Seminar;
import com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus;
import com.seminarhub.domain.seminar.enums.MemberSeminarStatus;
import com.seminarhub.domain.seminar.repository.MemberSeminarItemRepository;
import com.seminarhub.domain.seminar.repository.MemberSeminarRepository;
import com.seminarhub.domain.seminar.repository.MemberSeminarSettlementDateRepository;
import com.seminarhub.domain.seminar.repository.SeminarRepository;
import com.seminarhub.domain.seminar.service.MemberSeminarSearchQuery;
import com.seminarhub.domain.settlement.domain.Settlement;
import com.seminarhub.domain.settlement.domain.SettlementItem;
import com.seminarhub.domain.settlement.enums.SettlementStatus;
import com.seminarhub.domain.settlement.repository.SettlementAggregationRepository;
import com.seminarhub.domain.settlement.repository.SettlementItemRepository;
import com.seminarhub.domain.settlement.repository.SettlementRecordProjection;
import com.seminarhub.domain.settlement.repository.SettlementRepository;
import com.seminarhub.domain.settlement.service.SettlementService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MemberSeminarItemIntegrationTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private SeminarRepository seminarRepository;
    @Autowired private MemberSeminarRepository memberSeminarRepository;
    @Autowired private MemberSeminarItemRepository itemRepository;
    @Autowired private MemberSeminarSettlementDateRepository dateRepository;
    @Autowired private SettlementAggregationRepository aggregationRepository;
    @Autowired private SettlementRepository settlementRepository;
    @Autowired private SettlementItemRepository settlementItemRepository;
    @Autowired private SettlementService settlementService;
    @Autowired private DeliveryRepository deliveryRepository;

    @Test
    void usesItemsForSeminarsSettlementAndDeliveryWhileStatusesStayIndependent() {
        Member member = memberRepository.save(Member.builder().email("test-" + UUID.randomUUID()).build());
        MemberSeminar order = memberSeminarRepository.save(MemberSeminar.builder().member(member).build());
        Seminar firstSeminar = seminarRepository.save(Seminar.builder()
                .name("first-" + UUID.randomUUID()).price(100L).build());
        Seminar secondSeminar = seminarRepository.save(Seminar.builder()
                .name("second-" + UUID.randomUUID()).price(200L).build());
        MemberSeminarItem first = itemRepository.save(MemberSeminarItem.builder()
                .memberSeminar(order).seminar(firstSeminar).build());
        MemberSeminarItem second = itemRepository.save(MemberSeminarItem.builder()
                .memberSeminar(order).seminar(secondSeminar).build());

        assertThat(order.getOrderStatus()).isEqualTo(MemberSeminarStatus.ORDERED);
        assertThat(first.getMemberSeminarItemStatus()).isEqualTo(MemberSeminarItemStatus.ORDERED);
        assertThat(itemRepository.findAllByMemberSeminar_IdAndDeletedAtIsNull(order.getId()))
                .extracting(MemberSeminarItem::getId).containsExactlyInAnyOrder(first.getId(), second.getId());
        assertThat(memberSeminarRepository.search(new MemberSeminarSearchQuery(
                null, null, null, null, firstSeminar.getId(), null,
                null, null, null, null, null, null, null, null, null)))
                .extracting(MemberSeminar::getId).containsExactly(order.getId());

        first.syncStatus(MemberSeminarItemStatus.PAID);
        order.syncStatus(MemberSeminarStatus.PAID);
        assertThat(second.getMemberSeminarItemStatus()).isEqualTo(MemberSeminarItemStatus.ORDERED);
        assertThat(first.getMemberSeminarItemStatus()).isEqualTo(MemberSeminarItemStatus.PAID);

        LocalDate date = LocalDate.of(2026, 9, 20);
        MemberSeminarSettlementDate firstDate = new MemberSeminarSettlementDate(first);
        firstDate.updateDate(date);
        dateRepository.save(firstDate);
        MemberSeminarSettlementDate secondDate = new MemberSeminarSettlementDate(second);
        secondDate.updateDate(date);
        dateRepository.saveAndFlush(secondDate);

        List<SettlementRecordProjection> candidates = aggregationRepository.findAggregateTarget(date, date);
        assertThat(candidates).extracting(SettlementRecordProjection::getMemberSeminarItemId)
                .containsExactlyInAnyOrder(first.getId(), second.getId());
        assertThat(aggregationRepository.findAggregateTargetForUpdate(date, date))
                .extracting(SettlementRecordProjection::getMemberSeminarItemId)
                .containsExactlyInAnyOrder(first.getId(), second.getId());

        Settlement settlement = settlementRepository.save(Settlement.builder()
                .startDate(date).endDate(date).amount(BigDecimal.valueOf(100))
                .settlementStatus(SettlementStatus.READY).build());
        settlementItemRepository.saveAndFlush(SettlementItem.builder()
                .settlement(settlement).memberSeminarItem(first).amount(BigDecimal.valueOf(100)).build());

        assertThat(aggregationRepository.findAggregateTarget(date, date))
                .extracting(SettlementRecordProjection::getMemberSeminarItemId)
                .containsExactly(second.getId());
        assertThat(dateRepository.updateDateByMemberSeminarItemIdIfNotSettled(first.getId(), date.plusDays(1)))
                .isZero();
        assertThat(dateRepository.updateDateByMemberSeminarItemIdIfNotSettled(second.getId(), date.plusDays(1)))
                .isEqualTo(1);

        settlementService.aggregateWithReadCommitted(date.plusDays(1), date.plusDays(1));
        assertThat(settlementItemRepository.findAll())
                .extracting(item -> item.getMemberSeminarItem().getId())
                .containsExactlyInAnyOrder(first.getId(), second.getId());

        Delivery delivery = deliveryRepository.saveAndFlush(Delivery.builder()
                .memberSeminarItem(second).deliveryStatus(DeliveryStatus.PENDING).build());
        assertThat(deliveryRepository.findByMemberSeminarItem_Id(second.getId()))
                .get().extracting(Delivery::getId).isEqualTo(delivery.getId());
    }
}

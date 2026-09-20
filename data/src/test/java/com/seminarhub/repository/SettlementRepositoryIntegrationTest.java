package com.seminarhub.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.seminarhub.config.QuerydslConfig;
import com.seminarhub.domain.settlement.service.SettlementSearchQuery;
import com.seminarhub.domain.settlement.service.SettlementItemSearchQuery;
import com.seminarhub.entity.Member;
import com.seminarhub.entity.MemberSeminar;
import com.seminarhub.entity.Seminar;
import com.seminarhub.entity.Settlement;
import com.seminarhub.entity.SettlementItem;
import com.seminarhub.enums.SettlementStatus;
import com.seminarhub.global.dto.CursorRequest;
import com.seminarhub.global.dto.Direction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(
        classes = SettlementRepositoryIntegrationTest.TestApplication.class,
        properties = {
            "spring.datasource.url=jdbc:h2:mem:settlement;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;NON_KEYWORDS=MEMBER",
            "spring.datasource.driver-class-name=org.h2.Driver",
            "spring.jpa.hibernate.ddl-auto=create-drop"
        })
@Transactional
@ActiveProfiles("test")
class SettlementRepositoryIntegrationTest {

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @EntityScan("com.seminarhub.entity")
    @EnableJpaRepositories("com.seminarhub.repository")
    @Import(QuerydslConfig.class)
    static class TestApplication {}

    @org.springframework.beans.factory.annotation.Autowired
    SettlementRepository repository;

    @org.springframework.beans.factory.annotation.Autowired SettlementItemRepository itemRepository;
    @org.springframework.beans.factory.annotation.Autowired MemberRepository memberRepository;
    @org.springframework.beans.factory.annotation.Autowired SeminarRepository seminarRepository;
    @org.springframework.beans.factory.annotation.Autowired MemberSeminarRepository memberSeminarRepository;

    private Settlement oldest;
    private Settlement newest;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        oldest = repository.save(settlement(10, null));
        repository.save(settlement(20, LocalDateTime.of(2026, 1, 1, 0, 0)));
        newest = repository.save(settlement(30, null));
        repository.flush();
    }

    @Test
    void listFiltersDeletedRowsAndSupportsIds() {
        List<Settlement> active = repository.search(query(null));
        assertEquals(List.of(newest.getId(), oldest.getId()), active.stream().map(Settlement::getId).toList());

        List<Settlement> selected = repository.search(query(List.of(oldest.getId())));
        assertEquals(List.of(oldest.getId()), selected.stream().map(Settlement::getId).toList());
    }

    @Test
    void activeLookupsExcludeSoftDeletedRows() {
        Settlement deleted = repository.save(settlement(40, LocalDateTime.of(2026, 2, 1, 0, 0)));
        repository.flush();

        assertEquals(oldest.getId(), repository.findActiveById(oldest.getId()).orElseThrow().getId());
        assertTrue(repository.findActiveById(deleted.getId()).isEmpty());
        assertEquals(
                List.of(oldest.getId(), newest.getId()),
                repository.findActiveByIds(List.of(oldest.getId(), deleted.getId(), newest.getId())).stream()
                        .map(Settlement::getId)
                        .sorted()
                        .toList());
    }

    @Test
    void pageFallsBackToIdDescendingForUnknownSort() {
        Page<Settlement> page = repository.search(
                query(null), PageRequest.of(0, 1, Sort.by(Sort.Order.asc("unknown"))));
        assertEquals(newest.getId(), page.getContent().getFirst().getId());
        assertEquals(2, page.getTotalElements());
    }

    @Test
    void cursorUsesIdKeysetAndFetchesLookAhead() {
        List<Settlement> descending = repository.search(query(null), CursorRequest.of(1, null, Direction.DESC));
        assertEquals(2, descending.size());
        assertEquals(newest.getId(), descending.getFirst().getId());

        List<Settlement> ascending = repository.search(
                query(null), CursorRequest.of(10, oldest.getId(), Direction.ASC));
        assertEquals(List.of(newest.getId()), ascending.stream().map(Settlement::getId).toList());
    }

    @Test
    void settlementItemSupportsRelationshipIdFiltersAndCursor() {
        Member member = memberRepository.save(Member.builder().email("reader@example.com").build());
        Seminar seminar = seminarRepository.save(Seminar.builder().name("QueryDSL test").price(100L).build());
        MemberSeminar memberSeminar = memberSeminarRepository.save(
                MemberSeminar.builder().member(member).seminar(seminar).build());
        SettlementItem first = itemRepository.save(SettlementItem.builder()
                .settlement(oldest).memberSeminar(memberSeminar).amount(BigDecimal.ONE).build());
        SettlementItem second = itemRepository.save(SettlementItem.builder()
                .settlement(newest).memberSeminar(memberSeminar).amount(BigDecimal.TEN).build());
        itemRepository.flush();

        SettlementItemSearchQuery query = new SettlementItemSearchQuery(
                null, null, newest.getId(), List.of(newest.getId()), memberSeminar.getId(),
                List.of(memberSeminar.getId()), null, null, null, null, null, null, null, null, null, null, null,
                null);
        assertEquals(List.of(second.getId()), itemRepository.search(query).stream().map(SettlementItem::getId).toList());

        SettlementItemSearchQuery all = new SettlementItemSearchQuery(
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null);
        List<SettlementItem> fetched = itemRepository.search(all, CursorRequest.of(1, null, Direction.DESC));
        assertEquals(2, fetched.size());
        assertEquals(second.getId(), fetched.getFirst().getId());
        assertEquals(first.getId(), fetched.get(1).getId());
    }

    private Settlement settlement(long amount, LocalDateTime deletedAt) {
        return Settlement.builder()
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 1, 31))
                .amount(BigDecimal.valueOf(amount))
                .settlement_status(SettlementStatus.COMPLETED)
                .deleted_at(deletedAt)
                .build();
    }

    private SettlementSearchQuery query(List<Long> ids) {
        return new SettlementSearchQuery(
                null, ids, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null);
    }
}

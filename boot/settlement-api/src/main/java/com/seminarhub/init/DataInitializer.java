package com.seminarhub.init;

import com.seminarhub.entity.Member;
import com.seminarhub.entity.MemberSeminarSettlementDate;
import com.seminarhub.entity.MemberSeminar;
import com.seminarhub.entity.Seminar;
import com.seminarhub.repository.MemberRepository;
import com.seminarhub.repository.MemberSeminarRepository;
import com.seminarhub.repository.MemberSeminarSettlementDateRepository;
import com.seminarhub.repository.SeminarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
//@Component
//@Profile("!test")
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private static final LocalDate DATE_JAN_15 = LocalDate.of(2025, 1, 15);
    private static final LocalDate DATE_JAN_20 = LocalDate.of(2025, 1, 20);
    private static final LocalDate DATE_FEB_01 = LocalDate.of(2025, 2, 1);

    private final MemberRepository memberRepository;
    private final SeminarRepository seminarRepository;
    private final MemberSeminarRepository memberSeminarRepository;
    private final MemberSeminarSettlementDateRepository settlementDateRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (settlementDateRepository.count() > 0) {
            log.info("[DataInitializer] Already initialized. Skipping data insertion.");
            return;
        }

        log.info("[DataInitializer] Starting test data initialization...");

        Member member1 = memberRepository.save(
                Member.builder()
                        .email("test-member-1")
                        .nickname("테스터1")
                        .password("password")
                        .from_social(false)
                        .build()
        );
        Member member2 = memberRepository.save(
                Member.builder()
                        .email("test-member-2")
                        .nickname("테스터2")
                        .password("password")
                        .from_social(false)
                        .build()
        );
        log.info("[DataInitializer] [1/4] Members saved: id={}, id={}", member1.getId(), member2.getId());

        Seminar seminarSpring = seminarRepository.save(
                Seminar.builder()
                        .name("Spring 기초")
                        .explanation("Spring Framework 입문 과정")
                        .price(50_000L)
                        .max_participants(30L)
                        .participants_cnt(0L)
                        .build()
        );
        Seminar seminarJpa = seminarRepository.save(
                Seminar.builder()
                        .name("JPA 심화")
                        .explanation("JPA 및 Hibernate 심화 과정")
                        .price(80_000L)
                        .max_participants(20L)
                        .participants_cnt(0L)
                        .build()
        );
        Seminar seminarConcurrency = seminarRepository.save(
                Seminar.builder()
                        .name("동시성 제어")
                        .explanation("트랜잭션 격리 수준 및 락 전략 비교")
                        .price(100_000L)
                        .max_participants(15L)
                        .participants_cnt(0L)
                        .build()
        );
        log.info("[DataInitializer] [2/4] Seminars saved: ids={}, {}, {}",
                seminarSpring.getId(), seminarJpa.getId(), seminarConcurrency.getId());

        MemberSeminar ms1 = memberSeminarRepository.save(MemberSeminar.builder().seminar(seminarSpring).member(member1).build());
        MemberSeminar ms2 = memberSeminarRepository.save(MemberSeminar.builder().seminar(seminarSpring).member(member2).build());
        MemberSeminar ms3 = memberSeminarRepository.save(MemberSeminar.builder().seminar(seminarJpa).member(member1).build());
        MemberSeminar ms4 = memberSeminarRepository.save(MemberSeminar.builder().seminar(seminarJpa).member(member2).build());
        MemberSeminar ms5 = memberSeminarRepository.save(MemberSeminar.builder().seminar(seminarConcurrency).member(member1).build());
        MemberSeminar ms6 = memberSeminarRepository.save(MemberSeminar.builder().seminar(seminarConcurrency).member(member2).build());
        log.info("[DataInitializer] [3/4] MemberSeminars saved: 6 records");

        List<MemberSeminarSettlementDate> settlementDates = List.of(
                createSettlementDate(ms1, DATE_JAN_15),
                createSettlementDate(ms2, DATE_JAN_15),
                createSettlementDate(ms3, DATE_JAN_20),
                createSettlementDate(ms4, DATE_JAN_20),
                createSettlementDate(ms5, DATE_FEB_01),
                createSettlementDate(ms6, DATE_FEB_01)
        );
        settlementDateRepository.saveAll(settlementDates);
        log.info("[DataInitializer] [4/4] SettlementDates saved: 6 records");

        log.info("[DataInitializer] ✅ Test data initialized successfully.");
        log.info("[DataInitializer] - Members: 2, Seminars: 3, MemberSeminars: 6, SettlementDates: 6");
        log.info("[DataInitializer] - Test aggregate range: 2025-01-01 ~ 2025-01-31 → expected 4 rows (260,000원)");
        log.info("[DataInitializer] - Update target settlementDateId: {} (date={})", settlementDates.get(0).getId(), DATE_JAN_15);
    }

    private MemberSeminarSettlementDate createSettlementDate(MemberSeminar memberSeminar, LocalDate date) {
        MemberSeminarSettlementDate sd = new MemberSeminarSettlementDate(memberSeminar);
        sd.updateDate(date);
        return sd;
    }
}

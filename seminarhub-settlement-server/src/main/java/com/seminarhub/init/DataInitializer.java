package com.seminarhub.init;

import com.seminarhub.entity.Member;
import com.seminarhub.entity.MemberSeminarSettlementDate;
import com.seminarhub.entity.Member_Seminar;
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
@Component
@Profile("!test")
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
                        .member_id("test-member-1")
                        .member_nickname("테스터1")
                        .member_password("password")
                        .member_from_social(false)
                        .build()
        );
        Member member2 = memberRepository.save(
                Member.builder()
                        .member_id("test-member-2")
                        .member_nickname("테스터2")
                        .member_password("password")
                        .member_from_social(false)
                        .build()
        );
        log.info("[DataInitializer] [1/4] Members saved: id={}, id={}", member1.getMember_no(), member2.getMember_no());

        Seminar seminarSpring = seminarRepository.save(
                Seminar.builder()
                        .seminar_name("Spring 기초")
                        .seminar_explanation("Spring Framework 입문 과정")
                        .seminar_price(50_000L)
                        .seminar_max_participants(30L)
                        .seminar_participants_cnt(0L)
                        .build()
        );
        Seminar seminarJpa = seminarRepository.save(
                Seminar.builder()
                        .seminar_name("JPA 심화")
                        .seminar_explanation("JPA 및 Hibernate 심화 과정")
                        .seminar_price(80_000L)
                        .seminar_max_participants(20L)
                        .seminar_participants_cnt(0L)
                        .build()
        );
        Seminar seminarConcurrency = seminarRepository.save(
                Seminar.builder()
                        .seminar_name("동시성 제어")
                        .seminar_explanation("트랜잭션 격리 수준 및 락 전략 비교")
                        .seminar_price(100_000L)
                        .seminar_max_participants(15L)
                        .seminar_participants_cnt(0L)
                        .build()
        );
        log.info("[DataInitializer] [2/4] Seminars saved: ids={}, {}, {}",
                seminarSpring.getSeminar_no(), seminarJpa.getSeminar_no(), seminarConcurrency.getSeminar_no());

        Member_Seminar ms1 = memberSeminarRepository.save(Member_Seminar.builder().seminar(seminarSpring).member(member1).build());
        Member_Seminar ms2 = memberSeminarRepository.save(Member_Seminar.builder().seminar(seminarSpring).member(member2).build());
        Member_Seminar ms3 = memberSeminarRepository.save(Member_Seminar.builder().seminar(seminarJpa).member(member1).build());
        Member_Seminar ms4 = memberSeminarRepository.save(Member_Seminar.builder().seminar(seminarJpa).member(member2).build());
        Member_Seminar ms5 = memberSeminarRepository.save(Member_Seminar.builder().seminar(seminarConcurrency).member(member1).build());
        Member_Seminar ms6 = memberSeminarRepository.save(Member_Seminar.builder().seminar(seminarConcurrency).member(member2).build());
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

    private MemberSeminarSettlementDate createSettlementDate(Member_Seminar memberSeminar, LocalDate date) {
        MemberSeminarSettlementDate sd = new MemberSeminarSettlementDate(memberSeminar);
        sd.updateDate(date);
        return sd;
    }
}

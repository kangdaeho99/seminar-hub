package com.seminarhub.repository;


import com.seminarhub.entity.Seminar;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.assertNotNull;


/**
 * [ 2023-08-08 daeho.kang ]
 * Description : Test For QueryDSL Or JPQL Custom Method
 *
 */
@SpringBootTest
public class SeminarRepositoryTests {
    @Autowired
    private SeminarRepository seminarRepository;

    @Autowired
    private SeminarQuerydslRepository seminarQuerydslRepository;
    private final String seminar_name= "SeminarTest";
    private final String seminar_remove_name= "SeminarRemoveTest";
    /**
     * [ 2023-08-08 daeho.kang ]
     * Description : Setting the Data for Test
     */
//    @BeforeEach
//    public void setup() throws Exception{
//        if(seminarRepository.findBySeminar_name(seminar_name).isEmpty()){
//            // CREATE Seminar
//            Seminar seminar = Seminar.builder()
//                    .seminar_name(seminar_name)
//                    .build();
//
//            seminarRepository.save(seminar);
//        }
//        if(seminarRepository.findBySeminar_name(seminar_remove_name).isEmpty()){
//            // CREATE Seminar
//            Seminar seminar = Seminar.builder()
//                    .seminar_name(seminar_remove_name)
//                    .build();
//
//            seminarRepository.save(seminar);
//        }
//    }

    /**
     * [ 2023-08-08 daeho.kang ]
     * Description : seminarRepository findBySeminar_name Test
     * Find Seminar by seminar_name
     *
     *     Hibernate:
     *     select
     *         s1_0.seminar_no,
     *         s1_0.del_dt,
     *         s1_0.inst_dt,
     *         s1_0.seminar_explanation,
     *         s1_0.seminar_max_participants,
     *         s1_0.seminar_name,
     *         s1_0.seminar_price,
     *         s1_0.updt_dt
     *     from
     *         seminar s1_0
     *     where
     *         s1_0.seminar_name=?
     *         and s1_0.del_dt is null
     *      Seminar(seminar_no=1, seminar_name=SeminarTest, seminar_explanation=null, del_dt=null)
     */
    @DisplayName("findBySeminar_name Test")
    @Test
    public void testFindBySeminar_name(){
        // given, when
        Optional<Seminar> seminar = seminarRepository.findBySeminar_name("SeminarTest");

        // then
        assertNotNull(seminar.get());
        System.out.println(seminar.get());
    }

    /**
     * Description :
     *     update
     *         seminar
     *     set
     *         seminar_participants_cnt=(seminar_participants_cnt+1)
     *     where
     *         seminar_no=?
     *         and del_dt is null
     */
    @DisplayName("incrementParticipantsCnt Test")
    @Test
    public void testIncreaseParticipantsCnt(){
        // given
        Seminar seminar = new Seminar();
        seminar.setSeminar_name("TestSeminarForRepositoryTest");
        seminar.setSeminar_participants_cnt(0); // Initial participant count is 0
        seminarRepository.save(seminar);

        // when
        seminarRepository.incrementParticipantsCnt(seminar.getSeminar_no());

        // then
        Optional<Seminar> updatedSeminar = seminarRepository.findById(seminar.getSeminar_no());
        Assertions.assertTrue(updatedSeminar.isPresent());
        Assertions.assertEquals(1, updatedSeminar.get().getSeminar_participants_cnt());
    }

    /**
     * Description :
     *     update
     *         seminar
     *     set
     *         seminar_participants_cnt=(seminar_participants_cnt-1)
     *     where
     *         seminar_no=?
     *         and del_dt is null
     */
    @DisplayName("decreaseParticipantsCnt Test")
    @Test
    public void testDecreaseParticipantsCnt(){
        // given
        Seminar seminar = new Seminar();
        seminar.setSeminar_name("TestSeminarForRepositoryTest");
        seminar.setSeminar_participants_cnt(2);
        seminarRepository.save(seminar);

        // when
        seminarRepository.decreaseParticipantsCnt(seminar.getSeminar_no());

        // then
        Optional<Seminar> updatedSeminar = seminarRepository.findById(seminar.getSeminar_no());
        Assertions.assertTrue(updatedSeminar.isPresent());
        Assertions.assertEquals(1, updatedSeminar.get().getSeminar_participants_cnt());
    }

    /*
    String member_id = "passionfruit200@naver.com";
    String[] seminar_name_arr = new String[] { "2024년 상반기 스타크래프트 테란 세미나", "2024년 상반기 스타크래프트 프로토스 세미나", "스타크래프트 세미나", "2024년 상반기 스타크래프트 저그 세미나"};
    boolean[] visited = new boolean[seminar_name_arr.length];
    public List<String> memberSeminarRegisterRequestDTOList = new ArrayList<>();
    String[] seminar_namebuild_arr = new String[] {"8배럭", "더블 커맨드", "원배럭 더블", "원팩 더블", "FD테란", "메카닉 테란", "5팩토리 타이밍 러쉬", "5팩 골리앗", "레이트 메카닉", "발리앗", "안티 캐리어", "트리플 커맨드", "마인 트리플", "업테란", "바이오닉 테란", "노점단속", "미친테란", "레이트 바이오닉", "바카닉 테란", "발리오닉", "선엔베", "투배럭 아카데미", "SK테란", "네오 SK테란", "원팩 원스타", "1/1/1 체제", "4벌쳐 드랍", "투스타포트", "투스타 레이스", "투팩토리", "투팩 원스타", "대나무류 조이기", "벙커링", "우주방어", "지우개", "15투게이트", "21투게이트", "더블넥서스", "포지 더블넥", "원게이트 더블", "23넥서스", "3드라 더블", "다크 더블", "T1더블", "용새류", "전진 게이트", "패스트 다크 템플러", "패스트 아비터", "29아비터", "패스트 캐리어", "캐논 러쉬", "커세어 공발업 질럿", "커세어 다크", "커세어 리버", "하드코어 질럿", "2게이트 질럿 러쉬", "도망자 토스", "드라군리버", "드라템", "리버 캐리어", "매너 파일런", "속업셔틀", "수비형 프로토스", "원게이트", "옵드라", "앞마당 3해처리 운영", "3해처리 히드라", "선러커", "히드라러커", "3센치드랍", "땅굴저그", "레어 삼지창", "저글링러커", "연탄 조이기", "뮤탈짤짤이", "폭탄 드랍", "잠복", "러커 겹치기", "스탑러커", "장판파", "4드론", "원해처리 러커", "노 스포닝 풀 3해처리", "2해처리 레어", "2해처리 뮤탈리스크", "미친저그", "목동저그", "5드론", "6드론", "5해처리 히드라", "12앞마당", "12드론 스포닝 풀 (12풀)", "9드론 스포닝 풀 (9발업)", "9오버풀", "선 가스", "9드론 투해처리 (9투)", "7드론", "4드론, 5드론 (이후 성큰러쉬)" };
    String[] seminar_Year_Quarter = new String[] {"1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"};
    public List<String> seminarNameArrList = new ArrayList<>();
    int startYear = 1924; int size = 100;
    String[] year = new String[size + 1];
    int[] answer;
    */

    @DisplayName("DUmmyDataInsert")
    @Test
    public void dummyInsert(){
        Random random = new Random();

        //모든 경우에 대해 순열로 만들자.
        for(int i=5; i<7; i++){
            Seminar seminar  = Seminar.builder()
                    .seminar_name("SeminarDummyIndex"+i)
                    .seminar_explanation("SeminarDummyExplanationIndex"+i)
                    .seminar_price(random.nextLong() * 1000)
                    .build();
            seminarRepository.save(seminar);
        }
    }
    /**
     * [ 2024-03-xx passionfruit200 ]
     * Description :
     * Hibernate:
     *     select
     *         s1_0.seminar_no,
     *         s1_0.del_dt,
     *         s1_0.inst_dt,
     *         s1_0.seminar_explanation,
     *         s1_0.seminar_max_participants,
     *         s1_0.seminar_name,
     *         s1_0.seminar_participants_cnt,
     *         s1_0.seminar_price,
     *         s1_0.updt_dt
     *     from
     *         seminar s1_0
     *     where
     *         s1_0.seminar_name=?
     *         and s1_0.del_dt is null for update
     */
    @DisplayName("getBySeminar_NameWithPessimisticLock Test ")
    @Transactional
    @Test
    public void testGetBySeminar_NameWithPessimisticLockQuerydsl(){
        // given // when
        Optional<Seminar> seminar = seminarQuerydslRepository.findBySeminar_NameWithPessimisticLock("스타크래프트 세미나");
        System.out.println(" "+seminar.toString());
        // then
        assertNotNull(seminar.get());
    }

}
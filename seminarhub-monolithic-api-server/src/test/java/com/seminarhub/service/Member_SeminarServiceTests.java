package com.seminarhub.service;

import com.seminarhub.common.exception.SeminarRegistrationFullException;
import com.seminarhub.dto.MemberDTO;
import com.seminarhub.dto.MemberSeminarRegisterRequestDTO;
import com.seminarhub.dto.SeminarDTO;
import com.seminarhub.entity.Member;
import com.seminarhub.entity.Member_Seminar;
import com.seminarhub.entity.Member_Seminar_Payment_History;
import com.seminarhub.entity.Seminar;
import com.seminarhub.repository.Member_SeminarRepository;
import com.seminarhub.repository.Member_Seminar_Payment_HistoryRepository;
import com.seminarhub.repository.SeminarQuerydslRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class Member_SeminarServiceTests {

//    @MockBean
//    private SeminarService seminarService;
//
//    @MockBean
//    private MemberService memberService;
//
//    @MockBean
//    private Member_SeminarRepository member_seminarRepository;
//
//    @MockBean
//    private SeminarQuerydslRepository seminarQuerydslRepository;
//
//    @MockBean
//    private Member_Seminar_Payment_HistoryRepository member_seminar_payment_historyRepository;
    @Autowired
    private SeminarService seminarService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private Member_SeminarRepository member_seminarRepository;

    @Autowired
    private SeminarQuerydslRepository seminarQuerydslRepository;

    @Autowired
    private Member_Seminar_Payment_HistoryRepository member_seminar_payment_historyRepository;

    @Autowired
    private Member_SeminarService memberSeminarService;

//    @Transactional
    @DisplayName("Member_Seminar Service RegisterForSeminar Test")
    @Test
    public void testRegisterForSeminar() throws SeminarRegistrationFullException {

        // given
        Member member = Member.builder()
                .member_id("passionfruit200@naver.com")
                .build();

        Seminar seminar = Seminar.builder()
                .seminar_name("SeminarTest")
                .build();

        MemberDTO memberDTO = MemberDTO.builder()
                .member_id("passionfruit200@naver.com")
                .build();

        SeminarDTO seminarDTO = SeminarDTO.builder()
                .seminar_name("SeminarTest")
                .seminar_no(3L)
                .seminar_max_participants(40L) //SET seminar_max_participants = 40;
                .seminar_participants_cnt(23L)
                .seminar_price(10000L)
                .build();

        Member_Seminar member_seminar = Member_Seminar.builder()
                .member_seminar_no(1L)
                .member(member)
                .seminar(seminar)
                .build();

        Member_Seminar_Payment_History member_seminar_payment_history = Member_Seminar_Payment_History.builder()
                        .member_seminar_payment_history_no(4L)
                        .member_seminar_payment_history_amount(seminarDTO.getSeminar_price())
                        .build();

        MemberSeminarRegisterRequestDTO memberSeminarRegisterRequestDTO= MemberSeminarRegisterRequestDTO.builder()
                .member_id("passionfruit200@naver.com")
                .seminar_name("SeminarTest")
                .build();

        Mockito.when(memberService.get("passionfruit200@naver.com")).thenReturn(memberDTO);
        Mockito.when(seminarService.get("SeminarTest")).thenReturn(seminarDTO);
        Mockito.when(member_seminar_payment_historyRepository.save(any(Member_Seminar_Payment_History.class))).thenReturn(member_seminar_payment_history);
        Mockito.doNothing().when(seminarService).increaseParticipantsCnt(seminarDTO.getSeminar_no());
        Mockito.when(member_seminarRepository.save(any(Member_Seminar.class))).thenReturn(member_seminar);

        // when
        Long result = memberSeminarService.registerForSeminar(memberSeminarRegisterRequestDTO);

        // then
        verify(memberService).get("passionfruit200@naver.com");
        verify(seminarService).get("SeminarTest");
        verify(member_seminar_payment_historyRepository).save(any(Member_Seminar_Payment_History.class));
        verify(seminarService).increaseParticipantsCnt(seminarDTO.getSeminar_no());
        verify(member_seminarRepository).save(any(Member_Seminar.class));
    }

    @DisplayName("Member_Seminar Service RegisterForSeminar Test")
    @Test
    public void testWithThreadRegisterForSeminar() throws SeminarRegistrationFullException {
        String member_id = "passionfruit200@naver.com";
        String seminar_name = "SeminarTest";

        final int executeNumber = 100;
        final ExecutorService executorService = Executors.newFixedThreadPool(50);
        final CountDownLatch countDownLatch = new CountDownLatch(executeNumber);

        for(int i=0;i<executeNumber; i++){
            executorService.execute( () -> {
                try{
                    memberSeminarService.registerForSeminar(new MemberSeminarRegisterRequestDTO(member_id, seminar_name));
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }finally {
                    countDownLatch.countDown();
                }
            });
        }

        try {
            // 모든 스레드가 종료될 때까지 대기
            countDownLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted while waiting for completion.");
        } finally {
            // ExecutorService 종료
            executorService.shutdown();
        }

    }

    @DisplayName("Member_Seminar Service RegisterForSeminar Test")
    @Test
    public void testRegisterForSeminars() throws SeminarRegistrationFullException {
        // given
        Member member = Member.builder()
                .member_id("passionfruit200@naver.com")
                .build();

        Seminar seminar = Seminar.builder()
                .seminar_name("SeminarTest")
                .build();

        MemberDTO memberDTO = MemberDTO.builder()
                .member_id("passionfruit200@naver.com")
                .build();

        SeminarDTO seminarDTO = SeminarDTO.builder()
                .seminar_name("SeminarTest")
                .seminar_no(3L)
                .seminar_max_participants(40L) //SET seminar_max_participants = 40;
                .seminar_participants_cnt(23L)
                .seminar_price(10000L)
                .build();

        Member_Seminar member_seminar = Member_Seminar.builder()
                .member_seminar_no(1L)
                .member(member)
                .seminar(seminar)
                .build();

        Member_Seminar_Payment_History member_seminar_payment_history = Member_Seminar_Payment_History.builder()
                .member_seminar_payment_history_no(4L)
                .member_seminar_payment_history_amount(seminarDTO.getSeminar_price())
                .build();

        MemberSeminarRegisterRequestDTO memberSeminarRegisterRequestDTO= MemberSeminarRegisterRequestDTO.builder()
                .member_id("passionfruit200@naver.com")
                .seminar_name("SeminarTest")
                .build();

        Mockito.when(memberService.get("passionfruit200@naver.com")).thenReturn(memberDTO);
        Mockito.when(seminarService.getWithPessimisticLock("SeminarTest")).thenReturn(seminarDTO);
        Mockito.when(member_seminar_payment_historyRepository.save(any(Member_Seminar_Payment_History.class))).thenReturn(member_seminar_payment_history);
        Mockito.doNothing().when(seminarService).increaseParticipantsCnt(seminarDTO.getSeminar_no());
        Mockito.when(member_seminarRepository.save(any(Member_Seminar.class))).thenReturn(member_seminar);

        // when
        Long result = memberSeminarService.registerForSeminar(memberSeminarRegisterRequestDTO);

        // then
        verify(memberService).get("passionfruit200@naver.com");
        verify(seminarService).getWithPessimisticLock("SeminarTest");
        verify(member_seminar_payment_historyRepository).save(any(Member_Seminar_Payment_History.class));
        verify(seminarService).increaseParticipantsCnt(seminarDTO.getSeminar_no());
        verify(member_seminarRepository).save(any(Member_Seminar.class));
    }

    @DisplayName("Member_Seminar Service RegisterForSeminar Test")
    @Test
    public void testWithThreadsRegisterForSeminar() throws SeminarRegistrationFullException {
        String member_id = "passionfruit200@naver.com";
        String seminar_name = "스타크래프트 세미나";

        final int executeNumber = 5000;
        final ExecutorService executorService = Executors.newFixedThreadPool(500);
        final CountDownLatch countDownLatch = new CountDownLatch(executeNumber);

        for(int i=0;i<executeNumber; i++){
            executorService.execute( () -> {
                try{
                    memberSeminarService.registerForSeminar(new MemberSeminarRegisterRequestDTO(member_id, seminar_name));
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }finally {
                    countDownLatch.countDown();
                }
            });
        }

        try {
            // 모든 스레드가 종료될 때까지 대기
            countDownLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted while waiting for completion.");
        } finally {
            // ExecutorService 종료
            executorService.shutdown();
        }

    }



//	처음에는 seminar_name으로 검색했었기에 해당 배열도 냅두었습니다.
//    String[] seminar_name_arr = new String[] { "2024년 상반기 스타크래프트 테란 세미나", "2024년 상반기 스타크래프트 프로토스 세미나", "스타크래프트 세미나", "2024년 상반기 스타크래프트 저그 세미나"};
    Long[] seminar_no_arr = new Long[] { 11L, 12L, 13L, 14L};
    boolean[] visited = new boolean[seminar_no_arr.length];
    public List<String> memberSeminarRegisterRequestDTOList = new ArrayList<>();
    int[] answer;
    public void seminar_no_Permutation(int level, int size, int maxSize){
        if(level == maxSize){
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<answer.length;i++){
                sb.append(answer[i]+" ");
            }
            memberSeminarRegisterRequestDTOList.add(sb.toString());
            return ;
        }
        for(int i=0;i<seminar_no_arr.length;i++){
            if(visited[i] == false){
                visited[i] = true;
                answer[level] = i;
                seminar_no_Permutation(level + 1, size + 1, maxSize);
                visited[i] = false;
            }
        }

    }
    @DisplayName("Member_Seminar Service RegisterForSeminar Test")
    @Test
    public void testWithThreadsRegisterForSeminarList() throws SeminarRegistrationFullException {
        String member_id = "passionfruit200@naver.com";

        for(int i=1;i<=seminar_no_arr.length;i++){
            answer = new int[i];
            seminar_no_Permutation(0, 0, i);
        }

//      생성된 모든 장바구니 담기 조합을 보여줍니다
//        for(int i=0;i<memberSeminarRegisterRequestDTOList.size();i++){
//            System.out.println("KIND --- ");
//            System.out.println(memberSeminarRegisterRequestDTOList.get(i));
//        }

        Random random = new Random();
        //총 몇번의 트랜잭션이 시작되었는지 counting 합니다. 이를 통해 올바르게 롤백되었는지도 확인할 수 있습니다.
        AtomicInteger allNumber = new AtomicInteger();
        AtomicInteger successNumber = new AtomicInteger();
        AtomicInteger failedNumber = new AtomicInteger();
        final int executeNumber = 1000;
        final ExecutorService executorService = Executors.newFixedThreadPool(500);
        final CountDownLatch countDownLatch = new CountDownLatch(executeNumber);
        for(int i=0;i<executeNumber; i++){
            executorService.execute( () -> {
                try{
                    int randomIndex = random.nextInt(memberSeminarRegisterRequestDTOList.size()); // memberSeminarRegisterRequestDTOList에서 랜덤한 인덱스를 선택합니다.
                    String[] info = memberSeminarRegisterRequestDTOList.get( (randomIndex) ).split(" ");
                    //수동 정렬
                    //Arrays.sort(info);

                    List<MemberSeminarRegisterRequestDTO> MemberSeminarRegisterRequestDTOLIST = new ArrayList<>();
                    for(int j=0;j<info.length;j++){
                        MemberSeminarRegisterRequestDTOLIST.add(new MemberSeminarRegisterRequestDTO(member_id, seminar_no_arr[ Integer.parseInt(info[j])]));
                    }

//					올바르게 랜덤 장바구니 담기 조합이 만들어져서 들어가는지 테스트합니다.
//                    for(int j=0;j<info.length;j++){
//                        System.out.println(MemberSeminarRegisterRequestDTOLIST.toString());
//
//                    }
                    try {
                        memberSeminarService.registerForSeminarWithList(MemberSeminarRegisterRequestDTOLIST);
                        allNumber.addAndGet(info.length);
                        successNumber.addAndGet(1);
                    }catch (Exception e){
                        failedNumber.addAndGet(1);
                        throw e;
                    }
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }finally {
                    countDownLatch.countDown();
                }
            });
        }

        try {
            // 모든 스레드가 종료될 때까지 대기
            countDownLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted while waiting for completion.");
        } finally {
            // ExecutorService 종료
            executorService.shutdown();
        }
        
        System.out.println("allNumber:"+allNumber);
        System.out.println("successNumber:"+successNumber);
        System.out.println("failedNumber:"+failedNumber);

    }

}

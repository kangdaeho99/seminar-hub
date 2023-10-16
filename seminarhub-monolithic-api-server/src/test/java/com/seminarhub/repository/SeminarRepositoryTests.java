package com.seminarhub.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seminarhub.dto.SeminarDTO;
import com.seminarhub.dto.SeminarPageResultDTO;
import com.seminarhub.dto.Seminar_Member_Seminar_PaymentResponseDTO;
import com.seminarhub.entity.Member_Seminar;
import com.seminarhub.entity.Seminar;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
     *     select
     *         s1_0.seminar_no,
     *         s1_0.del_dt,
     *         s1_0.inst_dt,
     *         s1_0.seminar_explanation,
     *         s1_0.seminar_name,
     *         s1_0.updt_dt
     *     from
     *         seminar s1_0
     *     where
     *         s1_0.seminar_name=?
     *         and s1_0.del_dt is null
     *      Seminar(seminar_no=1, seminar_name=SeminarTest, seminar_explanation=null, del_dt=null)
     */
//    @DisplayName("findBySeminar_name Test")
//    @Test
//    public void testGetWithSeminar_name(){
//        // given, when
//        Optional<Seminar> seminar = seminarRepository.findBySeminar_name(seminar_name);
//
//        // then
//        assertNotNull(seminar.get());
//        System.out.println(seminar.get());
//    }

//    @DisplayName("DUmmyDataInsert")
//    @Test
//    public void dummyInsert(){
//
//        for(int i=2; i<1000000; i++){
//            Seminar seminar  = Seminar.builder()
//                    .seminar_name("SeminarDummyIndex"+i)
//                    .seminar_explanation("SeminarDummyExplanationIndex"+i)
//                    .build();
//            seminarRepository.save(seminar);
//        }
//    }
    @DisplayName("jpaQueryFactoryfindBySeminar_name Test")
    @Test
    public void testGetWithSeminar_name123(){
        // given, when
        List<Seminar> seminar = seminarQuerydslRepository.findByName("SeminarDummyIndex5");

        // then
        assertNotNull(seminar.size());
        System.out.println(seminar.get(0));
    }
    
    
    @DisplayName("FindSeminarByBooleanBuilder Test")
    @Test
    public void testFindSeminarByBooleanBuilder(){
        // given // when
        List<Seminar> seminarList = seminarQuerydslRepository.findSeminarByBooleanBuilder("SeminarDummyIndex23", "");

        // then
        assertNotNull( seminarList.size());
//        System.out.println(seminar.);
        seminarList.stream().forEach(seminar -> System.out.println(seminar.toString()));
    }

    @DisplayName("FindSeminarByBooleanExpression Test")
    @Test
    public void testFindSeminarByBooleanExpression(){
        // given // when
        List<Seminar> seminarList = seminarQuerydslRepository.findSeminarByBooleanExpression("SeminarDummyIndex23", "");

        // then
        assertNotNull( seminarList.size());
//        System.out.println(seminar.);
        seminarList.stream().forEach(seminar -> System.out.println(seminar.toString()));
    }

    @DisplayName("FindSeminarWithExists")
    @Test
    public void testwithExists(){
        boolean flag = seminarQuerydslRepository.exist("SeminarDummyIndex25");

        System.out.println(flag);
    }

    @DisplayName("testCrossJoin")
    @Test
    @Transactional // To Maintain the Proxy Object Until the End of the Test
    public void testCrossJoin(){
        List<Member_Seminar> memberSeminarList = seminarQuerydslRepository.crossJoin();

        memberSeminarList.stream().forEach(memberSeminar -> System.out.println(memberSeminar.getMember()+" "+memberSeminar.getSeminar()));

    }

    @DisplayName("dummyInsertWithJdbcTemplate")
    @Test
    public void dummyInsertWithJdbcTemplate(){
        List<SeminarDTO> seminarDTOList = new LinkedList<>();
        Random random = new Random();
        for(int i=2000028; i<=2000028; i++) {
            SeminarDTO seminarDTO = SeminarDTO.builder()
                    .seminar_name("SeminarDummyIndex" + i)
                    .seminar_explanation("SeminarDummyExplanation" + i)
                    .seminar_price(random.nextLong(999999) + 1)
                    .seminar_maxParticipants((long) 40)
                    .build();
            seminarDTOList.add(seminarDTO);
        }
        Long startTime = System.currentTimeMillis();
        seminarQuerydslRepository.jdbcBulkInsert(seminarDTOList);
        Long endTime = System.currentTimeMillis();
        System.out.println("Execution Time:"+ (endTime - startTime) + "ms");

    }

    @Autowired
    private JPAQueryFactory queryFactory;

    @DisplayName("Querydsl PagingExample")
    @Test
    public void PagingExample(){
        int pageNo = 190000 - 1;
        String keyword="Seminar";
        int pageSize = 10;
        List<SeminarPageResultDTO> seminarPageResultDTOList = seminarQuerydslRepository.pagingSeminarWithKeyword(keyword,pageNo, pageSize);
        for(int i=0;i<seminarPageResultDTOList.size();i++){
            System.out.println("cnt:"+i+" "+seminarPageResultDTOList.get(i).toString());
        }
    }
    @Test
    public void bulkUpdate(){
            seminarQuerydslRepository.bulkUpdate();
    }
    @DisplayName("Querydsl PagingExampleWithCoveringIndex")
    @Test
    public void PagingExampleWithCoveringIndex(){
        int pageNo =190000 - 1;
        String keyword="Seminar";
        int pageSize = 10;
        List<SeminarPageResultDTO> seminarPageResultDTOList = seminarQuerydslRepository.pagingSeminarWithKeywordWithCoveringIndex(keyword,pageNo, pageSize);
        for(int i=0;i<seminarPageResultDTOList.size();i++){
            System.out.println("cnt:"+i+" "+seminarPageResultDTOList.get(i).toString());
        }
    }
    @DisplayName("testPagingSeminarWithSeminar_Price test")
    @Test
    public void testPagingSeminarWithSeminar_Price(){
        int pageNo = 30000;
        long seminar_price = 10000;
        int pageSize = 10;
        List<SeminarPageResultDTO> seminarPageResultDTOList = seminarQuerydslRepository.pagingSeminarWithSeminar_Price(seminar_price ,pageNo, pageSize);
        for(int i=0;i<seminarPageResultDTOList.size();i++){
            System.out.println("cnt:"+i+" "+seminarPageResultDTOList.get(i).toString());
        }
    }

    @DisplayName("testPagingSeminarWithCoveringIndexWithSeminar_Price")
    @Test
    public void testPagingSeminarWithCoveringIndexWithSeminar_Price(){
        int pageNo =30000;
        long seminar_price = 10000;
        int pageSize = 10;
        List<SeminarPageResultDTO> seminarPageResultDTOList = seminarQuerydslRepository.pagingSeminarWithCoveringIndexWithSeminar_Price(seminar_price,pageNo, pageSize);
        for(int i=0;i<seminarPageResultDTOList.size();i++){
            System.out.println("cnt:"+i+" "+seminarPageResultDTOList.get(i).toString());
        }
    }

    @DisplayName("testParticipateOnSeminar")
    @Test
    public void testParticipateOnSeminar(){
//        Seminar s
//        seminarRepository.save()
        Long member_no = 1L;
        Long seminar_no = 2000028L;
        seminarQuerydslRepository.participateOnSeminar(member_no, seminar_no);

    }

    @DisplayName("testParticipateOnSeminarWithoutLock")
    @Test
    public void testParticipateOnSeminarWithoutLock() throws InterruptedException {
        Long member_no = 1L;
        Long seminar_no = 2000028L;

        final int executeNumber = 100;
        final ExecutorService executorService = Executors.newFixedThreadPool(40);
        final CountDownLatch countDownLatch = new CountDownLatch(executeNumber);

        for(int i=0;i<executeNumber;i++){
            executorService.execute( () -> {
                try{
                    seminarQuerydslRepository.participateOnSeminar(member_no, seminar_no);
                } catch(Exception e){
                    System.out.println(e.getMessage());
                }finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
    }

    @DisplayName("testParticipateOnSeminarWithPessimisticLock")
    @Test
    public void testParticipateOnSeminarWithMultiThread() throws InterruptedException {
        Long member_no = 1L;
        Long seminar_no = 2000028L;

        final int executeNumber = 200;
        final ExecutorService executorService = Executors.newFixedThreadPool(40);
        final CountDownLatch countDownLatch = new CountDownLatch(executeNumber);

        for(int i=0;i<executeNumber;i++){
            executorService.execute( () -> {
                try{
                    seminarQuerydslRepository.participateOnSeminarWithPESSIMISTICLock(member_no, seminar_no);
                } catch(Exception e){
                    System.out.println(e.getMessage());
                }finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
    }

    @DisplayName("testParticipateOnSeminarWithPessimisticLock")
    @Test
    public void testparticipateOnSeminarWithPESSIMISTICLockAddPayment() throws InterruptedException {
        Long member_no = 1L;
        Long seminar_no = 2000028L;

        final int executeNumber = 40;
        final ExecutorService executorService = Executors.newFixedThreadPool(40);
        final CountDownLatch countDownLatch = new CountDownLatch(executeNumber);

        for(int i=0;i<executeNumber;i++){
            executorService.execute( () -> {
                try{
                    seminarQuerydslRepository.participateOnSeminarWithPESSIMISTICLockAddPayment(member_no, seminar_no);
                } catch(Exception e){
                    System.out.println(e.getMessage());
                }finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
    }

    @Transactional //Proxy 유지
    @DisplayName("test getListForMember_SeminarAndPayment")
    @Test
    public void testgetListForMember_SeminarAndPayment() throws InterruptedException {
        Long member_no = 1L;
        Long seminar_no = 2000028L;

        List<Seminar> seminar = seminarQuerydslRepository.getSeminar(seminar_no);
        System.out.println(seminar.get(0).toString());
        for(int i=0;i<seminar.get(0).getMember_seminar_list().size();i++){
            System.out.println("----------------------------------------");
            System.out.println(seminar.get(0).getMember_seminar_list().get(i).toString());
            System.out.println(seminar.get(0).getMember_seminar_list().get(i).getPayment().toString());
        }

    }

    @Transactional //Proxy 유지
    @DisplayName("test getListForMember_SeminarAndPaymentFetchJoin")
    @Test
    public void testgetListForMember_SeminarAndPaymentWithFetchJoin() throws InterruptedException {
        Long member_no = 1L;
        Long seminar_no = 2000028L;

        List<Seminar> seminar = seminarQuerydslRepository.getSeminarFetchJoin(seminar_no);
        System.out.println(seminar.get(0).toString());
        for(int i=0;i<seminar.get(0).getMember_seminar_list().size();i++){
            System.out.println("----------------------------------------");
            System.out.println(seminar.get(0).getMember_seminar_list().get(i).toString());
            System.out.println(seminar.get(0).getMember_seminar_list().get(i).getPayment().toString());
        }
    }

    @Transactional //Proxy 유지
    @DisplayName("test getListForMember_SeminarAndPayment")
    @Test
    public void testgetListSeminar() throws InterruptedException {
        Long member_no = 1L;
        Long seminar_no = 2000028L;
        int pageNo = 0;
        int pageSize = 10;
        List<Seminar> seminar = seminarQuerydslRepository.getListSeminar(pageNo, pageSize);
        System.out.println("CNT:"+seminar.size());
        for(int i=0;i<seminar.size();i++){
            System.out.println("---------------------------------------");
            System.out.println("Seminar:"+seminar.get(i).toString());
            for(int j=0;j<seminar.get(i).getMember_seminar_list().size();j++){
                System.out.println(seminar.get(i).getMember_seminar_list().get(j).toString());
                System.out.println(seminar.get(i).getMember_seminar_list().get(j).getPayment().toString());
            }
        }
    }

    @Transactional //Proxy 유지
    @DisplayName("test getListForMember_SeminarAndPayment")
    @Test
    public void testgetListSeminarWithFetchJoin() throws InterruptedException {
        Long member_no = 1L;
        Long seminar_no = 2000028L;
        int pageNo = 0;
        int pageSize = 50;
        List<Seminar> seminar = seminarQuerydslRepository.getListSeminarWithFetchJoin(pageNo, pageSize);
        System.out.println("CNT:"+seminar.size());
        for(int i=0;i<seminar.size();i++){

            System.out.println("---------------------------------------");
            System.out.println("Seminar:"+seminar.get(i).toString());
            System.out.println(seminar.get(i).getMember_seminar_list().size());

            for(int j=0;j<seminar.get(i).getMember_seminar_list().size();j++){
                Member_Seminar member_seminar = seminar.get(i).getMember_seminar_list().get(j);
                System.out.println(member_seminar.toString());
//                System.out.println(member_seminar.getPayment().toString());
            }
        }
    }

    @Transactional //Proxy 유지
    @DisplayName("test getListForMember_SeminarAndPayment")
    @Test
    public void testgetListSeminarWithBatch() throws InterruptedException {
        Long member_no = 1L;
        Long seminar_no = 2000028L;
        int pageNo = 0;
        int pageSize = 50;
        List<Seminar> seminar = seminarQuerydslRepository.getListSeminarWithBatch(pageNo, pageSize);
        System.out.println("CNT:"+seminar.size());
        for(int i=0;i<seminar.size();i++){

            System.out.println("---------------------------------------");
//            System.out.println("Seminar:"+seminar.get(i).toString());
            for(int j=0;j<seminar.get(i).getMember_seminar_list().size();j++){
                Member_Seminar member_seminar = seminar.get(i).getMember_seminar_list().get(j);
                System.out.println(member_seminar.toString());
//                System.out.println(member_seminar.getPayment().toString());
            }
        }
    }



    @Transactional //Proxy 유지
    @DisplayName("test getListForMember_SeminarAndPayment With DTO")
    @Test
    public void testgetListSeminarWithFetchJoinWIthDTO() throws InterruptedException {
        Long member_no = 1L;
        Long seminar_no = 2000028L;
        int pageNo = 0;
        int pageSize = 10;
        List<Seminar_Member_Seminar_PaymentResponseDTO> seminar = seminarQuerydslRepository.getListForMember_SeminarAndPayment(seminar_no);
        System.out.println("CNT:"+seminar.size());
        for(int i=0;i<seminar.size();i++){
            System.out.println("---------------------------------------");
            System.out.println("Seminar:"+seminar.get(i).toString());
            seminar.get(i).getMember_seminar_no();
            seminar.get(i).getMember_no();
            seminar.get(i).getSeminar_explanation();
            seminar.get(i).getSeminar_price();
            seminar.get(i).getSeminar_explanation();
            seminar.get(i).getMember_seminar_no();
            seminar.get(i).getMember_no();
            seminar.get(i).getPayment_no();
            seminar.get(i).getAmount();
        }
    }


}
package com.seminarhub.repository;


import com.seminarhub.entity.Member_Seminar;
import com.seminarhub.entity.Seminar;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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


}
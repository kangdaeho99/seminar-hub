package com.seminarhub.repository;


import com.seminarhub.entity.Seminar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

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

    @DisplayName("DUmmyDataInsert")
    @Test
    public void dummyInsert(){

        for(int i=5; i<7; i++){
            Seminar seminar  = Seminar.builder()
                    .seminar_name("SeminarDummyIndex"+i)
                    .seminar_explanation("SeminarDummyExplanationIndex"+i)
                    .seminar_price((long) 2000)
                    .build();
            seminarRepository.save(seminar);
        }
    }

}
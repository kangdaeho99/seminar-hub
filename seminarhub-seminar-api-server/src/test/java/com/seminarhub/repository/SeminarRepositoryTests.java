package com.seminarhub.repository;

import com.seminarhub.core.entity.Seminar;
import org.junit.jupiter.api.BeforeEach;
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

    private final String seminar_name= "SeminarTest";
    /**
     * [ 2023-08-08 daeho.kang ]
     * Description : Setting the Data for Test
     */
    @BeforeEach
    public void setup() throws Exception{
        if(seminarRepository.findBySeminar_name("SeminarTest").isEmpty()){
            // CREATE Seminar
            Seminar seminar = Seminar.builder()
                    .seminar_name("SeminarTest")
                    .build();

            seminarRepository.save(seminar);
        }
        if(seminarRepository.findBySeminar_name("SeminarRemoveTest").isEmpty()){
            // CREATE Seminar
            Seminar seminar = Seminar.builder()
                    .seminar_name("SeminarRemoveTest")
                    .build();

            seminarRepository.save(seminar);
        }
    }

    /**
     * [ 2023-08-08 daeho.kang ]
     * Description : seminarRepository findBySeminar_name Test
     * Find Seminar by seminar_name
     *
     *     select
     *         s1_0.seminar_no,
     *         s1_0.del_dt,
     *         s1_0.inst_dt,
     *         s1_0.seminar_name,
     *         s1_0.updt_dt
     *     from
     *         seminar s1_0
     *     where
     *         s1_0.seminar_name=?
     *         and s1_0.del_dt is null
     */
    @DisplayName("findBySeminar_name Test")
    @Test
    public void testGetWithSeminar_name(){
        // given, when
        Optional<Seminar> seminar = seminarRepository.findBySeminar_name("SeminarTest");

        // then
        assertNotNull(seminar.get());
        System.out.println(seminar.get());
    }

    /**
     * [ 2023-08-09 daeho.kang ]
     * Description : deleteWithSeminar_name
     *
     *     update
     *         seminar
     *     set
     *         del_dt=current_timestamp(6)
     *     where
     *         seminar_name=?
     */
    @DisplayName("deleteWithSeminar_name Test")
    @Test
    public void testDeleteWithSeminar_name(){
        // given, when, then
        seminarRepository.deleteBySeminar_name("SeminarRemoveTest");
    }




}
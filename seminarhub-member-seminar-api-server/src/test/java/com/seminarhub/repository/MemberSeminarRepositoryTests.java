package com.seminarhub.repository;

import com.seminarhub.entity.Member;
import com.seminarhub.entity.Member_Seminar;
import com.seminarhub.entity.Seminar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;


/**
 * [ 2023-08-08 daeho.kang ]
 * Description : Test For QueryDSL Or JPQL Custom Method
 *
 */
@SpringBootTest
public class MemberSeminarRepositoryTests {
    @Autowired
    private MemberSeminarRepository memberSeminarRepository;

    private final Long member_seminar_no = 1L;
    private final String member_id = "daeho.kang@naver.com"+ UUID.randomUUID();

    private final String member_password = "123123123";
    private final String seminar_name= "SeminarTest" + UUID.randomUUID();
    /**
     * [ 2023-08-08 daeho.kang ]
     * Description : Setting the Data for Test
     * For Data, I Added cascade.all for Member_Seminar Entity, it makes insert data and test easily.
     */
    @BeforeEach
    public void setup() throws Exception{
        if(memberSeminarRepository.findAllByMember_id(member_id).isEmpty()){

            // Member와 Seminar를 모방하는 Stubs/Mocks 생성
            Member memberStub = Member.builder()
                    .member_id(member_id)
                    .member_password(member_password)
                    .build();

            Seminar seminarStub = new Seminar();
            seminarStub.setSeminar_name(seminar_name);

            // CREATE Member_Seminar
            Member_Seminar member_seminar = Member_Seminar.builder()
                    .member(memberStub)
                    .seminar(seminarStub)
                    .build();

            memberSeminarRepository.save(member_seminar);
        }
        if(memberSeminarRepository.findAllBySeminar_name(seminar_name).isEmpty()){

            // Member와 Seminar를 모방하는 Stubs/Mocks 생성
            Member memberStub = Member.builder()
                    .member_id(member_id)
                    .member_password(member_password)
                    .build();

            Seminar seminarStub = new Seminar();
            seminarStub.setSeminar_name(seminar_name);

            // CREATE Member_Seminar
            Member_Seminar member_seminar = Member_Seminar.builder()
                    .member(memberStub)
                    .seminar(seminarStub)
                    .build();

            memberSeminarRepository.save(member_seminar);
        }
    }

    /**
     * [ 2023-08-08 daeho.kang ]
     * Description : memberSeminarRepository findBySeminar_name Test
     * Find Seminar by seminar_name
     *
     *     select
     *         m1_0.member_seminar_no,
     *         m1_0.del_dt,
     *         m1_0.inst_dt,
     *         m1_0.member_id,
     *         m1_0.seminar_name,
     *         m1_0.updt_dt
     *     from
     *         member_seminar m1_0
     *     where
     *         m1_0.member_seminar_no=?
     *         and m1_0.del_dt is null
     */
    @DisplayName("findByMember_Seminar_no Test")
    @Test
    public void testGetWithMember_Seminar_no(){
        // given, when, then
        Optional<Member_Seminar> member_seminar = memberSeminarRepository.findByMember_Seminar_no(member_seminar_no);

        // then
//        assertNotNull(member_seminar.get());
//        System.out.println(member_seminar.get());
    }


    /**
     * [ 2023-08-08 daeho.kang ]
     * Description : memberSeminarRepository findBySeminar_name Test
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
     *         Member_Seminar(member_seminar_no=17, del_dt=null)
     */
    @DisplayName("findAllByMember_id Test")
    @Test
    public void testGetWithMember_id(){
        // given, when
        List<Member_Seminar> member_seminar = memberSeminarRepository.findAllByMember_id(member_id);

        // then
        assertNotNull(member_seminar);
        member_seminar.stream().forEach(System.out::println);
    }

    /**
     * [ 2023-08-08 daeho.kang ]
     * Description : memberSeminarRepository findBySeminar_name Test
     * Find Seminar by seminar_name
     *
     *     select
     *         m1_0.member_seminar_no,
     *         m1_0.del_dt,
     *         m1_0.inst_dt,
     *         m1_0.member_id,
     *         m1_0.seminar_name,
     *         m1_0.updt_dt
     *     from
     *         member_seminar m1_0
     *     join
     *         seminar s1_0
     *             on s1_0.seminar_no=m1_0.seminar_name
     *     where
     *         s1_0.seminar_name=?
     *         and m1_0.del_dt is null
     *     Member_Seminar(member_seminar_no=16, del_dt=null)
     */
    @DisplayName("findAllBySeminar_name Test")
    @Test
    public void testGetWithSeminar_name(){
        // given, when
        List<Member_Seminar> member_seminar = memberSeminarRepository.findAllBySeminar_name(seminar_name);

        // then
        assertNotNull(member_seminar);
        member_seminar.stream().forEach(System.out::println);
    }



}
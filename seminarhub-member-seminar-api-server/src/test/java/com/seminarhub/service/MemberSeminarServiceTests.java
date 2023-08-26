package com.seminarhub.service;

import com.seminarhub.common.exception.DuplicateSeminarException;
import com.seminarhub.dto.MemberSeminarDTO;
import com.seminarhub.entity.Member;
import com.seminarhub.entity.Member_Seminar;
import com.seminarhub.entity.Seminar;
import com.seminarhub.repository.MemberSeminarRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * [ 2023-07-30 daeho.kang ]
 * Description : MemberServiceImpl를 테스트합니다.
 */
@SpringBootTest(classes = {MemberSeminarServiceImpl.class})
public class MemberSeminarServiceTests {
    @MockBean
    private MemberSeminarRepository memberSeminarRepository;

    @Autowired
    private MemberSeminarService memberSeminarService;

    private Long member_seminar_no = 1L;
    private String seminar_name = "SeminarTest";

    private String member_id = "daeho.kang@naver.com";

    private Member member = Member.builder()
            .member_id(member_id)
            .build();

    private Seminar seminar = Seminar.builder()
            .seminar_name(seminar_name)
            .build();

    /**
     * [ 2023-06-28 daeho.kang ]
     * Description : memberSeminarService Register Test
     */
    @DisplayName("Member_Seminar Service Register Test")
    @Test
    public void registerSeminarTest() throws DuplicateSeminarException {
        // given
        Member_Seminar member_seminar = Member_Seminar.builder()
                .member_seminar_no(member_seminar_no)
                .seminar(seminar)
                .member(member)
                .build();

        Mockito.when(memberSeminarRepository.save(any(Member_Seminar.class))).thenReturn(member_seminar);

        // when
        MemberSeminarDTO memberSeminarDTO = MemberSeminarDTO.builder()
                .member_seminar_no(member_seminar_no)
                .member_id(member_id)
                .seminar_name(seminar_name)
                .build();

        Long result = memberSeminarService.register(memberSeminarDTO);

        // then
        Assertions.assertEquals(result, member_seminar_no);
        verify(memberSeminarRepository).save(any(Member_Seminar.class));
    }

    /**
     * [ 2023-06-28 daeho.kang ]
     * Description : memberSeminarService Get Test
     */
    @DisplayName("Member_Seminar Service Get Test")
    @Test
    public void getMember_SeminarTest() {
        // given
        Member_Seminar existingMember_Seminar = Member_Seminar.builder()
                .member_seminar_no(member_seminar_no)
                .member(member)
                .seminar(seminar)
                .build();

        Mockito.when(memberSeminarRepository.findByMember_Seminar_no(member_seminar_no)).thenReturn(Optional.of(existingMember_Seminar));

        // when
        MemberSeminarDTO memberSeminarDTO = memberSeminarService.get(member_seminar_no);

        // then
        Assertions.assertEquals(memberSeminarDTO.getMember_id(), member_id);
        Assertions.assertEquals(memberSeminarDTO.getSeminar_name(), seminar_name);

        verify(memberSeminarRepository).findByMember_Seminar_no(member_seminar_no);
    }


    /**
     * [ 2023-06-28 daeho.kang ]
     * Description : memberSeminarService SOFT Remove Test
     */
    @DisplayName("Member_Seminar Service Soft Remove Test")
    @Test
    public void removeMember_SeminarTest() {
        // given
        LocalDateTime del_dt = LocalDateTime.now();
        Member_Seminar existingMember_Seminar = Member_Seminar.builder()
                .member(member)
                .seminar(seminar)
                .del_dt(del_dt)
                .build();

        Mockito.when(memberSeminarRepository.findByMember_Seminar_no(member_seminar_no)).thenReturn(Optional.of(existingMember_Seminar));

        // when
        memberSeminarService.remove(member_seminar_no);

        // then
        Assertions.assertNotNull(memberSeminarRepository.findByMember_Seminar_no(member_seminar_no).get().getDel_dt());
    }

}

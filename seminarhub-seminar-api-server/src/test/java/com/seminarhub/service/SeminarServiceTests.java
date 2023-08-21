package com.seminarhub.service;

import com.seminarhub.common.exception.DuplicateSeminarException;
import com.seminarhub.dto.SeminarDTO;
import com.seminarhub.dto.SeminarDTO;
import com.seminarhub.core.entity.Seminar;
import com.seminarhub.repository.SeminarRepository;
import com.seminarhub.service.SeminarService;
import com.seminarhub.service.SeminarServiceImpl;
import javassist.bytecode.DuplicateMemberException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;

/**
 * [ 2023-07-30 daeho.kang ]
 * Description : MemberServiceImpl를 테스트합니다.
 *
 */
@SpringBootTest(classes = {SeminarServiceImpl.class})
public class SeminarServiceTests {
    @MockBean
    private SeminarRepository seminarRepository;

    @Autowired
    private SeminarService seminarService;

    /**
     * [ 2023-06-28 daeho.kang ]
     * Description : seminarService Register Test
     */
    @DisplayName("Seminar Service Register Test")
    @Test
    public void registerSeminarTest() throws DuplicateSeminarException {
        // given
        Seminar seminar = Seminar.builder()
                .seminar_no((long)123)
                .seminar_name("SeminarTest")
                .seminar_explanation("SeminarExplanation")
                .build();

        Mockito.when(seminarRepository.save(refEq(seminar))).thenReturn(seminar);

        // when
        SeminarDTO seminarDTO = SeminarDTO.builder()
                .seminar_no((long)123)
                .seminar_name("SeminarTest")
                .seminar_explanation("SeminarExplanation")
                .build();

        Long result = seminarService.register(seminarDTO);

        // then
        Assertions.assertEquals(result, 123);
        verify(seminarRepository).save(refEq(seminar));
    }

    /**
     * [ 2023-06-28 daeho.kang ]
     * Description : seminarService Get Test
     */
    @DisplayName("Seminar Service Get Test")
    @Test
    public void getSeminarTest(){
        // given
        Seminar existingSeminar = Seminar.builder()
                .seminar_no((long)123)
                .seminar_name("SeminarTest")
                .seminar_explanation("SeminarExplanation")
                .build();
        Mockito.when(seminarRepository.findBySeminar_name("SeminarTest")).thenReturn(Optional.of(existingSeminar));

        // when
        SeminarDTO seminarDTO = seminarService.get("SeminarTest");

        // then
        Assertions.assertEquals(seminarDTO.getSeminar_no(), 123L);
        Assertions.assertEquals(seminarDTO.getSeminar_name(), "SeminarTest");
        Assertions.assertEquals(seminarDTO.getSeminar_explanation(), "SeminarExplanation");

        verify(seminarRepository).findBySeminar_name("SeminarTest");
    }

    /**
     * [ 2023-06-28 daeho.kang ]
     * Description : seminarService modify Test
     */
    @DisplayName("Seminar Service Modify Test")
    @Test
    public void modifySeminarTest() {
        // given
        Seminar existingSeminar = Seminar.builder()
                .seminar_no(123L)
                .seminar_name("SeminarTest")
                .seminar_explanation("modified_SeminarTest_Explanation")
                .build();
        Mockito.when(seminarRepository.findBySeminar_name("SeminarTest")).thenReturn(Optional.of(existingSeminar));
        SeminarDTO seminarDTO = SeminarDTO.builder()
                .seminar_no(123L)
                .seminar_name("SeminarTest")
                .seminar_explanation("modified_SeminarTest_Explanation")
                .build();
        // when
        seminarService.modify(seminarDTO);

        // then
        Mockito.verify(seminarRepository).save(Mockito.any(Seminar.class));
        Assertions.assertEquals("modified_SeminarTest_Explanation", existingSeminar.getSeminar_explanation());
    }

    /**
     * [ 2023-06-28 daeho.kang ]
     * Description : seminarService SOFT Remove Test
     */

    @DisplayName("Seminar Service Soft Remove Test")
    @Test
    public void removeSeminarTest() {
        // given
        LocalDateTime del_dt = LocalDateTime.now();
        Seminar existingSeminar = Seminar.builder()
                .seminar_no((long)123)
                .seminar_name("SeminarTest")
                .del_dt(del_dt)
                .build();

        Mockito.when(seminarRepository.findBySeminar_name("SeminarTest")).thenReturn(Optional.of(existingSeminar));

        // when
        seminarService.remove("SeminarTest");

        // then
        Mockito.verify(seminarRepository).deleteBySeminar_name("SeminarTest");
        Assertions.assertNotNull(seminarRepository.findBySeminar_name("SeminarTest").get().getDel_dt());
    }

}

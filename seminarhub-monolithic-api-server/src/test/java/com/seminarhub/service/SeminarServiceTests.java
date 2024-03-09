package com.seminarhub.service;

import com.seminarhub.dto.SeminarDTO;
import com.seminarhub.entity.Seminar;
import com.seminarhub.repository.SeminarQuerydslRepository;
import com.seminarhub.repository.SeminarRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.Mockito.verify;

/**
 * [ 2023-07-30 daeho.kang ]
 * Description: Test class for SeminarServiceImpl
 */
//@SpringBootTest(classes = {SeminarServiceImpl.class, SeminarQuerydslRepository.class, LogAdvice.class})
@SpringBootTest
public class SeminarServiceTests {
    @MockBean
    private SeminarRepository seminarRepository;

    @MockBean
    private SeminarQuerydslRepository seminarQuerydslRepository;

    @Autowired
    private SeminarService seminarService;

    @DisplayName("Seminar Service Get Test")
    @Test
    public void testGetSeminar(){
        // Given
        Seminar existingSeminar = Seminar.builder()
                .seminar_no((long)123L)
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

    @DisplayName("Seminar Service getWithPessimisticLock Test")
    @Transactional
    @Test
    public void testGetWithPessimisticLockSeminar(){
        // Given
        Seminar existingSeminar = Seminar.builder()
                .seminar_no((long)123L)
                .seminar_name("SeminarTest")
                .seminar_explanation("SeminarExplanation")
                .build();
        Mockito.when(seminarQuerydslRepository.findBySeminar_NameWithPessimisticLock("SeminarTest")).thenReturn(Optional.of(existingSeminar));

        // when
        SeminarDTO seminarDTO = seminarService.getWithPessimisticLock("SeminarTest");

        // then
        Assertions.assertEquals(seminarDTO.getSeminar_no(), 123L);
        Assertions.assertEquals(seminarDTO.getSeminar_name(), "SeminarTest");
        Assertions.assertEquals(seminarDTO.getSeminar_explanation(), "SeminarExplanation");

        verify(seminarQuerydslRepository).findBySeminar_NameWithPessimisticLock("SeminarTest");
    }

    @DisplayName("Seminar Service Increase Participants Count Test")
    @Test
    public void testIncreaseParticipantsCnt() {
        // Given
        Seminar seminar = Seminar.builder()
                .seminar_no((long) 123L)
                .seminar_name("SeminarTest")
                .build();
        Mockito.when(seminarRepository.findById(123L)).thenReturn(Optional.of(seminar));

        // When
        seminarService.increaseParticipantsCnt(123L);

        // Then
        verify(seminarRepository).incrementParticipantsCnt(123L);
    }

    @DisplayName("Seminar Service Decrease Participants Count Test")
    @Test
    public void testDecreaseParticipantsCnt() {
        // Given
        Seminar seminar = Seminar.builder()
                .seminar_no((long) 123L)
                .seminar_name("SeminarTest")
                .build();
        Mockito.when(seminarRepository.findById(123L)).thenReturn(Optional.of(seminar));

        // When
        seminarService.decreaseParticipantsCnt(123L);

        // Then
        verify(seminarRepository).decreaseParticipantsCnt(123L);
    }


    @Test
    public void testClass(){
        System.out.println(seminarService.printOutString("helloAOP"));
        System.out.println(seminarService.printOutString(""));
    }



}

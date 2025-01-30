package com.seminarhub.repository;

import com.seminarhub.entity.SeminarDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class SeminarRepositoryTest {
    @Autowired
    private SeminarRepository seminarRepository;

    // CREATE 테스트
    @Test
    public void testInsertSeminar() {
        SeminarDTO seminar = new SeminarDTO();
        seminar.setName("Test Seminar");
        seminar.setDescription("This is a test seminar.");
        seminar.setPrice(10000L);
        seminar.setAvailable_seats(50L);
        seminar.setReg_start_date(LocalDateTime.now().minusDays(1));
        seminar.setReg_end_date(LocalDateTime.now().plusDays(5));
        seminar.setStart_date(LocalDateTime.now().plusDays(10));
        seminar.setEnd_date(LocalDateTime.now().plusDays(15));
        seminar.setAddress("Seoul, Korea");
        seminar.setCompany_no(1L);
        seminar.setMax_capacity(100L);

        int result = seminarRepository.insert(seminar);
        Assertions.assertEquals(1, result, "Seminar should be inserted successfully.");
    }

    // SELECT BY ID 테스트
    @Test
    public void testFindSeminarById() {
        Long seminar_no = 1L; // 테스트에 맞는 실제 seminar_no 값으로 변경
        SeminarDTO seminar = seminarRepository.findSeminarById(seminar_no.intValue());
        Assertions.assertNotNull(seminar, "Seminar should not be null.");
        System.out.println(seminar);
    }

    // SELECT LIST BY COMPANY_NO 테스트
    @Test
    public void testFindSeminarsByCompanyNo() {
        Long company_no = 1L; // 테스트에 맞는 실제 company_no 값으로 변경
        List<SeminarDTO> seminars = seminarRepository.findSeminarsByCompanyNo(company_no.intValue());
        Assertions.assertFalse(seminars.isEmpty(), "Seminars list should not be empty.");
        seminars.forEach(System.out::println);
    }

    // UPDATE 테스트
    @Test
    public void testUpdateSeminar() {
        SeminarDTO seminar = new SeminarDTO();
        seminar.setSeminar_no(1L); // 테스트에 맞는 실제 seminar_no 값으로 변경
        seminar.setName("Updated Seminar Name");
        seminar.setDescription("This is an updated seminar description.");
        seminar.setPrice(15000L);
        seminar.setAvailable_seats(40L);
        seminar.setReg_start_date(LocalDateTime.now().minusDays(2));
        seminar.setReg_end_date(LocalDateTime.now().plusDays(4));
        seminar.setStart_date(LocalDateTime.now().plusDays(8));
        seminar.setEnd_date(LocalDateTime.now().plusDays(12));
        seminar.setAddress("Busan, Korea");
        seminar.setCompany_no(1L);
        seminar.setMax_capacity(90L);

        int result = seminarRepository.update(seminar);
        Assertions.assertEquals(1, result, "Seminar should be updated successfully.");
    }

    // DELETE (Soft Delete) 테스트
    @Test
    public void testSoftDeleteSeminar() {
        Long seminar_no = 1L; // 테스트에 맞는 실제 seminar_no 값으로 변경
        int result = seminarRepository.softDelete(seminar_no);
        Assertions.assertEquals(1, result, "Soft delete should be successful.");
    }
}
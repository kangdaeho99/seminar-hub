package com.seminarhub.service;

import com.seminarhub.common.exception.DuplicateSeminarException;
import com.seminarhub.entity.Member_Seminar;
import com.seminarhub.dto.MemberSeminarDTO;
import com.seminarhub.repository.MemberSeminarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * [ 2023-08-21 daeho.kang ]
 * Description: Implementation of MemberSeminarService interface
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class MemberSeminarServiceImpl implements MemberSeminarService {

    private final MemberSeminarRepository memberSeminarRepository;

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description: Register a new seminar
     * Checks if the seminarName already exists. If duplicated, throws DuplicateSeminarException.
     * If not duplicated, saves the seminar.
     */
    @Override
    public Long register(MemberSeminarDTO memberSeminarDTO) {
        Member_Seminar member_seminar = dtoToEntity(memberSeminarDTO);
        memberSeminarRepository.save(member_seminar);
        return member_seminar.getMember_seminar_no();
    }

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description: Retrieve seminar information
     * Retrieves seminar information based on seminar_name.
     * If information exists, converts it to DTO and returns.
     */
    @Override
    public MemberSeminarDTO get(Long member_seminar_no) {
        Optional<Member_Seminar> result = memberSeminarRepository.findByMember_Seminar_no(member_seminar_no);
        if(result.isPresent()){
            log.info("---------------------GET----------------------");
            log.info(result.get());
            return entityToDTO(result.get());
        }
        return null;
    }

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description: Remove a seminar (SOFT deletion)
     * Checks if the seminar_name exists and performs SOFT DELETE.
     */
    @Override
    public void remove(Long member_seminar_no) {
        Optional<Member_Seminar> result = memberSeminarRepository.findByMember_Seminar_no(member_seminar_no);
        if(result.isPresent()){
            result.get().setDel_dt(LocalDateTime.now());
        }
    }

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description: Retrieve all Member_Seminar information by member_id
     * Retrieves all seminars associated with a given member_id.
     * Converts the retrieved entities to DTOs and returns as a list.
     */
    @Override
    public List<MemberSeminarDTO> getListByMember_id(String member_id) {
        List<Member_Seminar> result = memberSeminarRepository.findAllByMember_id(member_id);

        return result.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }


}

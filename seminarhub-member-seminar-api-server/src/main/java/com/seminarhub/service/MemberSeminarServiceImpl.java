package com.seminarhub.service;

import com.seminarhub.common.exception.DuplicateSeminarException;
import com.seminarhub.entity.Member_Seminar;
import com.seminarhub.dto.MemberSeminarDTO;
import com.seminarhub.repository.MemberSeminarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * [ 2023-08-21 daeho.kang ]
 * Description : SeminarServiceImpl 구현체
 *
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class MemberSeminarServiceImpl implements MemberSeminarService {

    private final MemberSeminarRepository memberSeminarRepository;

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description :
     * seminarName을 통해 이미 중복하는 아이디가 존재하는지 확인합니다. 중복일시 DuplicateSeminarException 이 발생합니다.
     * 통과하였다면, 세미나가 저장됩니다.
     */
    @Override
    public Long register(MemberSeminarDTO memberSeminarDTO) {
        Member_Seminar member_seminar = dtoToEntity(memberSeminarDTO);
        memberSeminarRepository.save(member_seminar);
        return member_seminar.getMember_seminar_no();
    }

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description :
     * seminar_name을 통해 seminar의 정보를 가져옵니다.
     * 정보가 존재한다면, 해당 데이터를 DTO로 변환하여 전달합니다.
     */
    @Override
    public MemberSeminarDTO get(Long member_seminar_no) {
        Optional<Member_Seminar> result = memberSeminarRepository.findByMember_Seminar_no(member_seminar_no);
        if(result.isPresent()){
            return entityToDTO(result.get());
        }
        return null;
    }

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description :
     * seminar_name을 통해 해당 값이 존재하는지 확인하고, 값이 존재한다면 SOFT DELETE 처리합니다.
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
     * Description :
     * seminar_name을 통해 seminar의 정보를 가져옵니다.
     * 정보가 존재한다면, 해당 데이터를 DTO로 변환하여 전달합니다.
     */
    @Override
    public List<MemberSeminarDTO> getListByMember_id(String member_id) {
        List<Member_Seminar> result = memberSeminarRepository.findAllByMember_id(member_id);

        return result.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }


}

package com.seminarhub.service;

import com.seminarhub.common.exception.SeminarRegistrationFullException;
import com.seminarhub.dto.MemberDTO;
import com.seminarhub.dto.MemberSeminarRegisterRequestDTO;
import com.seminarhub.dto.Member_SeminarDTO;
import com.seminarhub.dto.SeminarDTO;
import com.seminarhub.entity.Member_Seminar;
import com.seminarhub.entity.Member_Seminar_Payment_History;
import com.seminarhub.repository.Member_SeminarRepository;
import com.seminarhub.repository.Member_Seminar_Payment_HistoryRepository;
import com.seminarhub.repository.SeminarQuerydslRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class Member_SeminarServiceImpl implements Member_SeminarService{

    private final Member_SeminarRepository member_seminarRepository;

    private final Member_Seminar_Payment_HistoryRepository member_seminar_payment_historyRepository;

    private final SeminarQuerydslRepository seminarQuerydslRepository;

    private final SeminarService seminarService;
    private final MemberService memberService;

    @Override
    public Long register(Member_SeminarDTO member_SeminarDTO) {
        Member_Seminar member_seminar = dtoToEntity(member_SeminarDTO);
        log.info("===============member_seminar Register===============");
        member_seminarRepository.save(member_seminar);
        return null;
    }

    @Override
    public Member_SeminarDTO get(long member_seminarno) {
        Optional<Member_Seminar> result = member_seminarRepository.findByMember_Seminar_no(member_seminarno);
        if(result.isPresent()){
            return entityToDTO(result.get());
        }
        return null;
    }

    @Override
    public void modify(Member_SeminarDTO member_SeminarDTO) {
        long member_seminarno = member_SeminarDTO.getMember_seminar_no();
        Optional<Member_Seminar> result = member_seminarRepository.findByMember_Seminar_no(member_seminarno);
        if(result.isPresent()){
            Member_Seminar member_seminar = result.get();
            //수정할 것 수정합니다.
            member_seminarRepository.save(member_seminar);
        }
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Override
    public Long registerForSeminar(MemberSeminarRegisterRequestDTO memberSeminarRegisterRequestDTO) throws SeminarRegistrationFullException {
        log.info(memberSeminarRegisterRequestDTO.toString());
        MemberDTO memberDTO = memberService.get(memberSeminarRegisterRequestDTO.getMember_id());
        SeminarDTO seminarDTO = seminarService.getWithPessimisticLock(memberSeminarRegisterRequestDTO.getSeminar_name());

        if (seminarDTO == null || memberDTO == null) {
            // 예외 처리: 세미나나 멤버가 존재하지 않는 경우
            throw new SeminarRegistrationFullException("There are no Info Of Member || Seminar");
        }

        //신청하려는 Seminar가 아직 남아있는지 확인해야하고, Transactional로 격리상태를 유지해야합니다.
//        Long currentParticipateCnt = seminarQuerydslRepository.findCurrentParticipateCount(seminarDTO);
        Long currentParticipateCnt = seminarDTO.getSeminar_participants_cnt();
        if (seminarDTO.getSeminar_max_participants() <= (currentParticipateCnt)) {
            throw new SeminarRegistrationFullException("SeminarInfo:" + seminarDTO.getSeminar_name() + "is already " + currentParticipateCnt + "/" + seminarDTO.getSeminar_max_participants() + " full. Registration failed.");
        }
        seminarService.increaseParticipantsCnt(seminarDTO.getSeminar_no());

        Member_Seminar_Payment_History member_seminar_payment_history = Member_Seminar_Payment_History.builder()
                .member_seminar_payment_history_amount(seminarDTO.getSeminar_price())
                .build();
        member_seminar_payment_historyRepository.save(member_seminar_payment_history);

        Member_SeminarDTO member_seminarDTO = Member_SeminarDTO.builder()
                .member_no(memberDTO.getMember_no())
                .seminar_no(seminarDTO.getSeminar_no())
                .member_seminar_payment_history_no(member_seminar_payment_history.getMember_seminar_payment_history_no())
                .build();
        Member_Seminar member_seminar = dtoToEntity(member_seminarDTO);
        member_seminarRepository.save(member_seminar);
        return member_seminar.getMember_seminar_no();
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    @Override
    public void registerForSeminarWithList(List<MemberSeminarRegisterRequestDTO> memberSeminarRegisterRequestDTO) throws SeminarRegistrationFullException {
//        log.info(memberSeminarRegisterRequestDTO.toString());
        for(int i=0;i<memberSeminarRegisterRequestDTO.size();i++) {
            try{
                registerSeminarIndependently(memberSeminarRegisterRequestDTO.get(i));
            }catch (Throwable e){
                e.printStackTrace();
                throw e; // 다시 예외를 던지면서 상위 호출자에게 전달
            }
        }

//        return member_seminar.getMember_seminar_no();
    }

    @Transactional( isolation = Isolation.READ_UNCOMMITTED)
    public void registerSeminarIndependently(MemberSeminarRegisterRequestDTO memberSeminarRegisterRequestDTO){
        MemberDTO memberDTO = memberService.get(memberSeminarRegisterRequestDTO.getMember_id());
        SeminarDTO seminarDTO = seminarService.getWithPessimisticLock(memberSeminarRegisterRequestDTO.getSeminar_name());

        if (seminarDTO == null || memberDTO == null) {
            // 예외 처리: 세미나나 멤버가 존재하지 않는 경우
            throw new SeminarRegistrationFullException("There are no Info Of Member || Seminar");
        }

        Long currentParticipateCnt = seminarDTO.getSeminar_participants_cnt();
        if (seminarDTO.getSeminar_max_participants() <= (currentParticipateCnt)) {
            throw new SeminarRegistrationFullException("SeminarInfo:" + seminarDTO.getSeminar_name() + "is already " + currentParticipateCnt + "/" + seminarDTO.getSeminar_max_participants() + " full. Registration failed.");
        }
        seminarService.increaseParticipantsCnt(seminarDTO.getSeminar_no());

        Member_Seminar_Payment_History member_seminar_payment_history = Member_Seminar_Payment_History.builder()
                .member_seminar_payment_history_amount(seminarDTO.getSeminar_price())
                .build();
        member_seminar_payment_historyRepository.save(member_seminar_payment_history);

        Member_SeminarDTO member_seminarDTO = Member_SeminarDTO.builder()
                .member_no(memberDTO.getMember_no())
                .seminar_no(seminarDTO.getSeminar_no())
                .member_seminar_payment_history_no(member_seminar_payment_history.getMember_seminar_payment_history_no())
                .build();
        Member_Seminar member_seminar = dtoToEntity(member_seminarDTO);
        member_seminarRepository.save(member_seminar);
    }


//    @Transactional
//    @Override
//    public Long registerForSeminar(MemberSeminarRegisterRequestDTO memberSeminarRegisterRequestDTO) throws SeminarRegistrationFullException {
//        log.info(memberSeminarRegisterRequestDTO.toString());
//        SeminarDTO seminarDTO = seminarService.get(memberSeminarRegisterRequestDTO.getSeminar_name());
//        MemberDTO memberDTO = memberService.get(memberSeminarRegisterRequestDTO.getMember_id());
//        if (seminarDTO == null || memberDTO == null) {
//            // 예외 처리: 세미나나 멤버가 존재하지 않는 경우
//            throw new SeminarRegistrationFullException("There are no Info Of Member || Seminar");
//        }
//
//        //신청하려는 Seminar가 아직 남아있는지 확인해야하고, Transactional로 격리상태를 유지해야합니다.
//        Long currentParticipateCnt = seminarQuerydslRepository.findCurrentParticipateCount(seminarDTO);
//        if (seminarDTO.getSeminar_max_participants() < currentParticipateCnt) {
//            throw new SeminarRegistrationFullException("SeminarInfo:" + seminarDTO.getSeminar_name() + "is already " + currentParticipateCnt + "/" + seminarDTO.getSeminar_max_participants() + " full. Registration failed.");
//        }
//
//        Member_Seminar_Payment_History member_seminar_payment_history = Member_Seminar_Payment_History.builder()
//                .member_seminar_payment_history_amount(seminarDTO.getSeminar_price())
//                .build();
//        member_seminar_payment_historyRepository.save(member_seminar_payment_history);
//
//        Member_SeminarDTO member_seminarDTO = Member_SeminarDTO.builder()
//                .member_no(memberDTO.getMember_no())
//                .seminar_no(seminarDTO.getSeminar_no())
//                .member_seminar_payment_history_no(member_seminar_payment_history.getMember_seminar_payment_history_no())
//                .build();
//        Member_Seminar member_seminar = dtoToEntity(member_seminarDTO);
//        member_seminarRepository.save(member_seminar);
//        return member_seminar.getMember_seminar_no();
//    }


//    @Override
//    public Long registerForSeminar(MemberSeminarRegisterRequestDTO memberSeminarRegisterRequestDTO) throws SeminarRegistrationFullException {
//        SeminarDTO seminarDTO = seminarService.get(memberSeminarRegisterRequestDTO.getSeminar_name()); //결제가 완료되고 바뀌는 순간에 Seminar 가격정보가 바뀐다면?? 실제로 8000원을 입금했는데, 10000원이 정산되는 사례 발생가능.
//        MemberDTO memberDTO = memberService.get(memberSeminarRegisterRequestDTO.getMember_id());
//        if (seminarDTO == null || memberDTO == null) {
//            throw new SeminarRegistrationFullException("There are no Info Of Member || Seminar");
//        }
//
//        Long currentParticipateCnt = seminarQuerydslRepository.findCurrentParticipateCount(seminarDTO);
//        if (seminarDTO.getSeminar_max_participants() < currentParticipateCnt) {
//            throw new SeminarRegistrationFullException("SeminarInfo:" + seminarDTO.getSeminar_name() + "is already " + currentParticipateCnt + "/" + seminarDTO.getSeminar_max_participants() + " full. Registration failed.");
//        }
//
//        return register(seminarDTO, memberDTO);
//    }

    //readOnly = false : save 작업이 존재하므로 쓰기 작업이 진행됩니다.
    //propagation = Propagation.REQUIRED : 현재 진행하는 트랜잭션이 잇으면 이 트랜잭션에 포함하고, 진행하는 트랜잭션이 없으면 새로운 트랜잭션을 시작한 후 해당 메서드를 실행한다.
    //isolation = Isolation.REPEATABLE_READ :

//    @Transactional(readOnly = false,  propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
//    public Long register(SeminarDTO seminarDTO, MemberDTO memberDTO) {
//        Member_Seminar_Payment_History member_seminar_payment_history = Member_Seminar_Payment_History.builder()
//                .member_seminar_payment_history_amount(seminarDTO.getSeminar_price())
//                .build();
//        member_seminar_payment_historyRepository.save(member_seminar_payment_history);
//
//        Member_SeminarDTO member_seminarDTO = Member_SeminarDTO.builder()
//                .member_no(memberDTO.getMember_no())
//                .seminar_no(seminarDTO.getSeminar_no())
//                .member_seminar_payment_history_no(member_seminar_payment_history.getMember_seminar_payment_history_no())
//                .build();
//        Member_Seminar member_seminar = dtoToEntity(member_seminarDTO);
//        member_seminarRepository.save(member_seminar);
//        return member_seminar.getMember_seminar_no();
//    }



    @Override
    public void remove(long member_seminarno) {
        Optional<Member_Seminar> result = member_seminarRepository.findByMember_Seminar_no(member_seminarno);
        if(result.isPresent()){
            Member_Seminar member_seminar = result.get();
            member_seminar.setDel_dt(LocalDateTime.now());
        }
    }
}

package com.seminarhub.domain.seminar.repository.querydsl;
import com.seminarhub.domain.seminar.domain.MemberSeminarSettlementDate;
import com.seminarhub.domain.seminar.service.MemberSeminarSettlementDateSearchQuery;
import com.seminarhub.global.dto.CursorRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface MemberSeminarSettlementDateRepositoryCustom { List<MemberSeminarSettlementDate> search(MemberSeminarSettlementDateSearchQuery q); Page<MemberSeminarSettlementDate> search(MemberSeminarSettlementDateSearchQuery q, Pageable p); List<MemberSeminarSettlementDate> search(MemberSeminarSettlementDateSearchQuery q, CursorRequest c); }

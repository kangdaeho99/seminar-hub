package com.seminarhub.domain.seminar.repository.querydsl;
import com.seminarhub.domain.seminar.domain.MemberSeminarPaymentHistory;
import com.seminarhub.domain.seminar.service.MemberSeminarPaymentHistorySearchQuery;
import com.seminarhub.global.dto.CursorRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface MemberSeminarPaymentHistoryRepositoryCustom { List<MemberSeminarPaymentHistory> search(MemberSeminarPaymentHistorySearchQuery q); Page<MemberSeminarPaymentHistory> search(MemberSeminarPaymentHistorySearchQuery q, Pageable p); List<MemberSeminarPaymentHistory> search(MemberSeminarPaymentHistorySearchQuery q, CursorRequest c); }

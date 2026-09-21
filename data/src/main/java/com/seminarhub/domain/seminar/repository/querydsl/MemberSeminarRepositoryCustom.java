package com.seminarhub.domain.seminar.repository.querydsl;
import com.seminarhub.domain.seminar.domain.MemberSeminar;
import com.seminarhub.domain.seminar.service.MemberSeminarSearchQuery;
import com.seminarhub.global.dto.CursorRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface MemberSeminarRepositoryCustom { List<MemberSeminar> search(MemberSeminarSearchQuery q); Page<MemberSeminar> search(MemberSeminarSearchQuery q, Pageable p); List<MemberSeminar> search(MemberSeminarSearchQuery q, CursorRequest c); }

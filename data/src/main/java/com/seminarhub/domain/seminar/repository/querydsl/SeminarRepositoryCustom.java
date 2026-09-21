package com.seminarhub.domain.seminar.repository.querydsl;
import com.seminarhub.domain.seminar.domain.Seminar;
import com.seminarhub.domain.seminar.service.SeminarSearchQuery;
import com.seminarhub.global.dto.CursorRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface SeminarRepositoryCustom { List<Seminar> search(SeminarSearchQuery q); Page<Seminar> search(SeminarSearchQuery q, Pageable p); List<Seminar> search(SeminarSearchQuery q, CursorRequest c); }

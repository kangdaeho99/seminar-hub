package com.seminarhub.domain.member.repository.querydsl;
import com.seminarhub.domain.member.domain.Member;
import com.seminarhub.domain.member.service.MemberSearchQuery;
import com.seminarhub.global.dto.CursorRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface MemberRepositoryCustom {
    List<Member> search(MemberSearchQuery query);
    Page<Member> search(MemberSearchQuery query, Pageable pageable);
    List<Member> search(MemberSearchQuery query, CursorRequest cursorRequest);
}

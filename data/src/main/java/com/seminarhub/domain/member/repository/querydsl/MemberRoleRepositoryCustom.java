package com.seminarhub.domain.member.repository.querydsl;
import com.seminarhub.domain.member.domain.MemberRole;
import com.seminarhub.domain.member.service.MemberRoleSearchQuery;
import com.seminarhub.global.dto.CursorRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface MemberRoleRepositoryCustom {
    List<MemberRole> search(MemberRoleSearchQuery query);
    Page<MemberRole> search(MemberRoleSearchQuery query, Pageable pageable);
    List<MemberRole> search(MemberRoleSearchQuery query, CursorRequest cursorRequest);
}

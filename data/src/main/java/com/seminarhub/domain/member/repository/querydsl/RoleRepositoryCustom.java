package com.seminarhub.domain.member.repository.querydsl;
import com.seminarhub.domain.member.domain.Role;
import com.seminarhub.domain.member.service.RoleSearchQuery;
import com.seminarhub.global.dto.CursorRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface RoleRepositoryCustom {
    List<Role> search(RoleSearchQuery query);
    Page<Role> search(RoleSearchQuery query, Pageable pageable);
    List<Role> search(RoleSearchQuery query, CursorRequest cursorRequest);
}

package com.seminarhub.domain.member.service;
import com.seminarhub.domain.member.domain.Member;
import com.seminarhub.domain.member.domain.MemberRole;
import com.seminarhub.domain.member.domain.Role;
import com.seminarhub.domain.member.repository.MemberRoleRepository;
import com.seminarhub.global.dto.CursorRequest;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class MemberRoleService {
    private final MemberRoleRepository repository;
    @Transactional public MemberRole save(MemberRole e) { return repository.save(e); }
    public Optional<MemberRole> findById(Long id) { return repository.findByIdAndDeletedAtIsNull(id); }
    public List<MemberRole> findByIds(List<Long> ids) { return repository.findAllByIdInAndDeletedAtIsNull(ids); }
    public List<MemberRole> findAll(MemberRoleSearchQuery q) { return repository.search(q); }
    public Page<MemberRole> findAll(MemberRoleSearchQuery q, Pageable p) { return repository.search(q, p); }
    public List<MemberRole> findByCursor(MemberRoleSearchQuery q, CursorRequest c) { return repository.search(q, c); }
    @Transactional public MemberRole update(MemberRole e, Member member, Role role) { e.update(member, role); return e; }
    @Transactional public MemberRole delete(MemberRole e) { e.delete(); return e; }
    @Transactional public List<MemberRole> deleteAll(List<MemberRole> es) { es.forEach(MemberRole::delete); return es; }
}

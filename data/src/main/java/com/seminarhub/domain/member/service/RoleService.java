package com.seminarhub.domain.member.service;
import com.seminarhub.domain.member.domain.Role;
import com.seminarhub.domain.member.enums.RoleType;
import com.seminarhub.domain.member.repository.RoleRepository;
import com.seminarhub.global.dto.CursorRequest;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class RoleService {
    private final RoleRepository repository;
    @Transactional public Role save(Role e) { return repository.save(e); }
    public Optional<Role> findById(Long id) { return repository.findByIdAndDeletedAtIsNull(id); }
    public List<Role> findByIds(List<Long> ids) { return repository.findAllByIdInAndDeletedAtIsNull(ids); }
    public List<Role> findAll(RoleSearchQuery q) { return repository.search(q); }
    public Page<Role> findAll(RoleSearchQuery q, Pageable p) { return repository.search(q, p); }
    public List<Role> findByCursor(RoleSearchQuery q, CursorRequest c) { return repository.search(q, c); }
    @Transactional public Role update(Role e, RoleType roleType) { e.update(roleType); return e; }
    @Transactional public Role delete(Role e) { e.delete(); return e; }
    @Transactional public List<Role> deleteAll(List<Role> es) { es.forEach(Role::delete); return es; }
}

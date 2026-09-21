package com.seminarhub.domain.member.service;
import com.seminarhub.domain.member.domain.Member;
import com.seminarhub.domain.member.repository.MemberRepository;
import com.seminarhub.global.dto.CursorRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository repository;
    @Transactional public Member save(Member e) { return repository.save(e); }
    public Optional<Member> findById(Long id) { return repository.findByIdAndDeletedAtIsNull(id); }
    public List<Member> findByIds(List<Long> ids) { return repository.findAllByIdInAndDeletedAtIsNull(ids); }
    public List<Member> findAll(MemberSearchQuery q) { return repository.search(q); }
    public Page<Member> findAll(MemberSearchQuery q, Pageable p) { return repository.search(q, p); }
    public List<Member> findByCursor(MemberSearchQuery q, CursorRequest c) { return repository.search(q, c); }
    @Transactional public Member update(Member e, String email, String password, String nickname, Boolean fromSocial, BigDecimal chargedMoney) { e.update(email, password, nickname, fromSocial, chargedMoney); return e; }
    @Transactional public Member delete(Member e) { e.delete(); return e; }
    @Transactional public List<Member> deleteAll(List<Member> es) { es.forEach(Member::delete); return es; }
}

package com.seminarhub.domain.seminar.repository;
import com.seminarhub.domain.seminar.domain.MemberSeminar;
import com.seminarhub.domain.seminar.repository.querydsl.MemberSeminarRepositoryCustom;
import java.util.Collection; import java.util.List; import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MemberSeminarRepository extends JpaRepository<MemberSeminar, Long>, MemberSeminarRepositoryCustom {
    Optional<MemberSeminar> findByIdAndDeletedAtIsNull(Long id); List<MemberSeminar> findAllByIdInAndDeletedAtIsNull(Collection<Long> ids);
}

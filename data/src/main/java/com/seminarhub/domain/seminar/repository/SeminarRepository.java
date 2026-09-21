package com.seminarhub.domain.seminar.repository;
import com.seminarhub.domain.seminar.domain.Seminar;
import com.seminarhub.domain.seminar.repository.querydsl.SeminarRepositoryCustom;
import java.util.Collection; import java.util.List; import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SeminarRepository extends JpaRepository<Seminar, Long>, SeminarRepositoryCustom {
    Optional<Seminar> findByIdAndDeletedAtIsNull(Long id); List<Seminar> findAllByIdInAndDeletedAtIsNull(Collection<Long> ids);
}

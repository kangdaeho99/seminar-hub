package com.seminarhub.domain.seminar.repository;
import com.seminarhub.domain.seminar.domain.MemberSeminar;
import com.seminarhub.domain.seminar.repository.querydsl.MemberSeminarRepositoryCustom;
import jakarta.persistence.LockModeType;
import java.util.Collection; import java.util.List; import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface MemberSeminarRepository extends JpaRepository<MemberSeminar, Long>, MemberSeminarRepositoryCustom {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select m
            from MemberSeminar m
            where m.id = :id
              and m.deletedAt is null
            """)
    Optional<MemberSeminar> findByIdForUpdate(@Param("id") Long id);

    Optional<MemberSeminar> findByIdAndDeletedAtIsNull(Long id); List<MemberSeminar> findAllByIdInAndDeletedAtIsNull(Collection<Long> ids);
}

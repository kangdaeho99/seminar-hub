package com.seminarhub.domain.seminar.repository;
import com.seminarhub.domain.seminar.domain.MemberSeminarSettlementDate;
import com.seminarhub.domain.seminar.repository.querydsl.MemberSeminarSettlementDateRepositoryCustom;
import java.time.LocalDate; import java.util.Collection; import java.util.List; import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface MemberSeminarSettlementDateRepository extends JpaRepository<MemberSeminarSettlementDate, Long>, MemberSeminarSettlementDateRepositoryCustom {
    Optional<MemberSeminarSettlementDate> findByIdAndDeletedAtIsNull(Long id); List<MemberSeminarSettlementDate> findAllByIdInAndDeletedAtIsNull(Collection<Long> ids);
    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE MemberSeminarSettlementDate d SET d.date = :targetDate
            WHERE d.memberSeminarItem.id = :memberSeminarItemId AND NOT EXISTS (
            SELECT 1 FROM SettlementItem i JOIN i.settlement s
            WHERE i.memberSeminarItem = d.memberSeminarItem AND s.deletedAt IS NULL AND i.deletedAt IS NULL)""")
    int updateDateByMemberSeminarItemIdIfNotSettled(@Param("memberSeminarItemId") Long memberSeminarItemId, @Param("targetDate") LocalDate targetDate);
}

package com.seminarhub.domain.seminar.domain;

import com.seminarhub.global.domain.base.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "memberSeminars")
public class Seminar extends AuditMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @Column(length = 500)
    private String explanation;

    @Column
    private Long price;

    @Column
    private Long maxParticipants;

    @Column
    private Long participantsCount;

    @OneToMany(mappedBy = "seminar", fetch = FetchType.LAZY)
    private List<MemberSeminar> memberSeminars;

    public void update(String name, String explanation, Long price, Long maxParticipants, Long participantsCount) {
        if (name != null) this.name = name;
        if (explanation != null) this.explanation = explanation;
        if (price != null) this.price = price;
        if (maxParticipants != null) this.maxParticipants = maxParticipants;
        if (participantsCount != null) this.participantsCount = participantsCount;
        markUpdated();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParticipants_cnt(long participantsCount) {
        this.participantsCount = participantsCount;
    }

    public void delete() {
        markDeleted(LocalDateTime.now());
    }
}

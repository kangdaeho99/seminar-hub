package com.seminarhub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member_seminar_list"})
public class Seminar extends BaseEntity {

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
    private Long max_participants;

    @Column
    private Long participants_cnt;

//    @Version
//    private Integer version;

    @Column(nullable=true)
    private LocalDateTime del_dt;

    @OneToMany(mappedBy = "seminar", fetch = FetchType.LAZY)
    private List<MemberSeminar> member_seminar_list;

    public void setDel_dt(LocalDateTime del_dt){ this.del_dt = del_dt; }

    public void setName(String name){
        this.name = name;
    }

    public void setParticipants_cnt(long participants_cnt) { this.participants_cnt = participants_cnt;}
}

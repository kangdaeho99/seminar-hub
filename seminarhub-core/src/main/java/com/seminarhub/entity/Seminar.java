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
    private Long seminar_no;

    @Column(length = 100, nullable = false, unique = true)
    private String seminar_name;

    @Column(length = 500)
    private String seminar_explanation;

    @Column(nullable=true)
    private LocalDateTime del_dt;

    @OneToMany(mappedBy = "seminar", fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Member_Seminar> member_seminar_list;

    public void setDel_dt(LocalDateTime del_dt){ this.del_dt = del_dt; }

    public void setSeminar_name(String seminar_name){
        this.seminar_name = seminar_name;
    }
}

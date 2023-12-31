package com.infinity.fashionity.consultants.entity;

import com.infinity.fashionity.global.entity.CUDEntity;
import com.infinity.fashionity.members.entity.MemberEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="reservations")
@SQLDelete(sql = "UPDATE reservations SET deleted_at = now() WHERE reservation_seq = ?")
@Where(clause = "deleted_at is null")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationEntity extends CUDEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_seq")
    private Long seq;

    @JoinColumn(name = "schedule_seq", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private ScheduleEntity schedule;

    @JoinColumn(name = "member_seq", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    // 예약 일시
    @Column(name = "reservation_date", unique = false, nullable = true)
    private LocalDateTime date;

    // 예약 상세
    @Column(name = "reservation_detail", unique = false, nullable = true, length = 200)
    private String detail;

    // 컨설턴트 예약 사진
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reservation", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ConsultantImageEntity> consultantImages = new ArrayList<>();

    // 유저 예약 사진
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reservation", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MemberImageEntity> memberImages = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "reservation")
    private ReviewEntity review;

    public void setMemberImages(List<MemberImageEntity> memberImages) {
        this.memberImages = memberImages;
    }
    public void setConsultantImages(List<ConsultantImageEntity> consultantImages) {
        this.consultantImages = consultantImages;
    }
}

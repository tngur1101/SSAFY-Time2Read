package org.ssafy.bibibig.timeAttact.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ssafy.bibibig.oauth.domain.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="time_attacks")
public class TimeAttacks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="time_attack_time")
    private int timeAttackTime;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }



}

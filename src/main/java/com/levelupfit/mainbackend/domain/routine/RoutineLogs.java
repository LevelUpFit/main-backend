package com.levelupfit.mainbackend.domain.routine;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutineLogs {

    @Id
    @Column(name="routine_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="user_id")
    private int userId;

    @Column(name="routine_id")
    private int routineId;

    @Column(name="performed_date")
    private LocalDate performedDate;

    @Column(name="created_at")
    private LocalDate createdAt;

    @PrePersist //save() 할 때 불러와짐
    public void setCreatedAt() {
        this.createdAt = LocalDate.now();
    }
}

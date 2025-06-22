package com.levelupfit.mainbackend.domain.feedbacks;

import com.levelupfit.mainbackend.domain.exercise.Exercise;
import com.levelupfit.mainbackend.domain.user.User;
import com.levelupfit.mainbackend.dto.feedback.MovementSpeedInfo;
import com.levelupfit.mainbackend.util.MovementSpeedConverter;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "exercise_feedbacks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseFeedbacks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private int feedbackId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Column(name="level")
    private int level;

    @Column(name = "video_url", nullable = false, columnDefinition = "TEXT")
    private String videoUrl;

    @Column(name = "feedback_text", nullable = false, columnDefinition = "TEXT")
    private String feedbackText;

    @Column(name = "accuracy")
    private Float accuracy;

    @Column(name="movement_range")
    private Float movementRange;

    @Convert(converter = MovementSpeedConverter.class)
    @Column(name="movement_speed", columnDefinition = "TEXT")
    private MovementSpeedInfo movementSpeed;

    @Column(name = "performed_date", nullable = false)
    private LocalDate performedDate;

}

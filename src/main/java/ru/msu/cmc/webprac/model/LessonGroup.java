package ru.msu.cmc.webprac.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lesson_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_group_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private StudyGroup group;
}
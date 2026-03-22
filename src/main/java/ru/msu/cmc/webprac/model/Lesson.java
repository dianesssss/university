package ru.msu.cmc.webprac.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(optional = false)
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    @ManyToOne(optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Column(name = "lesson_date", nullable = false)
    private LocalDate lessonDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
}
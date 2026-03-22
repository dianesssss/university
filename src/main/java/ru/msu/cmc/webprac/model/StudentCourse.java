package ru.msu.cmc.webprac.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_course_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "enrolled_at", nullable = false)
    private LocalDateTime enrolledAt;
}
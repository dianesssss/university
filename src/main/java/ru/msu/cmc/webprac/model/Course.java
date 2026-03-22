package ru.msu.cmc.webprac.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_type", nullable = false, length = 20)
    private CourseType courseType;

    @Column(name = "max_students")
    private Integer maxStudents;

    @Column(name = "free_places")
    private Integer freePlaces;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}
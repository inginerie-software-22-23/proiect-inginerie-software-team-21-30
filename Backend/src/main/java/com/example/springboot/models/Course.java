package com.example.springboot.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String shortDescription;

    private String longDescription;

    private String meetLink;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "course-user")
    private User user;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    @JsonBackReference(value = "course-subscriptions")
    private Set<Subscription> subscriptions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Course course = (Course) o;
        return id != null && Objects.equals(id, course.id)
                && subscriptions != null && subscriptions.equals(course.subscriptions);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

package com.example.springboot.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "user-subscriptions")
    private User user;


    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonBackReference(value = "course-subscriptions")
    private Course course;
    private String joinDate;

    public Subscription(User user, Course course) {
        this.user = user;
        this.course = course;
        this.joinDate = LocalDateTime.now().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Subscription that = (Subscription) o;
        return user != null && Objects.equals(user, that.user)
                && course != null && Objects.equals(course, that.course);
    }

    @Override
    public int hashCode() {
        int result = 17;

        if (user != null) {
            result = 31 * result + user.hashCode();
        }

        if (course != null) {
            result = 31 * result + course.hashCode();
        }

        return result;
    }
}

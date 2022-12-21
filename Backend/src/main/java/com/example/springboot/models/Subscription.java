package com.example.springboot.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
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
    private User trainee;

    @ManyToOne
    private Course course;

    private String joinDate;

    public Subscription(User trainee, Course course) {
        this.trainee = trainee;
        this.course = course;
        this.joinDate = LocalDate.now().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Subscription that = (Subscription) o;
        return trainee != null && Objects.equals(trainee, that.getTrainee())
                && course != null && Objects.equals(course, that.getCourse());
    }

    @Override
    public int hashCode() {
        int result = 17;

        if (trainee != null) {
            result = 31 * result + trainee.hashCode();
        }

        if (course != null) {
            result = 31 * result + course.hashCode();
        }

        return result;
    }
}

package com.example.springboot.models;

import lombok.Data;

import javax.persistence.*;

@Data
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
    private User user;
}

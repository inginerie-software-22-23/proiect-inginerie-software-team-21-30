package com.example.springboot.repositories;

import com.example.springboot.models.Subscription;
import com.example.springboot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionsRepositiory extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByTrainee(User trainee);
}

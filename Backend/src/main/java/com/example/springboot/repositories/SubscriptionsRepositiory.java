package com.example.springboot.repositories;

import com.example.springboot.models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionsRepositiory extends JpaRepository<Subscription, Long> {
}

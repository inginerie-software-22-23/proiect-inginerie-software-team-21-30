package com.example.springboot.DTOs;

import com.example.springboot.models.Subscription;
import com.example.springboot.models.User;
import lombok.Data;

@Data
public class SubscriptionWithUserDTO {
    private Long id;
    private User user;
    private String joinDate;

    public SubscriptionWithUserDTO(Subscription subscription) {
        this.id = subscription.getId();
        this.user = subscription.getUser();
        this.joinDate = subscription.getJoinDate();
    }
}

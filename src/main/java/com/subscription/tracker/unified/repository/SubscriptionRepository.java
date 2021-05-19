package com.subscription.tracker.unified.repository;

import com.subscription.tracker.unified.model.Subscription;
import com.subscription.tracker.unified.model.UserSubscription;

import java.util.List;

public interface SubscriptionRepository {

    List<Subscription> getSubscriptionsForClub(String clubId);

    Integer isInitiatorAllowedForAddSubscription(String initiatorId, String subscriptionId);

    Integer addSubscriptionToUser(String userId, String subscriptionId, int term);

    List<UserSubscription> getSubscriptionsForUser(String userId);

    Integer useUserSubscription(String userId, String subscriptionId);
}

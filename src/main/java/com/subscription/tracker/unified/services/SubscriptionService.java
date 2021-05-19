package com.subscription.tracker.unified.services;

import com.subscription.tracker.unified.model.Subscription;
import com.subscription.tracker.unified.model.UserSubscription;

import java.util.List;

public interface SubscriptionService {

    List<Subscription> getSubscriptionsForClub(String clubId);

    boolean isInitiatorAllowedForAddSubscription(String initiatorId, String subscriptionId);

    boolean addSubscriptionToUser(String userId, String subscriptionId, int term);

    UserSubscription getSubscriptionForUser(String userId);

    boolean isInitiatorAllowedForUseSubscription(String initiatorId, String subscriptionId);

    boolean useUserSubscription(String userId, String subscriptionId);
}

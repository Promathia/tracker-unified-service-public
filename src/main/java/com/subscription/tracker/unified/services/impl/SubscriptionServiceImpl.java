package com.subscription.tracker.unified.services.impl;

import com.subscription.tracker.unified.model.Subscription;
import com.subscription.tracker.unified.model.UserSubscription;
import com.subscription.tracker.unified.repository.SubscriptionRepository;
import com.subscription.tracker.unified.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public List<Subscription> getSubscriptionsForClub(String clubId) {
        return subscriptionRepository.getSubscriptionsForClub(clubId);
    }

    @Override
    public boolean isInitiatorAllowedForAddSubscription(String initiatorId, String subscriptionId) {
        return subscriptionRepository.isInitiatorAllowedForAddSubscription(initiatorId, subscriptionId) > 0;
    }

    @Override
    public boolean isInitiatorAllowedForUseSubscription(String initiatorId, String subscriptionId) {
        return subscriptionRepository.isInitiatorAllowedForAddSubscription(initiatorId, subscriptionId) > 0;
    }

    @Override
    public boolean addSubscriptionToUser(String userId, String subscriptionId, int term) {
        return subscriptionRepository.addSubscriptionToUser(userId, subscriptionId, term) > 0;
    }

    @Override
    public UserSubscription getSubscriptionForUser(String userId) {
        final List<UserSubscription> userSubscriptions = subscriptionRepository.getSubscriptionsForUser(userId);
        if (userSubscriptions == null || userSubscriptions.isEmpty()) {
            return null;
        }
        userSubscriptions.sort(Comparator.comparing(UserSubscription::getBuyDate));
        return userSubscriptions.get(0);
    }

    @Override
    public boolean useUserSubscription(String userId, String subscriptionId) {
        return subscriptionRepository.useUserSubscription(userId, subscriptionId) > 0;
    }
}

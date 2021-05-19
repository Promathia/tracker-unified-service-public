package com.subscription.tracker.unified.controller;

import com.subscription.tracker.unified.model.Subscription;
import com.subscription.tracker.unified.model.UserSubscription;
import com.subscription.tracker.unified.model.request.AddSubscriptionToUserRequest;
import com.subscription.tracker.unified.model.request.InitiatorRequest;
import com.subscription.tracker.unified.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SubscriptionController {

    private SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/club/{clubId}/subscriptions")
    public ResponseEntity<List<Subscription>> getSubscriptionsForClub(@PathVariable String clubId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsForClub(clubId));
    }

    @GetMapping("/user/{userId}/subscription")
    public ResponseEntity<UserSubscription> getSubscriptionForUser(@PathVariable String userId) {
        final UserSubscription subscriptionForUser = subscriptionService.getSubscriptionForUser(userId);
        if (subscriptionForUser == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(subscriptionForUser);
    }

    @PostMapping("/user/{userId}/subscription/{subscriptionId}/add")
    public ResponseEntity<Map<String, String>> addSubscriptionToUser(@Valid @RequestBody AddSubscriptionToUserRequest body,
                                                                     @PathVariable String userId,
                                                                     @PathVariable String subscriptionId) {
        if (!subscriptionService.isInitiatorAllowedForAddSubscription(body.getInitiatorId(), subscriptionId)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        if (subscriptionService.addSubscriptionToUser(userId, subscriptionId, body.getTerm())) {
            Map<String, String> result = new HashMap<>();
            result.put("status", "success");
            return ResponseEntity.ok(result);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/user/{userId}/subscription/{subscriptionId}/use")
    public ResponseEntity<Map<String, String>> useUserSubscription(@Valid @RequestBody InitiatorRequest body,
                                                                   @PathVariable String userId,
                                                                   @PathVariable String subscriptionId) {
        if (!subscriptionService.isInitiatorAllowedForUseSubscription(body.getInitiatorId(), subscriptionId)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        if (subscriptionService.useUserSubscription(userId, subscriptionId)) {
            Map<String, String> result = new HashMap<>();
            result.put("status", "success");
            return ResponseEntity.ok(result);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}

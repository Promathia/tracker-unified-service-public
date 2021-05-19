package com.subscription.tracker.unified.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Subscription {

    @JsonProperty("id")
    private Integer subscriptionId;
    private Integer clubId;
    private Integer visitsLimit;
    private Integer termDays;

    public Integer getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Integer subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Integer getClubId() {
        return clubId;
    }

    public void setClubId(Integer clubId) {
        this.clubId = clubId;
    }

    public Integer getVisitsLimit() {
        return visitsLimit;
    }

    public void setVisitsLimit(Integer visitsLimit) {
        this.visitsLimit = visitsLimit;
    }

    public Integer getTermDays() {
        return termDays;
    }

    public void setTermDays(Integer termDays) {
        this.termDays = termDays;
    }
}

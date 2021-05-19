package com.subscription.tracker.unified.model.request;

import javax.validation.constraints.NotBlank;

public class AddSubscriptionToUserRequest extends InitiatorRequest {

    @NotBlank
    private Integer term;

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

}

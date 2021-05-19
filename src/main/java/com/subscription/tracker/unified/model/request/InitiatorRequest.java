package com.subscription.tracker.unified.model.request;

import javax.validation.constraints.NotBlank;

public class InitiatorRequest {

    @NotBlank
    private String initiatorId;

    public String getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(String initiatorId) {
        this.initiatorId = initiatorId;
    }

}

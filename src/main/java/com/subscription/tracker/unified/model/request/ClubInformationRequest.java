package com.subscription.tracker.unified.model.request;

import javax.validation.constraints.NotBlank;

public class ClubInformationRequest {

    @NotBlank
    private String information;

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}

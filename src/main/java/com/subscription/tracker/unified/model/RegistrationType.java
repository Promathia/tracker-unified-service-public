package com.subscription.tracker.unified.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationType {

    private Integer id;
    private String registrationTypeName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegistrationTypeName() {
        return registrationTypeName;
    }

    public void setRegistrationTypeName(String registrationTypeName) {
        this.registrationTypeName = registrationTypeName;
    }
}

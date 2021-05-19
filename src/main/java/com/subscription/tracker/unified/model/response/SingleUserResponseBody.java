package com.subscription.tracker.unified.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.subscription.tracker.unified.model.RegistrationType;
import com.subscription.tracker.unified.model.User;
import com.subscription.tracker.unified.model.UserClub;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SingleUserResponseBody {

    private User sourceUser;
    private Integer id;
    private String externalId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Date birthDate;
    private RegistrationType registrationType;
    private Boolean isFilled;
    private List<UserClub> userClubs;

    public SingleUserResponseBody(User sourceUser) {
        this.sourceUser = sourceUser;
    }

    public Integer getId() {
        return sourceUser.getUid();
    }

    public String getExternalId() {
        return sourceUser.getExternalId();
    }

    public String getFirstName() {
        return sourceUser.getFirstName();
    }

    public String getLastName() {
        return sourceUser.getLastName();
    }

    public String getPhone() {
        return sourceUser.getPhone();
    }

    public String getEmail() {
        return sourceUser.getEmail();
    }

    public Date getBirthDate() {
        return sourceUser.getBirthDate();
    }

    public RegistrationType getRegistrationType() {
        return sourceUser.getRegistrationType();
    }

    public Boolean isFilled() {
        return isFilled;
    }

    public void setFilled(Boolean filled) {
        isFilled = filled;
    }

    public List<UserClub> getUserClubs() {
        return userClubs;
    }

    public void setUserClubs(List<UserClub> userClubs) {
        this.userClubs = userClubs;
    }

}

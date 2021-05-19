package com.subscription.tracker.unified.model.request;

import com.subscription.tracker.unified.model.RegistrationType;

import javax.validation.constraints.NotBlank;
import java.util.Date;

public class UserCreateBody {

    @NotBlank
    private String externalId;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String phone;
    private String email;
    @NotBlank
    private Date birthDate;
    @NotBlank
    private RegistrationType registrationType;
    @NotBlank
    private int clubId;

    public String getExternalId() {
        return externalId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public RegistrationType getRegistrationType() {
        return registrationType;
    }

    public int getClubId() {
        return clubId;
    }
}

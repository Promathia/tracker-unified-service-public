package com.subscription.tracker.unified.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.subscription.tracker.unified.model.Role;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserForClubResponseBody {

    private Integer id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Date birthDate;
    private Role role;
    private Boolean userAccepted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getUserAccepted() {
        return userAccepted;
    }

    public void setUserAccepted(Boolean userAccepted) {
        this.userAccepted = userAccepted;
    }
}

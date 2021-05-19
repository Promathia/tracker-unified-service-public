package com.subscription.tracker.unified.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserClub {

    private Club club;
    private Role role;
    private Boolean userAccepted;
    private Boolean activeClub;

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Boolean isUserAccepted() {
        return userAccepted;
    }

    public void setUserAccepted(Boolean userAccepted) {
        this.userAccepted = userAccepted;
    }

    public Boolean isActiveClub() {
        return activeClub;
    }

    public void setActiveClub(Boolean activeClub) {
        this.activeClub = activeClub;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

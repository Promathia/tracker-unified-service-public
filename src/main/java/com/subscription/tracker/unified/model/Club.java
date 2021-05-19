package com.subscription.tracker.unified.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Club {

    @JsonProperty("id")
    private Integer clubId;
    private String clubName;
    private String clubNameAlt;
    private String imageName;

    public Integer getClubId() {
        return clubId;
    }

    public void setClubId(Integer clubId) {
        this.clubId = clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubNameAlt() {
        return clubNameAlt;
    }

    public void setClubNameAlt(String clubNameAlt) {
        this.clubNameAlt = clubNameAlt;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}

package com.subscription.tracker.unified.model.response;

import org.springframework.web.util.HtmlUtils;

public class ClubInformationResponse {

    private Integer id;
    private String information;

    public String getInformation() {
        return HtmlUtils.htmlUnescape(information);
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

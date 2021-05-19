package com.subscription.tracker.unified.model.response;

import org.springframework.web.util.HtmlUtils;

public class ClubTimetableResponse {

    private Integer id;
    private String timetable;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTimetable() {
        return HtmlUtils.htmlUnescape(timetable);
    }

    public void setTimetable(String timetable) {
        this.timetable = timetable;
    }
}

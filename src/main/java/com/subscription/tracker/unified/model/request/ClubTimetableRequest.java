package com.subscription.tracker.unified.model.request;

import javax.validation.constraints.NotBlank;

public class ClubTimetableRequest {

    @NotBlank
    private String timetable;

    public String getTimetable() {
        return timetable;
    }

    public void setTimetable(String timetable) {
        this.timetable = timetable;
    }
}

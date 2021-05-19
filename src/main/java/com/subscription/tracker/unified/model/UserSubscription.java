package com.subscription.tracker.unified.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSubscription implements Serializable {

    private Integer id;
    private Integer visitsLimit;
    private Integer visitCounter;
    private Date deadline;
    @JsonIgnore
    private Date buyDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVisitsLimit() {
        return visitsLimit;
    }

    public void setVisitsLimit(Integer visitsLimit) {
        this.visitsLimit = visitsLimit;
    }

    public Integer getVisitCounter() {
        return visitCounter;
    }

    public void setVisitCounter(Integer visitCounter) {
        this.visitCounter = visitCounter;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }
}

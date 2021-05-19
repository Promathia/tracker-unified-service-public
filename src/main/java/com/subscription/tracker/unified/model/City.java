package com.subscription.tracker.unified.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class City {

    private Integer id;
    private String cityNameEn;
    private String cityNameRu;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCityNameEn() {
        return cityNameEn;
    }

    public void setCityNameEn(String cityNameEn) {
        this.cityNameEn = cityNameEn;
    }

    public String getCityNameRu() {
        return cityNameRu;
    }

    public void setCityNameRu(String cityNameRu) {
        this.cityNameRu = cityNameRu;
    }
}

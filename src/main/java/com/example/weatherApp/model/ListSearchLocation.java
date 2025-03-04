package com.example.weatherApp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationsInfo {
    private List<SearchLocationInfo> list;

    public List<SearchLocationInfo> getList() {
        return list;
    }

    public void setList(List<SearchLocationInfo> list) {
        this.list = list;
    }
}
package com.example.weatherApp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {
    private List<LocationInfo> list;

    public List<LocationInfo> getList() {
        return list;
    }

    public void setList(List<LocationInfo> list) {
        this.list = list;
    }
}
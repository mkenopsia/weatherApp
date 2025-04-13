package com.example.weatherApp.service.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListSearchLocation {
    private List<SearchLocation> list;

    public List<SearchLocation> getList() {
        return list;
    }

    public void setList(List<SearchLocation> list) {
        this.list = list;
    }
}
package com.example.weatherApp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SavedLocationsInfo {
    private List<WeatherInfo> weather;
    private MainInfo main;
    private WindInfo wind;
    private Sys sys;
    private String 

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeatherInfo {
        String main;
        String description;
        // "main": "Clouds",
        // "description": "broken clouds",

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MainInfo {
        private Double temp;
        private Double feels_like;
        private Integer humidity;
        private Double sea_level;
        /*
        "temp": -10.14,
        "feels_like": -10.14,
        "humidity": 97,
        "sea_level": 1033,
         */

        public Double getTemp() {
            return temp;
        }

        public void setTemp(Double temp) {
            this.temp = temp;
        }

        public Double getFeels_like() {
            return feels_like;
        }

        public void setFeels_like(Double feels_like) {
            this.feels_like = feels_like;
        }

        public Integer getHumidity() {
            return humidity;
        }

        public void setHumidity(Integer humidity) {
            this.humidity = humidity;
        }

        public Double getSea_level() {
            return sea_level;
        }

        public void setSea_level(Double sea_level) {
            this.sea_level = sea_level;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WindInfo {
        Double speed;

        public Double getSpeed() {
            return speed;
        }

        public void setSpeed(Double speed) {
            this.speed = speed;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sys {
        String country;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

}

package SchedulingApp.Models;

import SchedulingApp.DataBase.DataBaseManager;

import java.sql.Date;
import java.sql.Timestamp;

public class City {
    int cityId;
    int countryId;
    String city;


    public City() {
    }
    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}

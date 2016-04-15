package com.example.quan_bui.countryinfo;

/**
 * Created by Quan Bui on 4/14/16.
 */
public class Country {
    private String name;
    private String capital;
    private String region;
    private String countryCode;

    public Country(String name, String capital, String region) {
        this.name = name;
        this.capital = capital;
        this.region = region;
    }

    public Country() {

    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}

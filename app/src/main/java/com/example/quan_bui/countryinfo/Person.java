package com.example.quan_bui.countryinfo;

/**
 * Created by Quan Bui on 4/15/16.
 */
public class Person {

    private String name;
    private int age;
    private String countryCode;
    private String countryName;

    public Person() {
    }

    public Person(String name, int age, String countryCode) {
        this.name = name;
        this.age = age;
        this.countryCode = countryCode;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void combine(Country country) {
        if (getCountryCode().equalsIgnoreCase(country.getCountryCode())) {
            setCountryName(country.getName());
        }
    }
}

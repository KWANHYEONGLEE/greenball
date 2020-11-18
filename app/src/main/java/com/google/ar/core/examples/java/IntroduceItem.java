package com.google.ar.core.examples.java;

import java.io.Serializable;

public class IntroduceItem implements Serializable {

    //이름
    private String name;
    //설명
    private String description;
    //위치
    private String location;
    //주소
    private String address;
    //전화번호
    private String number;
    //이미지
    private int image;

    public IntroduceItem(String name, String description, String location, String address, String number, int image) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.address = address;
        this.number = number;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}

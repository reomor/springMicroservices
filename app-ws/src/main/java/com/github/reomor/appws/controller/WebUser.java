package com.github.reomor.appws.controller;

public class WebUser {
    private Integer userId;
    private String FirstName;
    private String LastName;
    private String email;

    public WebUser(Integer userId, String firstName, String lastName, String email) {
        this.userId = userId;
        FirstName = firstName;
        LastName = lastName;
        this.email = email;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

package com.github.reomor.appws.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateUserDetailsRequestModel {
    @NotNull(message = "First name cannot be null")
    @Size(min = 3)
    private String firstName;
    @Size(min = 3)
    @NotNull(message = "Last name cannot be null")
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

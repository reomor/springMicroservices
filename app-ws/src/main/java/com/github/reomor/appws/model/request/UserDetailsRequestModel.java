package com.github.reomor.appws.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDetailsRequestModel {
    @NotNull(message = "First name cannot be null")
    @Size(min = 3)
    private String firstName;
    @Size(min = 3)
    @NotNull(message = "Last name cannot be null")
    private String lastName;
    @NotNull(message = "Email cannot be null")
    @Size(min = 3, max = 16, message = "Size of email must be between 3 and 16")
    @Email(message = "Email must be valid")
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

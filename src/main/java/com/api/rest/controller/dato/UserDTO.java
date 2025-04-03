package com.api.rest.controller.dato;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Set;

@Value
public class UserDTO {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Set<String> roles;

    public UserDTO() {
    }

    public UserDTO(String username, String email, String firstName, String lastName, String password, Set<String> roles) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getRoles() {
        return roles;
    }
}

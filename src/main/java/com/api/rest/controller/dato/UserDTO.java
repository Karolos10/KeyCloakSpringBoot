package com.api.rest.controller.dato;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Set;

@Builder
public record UserDTO(String username, String email, String firstName, String lastName, String password,
                      Set<String> roles) {

}

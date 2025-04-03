package com.api.rest.service;

import com.api.rest.controller.dato.UserDTO;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IKeycloakService {

    List<UserRepresentation> findAllUsers();
    List<UserRepresentation> searchUserByUsername(String username);
    String createUser(UserDTO userDTO);
    void deleteUser(String userId);
    void updateUser(String userId, UserDTO userDTO);
}

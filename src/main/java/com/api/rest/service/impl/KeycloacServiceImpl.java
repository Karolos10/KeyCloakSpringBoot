package com.api.rest.service.impl;

import com.api.rest.controller.dato.UserDTO;
import com.api.rest.service.IKeycloakService;
import com.api.rest.util.KeycloackProvider;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.lang.NonNull;

import java.util.Collections;
import java.util.List;

public class KeycloacServiceImpl implements IKeycloakService {

    /**
     * Metodo para listar todos los ususarios de Keycloack
     *
     * @return List<UserRepresentation>
     */
    @Override
    public List<UserRepresentation> findAllUsers() {
        return KeycloackProvider.getRealmResource()
                .users()
                .list();
    }

    /**
     * Metodo para buscar un usuario por su username
     *
     * @return List<UserRepresentation>
     */
    @Override
    public List<UserRepresentation> searchUserByUsername(String username) {
        return KeycloackProvider.getRealmResource()
                .users()
                .searchByUsername(username, true);
    }

    /**
     * Metodo para crear un usuario en Keycloack
     * @return String
     */
    @Override
    public String createUser(@NonNull UserDTO userDTO) {

        int status = 0;
        UsersResource userResource = KeycloackProvider.getUserResource();
        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setFirstName(userDTO.getFirstName());
        userRepresentation.setLastName(userDTO.getLastName());
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.setUsername(userDTO.getUsername());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);

        Response response = userResource.create(userRepresentation);
        status = response.getStatus();

        if(status == 201){
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/") + 1);

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(OAuth2Constants.PASSWORD);
            credentialRepresentation.setValue(userDTO.getPassword());

            userResource.get(userId)
                    .resetPassword(credentialRepresentation);

            RealmResource realmResource = KeycloackProvider.getRealmResource();

            List<RoleRepresentation> roleRepresentations = null;

            if(userDTO.getRoles() == null || userDTO.getRoles().isEmpty()){
                roleRepresentations = List.of(realmResource.roles().get("user").toRepresentation());
            }else{
                roleRepresentations = realmResource.roles()
                        .list()
                        .stream()
                        .filter(role -> userDTO.getRoles()
                                .stream()
                                .anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))
                        .toList();
            }

            realmResource.users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .add(roleRepresentations);

            return "User created successfully";
        }else if (status == 409) {
            /*log.error("User already exists");*/
            return "User already exists";
        }else {
            /*log.error("Error creating user");*/
            return "Error creating user";
        }

    }

    /*     * Metodo para eliminar un usuario por su id
     * @return void
     */
    @Override
    public void deleteUser(String userId) {
        KeycloackProvider.getUserResource()
                .get(userId)
                .remove();

    }

    /*     * Metodo para actualizar un usuario por su id
     * @return void
     */
    @Override
    public void updateUser(String userId, @NonNull UserDTO userDTO) {

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();

        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue(userDTO.getPassword());

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userDTO.getFirstName());
        userRepresentation.setLastName(userDTO.getLastName());
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.setUsername(userDTO.getUsername());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

        UserResource userResource = KeycloackProvider.getUserResource().get(userId);
        userResource.update(userRepresentation);
    }
}

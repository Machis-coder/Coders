package com.codingtrainers.duocoding.controllers;

import com.codingtrainers.duocoding.dto.input.LoginRequestDTO;
import com.codingtrainers.duocoding.dto.output.UserResponseDTO;
import com.codingtrainers.duocoding.entities.User;
import com.codingtrainers.duocoding.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private LoginController loginController;

    private LoginRequestDTO loginRequestDTO;
    private User user;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        loginRequestDTO = new LoginRequestDTO("testuser", "testpassword");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        userResponseDTO = new UserResponseDTO(user);
    }

    @Test
    void doLogin_Success_ShouldReturnOkAndUserDTO() throws Exception {
        when(userService.findUserByUsernameAndPassword("testuser", "testpassword")).thenReturn(user);

        ResponseEntity<UserResponseDTO> response = loginController.doLogin(loginRequestDTO);

        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El estado HTTP debería ser OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(userResponseDTO.getId(), response.getBody().getId(), "El ID del usuario debería coincidir");
        assertEquals(userResponseDTO.getUsername(), response.getBody().getUsername(), "El username debería coincidir");
        assertEquals(userResponseDTO.getEmail(), response.getBody().getEmail(), "El email debería coincidir");
    }


    @Test
    void doLogin_UserNotFound_ShouldReturnNotFound() throws Exception {
        when(userService.findUserByUsernameAndPassword("testuser", "testpassword"))
                .thenThrow(new RuntimeException("User not found"));

        ResponseEntity<UserResponseDTO> response = loginController.doLogin(loginRequestDTO);

        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "El estado HTTP debería ser NOT_FOUND");
        assertEquals(null, response.getBody(), "El cuerpo de la respuesta debería ser nulo para un 404");
    }


    @Test
    void doLogin_NullInputCausesServiceError_ShouldReturnNotFound() throws Exception {
        LoginRequestDTO nullUsernameLogin = new LoginRequestDTO(null, "password");

        when(userService.findUserByUsernameAndPassword(null, "password"))
                .thenThrow(new IllegalArgumentException("Username cannot be null"));

        ResponseEntity<UserResponseDTO> response = loginController.doLogin(nullUsernameLogin);

        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "El estado HTTP debería ser NOT_FOUND");
        assertEquals(null, response.getBody(), "El cuerpo de la respuesta debería ser nulo para un 404");
    }
}
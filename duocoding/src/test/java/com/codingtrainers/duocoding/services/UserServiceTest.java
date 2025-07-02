package com.codingtrainers.duocoding.services;

import com.codingtrainers.duocoding.dto.output.UserResponseDTO;
import com.codingtrainers.duocoding.entities.Role;
import com.codingtrainers.duocoding.entities.User;
import com.codingtrainers.duocoding.repositories.UserRepository;
import com.codingtrainers.duocoding.utils.HashUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getAll_ReturnsUsers() {
        User user = new User(1L, "John", "john@example.com", "johndoe", "hashedPassword", "Doe", LocalDate.of(1990, 1, 1), "12345678A", Role.PUPIL, true);
        when(userRepository.findAllByActiveTrueExceptSuper()).thenReturn(List.of(user));

        List<UserResponseDTO> result = userService.getAll();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
        assertEquals("Doe", result.get(0).getSurname());
        assertEquals("johndoe", result.get(0).getUsername());
        assertEquals("john@example.com", result.get(0).getEmail());
        assertEquals("12345678A", result.get(0).getDni());
        assertEquals(Role.PUPIL, result.get(0).getRole());
        assertTrue(result.get(0).isActive());
        verify(userRepository, times(1)).findAllByActiveTrueExceptSuper();
    }

    @Test
    void getAll_ReturnsEmpty() {
        when(userRepository.findAllByActiveTrueExceptSuper()).thenReturn(Collections.emptyList());

        List<UserResponseDTO> result = userService.getAll();

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAllByActiveTrueExceptSuper();
    }

    @Test
    void getById_Found() {
        User user = new User(1L, "John", "john@example.com", "johndoe", "hashedPassword", "Doe", LocalDate.of(1990, 1, 1), "12345678A", Role.PUPIL, true);
        when(userRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.getById(1L);

        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("Doe", result.getSurname());
        assertEquals("johndoe", result.getUsername());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("12345678A", result.getDni());
        assertEquals(Role.PUPIL, result.getRole());
        assertTrue(result.isActive());
        verify(userRepository, times(1)).findByIdAndActiveTrue(1L);
    }

    @Test
    void getById_NotFound_ThrowsException() {
        when(userRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getById(1L));

        assertEquals("User Not Found", ex.getMessage());
        verify(userRepository, times(1)).findByIdAndActiveTrue(1L);
    }

    @Test
    void update_Success() {
        User user = new User(1L, "John", "john@example.com", "johndoe", "hashedPassword", "Doe", LocalDate.of(1990, 1, 1), "12345678A", Role.PUPIL, false);
        User updatedUser = new User(1L, "Jane", "jane@example.com", "janedoe", "newHashedPassword", "Smith", LocalDate.of(1990, 1, 1), "12345678A", Role.PUPIL, true);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        userService.update(user);

        assertTrue(user.isActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void findByUsername_Found() {
        User user = new User(1L, "John", "john@example.com", "johndoe", "hashedPassword", "Doe", LocalDate.of(1990, 1, 1), "12345678A", Role.PUPIL, true);
        when(userRepository.findByUsernameAndActiveTrue("johndoe")).thenReturn(Optional.of(user));

        Optional<UserResponseDTO> result = userService.findByUsername("johndoe");

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getName());
        assertEquals("Doe", result.get().getSurname());
        assertEquals("johndoe", result.get().getUsername());
        assertEquals("john@example.com", result.get().getEmail());
        assertEquals("12345678A", result.get().getDni());
        assertEquals(Role.PUPIL, result.get().getRole());
        assertTrue(result.get().isActive(), "UserResponseDTO active field should be true");
        verify(userRepository, times(1)).findByUsernameAndActiveTrue("johndoe");
    }

    @Test
    void findByUsername_NotFound() {
        when(userRepository.findByUsernameAndActiveTrue("johndoe")).thenReturn(Optional.empty());

        Optional<UserResponseDTO> result = userService.findByUsername("johndoe");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUsernameAndActiveTrue("johndoe");
    }

    @Test
    void create_Success() {
        User user = new User(null, "John", "john@example.com", "johndoe", "password", "Doe", LocalDate.of(1990, 1, 1), "12345678A", Role.PUPIL, null);
        String hashedPassword = "hashedPassword";
        try (MockedStatic<HashUtils> mockedStatic = Mockito.mockStatic(HashUtils.class)) {
            mockedStatic.when(() -> HashUtils.sha256("password")).thenReturn(hashedPassword);
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User savedUser = invocation.getArgument(0);
                savedUser.setId(1L);
                return savedUser;
            });

            userService.create(user);

            assertTrue(user.isActive());
            assertEquals(hashedPassword, user.getPassword());
            verify(userRepository, times(1)).save(user);
            mockedStatic.verify(() -> HashUtils.sha256("password"), times(1));
        }
    }

    @Test
    void deleteUser_Success() {
        User user = new User(1L, "John", "john@example.com", "johndoe", "hashedPassword", "Doe", LocalDate.of(1990, 1, 1), "12345678A", Role.PUPIL, true);
        when(userRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.deleteUser(1L);

        assertFalse(user.isActive());
        verify(userRepository, times(1)).findByIdAndActiveTrue(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deleteUser_NotFound_ThrowsException() {
        when(userRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));

        assertEquals("User Not Found", ex.getMessage());
        verify(userRepository, times(1)).findByIdAndActiveTrue(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void activateUser_Success() {
        User user = new User(1L, "John", "john@example.com", "johndoe", "hashedPassword", "Doe", LocalDate.of(1990, 1, 1), "12345678A", Role.PUPIL, false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.activateUser(1L);

        assertTrue(user.isActive());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void activateUser_NotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.activateUser(1L));

        assertEquals("User Not Found", ex.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getInactiveUsers_ReturnsUsers() {
        User user = new User(1L, "John", "john@example.com", "johndoe", "hashedPassword", "Doe", LocalDate.of(1990, 1, 1), "12345678A", Role.PUPIL, false);
        when(userRepository.findAllByActiveFalse()).thenReturn(List.of(user));

        List<UserResponseDTO> result = userService.getInactiveUsers();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
        assertEquals("Doe", result.get(0).getSurname());
        assertEquals("johndoe", result.get(0).getUsername());
        assertEquals("john@example.com", result.get(0).getEmail());
        assertEquals("12345678A", result.get(0).getDni());
        assertEquals(Role.PUPIL, result.get(0).getRole());
        verify(userRepository, times(1)).findAllByActiveFalse();
    }

    @Test
    void getInactiveUsers_ReturnsEmpty() {
        when(userRepository.findAllByActiveFalse()).thenReturn(Collections.emptyList());

        List<UserResponseDTO> result = userService.getInactiveUsers();

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAllByActiveFalse();
    }

    @Test
    void findUserByUsernameAndPassword_Success() throws Exception {
        User user = new User(1L, "John", "john@example.com", "johndoe", "hashedPassword", "Doe",
                LocalDate.of(1990, 1, 1), "12345678A", Role.PUPIL, true);

        try (MockedStatic<HashUtils> mockedStatic = Mockito.mockStatic(HashUtils.class)) {
            mockedStatic.when(() -> HashUtils.sha256("password")).thenReturn("hashedPassword");

            when(userRepository.userLogin("johndoe", "hashedPassword")).thenReturn(List.of(user));

            User result = userService.findUserByUsernameAndPassword("johndoe", "password");

            assertNotNull(result);
            assertEquals("johndoe", result.getUsername());
            assertEquals("hashedPassword", result.getPassword());

            verify(userRepository, times(1)).userLogin("johndoe", "hashedPassword");
            mockedStatic.verify(() -> HashUtils.sha256("password"), times(1));
        }
    }


    @Test
    void findUserByUsernameAndPassword_NotFound_ThrowsException() {
        try (MockedStatic<HashUtils> mockedStatic = Mockito.mockStatic(HashUtils.class)) {
            mockedStatic.when(() -> HashUtils.sha256("password")).thenReturn("hashedPassword");
            when(userRepository.userLogin("johndoe", "hashedPassword")).thenReturn(Collections.emptyList());

            Exception ex = assertThrows(Exception.class, () ->
                    userService.findUserByUsernameAndPassword("johndoe", "password")
            );

            System.out.println("Mensaje real de la excepción: " + ex.getMessage());

            assertEquals("User not found by username and password", ex.getMessage());
            verify(userRepository, times(1)).userLogin("johndoe", "hashedPassword");
        }
    }

}
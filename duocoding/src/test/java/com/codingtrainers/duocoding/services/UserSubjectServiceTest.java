package com.codingtrainers.duocoding.services;

import com.codingtrainers.duocoding.dto.output.UserSubjectResponseDTO;
import com.codingtrainers.duocoding.entities.Subject;
import com.codingtrainers.duocoding.entities.User;
import com.codingtrainers.duocoding.entities.UserSubject;
import com.codingtrainers.duocoding.repositories.SubjectRepository;
import com.codingtrainers.duocoding.repositories.UserRepository;
import com.codingtrainers.duocoding.repositories.UserSubjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSubjectServiceTest {

    @Mock
    private UserSubjectRepository userSubjectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private UserSubjectService userSubjectService;

    @Test
    void getSubjectsByUser_ReturnsSubjects() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Subject subject = new Subject();
        subject.setId(2L);
        UserSubject userSubject = new UserSubject();
        userSubject.setId(1L);
        userSubject.setUser(user);
        userSubject.setSubject(subject);
        userSubject.setActive(true);
        when(userSubjectRepository.findByUserIdAndActiveTrue(userId))
                .thenReturn(List.of(userSubject));

        List<UserSubjectResponseDTO> result = userSubjectService.getSubjectsByUser(userId);

        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getUserId());
        assertEquals(2L, result.get(0).getSubjectId());
        verify(userSubjectRepository, times(1)).findByUserIdAndActiveTrue(userId);
    }

    @Test
    void getSubjectsByUser_ReturnsEmpty() {
        Long userId = 1L;
        when(userSubjectRepository.findByUserIdAndActiveTrue(userId))
                .thenReturn(Collections.emptyList());

        List<UserSubjectResponseDTO> result = userSubjectService.getSubjectsByUser(userId);

        assertTrue(result.isEmpty());
        verify(userSubjectRepository, times(1)).findByUserIdAndActiveTrue(userId);
    }

    @Test
    void getUsersBySubject_ReturnsUsers() {
        Long subjectId = 2L;
        User user = new User();
        user.setId(1L);
        Subject subject = new Subject();
        subject.setId(subjectId);
        UserSubject userSubject = new UserSubject();
        userSubject.setId(1L);
        userSubject.setUser(user);
        userSubject.setSubject(subject);
        userSubject.setActive(true);
        when(userSubjectRepository.findBySubjectIdAndActiveTrue(subjectId))
                .thenReturn(List.of(userSubject));

        List<UserSubjectResponseDTO> result = userSubjectService.getUsersBySubject(subjectId);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserId());
        assertEquals(subjectId, result.get(0).getSubjectId());
        verify(userSubjectRepository, times(1)).findBySubjectIdAndActiveTrue(subjectId);
    }

    @Test
    void getUsersBySubject_ReturnsEmpty() {
        Long subjectId = 2L;
        when(userSubjectRepository.findBySubjectIdAndActiveTrue(subjectId))
                .thenReturn(Collections.emptyList());

        List<UserSubjectResponseDTO> result = userSubjectService.getUsersBySubject(subjectId);

        assertTrue(result.isEmpty());
        verify(userSubjectRepository, times(1)).findBySubjectIdAndActiveTrue(subjectId);
    }

    @Test
    void assignUserToSubject_Success_NewRelation() {
        Long userId = 1L;
        Long subjectId = 2L;
        User user = new User();
        user.setId(userId);
        Subject subject = new Subject();
        subject.setId(subjectId);
        UserSubject saved = new UserSubject();
        saved.setId(1L);
        saved.setUser(user);
        saved.setSubject(subject);
        saved.setActive(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));
        when(userSubjectRepository.findByUserIdAndSubjectIdAndActiveTrue(userId, subjectId))
                .thenReturn(Optional.empty());
        when(userSubjectRepository.findByUserIdAndSubjectId(userId, subjectId))
                .thenReturn(Optional.empty());
        when(userSubjectRepository.save(any(UserSubject.class))).thenReturn(saved);

        UserSubject result = userSubjectService.assignUserToSubject(userId, subjectId);

        assertNotNull(result);
        assertEquals(userId, result.getUser().getId());
        assertEquals(subjectId, result.getSubject().getId());
        assertTrue(result.getActive());
        verify(userRepository, times(1)).findById(userId);
        verify(subjectRepository, times(1)).findById(subjectId);
        verify(userSubjectRepository, times(1)).findByUserIdAndSubjectIdAndActiveTrue(userId, subjectId);
        verify(userSubjectRepository, times(1)).findByUserIdAndSubjectId(userId, subjectId);
        verify(userSubjectRepository, times(1)).save(any(UserSubject.class));
    }

    @Test
    void assignUserToSubject_Success_ReactivateRelation() {
        Long userId = 1L;
        Long subjectId = 2L;
        User user = new User();
        user.setId(userId);
        Subject subject = new Subject();
        subject.setId(subjectId);
        UserSubject existing = new UserSubject();
        existing.setId(1L);
        existing.setUser(user);
        existing.setSubject(subject);
        existing.setActive(false);
        UserSubject saved = new UserSubject();
        saved.setId(1L);
        saved.setUser(user);
        saved.setSubject(subject);
        saved.setActive(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));
        when(userSubjectRepository.findByUserIdAndSubjectIdAndActiveTrue(userId, subjectId))
                .thenReturn(Optional.empty());
        when(userSubjectRepository.findByUserIdAndSubjectId(userId, subjectId))
                .thenReturn(Optional.of(existing));
        when(userSubjectRepository.save(existing)).thenReturn(saved);

        UserSubject result = userSubjectService.assignUserToSubject(userId, subjectId);

        assertNotNull(result);
        assertEquals(userId, result.getUser().getId());
        assertEquals(subjectId, result.getSubject().getId());
        assertTrue(result.getActive());
        verify(userRepository, times(1)).findById(userId);
        verify(subjectRepository, times(1)).findById(subjectId);
        verify(userSubjectRepository, times(1)).findByUserIdAndSubjectIdAndActiveTrue(userId, subjectId);
        verify(userSubjectRepository, times(1)).findByUserIdAndSubjectId(userId, subjectId);
        verify(userSubjectRepository, times(1)).save(existing);
    }

    @Test
    void assignUserToSubject_UserNotFound_ThrowsException() {
        Long userId = 1L;
        Long subjectId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userSubjectService.assignUserToSubject(userId, subjectId);
        });

        assertEquals("User not found with ID: " + userId, ex.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(subjectRepository, never()).findById(anyLong());
        verify(userSubjectRepository, never()).findByUserIdAndSubjectIdAndActiveTrue(anyLong(), anyLong());
        verify(userSubjectRepository, never()).findByUserIdAndSubjectId(anyLong(), anyLong());
        verify(userSubjectRepository, never()).save(any(UserSubject.class));
    }

    @Test
    void assignUserToSubject_SubjectNotFound_ThrowsException() {
        Long userId = 1L;
        Long subjectId = 2L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userSubjectService.assignUserToSubject(userId, subjectId);
        });

        assertEquals("Subject not found with ID: " + subjectId, ex.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(subjectRepository, times(1)).findById(subjectId);
        verify(userSubjectRepository, never()).findByUserIdAndSubjectIdAndActiveTrue(anyLong(), anyLong());
        verify(userSubjectRepository, never()).findByUserIdAndSubjectId(anyLong(), anyLong());
        verify(userSubjectRepository, never()).save(any(UserSubject.class));
    }

    @Test
    void assignUserToSubject_AlreadyAssigned_ThrowsException() {
        Long userId = 1L;
        Long subjectId = 2L;
        User user = new User();
        user.setId(userId);
        Subject subject = new Subject();
        subject.setId(subjectId);
        UserSubject existing = new UserSubject();
        existing.setId(1L);
        existing.setUser(user);
        existing.setSubject(subject);
        existing.setActive(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));
        when(userSubjectRepository.findByUserIdAndSubjectIdAndActiveTrue(userId, subjectId))
                .thenReturn(Optional.of(existing));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userSubjectService.assignUserToSubject(userId, subjectId);
        });

        assertEquals("User is already assigned to the subject.", ex.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(subjectRepository, times(1)).findById(subjectId);
        verify(userSubjectRepository, times(1)).findByUserIdAndSubjectIdAndActiveTrue(userId, subjectId);
        verify(userSubjectRepository, never()).findByUserIdAndSubjectId(anyLong(), anyLong());
        verify(userSubjectRepository, never()).save(any(UserSubject.class));
    }

    @Test
    void reactivateRelation_Success() {
        Long userId = 1L;
        Long subjectId = 2L;
        User user = new User();
        user.setId(userId);
        Subject subject = new Subject();
        subject.setId(subjectId);
        UserSubject relation = new UserSubject();
        relation.setId(1L);
        relation.setUser(user);
        relation.setSubject(subject);
        relation.setActive(false);
        UserSubject saved = new UserSubject();
        saved.setId(1L);
        saved.setUser(user);
        saved.setSubject(subject);
        saved.setActive(true);

        when(userSubjectRepository.findByUserIdAndSubjectId(userId, subjectId))
                .thenReturn(Optional.of(relation));
        when(userSubjectRepository.save(relation)).thenReturn(saved);

        UserSubject result = userSubjectService.reactivateRelation(userId, subjectId);

        assertNotNull(result);
        assertEquals(userId, result.getUser().getId());
        assertEquals(subjectId, result.getSubject().getId());
        assertTrue(result.getActive());
        verify(userSubjectRepository, times(1)).findByUserIdAndSubjectId(userId, subjectId);
        verify(userSubjectRepository, times(1)).save(relation);
    }

    @Test
    void reactivateRelation_NotFound_ThrowsException() {
        Long userId = 1L;
        Long subjectId = 2L;

        when(userSubjectRepository.findByUserIdAndSubjectId(userId, subjectId))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userSubjectService.reactivateRelation(userId, subjectId);
        });

        assertEquals("Relation not found.", ex.getMessage());
        verify(userSubjectRepository, times(1)).findByUserIdAndSubjectId(userId, subjectId);
        verify(userSubjectRepository, never()).save(any(UserSubject.class));
    }

    @Test
    void deleteRelation_Success() {
        Long userId = 1L;
        Long subjectId = 2L;
        User user = new User();
        user.setId(userId);
        Subject subject = new Subject();
        subject.setId(subjectId);
        UserSubject relation = new UserSubject();
        relation.setId(1L);
        relation.setUser(user);
        relation.setSubject(subject);
        relation.setActive(true);
        UserSubject saved = new UserSubject();
        saved.setId(1L);
        saved.setUser(user);
        saved.setSubject(subject);
        saved.setActive(false);

        when(userSubjectRepository.findByUserIdAndSubjectIdAndActiveTrue(userId, subjectId))
                .thenReturn(Optional.of(relation));
        when(userSubjectRepository.save(relation)).thenReturn(saved);

        UserSubject result = userSubjectService.deleteRelation(userId, subjectId);

        assertNotNull(result);
        assertEquals(userId, result.getUser().getId());
        assertEquals(subjectId, result.getSubject().getId());
        assertFalse(result.getActive());
        verify(userSubjectRepository, times(1)).findByUserIdAndSubjectIdAndActiveTrue(userId, subjectId);
        verify(userSubjectRepository, times(1)).save(relation);
    }

    @Test
    void deleteRelation_NotFound_ThrowsException() {
        Long userId = 1L;
        Long subjectId = 2L;

        when(userSubjectRepository.findByUserIdAndSubjectIdAndActiveTrue(userId, subjectId))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userSubjectService.deleteRelation(userId, subjectId);
        });

        assertEquals("Active relation not found.", ex.getMessage());
        verify(userSubjectRepository, times(1)).findByUserIdAndSubjectIdAndActiveTrue(userId, subjectId);
        verify(userSubjectRepository, never()).save(any(UserSubject.class));
    }
}
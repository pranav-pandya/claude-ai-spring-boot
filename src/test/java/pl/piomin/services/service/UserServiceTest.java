package pl.piomin.services.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.piomin.services.dto.UserRequest;
import pl.piomin.services.dto.UserResponse;
import pl.piomin.services.model.User;
import pl.piomin.services.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findAll_shouldReturnAllUsers() {
        User user = createUser(1L, "John Doe", "john@example.com");
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponse> result = userService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("John Doe");
        verify(userRepository).findAll();
    }

    @Test
    void findById_shouldReturnUser_whenExists() {
        User user = createUser(1L, "John Doe", "john@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse result = userService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("John Doe");
        verify(userRepository).findById(1L);
    }

    @Test
    void findById_shouldThrowException_whenNotExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User not found with id: 1");
    }

    @Test
    void create_shouldCreateUser_whenValid() {
        UserRequest request = new UserRequest("John Doe", "john@example.com");
        User savedUser = createUser(1L, "John Doe", "john@example.com");
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse result = userService.create(request);

        assertThat(result.name()).isEqualTo("John Doe");
        assertThat(result.email()).isEqualTo("john@example.com");
        verify(userRepository).existsByEmail("john@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void create_shouldThrowException_whenEmailExists() {
        UserRequest request = new UserRequest("John Doe", "john@example.com");
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already exists: john@example.com");
    }

    @Test
    void update_shouldUpdateUser_whenValid() {
        User existingUser = createUser(1L, "John Doe", "john@example.com");
        UserRequest request = new UserRequest("Jane Doe", "jane@example.com");
        User updatedUser = createUser(1L, "Jane Doe", "jane@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("jane@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserResponse result = userService.update(1L, request);

        assertThat(result.name()).isEqualTo("Jane Doe");
        assertThat(result.email()).isEqualTo("jane@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void update_shouldThrowException_whenUserNotFound() {
        UserRequest request = new UserRequest("Jane Doe", "jane@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(1L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User not found with id: 1");
    }

    @Test
    void update_shouldThrowException_whenEmailExists() {
        User existingUser = createUser(1L, "John Doe", "john@example.com");
        UserRequest request = new UserRequest("John Doe", "existing@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.update(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already exists: existing@example.com");
    }

    @Test
    void delete_shouldDeleteUser_whenExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrowException_whenNotExists() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> userService.delete(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User not found with id: 1");
    }

    private User createUser(Long id, String name, String email) {
        User user = new User(name, email);
        user.setId(id);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }
}
package pl.piomin.services.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.piomin.services.dto.UserRequest;
import pl.piomin.services.dto.UserResponse;
import pl.piomin.services.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void getAllUsers_shouldReturnUsers() throws Exception {
        UserResponse user = new UserResponse(1L, "John Doe", "john@example.com", LocalDateTime.now(), LocalDateTime.now());
        when(userService.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));

        verify(userService).findAll();
    }

    @Test
    void getUserById_shouldReturnUser() throws Exception {
        UserResponse user = new UserResponse(1L, "John Doe", "john@example.com", LocalDateTime.now(), LocalDateTime.now());
        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(userService).findById(1L);
    }

    @Test
    void createUser_shouldCreateUser_whenValid() throws Exception {
        UserRequest request = new UserRequest("John Doe", "john@example.com");
        UserResponse response = new UserResponse(1L, "John Doe", "john@example.com", LocalDateTime.now(), LocalDateTime.now());
        when(userService.create(any(UserRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(userService).create(any(UserRequest.class));
    }

    @Test
    void createUser_shouldReturn400_whenInvalid() throws Exception {
        UserRequest request = new UserRequest("", "invalid-email");

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    void updateUser_shouldUpdateUser_whenValid() throws Exception {
        UserRequest request = new UserRequest("Jane Doe", "jane@example.com");
        UserResponse response = new UserResponse(1L, "Jane Doe", "jane@example.com", LocalDateTime.now(), LocalDateTime.now());
        when(userService.update(eq(1L), any(UserRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane@example.com"));

        verify(userService).update(eq(1L), any(UserRequest.class));
    }

    @Test
    void deleteUser_shouldDeleteUser() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).delete(1L);
    }
}
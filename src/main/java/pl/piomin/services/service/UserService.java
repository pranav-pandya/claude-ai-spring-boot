package pl.piomin.services.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.piomin.services.dto.UserRequest;
import pl.piomin.services.dto.UserResponse;
import pl.piomin.services.model.User;
import pl.piomin.services.repository.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return toResponse(user);
    }

    @Transactional
    public UserResponse create(UserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists: " + request.email());
        }
        User user = new User(request.name(), request.email());
        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    @Transactional
    public UserResponse update(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        // Check if email is being changed and if it conflicts
        if (!user.getEmail().equals(request.email()) && userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists: " + request.email());
        }

        user.setName(request.name());
        user.setEmail(request.email());
        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
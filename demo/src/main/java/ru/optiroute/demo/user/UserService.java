package ru.optiroute.demo.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ru.optiroute.demo.auth.AuthResponse;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Transactional(readOnly = true)
    public AuthResponse.UserView getProfile(String email) {
        var user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден."));
        return AuthResponse.UserView.from(user);
    }

    @Transactional
    public AuthResponse.UserView updateCity(String email, String citySlug) {
        var user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден."));
        return AuthResponse.UserView.from(user);
    }
}

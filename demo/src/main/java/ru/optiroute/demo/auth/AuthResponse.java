package ru.optiroute.demo.auth;

import ru.optiroute.demo.user.AppUser;

public record AuthResponse(
        String token,
        UserView user
) {
    public static AuthResponse of(String token, AppUser user) {
        return new AuthResponse(token, UserView.from(user));
    }

    public record UserView(
            Long id,
            String name,
            String email
    ) {
        public static UserView from(AppUser user) {
            return new UserView(
                    user.getId(),
                    user.getName(),
                    user.getEmail()
            );
        }
    }
}


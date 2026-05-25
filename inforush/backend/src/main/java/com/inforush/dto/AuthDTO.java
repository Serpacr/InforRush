package com.inforush.dto;

import com.inforush.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class AuthDTO {

    public static class RegisterRequest {
        @NotBlank(message = "Nome é obrigatório")
        private String name;
        @Email(message = "E-mail inválido") @NotBlank(message = "E-mail é obrigatório")
        private String email;
        @Size(min = 6, message = "Senha deve ter ao menos 6 caracteres")
        private String password;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginRequest {
        @Email @NotBlank private String email;
        @NotBlank private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class AuthResponse {
        private UserDTO user;
        private String token;

        public AuthResponse(UserDTO user, String token) { this.user = user; this.token = token; }
        public UserDTO getUser() { return user; }
        public String getToken() { return token; }
    }

    public static class UserDTO {
        private String id, name, email, avatar;
        private User.Role role;
        private LocalDateTime createdAt;

        public static UserDTO from(User u) {
            UserDTO dto = new UserDTO();
            dto.id = u.getId(); dto.name = u.getName();
            dto.email = u.getEmail(); dto.avatar = u.getAvatar();
            dto.role = u.getRole(); dto.createdAt = u.getCreatedAt();
            return dto;
        }
        public String getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getAvatar() { return avatar; }
        public User.Role getRole() { return role; }
        public LocalDateTime getCreatedAt() { return createdAt; }
    }
}

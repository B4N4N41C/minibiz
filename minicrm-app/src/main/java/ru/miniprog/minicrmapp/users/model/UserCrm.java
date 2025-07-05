package ru.miniprog.minicrmapp.users.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Schema(description = "Модель пользователя системы")
public class UserCrm implements UserDetails {

    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "ivanov", required = true)
    private String username;

    @Schema(description = "Зашифрованный пароль пользователя", required = true)
    private String password;

    @Schema(description = "Email пользователя", example = "ivanov@example.com", required = true)
    private String email;

    @Schema(description = "Роль пользователя в системе", required = true)
    private Role role;

    @Schema(description = "Список идентификаторов чат-комнат, в которых участвует пользователь")
    private List<Long> chatRooms;

    public UserCrm(Long id, String username, String password, String email, Role role, List<Long> chatRooms) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.chatRooms = chatRooms;
    }

    public UserCrm() {
    }

    public static UserCrmBuilder builder() {
        return new UserCrmBuilder();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public Role getRole() {
        return this.role;
    }

    public List<Long> getChatRooms() {
        return this.chatRooms;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setChatRooms(List<Long> chatRooms) {
        this.chatRooms = chatRooms;
    }

    public static class UserCrmBuilder {
        private Long id;
        private String username;
        private String password;
        private String email;
        private Role role;
        private List<Long> chatRooms;

        UserCrmBuilder() {
        }

        public UserCrmBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserCrmBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserCrmBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserCrmBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserCrmBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UserCrmBuilder chatRooms(List<Long> chatRooms) {
            this.chatRooms = chatRooms;
            return this;
        }

        public UserCrm build() {
            return new UserCrm(this.id, this.username, this.password, this.email, this.role, this.chatRooms);
        }

        public String toString() {
            return "UserCrm.UserCrmBuilder(id=" + this.id + ", username=" + this.username + ", password=" + this.password + ", email=" + this.email + ", role=" + this.role + ", chatRooms=" + this.chatRooms + ")";
        }
    }
}

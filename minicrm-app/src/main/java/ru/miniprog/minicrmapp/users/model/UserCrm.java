package ru.miniprog.minicrmapp.users.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}

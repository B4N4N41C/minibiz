package ru.miniprog.minicrmapp.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на регистрацию")
public class SignUpRequest {

    @Schema(description = "Имя пользователя", example = "Jon")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String username;

    @Schema(description = "Адрес электронной почты", example = "jondoe@gmail.com")
    @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
    @NotBlank(message = "Адрес электронной почты не может быть пустыми")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    private String email;

    @Schema(description = "Пароль", example = "my_1secret1_password")
    @Size(max = 255, message = "Длина пароля должна быть не более 255 символов")
    private String password;

    public SignUpRequest() {
    }

    public @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов") @NotBlank(message = "Имя пользователя не может быть пустыми") String getUsername() {
        return this.username;
    }

    public @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов") @NotBlank(message = "Адрес электронной почты не может быть пустыми") @Email(message = "Email адрес должен быть в формате user@example.com") String getEmail() {
        return this.email;
    }

    public @Size(max = 255, message = "Длина пароля должна быть не более 255 символов") String getPassword() {
        return this.password;
    }

    public void setUsername(@Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов") @NotBlank(message = "Имя пользователя не может быть пустыми") String username) {
        this.username = username;
    }

    public void setEmail(@Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов") @NotBlank(message = "Адрес электронной почты не может быть пустыми") @Email(message = "Email адрес должен быть в формате user@example.com") String email) {
        this.email = email;
    }

    public void setPassword(@Size(max = 255, message = "Длина пароля должна быть не более 255 символов") String password) {
        this.password = password;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SignUpRequest)) return false;
        final SignUpRequest other = (SignUpRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$username = this.getUsername();
        final Object other$username = other.getUsername();
        if (this$username == null ? other$username != null : !this$username.equals(other$username)) return false;
        final Object this$email = this.getEmail();
        final Object other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SignUpRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $username = this.getUsername();
        result = result * PRIME + ($username == null ? 43 : $username.hashCode());
        final Object $email = this.getEmail();
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        return result;
    }

    public String toString() {
        return "SignUpRequest(username=" + this.getUsername() + ", email=" + this.getEmail() + ", password=" + this.getPassword() + ")";
    }
}

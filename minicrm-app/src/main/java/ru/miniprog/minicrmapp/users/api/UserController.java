package ru.miniprog.minicrmapp.users.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import ru.miniprog.minicrmapp.chat.api.ChatController;
import ru.miniprog.minicrmapp.users.api.payload.UpdateUserPayload;
import ru.miniprog.minicrmapp.users.dto.UserCrmDTO;
import ru.miniprog.minicrmapp.users.model.UserCrm;
import ru.miniprog.minicrmapp.users.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Tag(name = "Управление пользователями", description = "API для управления пользователями системы")
public class UserController {
	final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	Logger logger = LoggerFactory.getLogger(UserController.class);

	public UserController(UserRepository userRepository,
						  PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Operation(summary = "Получить список всех пользователей", description = "Возвращает список всех пользователей системы")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Список пользователей успешно получен"),
		@ApiResponse(responseCode = "401", description = "Требуется авторизация")
	})
	@GetMapping
	public List<UserCrmDTO> getAllUsers() {
		logger.info("Получение списка всех пользователей");
		List<UserCrmDTO> users = userRepository.findAll().stream()
				.map(user -> new UserCrmDTO(user.getId(), user.getUsername(), user.getEmail()))
				.collect(Collectors.toList());
		logger.info("Найдено {} пользователей", users.size());
		return users;
	}

	@Operation(summary = "Создать нового пользователя", description = "Создает нового пользователя в системе")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Пользователь успешно создан"),
		@ApiResponse(responseCode = "400", description = "Неверные данные пользователя"),
		@ApiResponse(responseCode = "401", description = "Требуется авторизация")
	})
	@PostMapping
	public ResponseEntity<UserCrmDTO> createUser(@Valid @RequestBody UpdateUserPayload userDTO) {
		logger.info("Создание нового пользователя: {}", userDTO.username());
		UserCrm user = new UserCrm();
		user.setUsername(userDTO.username());
		user.setEmail(userDTO.email());
		user.setPassword(passwordEncoder.encode(userDTO.password()));
		UserCrm savedUser = userRepository.save(user);
		logger.info("Пользователь успешно создан с ID: {}", savedUser.getId());
		return ResponseEntity.ok(new UserCrmDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail()));
	}

	@Operation(summary = "Обновить данные пользователя", description = "Обновляет информацию о существующем пользователе")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Данные пользователя успешно обновлены"),
		@ApiResponse(responseCode = "404", description = "Пользователь не найден"),
		@ApiResponse(responseCode = "401", description = "Требуется авторизация")
	})
	@PatchMapping("/{id}")
	public ResponseEntity<UserCrmDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserPayload userDTO) {
		logger.info("Обновление пользователя с ID: {}", id);
		UserCrm user = userRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		user.setUsername(userDTO.username());
		user.setEmail(userDTO.email());
		if (!userDTO.password().equals("") && userDTO.password().length() >= 8) {
			user.setPassword(passwordEncoder.encode(userDTO.password()));
		}
		UserCrm updatedUser = userRepository.save(user);
		logger.info("Пользователь с ID {} успешно обновлен", id);
		return ResponseEntity.ok(new UserCrmDTO(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getEmail()));
	}

	@Operation(summary = "Удалить пользователя", description = "Удаляет пользователя из системы")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "Пользователь успешно удален"),
		@ApiResponse(responseCode = "404", description = "Пользователь не найден"),
		@ApiResponse(responseCode = "401", description = "Требуется авторизация")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		logger.info("Удаление пользователя с ID: {}", id);
		userRepository.deleteById(id);
		logger.info("Пользователь с ID {} успешно удален", id);
		return ResponseEntity.noContent().build();
	}
}

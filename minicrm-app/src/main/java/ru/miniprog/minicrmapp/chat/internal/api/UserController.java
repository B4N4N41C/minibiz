package ru.miniprog.minicrmapp.chat.internal.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.miniprog.minicrmapp.chat.internal.api.payload.UpdateUserPayload;
import ru.miniprog.minicrmapp.chat.internal.dto.UserCrmDTO;
import ru.miniprog.minicrmapp.chat.internal.model.UserCrm;
import ru.miniprog.minicrmapp.chat.internal.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
	final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserController(UserRepository userRepository,
						  PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping
	public List<UserCrmDTO> getAllUsers() {
		return userRepository.findAll().stream()
				.map(user -> new UserCrmDTO(user.getId(), user.getUsername(), user.getEmail()))
				.collect(Collectors.toList());
	}

	@PostMapping
	public ResponseEntity<UserCrmDTO> createUser(@RequestBody UpdateUserPayload userDTO) {
		UserCrm user = new UserCrm();
		user.setUsername(userDTO.username());
		user.setEmail(userDTO.email());
		user.setPassword(passwordEncoder.encode(userDTO.password()));
		UserCrm savedUser = userRepository.save(user);
		return ResponseEntity.ok(new UserCrmDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail()));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<UserCrmDTO> updateUser(@PathVariable Long id, @RequestBody UpdateUserPayload userDTO) {
		UserCrm user = userRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		user.setUsername(userDTO.username());
		user.setEmail(userDTO.email());
		if (!userDTO.password().equals("") && userDTO.password().length() >= 8) {
			user.setPassword(passwordEncoder.encode(userDTO.password()));
		}
		UserCrm updatedUser = userRepository.save(user);
		return ResponseEntity.ok(new UserCrmDTO(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getEmail()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}

package ru.miniprog.minicrmapp.chat.internal.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.miniprog.minicrmapp.chat.internal.dto.UserCrmDTO;
import ru.miniprog.minicrmapp.chat.internal.model.UserCrm;
import ru.miniprog.minicrmapp.chat.internal.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
	final UserRepository userRepository;

	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/users")
	public List<UserCrmDTO> getMethodName() {
		List<UserCrm> users = userRepository.findAll();
		List<UserCrmDTO> usersCrmDTO = new ArrayList<>();
		for (UserCrm userCrm : users) {
			usersCrmDTO.add(new UserCrmDTO(userCrm.getId(), userCrm.getUsername(), userCrm.getEmail()));
		}
		return usersCrmDTO;
	}
}

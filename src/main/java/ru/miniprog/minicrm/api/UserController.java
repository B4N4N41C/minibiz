package ru.miniprog.minicrm.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

import ru.miniprog.minicrm.dto.UserCrmDTO;
import ru.miniprog.minicrm.model.UserCrm;
import ru.miniprog.minicrm.repository.UserRepository;

@RestController
// @RequestMapping("/user")
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

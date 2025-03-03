package ru.miniprog.minicrm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.miniprog.minicrm.model.UserCrm;
import ru.miniprog.minicrm.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserCrm registerUser(UserCrm user) {
        // Здесь можно добавить логику проверки и обработки данных пользователя
        return userRepository.save(user);
    }
}

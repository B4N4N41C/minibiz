// package ru.miniprog.minicrmapp;

// import org.junit.jupiter.api.Test;
// import org.mockito.Mock;
// import org.springframework.beans.factory.annotation.Autowired;
// import
// org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.context.annotation.Import;
// import org.springframework.http.MediaType;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.context.TestPropertySource;
// import org.springframework.test.web.servlet.MockMvc;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.springframework.transaction.annotation.Transactional;
// import ru.miniprog.minicrmapp.users.api.UserController;
// import ru.miniprog.minicrmapp.users.api.payload.UpdateUserPayload;
// import ru.miniprog.minicrmapp.users.config.SecurityConfiguration;
// import ru.miniprog.minicrmapp.users.model.Role;
// import ru.miniprog.minicrmapp.users.model.UserCrm;
// import ru.miniprog.minicrmapp.users.repository.UserRepository;

// import java.util.Arrays;
// import java.util.List;

// import static org.hamcrest.Matchers.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.when;
// import static
// org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static
// org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @SpringBootTest
// @AutoConfigureMockMvc
// @Import(SecurityConfiguration .class)
// @TestPropertySource(properties = {
// "spring.security.user.name=Николай",
// "spring.security.user.password=qwerqwer"
// })
// public class UserControllerTest {
// @Autowired
// private MockMvc mockMvc;

// @Mock
// private UserRepository userRepository;

// @Mock
// private PasswordEncoder passwordEncoder;

// @Test
// @WithMockUser
// void getAllUsers_ShouldReturnListOfUsers() throws Exception {
// mockMvc.perform(get("/users"))
// .andExpect(status().isOk());
// }

// @Test
// @WithMockUser
// @Transactional
// void createUser_WithValidData_ShouldCreateUser() throws Exception {
// UpdateUserPayload payload = new UpdateUserPayload(
// null, "newuser", "new@test.com", "passworD+123"
// );

// UserCrm savedUser = new UserCrm();
// savedUser.setUsername(payload.username());
// savedUser.setEmail(payload.email());

// when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
// when(userRepository.save(any(UserCrm.class))).thenReturn(savedUser);

// mockMvc.perform(post("/users")
// .contentType(MediaType.APPLICATION_JSON)
// .content(new ObjectMapper().writeValueAsString(payload)))
// .andExpect(status().isOk())
// .andExpect(jsonPath("$.username", is("newuser")));
// }
// }

package ru.miniprog.minicrmapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;
import ru.miniprog.minicrmapp.kanban.api.KanbanBoardController;
import ru.miniprog.minicrmapp.kanban.api.payload.NewTaskPayload;
import ru.miniprog.minicrmapp.kanban.model.Status;
import static org.hamcrest.Matchers.hasSize;

import ru.miniprog.minicrmapp.kanban.model.Task;
import ru.miniprog.minicrmapp.kanban.service.KanbanService;
import ru.miniprog.minicrmapp.kanban.repository.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class KanbanBoardControllerTest {
    @InjectMocks
    private KanbanBoardController kanbanBoardController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(kanbanBoardController).build();
        objectMapper = new ObjectMapper();
    }



    @Test
    void getAllStatus_ShouldReturnListOfStatuses() throws Exception {
        mockMvc.perform(get("/kanban/status"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllStatus_ShouldReturnListOfTasks() throws Exception {
        mockMvc.perform(get("/kanban/task"))
                .andExpect(status().isOk());
    }

}

package com.luis.taskapi;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskControllerTest {

  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper objectMapper;

  private String getToken() throws Exception {
    String suffix = UUID.randomUUID().toString().replace("-", "");
    String username = "testuser_" + suffix;
    String email = "testuser_" + suffix + "@example.com";

    Map<String, Object> register = Map.of(
        "username", username,
        "email", email,
        "password", "123456"
    );

    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(register)))
        .andExpect(status().isCreated());

    Map<String, Object> login = Map.of(
        "username", username,
        "password", "123456"
    );

    MvcResult res = mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(login)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").exists())
        .andReturn();

    JsonNode node = objectMapper.readTree(res.getResponse().getContentAsString());
    return node.get("token").asText();
  }

  @Test
  void shouldCreateTaskSuccessfully() throws Exception {
    String token = getToken();

    Map<String, Object> req = Map.of(
        "title", "My task",
        "description", "Description",
        "status", "TODO"
    );

    mockMvc.perform(post("/api/tasks")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.title").value("My task"))
        .andExpect(jsonPath("$.createdAt").exists());
  }

  @Test
  void shouldReturn400WhenTitleMissing() throws Exception {
    String token = getToken();

    Map<String, Object> req = Map.of(
        "description", "Description",
        "status", "TODO"
    );

    mockMvc.perform(post("/api/tasks")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.path").value("/api/tasks"));
  }
}

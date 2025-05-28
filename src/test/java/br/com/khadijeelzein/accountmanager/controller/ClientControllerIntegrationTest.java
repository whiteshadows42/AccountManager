package br.com.khadijeelzein.accountmanager.controller;

import br.com.khadijeelzein.accountmanager.dto.ClientRequest;
import br.com.khadijeelzein.accountmanager.repository.AbstractBaseIntegrationTest;
import br.com.khadijeelzein.accountmanager.repository.ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ClientRepository repository;

    @Container
    @ServiceConnection
    static MongoDBContainer mongoContainer = new MongoDBContainer("mongo:latest");

    @BeforeAll
    public static void setUp() {
        mongoContainer.start();
    }

    @AfterAll
    public static void destroy() {
        mongoContainer.stop();
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    public void givenClient_whenSave_thenCreated() throws Exception {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setCpf("62368887016");
        clientRequest.setName("Test");
        clientRequest.setBirthday(LocalDate.of(1996, 4, 1));
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(formatter));
        mapper.registerModule(javaTimeModule);
        String inputInJson = mapper.writeValueAsString(clientRequest);

        mvc.perform(MockMvcRequestBuilders.post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenRequestNull_whenSave_thenError() throws Exception {
        ClientRequest clientRequest = null;
        String inputInJson = mapper.writeValueAsString(clientRequest);

        mvc.perform(MockMvcRequestBuilders.post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenCpfInvalid_whenSave_thenError() throws Exception {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setCpf("123456");
        clientRequest.setName("Test");
        clientRequest.setBirthday(LocalDate.of(1996, 4, 1));
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(formatter));
        mapper.registerModule(javaTimeModule);
        String inputInJson = mapper.writeValueAsString(clientRequest);

        mvc.perform(MockMvcRequestBuilders.post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNameNull_whenSave_thenError() throws Exception {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setCpf("62368887016");
        clientRequest.setName(null);
        clientRequest.setBirthday(LocalDate.of(1996, 4, 1));
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(formatter));
        mapper.registerModule(javaTimeModule);
        String inputInJson = mapper.writeValueAsString(clientRequest);

        mvc.perform(MockMvcRequestBuilders.post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenDateNull_whenSave_thenError() throws Exception {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setCpf("62368887016");
        clientRequest.setName("Test");
        clientRequest.setBirthday(null);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(formatter));
        mapper.registerModule(javaTimeModule);
        String inputInJson = mapper.writeValueAsString(clientRequest);

        mvc.perform(MockMvcRequestBuilders.post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenDateInvalid_whenSave_thenError() throws Exception {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setCpf("62368887016");
        clientRequest.setName("Test");
        String inputInJson = mapper.writeValueAsString(clientRequest);
        JSONObject jsonObject = new JSONObject(inputInJson);
        jsonObject.put("data_nascimento", "2024-2-30");
        mvc.perform(MockMvcRequestBuilders.post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenCpfDuplicated_whenSave_thenError() throws Exception {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setCpf("62368887016");
        clientRequest.setName("Test");
        clientRequest.setBirthday(LocalDate.of(1996, 4, 1));
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(formatter));
        mapper.registerModule(javaTimeModule);
        String inputInJson = mapper.writeValueAsString(clientRequest);

        mvc.perform(MockMvcRequestBuilders.post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson))
                .andExpect(status().isCreated());
        mvc.perform(MockMvcRequestBuilders.post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson))
                .andExpect(status().isConflict());
    }
}

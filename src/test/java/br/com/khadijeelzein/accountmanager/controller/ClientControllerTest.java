package br.com.khadijeelzein.accountmanager.controller;

import br.com.khadijeelzein.accountmanager.dto.ClientRequest;
import br.com.khadijeelzein.accountmanager.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ClientController.class)
@AutoConfigureMockMvc
public class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientService clientService;

    @Test
    public void testAddClient() throws Exception {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setCpf("62368887016");
        clientRequest.setName("Test");
        clientRequest.setBirthday(LocalDate.of(1996, 4, 1));
        doNothing().when(clientService).addClient(clientRequest);

        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(formatter));
        mapper.registerModule(javaTimeModule);
        String inputInJson = mapper.writeValueAsString(clientRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/clients")
                        .contentType(MediaType.APPLICATION_JSON) // or other expected type
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAddClientWhenRequestIsNull() throws Exception {
        ClientRequest clientRequest = null;
        doNothing().when(clientService).addClient(clientRequest);
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(formatter));
        mapper.registerModule(javaTimeModule);
        String inputInJson = mapper.writeValueAsString(clientRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/clients")
                        .contentType(MediaType.APPLICATION_JSON) // or other expected type
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddClientWhenCpfIsInvalid() throws Exception {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setCpf("12345");
        clientRequest.setName("Test");
        clientRequest.setBirthday(LocalDate.of(1996, 4, 1));
        doNothing().when(clientService).addClient(clientRequest);

        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(formatter));
        mapper.registerModule(javaTimeModule);
        String inputInJson = mapper.writeValueAsString(clientRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/clients")
                        .contentType(MediaType.APPLICATION_JSON) // or other expected type
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddClientWhenNameIsNull() throws Exception {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setCpf("12345");
        clientRequest.setName(null);
        clientRequest.setBirthday(LocalDate.of(1996, 4, 1));
        doNothing().when(clientService).addClient(clientRequest);
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(formatter));
        mapper.registerModule(javaTimeModule);
        String inputInJson = mapper.writeValueAsString(clientRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/clients")
                        .contentType(MediaType.APPLICATION_JSON) // or other expected type
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddClientWhenDateIsNull() throws Exception {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setCpf("12345");
        clientRequest.setName("Test");
        clientRequest.setBirthday(null);
        doNothing().when(clientService).addClient(clientRequest);
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(formatter));
        mapper.registerModule(javaTimeModule);
        String inputInJson = mapper.writeValueAsString(clientRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/clients")
                        .contentType(MediaType.APPLICATION_JSON) // or other expected type
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }
}

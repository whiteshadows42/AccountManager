package br.com.khadijeelzein.accountmanager.controller;

import br.com.khadijeelzein.accountmanager.dto.AccountBalanceProjection;
import br.com.khadijeelzein.accountmanager.dto.AccountRequest;
import br.com.khadijeelzein.accountmanager.enums.TypeEnum;
import br.com.khadijeelzein.accountmanager.model.Account;
import br.com.khadijeelzein.accountmanager.model.Client;
import br.com.khadijeelzein.accountmanager.repository.AbstractBaseIntegrationTest;
import br.com.khadijeelzein.accountmanager.repository.AccountRepository;
import br.com.khadijeelzein.accountmanager.repository.ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.LocalDate;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private AccountRepository repository;

    @Autowired
    private ClientRepository clientRepository;

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
        clientRepository.deleteAll();
    }

    @Test
    public void givenValidAccountRequest_whenSave_thenCreated() throws Exception {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountType(TypeEnum.CORRENTE.getType());
        accountRequest.setClientCpf("62368887016");
        String inputInJson = mapper.writeValueAsString(accountRequest);

        var client = Client.builder()
                .name("test")
                .cpf("62368887016")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        clientRepository.save(client);
        mvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenInexistentClient_whenSave_thenBadRequest() throws Exception {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountType(TypeEnum.CORRENTE.getType());
        accountRequest.setClientCpf("62368887016");
        String inputInJson = mapper.writeValueAsString(accountRequest);
        mvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNullAccountClientRequest_whenAddingAccount_thenError() throws Exception {
        AccountRequest account = new AccountRequest();
        account.setAccountType(TypeEnum.CORRENTE.getType());
        account.setClientCpf(null);
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(account);
        mvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON) // or other expected type
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void givenClientFormattedCpfRequest_whenAddingAccount_thenOK() throws Exception {
        AccountRequest account = new AccountRequest();
        account.setAccountType(TypeEnum.CORRENTE.getType());
        account.setClientCpf("619.874.460-46");
        var client = Client.builder()
                .name("test")
                .cpf(("619.874.460-46").replaceAll("[^0-9]", ""))
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        clientRepository.save(client);
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(account);
        mvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenNullAccountTypeRequest_whenAddingAccount_thenThrowException() throws Exception {
        AccountRequest account = new AccountRequest();
        account.setAccountType(null);
        account.setClientCpf("81767219059");
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(account);
        mvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON) // or other expected type
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidAccountClientRequest_whenAddingAccount_thenThrowException() throws Exception {
        AccountRequest account = new AccountRequest();
        account.setAccountType(TypeEnum.CORRENTE.getType());
        account.setClientCpf("123456");
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(account);
        mvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidAccountTypeRequest_whenAddingAccount_thenThrowException() throws Exception {
        AccountRequest account = new AccountRequest();
        account.setAccountType("INEXISTENTE");
        account.setClientCpf("123456");
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(account);
        mvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON) // or other expected type
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNullAccountRequest_whenAddingAccount_thenThrowException() throws Exception {
        AccountRequest accountRequest = null;
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(accountRequest);
        mvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON) // or other expected type
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenValidAccountNumber_whenGetAccountBalance_thenOk() throws Exception {
        var accountNbr = 123456L;
        Double accountBalance = 0.0;
        var client = Client.builder()
                .name("test")
                .cpf("62368887016")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        clientRepository.save(client);
        var account = Account
                .builder()
                .accountNbr(accountNbr)
                .currentBalance(accountBalance)
                .type(TypeEnum.CORRENTE)
                .client(client)
                .build();
        repository.save(account);
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        Map<String, Double> map = Map.of("currentBalance", accountBalance);
        var projection = factory.createProjection(AccountBalanceProjection.class, map);
        mvc.perform(MockMvcRequestBuilders.get("/accounts/{id}/balance",String.valueOf(accountNbr))
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk());
    }

    @Test
    public void givenInvalidValidAccountNumber_whenGetAccountBalance_thenOk() throws Exception {
        var accountNbr = "abc";
        mvc.perform(MockMvcRequestBuilders.get("/accounts/{id}/balance",accountNbr)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }
}
package br.com.khadijeelzein.accountmanager.controller;

import br.com.khadijeelzein.accountmanager.dto.AccountMovementRequest;
import br.com.khadijeelzein.accountmanager.enums.MovementTypeEnum;
import br.com.khadijeelzein.accountmanager.enums.TypeEnum;
import br.com.khadijeelzein.accountmanager.mapper.AccountMovementMapper;
import br.com.khadijeelzein.accountmanager.model.Account;
import br.com.khadijeelzein.accountmanager.model.AccountMovement;
import br.com.khadijeelzein.accountmanager.model.Client;
import br.com.khadijeelzein.accountmanager.repository.AbstractBaseIntegrationTest;
import br.com.khadijeelzein.accountmanager.repository.AccountMovementRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class AccountMovementControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private AccountMovementRepository repository;

    @Autowired
    private AccountRepository accountRepository;

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
        accountRepository.deleteAll();
        clientRepository.deleteAll();
    }


    @Test
    public void givenValidAccountMovementRequest_whenAddingAccountMovement_thenOK() throws Exception {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrOrigin(123456L);
        accountMovementRequest.setAccountNbrDestination(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        var client = Client.builder()
                .name("test")
                .cpf("62368887016")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        clientRepository.save(client);
        Account accountOrigin = Account.builder()
                                .accountNbr(123456L)
                                .currentBalance(0.0d)
                                .type(TypeEnum.CORRENTE)
                                .client(client)
                                .build();
        Account accountDestination = Account.builder()
                .accountNbr(1234567L)
                .currentBalance(0.0d)
                .type(TypeEnum.CORRENTE)
                .client(client)
                .build();
        accountRepository.save(accountOrigin);
        accountRepository.save(accountDestination);
        repository.save(AccountMovementMapper.toAccountMovement(accountMovementRequest));
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(accountMovementRequest);
        mvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk());
    }

    @Test
    public void givenInvalidAccountOrigin_whenAddingAccountMovement_thenThrowException() throws Exception {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(-1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(accountMovementRequest);
        mvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidAccountDestination_whenAddingAccountMovement_thenThrowException() throws Exception {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(-123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(accountMovementRequest);
        mvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidAmount_whenAddingAccountMovement_thenThrowException() throws Exception {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(-10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(accountMovementRequest);
        mvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidType_whenAddingAccountMovement_thenThrowException() throws Exception {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setType("Inválido");
        accountMovementRequest.setAmount(10d);
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(accountMovementRequest);
        mvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNullAccountOrigin_whenAddingAccountMovement_thenConstraintViolation() throws Exception {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(null);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(accountMovementRequest);
        mvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNullAccountDestination_whenAddingAccountMovement_thenConstraintViolation() throws Exception {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(null);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(accountMovementRequest);
        mvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNullAmount_whenAddingAccountMovement_thenConstraintViolation() throws Exception {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(null);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(accountMovementRequest);
        mvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void givenNullType_whenAddingAccountMovement_thenConstraintViolation() throws Exception {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(null);
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(accountMovementRequest);
        mvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenSameAccounts_whenAddingAccountMovement_thenThrowException() throws Exception {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(1234567L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(accountMovementRequest);
        mvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInexistentAccounts_whenAddingAccountMovement_thenThrowException() throws Exception {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        ObjectMapper mapper = new ObjectMapper();
        repository.save(AccountMovementMapper.toAccountMovement(accountMovementRequest));
        String inputInJson = mapper.writeValueAsString(accountMovementRequest);
        mvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenValidAccountMovementRequest_whenFindingAccountMovementHistory_thenCallRepository() throws Exception {
        var accountNbr = String.valueOf(123456L);
        var startDate = LocalDate.now().minusDays(1);
        var endDate = LocalDate.now().plusDays(1);
        var client = Client.builder()
                .name("test")
                .cpf("62368887016")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        clientRepository.save(client);
        Account accountOrigin = Account.builder()
                .accountNbr(123456L)
                .currentBalance(0.0d)
                .type(TypeEnum.CORRENTE)
                .client(client)
                .build();
        Account accountDestination = Account.builder()
                .accountNbr(1234567L)
                .currentBalance(0.0d)
                .type(TypeEnum.CORRENTE)
                .client(client)
                .build();
        var accountMovement = AccountMovement.builder()
                .accountDestination(1234567L)
                .accountOrigin(123456L)
                .amount(10d)
                .type(MovementTypeEnum.TRANSFERENCIA)
                .dateTime(LocalDateTime.now())
                .build();
        accountRepository.save(accountOrigin);
        accountRepository.save(accountDestination);
        var pageable= PageRequest.of(0, 10);
        var accountMvmt = repository.save(accountMovement);
        var accountMovementResponse = repository.
                findAllByDateAndAccountOriginOrAccountDestination(startDate,endDate,Long.parseLong(accountNbr),pageable);
        mvc.perform(MockMvcRequestBuilders.get("/accounts/{id}/transactions",accountNbr)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNonPositiveAccountNbr_whenFindingAccountMovementHistory_thenThrowsException() throws Exception {
        var accountNbr = String.valueOf(-123456L);
        var startDate = LocalDate.now().minusDays(1);
        var endDate = LocalDate.now().plusDays(1);
        mvc.perform(MockMvcRequestBuilders.get("/accounts/{id}/transactions",accountNbr)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidAccountNbr_whenFindingAccountMovementHistory_thenThrowsException() throws Exception {
        var accountNbr = "abcdefghij";
        var startDate = LocalDate.now().minusDays(1);
        var endDate = LocalDate.now().plusDays(1);
        mvc.perform(MockMvcRequestBuilders.get("/accounts/{id}/transactions",accountNbr)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());    }

    @Test
    public void givenInvalidDates_whenFindingAccountMovementHistory_thenThrowsException() throws Exception {
        var accountNbr = String.valueOf(123456L);
        var startDate = LocalDate.now().plusDays(1);
        var endDate = LocalDate.now().minusDays(1);
        mvc.perform(MockMvcRequestBuilders.get("/accounts/{id}/transactions",accountNbr)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void givenNullAccountNbr_whenFindingAccountMovementHistory_thenThrowsException() throws Exception {
        String accountNbr = null;
        var startDate = LocalDate.now().minusDays(1);
        var endDate = LocalDate.now().plusDays(1);
        mvc.perform(MockMvcRequestBuilders.get("/accounts/{id}/transactions",accountNbr)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isNotFound());
    }
}

package br.com.khadijeelzein.accountmanager.controller;

import br.com.khadijeelzein.accountmanager.dto.AccountBalanceProjection;
import br.com.khadijeelzein.accountmanager.dto.AccountNbrResponse;
import br.com.khadijeelzein.accountmanager.dto.AccountRequest;
import br.com.khadijeelzein.accountmanager.enums.TypeEnum;
import br.com.khadijeelzein.accountmanager.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Map;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class)
@AutoConfigureMockMvc
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @Test
    public void givenValidAccountRequest_whenAddingAccount_thenOk() throws Exception {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setClientCpf("62368887016");
        accountRequest.setAccountType("CORRENTE");
        var accountNbrResponse = new AccountNbrResponse(1234567L);

        doReturn(accountNbrResponse).when(accountService).createAccount(accountRequest);
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(accountRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON) // or other expected type
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenNullAccountClientRequest_whenAddingAccount_thenError() throws Exception {
        AccountRequest account = new AccountRequest();
        account.setAccountType(TypeEnum.CORRENTE.getType());
        account.setClientCpf(null);
        doThrow(IllegalArgumentException.class).when(accountService).createAccount(account);
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(account);
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON) // or other expected type
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void givenClientFormattedCpfRequest_whenAddingAccount_thenOK() throws Exception {
        AccountRequest account = new AccountRequest();
        account.setAccountType(TypeEnum.CORRENTE.getType());
        account.setClientCpf("817.672.190-59");
        var accountNbrResponse = new AccountNbrResponse(1234567L);
        doReturn(accountNbrResponse).when(accountService).createAccount(account);
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(account);
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
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
        doThrow(NullPointerException.class).when(accountService).createAccount(account);
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(account);
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
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
        doThrow(ConstraintViolationException.class).when(accountService).createAccount(account);
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(account);
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidAccountTypeRequest_whenAddingAccount_thenThrowException() throws Exception {
        AccountRequest account = new AccountRequest();
        account.setAccountType("INEXISTENTE");
        account.setClientCpf("62368887016");
        doThrow(IllegalArgumentException.class).when(accountService).createAccount(account);
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(account);
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON) // or other expected type
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenValidAccountNameTypeRequest_whenAddingAccount_thenCreated() throws Exception {
        AccountRequest account = new AccountRequest();
        account.setAccountType("POUPANÇA");
        account.setClientCpf("62368887016");
        var accountNbrResponse = new AccountNbrResponse(1234567L);
        doReturn(accountNbrResponse).when(accountService).createAccount(account);
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(account);
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON) // or other expected type
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenValidAccountTypeRequest_whenAddingAccount_thenOk() throws Exception {
        AccountRequest account = new AccountRequest();
        account.setAccountType("POUPANCA");
        account.setClientCpf("62368887016");
        var accountNbrResponse = new AccountNbrResponse(1234567L);
        doReturn(accountNbrResponse).when(accountService).createAccount(account);
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(account);
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON) // or other expected type
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenNullAccountRequest_whenAddingAccount_thenThrowException() throws Exception {
        AccountRequest accountRequest = null;
        doThrow(NullPointerException.class).when(accountService).createAccount(accountRequest);
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(accountRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON) // or other expected type
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenValidAccountNumber_whenGetAccountBalance_thenOk() throws Exception {
        var accountNbr = 123456L;
        Double accountBalance = 0.0;
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        Map<String, Double> map = Map.of("currentBalance", accountBalance);
        var projection = factory.createProjection(AccountBalanceProjection.class, map);
        doReturn(projection).when(accountService).getAccountBalance(accountNbr);
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{id}/balance",String.valueOf(accountNbr))
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk());
    }

    @Test
    public void givenInvalidValidAccountNumber_whenGetAccountBalance_thenOk() throws Exception {
        var accountNbr = "abc";
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{id}/balance",accountNbr)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void givenNullResult_whenGetAccountBalance_thenNotFound() throws Exception {
        var accountNbr = 123456L;
        doReturn(null).when(accountService).getAccountBalance(accountNbr);
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{id}/balance",accountNbr)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isNotFound());
    }

}
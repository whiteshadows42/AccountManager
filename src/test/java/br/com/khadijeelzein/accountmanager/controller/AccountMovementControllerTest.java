package br.com.khadijeelzein.accountmanager.controller;

import br.com.khadijeelzein.accountmanager.dto.AccountMovementRequest;
import br.com.khadijeelzein.accountmanager.dto.AccountMovementResponse;
import br.com.khadijeelzein.accountmanager.enums.MovementTypeEnum;
import br.com.khadijeelzein.accountmanager.service.AccountMovementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountMovementController.class)
@AutoConfigureMockMvc
public class AccountMovementControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountMovementService accountMovementService;


    @Test
    public void givenValidAccountMovementRequest_whenAddingAccountMovement_thenOK() throws Exception {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        doNothing().when(accountMovementService).accountTransfer(accountMovementRequest);
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(accountMovementRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
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
        doThrow(IllegalArgumentException.class).when(accountMovementService).accountTransfer(accountMovementRequest);
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(accountMovementRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
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
        var accountMovementResponse = AccountMovementResponse.builder()
                .accountDestination(123456L)
                .accountOrigin(1234567L)
                .type(MovementTypeEnum.TRANSFERENCIA.name())
                .amount(10.0d)
                .dateTime(LocalDateTime.now())
                .build();
        var accountMovements = List.of(accountMovementResponse);
        var page = new PageImpl<>(accountMovements);
        doReturn(page).when(accountMovementService).accountTransferHistory(any(), any(), any(),any());
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{id}/transactions",accountNbr)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .param("page", "1")
                        .param("size", "5")
                .accept(MediaType.parseMediaType("application/json")))
               .andExpect(status().isOk());
    }

    @Test
    public void givenNullAccountNbr_whenFindingAccountMovementHistory_thenThrowsException() throws Exception {
        String accountNbr = null;
        var startDate = LocalDate.now().minusDays(1);
        var endDate = LocalDate.now().plusDays(1);
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{id}/transactions",accountNbr)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenValidAccountNameTypeRequest_whenAddingAccount_thenOk() throws Exception {
        AccountMovementRequest accountMovement = new AccountMovementRequest();
        accountMovement.setType("Transferência");
        accountMovement.setAmount(10.0d);
        accountMovement.setAccountNbrDestination(123456L);
        accountMovement.setAccountNbrOrigin(1234567L);
        doNothing().when(accountMovementService).accountTransfer(accountMovement);
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(accountMovement);
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON) // or other expected type
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk());
    }

    @Test
    public void givenValidAccountTypeRequest_whenAddingAccount_thenOk() throws Exception {
        AccountMovementRequest accountMovement = new AccountMovementRequest();
        accountMovement.setType("Transferencia");
        accountMovement.setAmount(10.0d);
        accountMovement.setAccountNbrDestination(123456L);
        accountMovement.setAccountNbrOrigin(1234567L);
        doNothing().when(accountMovementService).accountTransfer(accountMovement);
        ObjectMapper mapper = new ObjectMapper();
        String inputInJson = mapper.writeValueAsString(accountMovement);
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON) // or other expected type
                        .content(inputInJson)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNullStartDate_whenFindingAccountMovementHistory_thenOk() throws Exception {
        var accountNbr = String.valueOf(123456L);
        var endDate = LocalDate.now().plusDays(1);
        var accountMovementResponse = AccountMovementResponse.builder()
                .accountDestination(123456L)
                .accountOrigin(1234567L)
                .type(MovementTypeEnum.TRANSFERENCIA.name())
                .amount(10.0d)
                .dateTime(LocalDateTime.now())
                .build();
        var accountMovements = List.of(accountMovementResponse);
        var page = new PageImpl<>(accountMovements);
        doReturn(page).when(accountMovementService).accountTransferHistory(any(), any(), any(),any());
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{id}/transactions",accountNbr)
                        .param("endDate", endDate.toString())
                .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNullEndDate_whenFindingAccountMovementHistory_thenOk() throws Exception {
        var accountNbr = String.valueOf(123456L);
        var startDate = LocalDate.now().minusDays(1);
        var accountMovementResponse = AccountMovementResponse.builder()
                .accountDestination(123456L)
                .accountOrigin(1234567L)
                .type(MovementTypeEnum.TRANSFERENCIA.name())
                .amount(10.0d)
                .dateTime(LocalDateTime.now())
                .build();
        var accountMovements = List.of(accountMovementResponse);
        var page = new PageImpl<>(accountMovements);
        doReturn(page).when(accountMovementService).accountTransferHistory(any(), any(), any(),any());
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{id}/transactions",accountNbr)
                        .param("startDate", startDate.toString())
                .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk());
    }
    @Test
    public void givenNullResult_whenGetAccountMovementHistory_thenNotFound() throws Exception {
        var accountNbr = "123456L";
        var endDate = LocalDate.now().plusDays(1);
        var startDate = LocalDate.now().minusDays(1);
        var pageable = PageRequest.of(1, 10);
        doReturn(null).when(accountMovementService).accountTransferHistory(accountNbr,startDate,endDate,pageable);
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{id}/transactions",accountNbr)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenEmptyPageResult_whenGetAccountMovementHistory_thenNotFound() throws Exception {
        var accountNbr = "123456L";
        var endDate = LocalDate.now().plusDays(1);
        var startDate = LocalDate.now().minusDays(1);
        var pageable = PageRequest.of(1, 10);
        doReturn(new PageImpl<>(List.of())).when(accountMovementService).accountTransferHistory(accountNbr,startDate,endDate,pageable);
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{id}/transactions",accountNbr)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isNotFound());
    }
}
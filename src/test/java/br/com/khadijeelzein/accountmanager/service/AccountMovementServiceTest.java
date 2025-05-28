package br.com.khadijeelzein.accountmanager.service;

import br.com.khadijeelzein.accountmanager.dto.AccountMovementRequest;
import br.com.khadijeelzein.accountmanager.dto.AccountBalanceProjection;
import br.com.khadijeelzein.accountmanager.dto.AccountRequest;
import br.com.khadijeelzein.accountmanager.enums.MovementTypeEnum;
import br.com.khadijeelzein.accountmanager.enums.TypeEnum;
import br.com.khadijeelzein.accountmanager.mapper.AccountMovementMapper;
import br.com.khadijeelzein.accountmanager.model.Account;
import br.com.khadijeelzein.accountmanager.model.AccountMovement;
import br.com.khadijeelzein.accountmanager.model.Client;
import br.com.khadijeelzein.accountmanager.repository.AccountMovementRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension .class)
public class AccountMovementServiceTest {
    @Mock
    private AccountMovementRepository accountMovementRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountMovementService accountMovementService;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void givenValidAccountMovementRequest_whenAddingAccountMovement_thenCallRepository()  {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        AccountMovement accountMovement = AccountMovementMapper.toAccountMovement(accountMovementRequest);
        doReturn(accountMovement).when(accountMovementRepository).save(any(AccountMovement.class));
        Double accountBalance = 0.0;
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        Map<String, Double> map = Map.of("currentBalance", accountBalance);
        var projection = factory.createProjection(AccountBalanceProjection.class, map);
        doReturn(projection).when(accountService).getAccountBalance(any());
        doReturn(true).when(accountService).accountExists(accountMovementRequest.getAccountNbrOrigin());
        doReturn(true).when(accountService).accountExists(accountMovementRequest.getAccountNbrDestination());
        accountMovementService.accountTransfer(accountMovementRequest);
        verify(accountMovementRepository, times(1)).save(any(AccountMovement.class));
    }

    @Test
    public void givenInvalidAccountOrigin_whenAddingAccountMovement_thenThrowException() {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(-1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        Set<ConstraintViolation<AccountMovementRequest>> violations = validator.validate(accountMovementRequest);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void givenInvalidAccountDestination_whenAddingAccountMovement_thenThrowException() {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(-123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        Set<ConstraintViolation<AccountMovementRequest>> violations = validator.validate(accountMovementRequest);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void givenInvalidAmount_whenAddingAccountMovement_thenThrowException() {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(-10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        Set<ConstraintViolation<AccountMovementRequest>> violations = validator.validate(accountMovementRequest);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void givenInvalidType_whenAddingAccountMovement_thenThrowException() {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setType("Inválido");
        accountMovementRequest.setAmount(10d);
        assertThrows(IllegalArgumentException.class,() -> accountMovementService.accountTransfer(accountMovementRequest));
    }

    @Test
    public void givenNullAccountOrigin_whenAddingAccountMovement_thenConstraintViolation(){
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(null);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        Set<ConstraintViolation<AccountMovementRequest>> violations = validator.validate(accountMovementRequest);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void givenNullAccountDestination_whenAddingAccountMovement_thenConstraintViolation(){
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(null);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        Set<ConstraintViolation<AccountMovementRequest>> violations = validator.validate(accountMovementRequest);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void givenNullAmount_whenAddingAccountMovement_thenConstraintViolation(){
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(null);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        Set<ConstraintViolation<AccountMovementRequest>> violations = validator.validate(accountMovementRequest);
        assertThat(violations.size()).isEqualTo(1);
    }
    @Test
    public void givenNullType_whenAddingAccountMovement_thenConstraintViolation(){
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(null);
        Set<ConstraintViolation<AccountMovementRequest>> violations = validator.validate(accountMovementRequest);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void givenSameAccounts_whenAddingAccountMovement_thenThrowException(){
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(1234567L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        assertThrows(IllegalArgumentException.class,() -> accountMovementService.accountTransfer(accountMovementRequest));
    }

    @Test
    public void givenValidAccountMovementRequest_whenFindingAccountMovementHistory_thenCallRepository() {
        var accountNbr = String.valueOf(123456L);
        var startDate = LocalDate.now().minusDays(1);
        var endDate = LocalDate.now().plusDays(1);
        var accountMovement = AccountMovement.builder()
                .accountOrigin(Long.parseLong(accountNbr))
                .accountDestination(1234567L)
                .amount(10d)
                .id("1a")
                .type(MovementTypeEnum.TRANSFERENCIA)
                .dateTime(LocalDateTime.now())
                .build();
        var accountMovements = List.of(accountMovement);
        var accountMovementsResponse = AccountMovementMapper.toAccountMovementResponseList(accountMovements);
        Pageable pageable = PageRequest.of(1, 10);
        var accountMovementsPage = new PageImpl<>(accountMovementsResponse);
        doReturn(accountMovementsPage).when(accountMovementRepository).findAllByDateAndAccountOriginOrAccountDestination(
                startDate,
                endDate,
                Long.parseLong(accountNbr),
                pageable);
        var response = accountMovementService.accountTransferHistory(accountNbr,startDate,endDate,pageable);
        verify(accountMovementRepository, times(1))
                .findAllByDateAndAccountOriginOrAccountDestination(any(),any(),any(),any());
        assertThat(response).isNotNull();
        assertThat(response.getNumberOfElements()).isEqualTo(1);
        var listAccountMovements = AccountMovementMapper.toAccountMovementResponseList(accountMovements);
        assertEquals(listAccountMovements,response.getContent());
    }

    @Test
    public void givenNonPositiveAccountNbr_whenFindingAccountMovementHistory_thenThrowsException() {
        var accountNbr = String.valueOf(-123456L);
        var startDate = LocalDate.now().minusDays(1);
        var endDate = LocalDate.now().plusDays(1);
        Pageable pageable = PageRequest.of(1, 10);
        assertThrows(IllegalArgumentException.class,()-> accountMovementService.accountTransferHistory(accountNbr,startDate,endDate,pageable));
    }

    @Test
    public void givenInvalidAccountNbr_whenFindingAccountMovementHistory_thenThrowsException()  {
        var accountNbr = "abcdefghij";
        var startDate = LocalDate.now().minusDays(1);
        var endDate = LocalDate.now().plusDays(1);
        var pageable = PageRequest.of(1, 10);
        assertThrows(IllegalArgumentException.class,()-> accountMovementService.accountTransferHistory(accountNbr,startDate,endDate,pageable));
    }

    @Test
    public void givenInvalidDates_whenFindingAccountMovementHistory_thenThrowsException()  {
        var accountNbr = String.valueOf(123456L);
        var startDate = LocalDate.now().plusDays(1);
        var endDate = LocalDate.now().minusDays(1);
        var pageable = PageRequest.of(1, 10);
        assertThrows(IllegalArgumentException.class,()-> accountMovementService.accountTransferHistory(accountNbr,startDate,endDate,pageable));

    }

    @Test
    public void givenNullAccountNbr_whenFindingAccountMovementHistory_thenThrowsException(){
        var startDate = LocalDate.now().minusDays(1);
        var endDate = LocalDate.now().plusDays(1);
        var pageable = PageRequest.of(1, 10);
        assertThrows(IllegalArgumentException.class,()-> accountMovementService.accountTransferHistory(null,startDate,endDate,pageable));

    }
    @Test
    public void givenInexistentAccountsWhenAddingAccountMovement_thenThrowsException() {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        doReturn(false).when(accountService).accountExists(accountMovementRequest.getAccountNbrOrigin());
        doReturn(false).when(accountService).accountExists(accountMovementRequest.getAccountNbrDestination());
        assertThrows(IllegalArgumentException.class,()-> accountMovementService.accountTransfer(accountMovementRequest));
    }
    @Test
    public void givenInexistentAccountOriginWhenAddingAccountMovement_thenThrowsException(){
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        doReturn(false).when(accountService).accountExists(accountMovementRequest.getAccountNbrOrigin());
        doReturn(true).when(accountService).accountExists(accountMovementRequest.getAccountNbrDestination());
        assertThrows(IllegalArgumentException.class,()-> accountMovementService.accountTransfer(accountMovementRequest));
    }

    @Test
    public void givenInexistentAccountDestinationWhenAddingAccountMovement_thenThrowsException() {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        doReturn(true).when(accountService).accountExists(accountMovementRequest.getAccountNbrOrigin());
        doReturn(false).when(accountService).accountExists(accountMovementRequest.getAccountNbrDestination());
        assertThrows(IllegalArgumentException.class,()-> accountMovementService.accountTransfer(accountMovementRequest));
    }
    @Test
    public void givenStartDateAfterEndDate_thenThrowsException() {
        String accountNbr = "123456";
        var startDate = LocalDate.now().plusDays(1);
        var endDate = LocalDate.now().minusDays(1);
        var pageable = PageRequest.of(1, 10);
        assertThrows(IllegalArgumentException.class,()-> accountMovementService.accountTransferHistory(accountNbr,startDate,endDate,pageable));
    }
    @Test
    public void givenStartDateAfterToday_thenThrowsException() {
        String accountNbr = "123456";
        var startDate = LocalDate.now().plusDays(1);
        var pageable = PageRequest.of(1, 10);
        assertThrows(IllegalArgumentException.class,()-> accountMovementService.accountTransferHistory(accountNbr,startDate,null,pageable));
    }

    @Test
    public void givenDatesNull_thenOk() {
        var accountNbr = String.valueOf(123456L);
        var accountMovement = AccountMovement.builder()
                .accountOrigin(Long.parseLong(accountNbr))
                .accountDestination(1234567L)
                .amount(10d)
                .id("1a")
                .type(MovementTypeEnum.TRANSFERENCIA)
                .dateTime(LocalDateTime.now())
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        var accountMovements = List.of(accountMovement);
        var accountMovementsResponse = AccountMovementMapper.toAccountMovementResponseList(accountMovements);
        var accountMovementsPage = new PageImpl<>(accountMovementsResponse);
        doReturn(accountMovementsPage).when(accountMovementRepository).findAllByDateAndAccountOriginOrAccountDestination(
                null,
                null,
                Long.parseLong(accountNbr),
                pageable);
        var response = accountMovementService.accountTransferHistory(accountNbr,null,null,pageable);
        verify(accountMovementRepository, times(1))
                .findAllByDateAndAccountOriginOrAccountDestination(any(),any(),any(),any());
        assertThat(response).isNotNull();
        assertThat(response.getNumberOfElements()).isEqualTo(1);
        var listAccountMovements = AccountMovementMapper.toAccountMovementResponseList(accountMovements);
        assertEquals(listAccountMovements,response.getContent());
    }

    @Test
    public void givenStartDateNull_thenOk() {
        var accountNbr = String.valueOf(123456L);
        var endDate = LocalDate.now().plusDays(1);
        var accountMovement = AccountMovement.builder()
                .accountOrigin(Long.parseLong(accountNbr))
                .accountDestination(1234567L)
                .amount(10d)
                .id("1a")
                .type(MovementTypeEnum.TRANSFERENCIA)
                .dateTime(LocalDateTime.now())
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        var accountMovements = List.of(accountMovement);
        var accountMovementsResponse = AccountMovementMapper.toAccountMovementResponseList(accountMovements);
        var accountMovementsPage = new PageImpl<>(accountMovementsResponse);
        doReturn(accountMovementsPage).when(accountMovementRepository).findAllByDateAndAccountOriginOrAccountDestination(
                null,
                endDate,
                Long.parseLong(accountNbr),
                pageable);
         var response = accountMovementService.accountTransferHistory(
                accountNbr,null,endDate,pageable);
        verify(accountMovementRepository, times(1))
                .findAllByDateAndAccountOriginOrAccountDestination(any(),any(),any(),any());
        assertThat(response).isNotNull();
        assertThat(response.getNumberOfElements()).isEqualTo(1);
        var listAccountMovementsResponse = AccountMovementMapper.toAccountMovementResponseList(accountMovements);
        assertEquals(listAccountMovementsResponse,response.getContent());
    }

    @Test
    public void givenEndDateNull_thenOk() {
        var accountNbr = String.valueOf(123456L);
        var startDate = LocalDate.now().minusDays(1);
        var accountMovement = AccountMovement.builder()
                .accountOrigin(Long.parseLong(accountNbr))
                .accountDestination(1234567L)
                .amount(10d)
                .id("1a")
                .type(MovementTypeEnum.TRANSFERENCIA)
                .dateTime(LocalDateTime.now())
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        var accountMovements = List.of(accountMovement);
        var accountMovementsResponse = AccountMovementMapper.toAccountMovementResponseList(accountMovements);
        var accountMovementsPage = new PageImpl<>(accountMovementsResponse);
        doReturn(accountMovementsPage).when(accountMovementRepository).findAllByDateAndAccountOriginOrAccountDestination(
                startDate,
                null,
                Long.parseLong(accountNbr),
                pageable);
        var response = accountMovementService.accountTransferHistory(
                accountNbr,startDate,null,pageable);
        verify(accountMovementRepository, times(1))
                .findAllByDateAndAccountOriginOrAccountDestination(any(),any(),any(),any(Pageable.class));
        assertThat(response).isNotNull();
        assertThat(response.getNumberOfElements()).isEqualTo(1);
        var listAccountMovements = AccountMovementMapper.toAccountMovementResponseList(accountMovements);
        assertEquals(listAccountMovements,response.getContent());
    }

    @Test
    public void givenInvalidType_whenAddingAccount_thenThrowException() {
        var accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setType("Inválido");
        accountMovementRequest.setAmount(10.0d);
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        assertThrows(IllegalArgumentException.class, () ->accountMovementService.accountTransfer(accountMovementRequest));
    }

    @Test
    public void givenValidTypeName_whenAddingAccount_thenCreateAccount() {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType("TRANSFERÊNCIA");
        AccountMovement accountMovement = AccountMovementMapper.toAccountMovement(accountMovementRequest);
        doReturn(accountMovement).when(accountMovementRepository).save(any(AccountMovement.class));
        Double accountBalance = 0.0;
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        Map<String, Double> map = Map.of("currentBalance", accountBalance);
        var projection = factory.createProjection(AccountBalanceProjection.class, map);
        doReturn(projection).when(accountService).getAccountBalance(any());
        doReturn(true).when(accountService).accountExists(accountMovementRequest.getAccountNbrOrigin());
        doReturn(true).when(accountService).accountExists(accountMovementRequest.getAccountNbrDestination());
        accountMovementService.accountTransfer(accountMovementRequest);
        verify(accountMovementRepository, times(1)).save(any(AccountMovement.class));
    }
    @Test
    public void givenValidType_whenAddingAccount_thenCreateAccount() {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType("transferencia");
        AccountMovement accountMovement = AccountMovementMapper.toAccountMovement(accountMovementRequest);
        doReturn(accountMovement).when(accountMovementRepository).save(any(AccountMovement.class));
        Double accountBalance = 0.0;
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        Map<String, Double> map = Map.of("currentBalance", accountBalance);
        var projection = factory.createProjection(AccountBalanceProjection.class, map);
        doReturn(projection).when(accountService).getAccountBalance(any());
        doReturn(true).when(accountService).accountExists(accountMovementRequest.getAccountNbrOrigin());
        doReturn(true).when(accountService).accountExists(accountMovementRequest.getAccountNbrDestination());
        accountMovementService.accountTransfer(accountMovementRequest);
        verify(accountMovementRepository, times(1)).save(any(AccountMovement.class));
    }

    @Test
    public void givenNullType_whenAddingAccount_thenThrowException() {
        var accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(null);
        assertThrows(IllegalArgumentException.class, () ->accountMovementService.accountTransfer(accountMovementRequest));
    }
    @Test
    public void givenNegativeAccountNbrDestination_whenAddingAccount_thenThrowException() {
        var accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(-123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.getType());
        assertThrows(IllegalArgumentException.class, () ->accountMovementService.accountTransfer(accountMovementRequest));
    }
    @Test
    public void givenNegativeAccountNbrOrigin_whenAddingAccount_thenThrowException() {
        var accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(-10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.getType());
        assertThrows(IllegalArgumentException.class, () ->accountMovementService.accountTransfer(accountMovementRequest));
    }

    @Test
    public void givenNullAmount_whenAddingAccount_thenThrowException() {
        var accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(null);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.getType());
        assertThrows(IllegalArgumentException.class, () ->accountMovementService.accountTransfer(accountMovementRequest));
    }
    @Test
    public void givenNegativeAmount_whenAddingAccount_thenThrowException() {
        var accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(-10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.getType());
        assertThrows(IllegalArgumentException.class, () ->accountMovementService.accountTransfer(accountMovementRequest));
    }
    @Test
    public void givenZeroAmount_whenAddingAccount_thenThrowException() {
        var accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(0d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.getType());
        assertThrows(IllegalArgumentException.class, () ->accountMovementService.accountTransfer(accountMovementRequest));
    }
}                                                                                                
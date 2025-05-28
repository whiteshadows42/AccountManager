package br.com.khadijeelzein.accountmanager.repository;

import br.com.khadijeelzein.accountmanager.dto.AccountMovementRequest;
import br.com.khadijeelzein.accountmanager.dto.AccountMovementResponse;
import br.com.khadijeelzein.accountmanager.enums.MovementTypeEnum;
import br.com.khadijeelzein.accountmanager.mapper.AccountMovementMapper;
import br.com.khadijeelzein.accountmanager.model.AccountMovement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class AccountMovementRepositoryTest {

    @Mock
    private AccountMovementRepository accountMovementRepository;


    @Test
    public void givenValidAccountMovement_thenAddAccountMovement(){
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        AccountMovement accountMovement = AccountMovementMapper.toAccountMovement(accountMovementRequest);
        when(accountMovementRepository.save(accountMovement)).thenReturn(accountMovement);
        accountMovementRepository.save(accountMovement);
        verify(accountMovementRepository, times(1)).save(any(AccountMovement.class));
        var savedAccountMovement = accountMovementRepository.save(accountMovement);
        assertNotNull(savedAccountMovement);
        assertEquals(1234567L, savedAccountMovement.getAccountOrigin());
        assertEquals(123456L, savedAccountMovement.getAccountDestination());
        assertEquals(10d, savedAccountMovement.getAmount());
        assertEquals(MovementTypeEnum.TRANSFERENCIA, savedAccountMovement.getType());
    }

    @Test
    public void givenInvalidAccountOrigin_whenAddingAccountMovement_thenThrowException() {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(-1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        AccountMovement accountMovement = AccountMovementMapper.toAccountMovement(accountMovementRequest);
        when(accountMovementRepository.save(accountMovement)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> accountMovementRepository.save(accountMovement));
    }

    @Test
    public void givenInvalidAccountDestination_whenAddingAccountMovement_thenThrowException() {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(-123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        AccountMovement accountMovement = AccountMovementMapper.toAccountMovement(accountMovementRequest);
        when(accountMovementRepository.save(accountMovement)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> accountMovementRepository.save(accountMovement));
    }

    @Test
    public void givenInvalidAmount_whenAddingAccountMovement_thenThrowException() {
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(-10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        AccountMovement accountMovement = AccountMovementMapper.toAccountMovement(accountMovementRequest);
        when(accountMovementRepository.save(accountMovement)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> accountMovementRepository.save(accountMovement));
    }


    @Test
    public void givenNullAccountOrigin_whenAddingAccountMovement_thenConstraintViolation(){
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(null);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        AccountMovement accountMovement = AccountMovementMapper.toAccountMovement(accountMovementRequest);
        when(accountMovementRepository.save(accountMovement)).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> accountMovementRepository.save(accountMovement));
    }

    @Test
    public void givenNullAccountDestination_whenAddingAccountMovement_thenConstraintViolation(){
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(null);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        AccountMovement accountMovement = AccountMovementMapper.toAccountMovement(accountMovementRequest);
        when(accountMovementRepository.save(accountMovement)).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> accountMovementRepository.save(accountMovement));
    }

    @Test
    public void givenNullAmount_whenAddingAccountMovement_thenThrowsException(){
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(null);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        AccountMovement accountMovement = AccountMovementMapper.toAccountMovement(accountMovementRequest);
        when(accountMovementRepository.save(accountMovement)).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> accountMovementRepository.save(accountMovement));
    }

    @Test
    public void givenValidAccountMovementRequest_whenFindingAccountMovementHistory_thenReturnAccount() {
        var accountNbr = String.valueOf(123456L);
        var startDate = LocalDate.now().minusDays(1);
        var endDate = LocalDate.now().plusDays(1);
        var accountMovementResponse = AccountMovementResponse
                .builder()
                .accountDestination(1234567L)
                .accountOrigin(Long.parseLong(accountNbr))
                .amount(10d)
                .dateTime(LocalDateTime.now())
                .type(MovementTypeEnum.TRANSFERENCIA.getType())
                .build();
        var accountMovements = List.of(accountMovementResponse);
        var pageable = PageRequest.of(0, 10);
        var page = new PageImpl<>(accountMovements, pageable, accountMovements.size());
        doReturn(page).when(accountMovementRepository)
                .findAllByDateAndAccountOriginOrAccountDestination(
                startDate,
                endDate,
                Long.parseLong(accountNbr),pageable);
        var response = accountMovementRepository.findAllByDateAndAccountOriginOrAccountDestination(
                startDate,
                endDate,
                Long.parseLong(accountNbr),pageable);
        verify(accountMovementRepository, times(1))
                .findAllByDateAndAccountOriginOrAccountDestination(any(),any(),any(),any());
        assertThat(response).isNotNull();
        assertThat(response.getNumberOfElements()).isEqualTo(1);
        assertEquals(accountMovements,response.getContent());
    }

    @Test
    public void givenNonPositiveAccountNbr_whenFindingAccountMovementHistory_thenThrowsException() {
        var accountNbr = String.valueOf(-123456L);
        var startDate = LocalDate.now().minusDays(1);
        var endDate = LocalDate.now().plusDays(1);
        var pageable = PageRequest.of(0, 10);
        when(accountMovementRepository.findAllByDateAndAccountOriginOrAccountDestination(
                startDate,
                endDate,
                Long.parseLong(accountNbr),
                pageable)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () ->
                accountMovementRepository.findAllByDateAndAccountOriginOrAccountDestination(
                        startDate,
                        endDate,
                        Long.parseLong(accountNbr),pageable));
    }

    @Test
    public void givenInvalidAccountNbr_whenFindingAccountMovementHistory_thenThrowsException() {
        var accountNbr = "abcdefghij";
        var startDate = LocalDate.now().minusDays(1);
        var endDate = LocalDate.now().plusDays(1);
        var pageable = PageRequest.of(0, 10);
        assertThrows(IllegalArgumentException.class, () ->
                accountMovementRepository.findAllByDateAndAccountOriginOrAccountDestination(
                        startDate,
                        endDate,
                        Long.parseLong(accountNbr),pageable));
    }

    @Test
    public void givenInvalidDates_whenFindingAccountMovementHistory_thenThrowsException() {
        var accountNbr = String.valueOf(123456L);
        var startDate = LocalDate.now().plusDays(1);
        var endDate = LocalDate.now().minusDays(1);
        var pageable = PageRequest.of(0, 10);
        when(accountMovementRepository.findAllByDateAndAccountOriginOrAccountDestination(
                startDate,
                endDate,
                Long.parseLong(accountNbr),pageable)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () ->
                accountMovementRepository.findAllByDateAndAccountOriginOrAccountDestination(
                        startDate,
                        endDate,
                        Long.parseLong(accountNbr),pageable));
    }
    @Test
    public void givenNullAccountNbr_whenFindingAccountMovementHistory_thenThrowsException() {
        var startDate = LocalDate.now().minusDays(1);
        var endDate = LocalDate.now().plusDays(1);
        var pageable = PageRequest.of(0, 10);
        assertThrows(IllegalArgumentException.class, () ->
                accountMovementRepository.findAllByDateAndAccountOriginOrAccountDestination(
                        startDate,
                        endDate,
                        Long.parseLong(null),pageable));
    }

    @Test
    public void givenStartDateNull_whenFindingAccountMovementHistory_thenReturnAccount() {
        var accountNbr = String.valueOf(123456L);
        var endDate = LocalDate.now().plusDays(1);
        var accountMovement = AccountMovementResponse.builder()
                .accountOrigin(Long.parseLong(accountNbr))
                .accountDestination(1234567L)
                .amount(10d)
                .type(MovementTypeEnum.TRANSFERENCIA.getType())
                .dateTime(LocalDateTime.now())
                .build();
        var accountMovements = List.of(accountMovement);
        var page = new PageImpl<>(accountMovements);
        var pageable = PageRequest.of(0, 10);
        doReturn(page).when(accountMovementRepository)
                .findAllByDateAndAccountOriginOrAccountDestination(
                        null,
                        endDate,
                        Long.parseLong(accountNbr),pageable);
        var response = accountMovementRepository.findAllByDateAndAccountOriginOrAccountDestination(
                null,
                endDate,
                Long.parseLong(accountNbr),pageable);
        verify(accountMovementRepository, times(1))
                .findAllByDateAndAccountOriginOrAccountDestination(any(),any(),any(),any());
        assertThat(response).isNotNull();
        assertThat(response.getNumberOfElements()).isEqualTo(1);
        assertEquals(accountMovements,response.getContent());
    }
    @Test
    public void givenEndDateNull_whenFindingAccountMovementHistory_thenReturnAccount() {
        var accountNbr = String.valueOf(123456L);
        var startDate= LocalDate.now().minusDays(1);
        var accountMovement = AccountMovementResponse.builder()
                .accountOrigin(Long.parseLong(accountNbr))
                .accountDestination(1234567L)
                .amount(10d)
                .type(MovementTypeEnum.TRANSFERENCIA.getType())
                .dateTime(LocalDateTime.now())
                .build();
        var accountMovements = List.of(accountMovement);
        var page = new PageImpl<>(accountMovements);
        var pageable = PageRequest.of(0, 10);
        doReturn(page).when(accountMovementRepository)
                .findAllByDateAndAccountOriginOrAccountDestination(
                        startDate,
                        null,
                        Long.parseLong(accountNbr),pageable);
        var response = accountMovementRepository.findAllByDateAndAccountOriginOrAccountDestination(
                startDate,
                null,
                Long.parseLong(accountNbr),pageable);
        verify(accountMovementRepository, times(1))
                .findAllByDateAndAccountOriginOrAccountDestination(any(),any(),any(),any());
        assertThat(response).isNotNull();
        assertThat(response.getNumberOfElements()).isEqualTo(1);
        assertEquals(accountMovements,response.getContent());
    }

    @Test
    public void givenStartDateAfterToday_whenFindingAccountMovementHistory_thenReturnAccount() {
        var accountNbr = 123456L;
        var endDate = LocalDate.now().plusDays(2);
        var startDate = LocalDate.now().plusDays(1);
        var pageable = PageRequest.of(0, 10);
        when(accountMovementRepository
                .findAllByDateAndAccountOriginOrAccountDestination(
                        startDate,endDate,accountNbr,pageable)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class,
                () -> accountMovementRepository.
                        findAllByDateAndAccountOriginOrAccountDestination(startDate,endDate,accountNbr,pageable));
    }

}
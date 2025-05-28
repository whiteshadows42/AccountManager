package br.com.khadijeelzein.accountmanager.repository;

import br.com.khadijeelzein.accountmanager.dto.AccountBalanceProjection;
import br.com.khadijeelzein.accountmanager.enums.TypeEnum;
import br.com.khadijeelzein.accountmanager.model.Account;
import br.com.khadijeelzein.accountmanager.model.Client;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountRepositoryTest {
    @Mock
    private AccountRepository accountRepository;

    @Test
    public void givenAccountIsValid_ThenAddAccount() {
        var client = Client.builder()
                .cpf("62368887016")
                .id("1a")
                .birthday(LocalDate.of(1996, 4, 1))
                .name("John Doe")
                .build();
        var account = Account.builder()
                .accountNbr(123456L)
                .id("1a")
                .type(TypeEnum.CORRENTE)
                .client(client)
                .currentBalance(0.0d)
                .build();
        when(accountRepository.save(account)).thenReturn(account);
        var savedAccount = accountRepository.save(account);
        assertNotNull(savedAccount);
        assertEquals(123456L, savedAccount.getAccountNbr());
        assertEquals(TypeEnum.CORRENTE, savedAccount.getType());
        assertEquals(client, savedAccount.getClient());
        assertEquals(0.0d, savedAccount.getCurrentBalance());
        verify(accountRepository,times(1)).save(account);
    }

    @Test
    public void givenNullAccountClientRequest_whenAddingAccount_thenThrowException(){
        var account = Account.builder()
                .accountNbr(123456L)
                .id("1a")
                .type(TypeEnum.CORRENTE)
                .client(null)
                .currentBalance(0.0d)
                .build();
        when(accountRepository.save(account)).thenThrow(ConstraintViolationException.class);
        assertThrows(ConstraintViolationException.class, () -> accountRepository.save(account));
    }

    @Test
    public void givenClientFormattedCpfRequest_whenAddingAccount_theCallRepository() {
        var client = Client.builder()
                .cpf("623.688.870-16")
                .id("1a")
                .birthday(LocalDate.of(1996, 4, 1))
                .name("John Doe")
                .build();
        var account = Account.builder()
                .accountNbr(123456L)
                .id("1a")
                .type(TypeEnum.CORRENTE)
                .client(client)
                .currentBalance(0.0d)
                .build();
        when(accountRepository.save(account)).thenReturn(account);
        var savedAccount = accountRepository.save(account);
        assertNotNull(savedAccount);
        assertEquals(123456L, savedAccount.getAccountNbr());
        assertEquals(TypeEnum.CORRENTE, savedAccount.getType());
        assertEquals(client, savedAccount.getClient());
        assertEquals(0.0d, savedAccount.getCurrentBalance());
        verify(accountRepository,times(1)).save(account);
    }

    @Test
    public void givenNullAccountTypeRequest_whenAddingAccount_thenThrowException() {
        var client = Client.builder()
                .cpf("623.688.870-16")
                .id("1a")
                .birthday(LocalDate.of(1996, 4, 1))
                .name("John Doe")
                .build();
        var account = Account.builder()
                .accountNbr(123456L)
                .id("1a")
                .type(null)
                .client(client)
                .currentBalance(0.0d)
                .build();
        when(accountRepository.save(account)).thenThrow(ConstraintViolationException.class);
        assertThrows(ConstraintViolationException.class, () -> accountRepository.save(account));
    }

    @Test
    public void givenInvalidAccountClientRequest_whenAddingAccount_thenThrowException() {
        var client = Client.builder()
                .cpf("123456")
                .id("1a")
                .birthday(LocalDate.of(1996, 4, 1))
                .name("John Doe")
                .build();
        var account = Account.builder()
                .accountNbr(123456L)
                .id("1a")
                .type(null)
                .client(client)
                .currentBalance(0.0d)
                .build();
        when(accountRepository.save(account)).thenThrow(ConstraintViolationException.class);
        assertThrows(ConstraintViolationException.class, () -> accountRepository.save(account));
    }
    @Test
    public void givenNullAccountRequest_whenAddingAccount_thenThrowException() {
        when(accountRepository.save(null)).thenThrow(ConstraintViolationException.class);
        assertThrows(ConstraintViolationException.class, () -> accountRepository.save(null));
    }

    @Test
    public void givenValidAccountNumber_whenGetAccountBalance_thenCallRepository() {
        var accountNbr = 123456L;
        Double accountBalance = 0.0;
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        Map<String, Double> map = Map.of("currentBalance", accountBalance);
        var projection = factory.createProjection(AccountBalanceProjection.class, map);
        when(accountRepository.findCurrentBalanceByAccountNbr(accountNbr)).thenReturn(projection);
        var returnedProjection = accountRepository.findCurrentBalanceByAccountNbr(accountNbr);
        assertNotNull(returnedProjection);
        assertEquals(0.0d, returnedProjection.getCurrentBalance());
        verify(accountRepository,times(1)).findCurrentBalanceByAccountNbr(accountNbr);
    }

    @Test
    public void givenNullAccountNumber_whenGetAccountBalance_thenThrowException() {
        when(accountRepository.findCurrentBalanceByAccountNbr(null)).thenThrow(ConstraintViolationException.class);
        assertThrows(ConstraintViolationException.class, () -> accountRepository.findCurrentBalanceByAccountNbr(null));
    }

    @Test
    public void givenValidAccounts_whenUpdatingAccountBalance_thenCallRepository() {
        var clientResponse = Client.builder()
                .cpf("62368887016")
                .id("1a")
                .birthday(LocalDate.of(1996, 4, 1))
                .name("John Doe")
                .build();
        var account1 = Account.builder()
                .accountNbr(123456L)
                .id("1a")
                .type(TypeEnum.CORRENTE)
                .client(clientResponse)
                .currentBalance(10.0d)
                .build();
        var account2 = Account.builder()
                .accountNbr(1234567L)
                .id("2a")
                .type(TypeEnum.CORRENTE)
                .client(clientResponse)
                .currentBalance(0.0d)
                .build();
        var accounts = List.of(account1, account2);
        doNothing().when(accountRepository).updateAccountByAccountNbr(accounts);
        accountRepository.updateAccountByAccountNbr(accounts);
        verify(accountRepository, times(1)).updateAccountByAccountNbr(accounts);
    }

    @Test
    public void givenEmptyAccountsList_whenUpdatingAccountBalance_thenThrowException() {
        List<Account> accounts = new ArrayList<>();
        doThrow(ConstraintViolationException.class).when(accountRepository).updateAccountByAccountNbr(accounts);
        assertThrows(ConstraintViolationException.class, () -> accountRepository.updateAccountByAccountNbr(accounts));
    }

    @Test
    public void givenNullAccountsList_whenUpdatingAccountBalance_thenThrowException() {
        doThrow(ConstraintViolationException.class).when(accountRepository).updateAccountByAccountNbr(null);
        assertThrows(ConstraintViolationException.class, () ->accountRepository.updateAccountByAccountNbr(null));
    }
}

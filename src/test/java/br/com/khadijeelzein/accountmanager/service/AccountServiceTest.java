package br.com.khadijeelzein.accountmanager.service;

import br.com.khadijeelzein.accountmanager.dto.AccountBalanceProjection;
import br.com.khadijeelzein.accountmanager.dto.AccountRequest;
import br.com.khadijeelzein.accountmanager.enums.TypeEnum;
import br.com.khadijeelzein.accountmanager.model.Account;
import br.com.khadijeelzein.accountmanager.model.Client;
import br.com.khadijeelzein.accountmanager.repository.AccountRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private AccountService accountService;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    @Test
    public void givenNullAccountClientRequest_whenAddingAccount_thenThrowException() {
        AccountRequest account = new AccountRequest();
        account.setAccountType(TypeEnum.CORRENTE.getType());
        account.setClientCpf(null);
        Set<ConstraintViolation<AccountRequest>> violations = validator.validate(account);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void givenClientFormattedCpfRequest_whenAddingAccount_theCallRepository() {
        AccountRequest account = new AccountRequest();
        account.setAccountType(TypeEnum.CORRENTE.getType());
        account.setClientCpf("817.672.190-59");
        var clientResponse = Client.builder()
                .cpf("817.672.190-59")
                .id("1a")
                .birthday(LocalDate.of(1996, 4, 1))
                .name("John Doe")
                .build();
        var accountResponse = Account.builder()
                .accountNbr(123456L)
                .id("1a")
                .type(TypeEnum.CORRENTE)
                .client(clientResponse)
                .currentBalance(0.0d)
                .build();
        doReturn(accountResponse).when(accountRepository).save(any(Account.class));
        doReturn(true).when(clientService).existsClient("817.672.190-59");
        var accountNbr = accountService.createAccount(account);
        verify(accountRepository, times(1)).save(any(Account.class));
        assertThat(accountNbr.getAccountNbr()).isEqualTo(123456L);
    }

    @Test
    public void givenNullAccountTypeRequest_whenAddingAccount_thenThrowException() {
        AccountRequest account = new AccountRequest();
        account.setAccountType(null);
        account.setClientCpf("81767219059");
        assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(account));
    }

    @Test
    public void givenInvalidAccountClientRequest_whenAddingAccount_thenThrowException() {
        AccountRequest account = new AccountRequest();
        account.setAccountType(TypeEnum.CORRENTE.getType());
        account.setClientCpf("123456");
        Set<ConstraintViolation<AccountRequest>> violations = validator.validate(account);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void givenInvalidAccountTypeRequest_whenAddingAccount_thenThrowException() {
        AccountRequest account = new AccountRequest();
        account.setAccountType("INEXISTENTE");
        account.setClientCpf("123456");
        assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(account));
    }

    @Test
    public void givenValidAccountRequest_whenAddingAccount_thenCallRepository() {
        AccountRequest account = new AccountRequest();
        account.setAccountType(TypeEnum.CORRENTE.getType());
        account.setClientCpf("62368887016");
        var clientResponse = Client.builder()
                .cpf("62368887016")
                .id("1a")
                .birthday(LocalDate.of(1996, 4, 1))
                .name("John Doe")
                .build();
        var accountResponse = Account.builder()
                .accountNbr(123456L)
                .id("1a")
                .type(TypeEnum.CORRENTE)
                .client(clientResponse)
                .currentBalance(0.0d)
                .build();
        doReturn(accountResponse).when(accountRepository).save(any(Account.class));
        doReturn(true).when(clientService).existsClient("62368887016");
        var accountNbr = accountService.createAccount(account);
        verify(accountRepository, times(1)).save(any(Account.class));
        assertThat(accountNbr.getAccountNbr()).isEqualTo(123456L);
    }

    @Test
    public void givenNullAccountRequest_whenAddingAccount_thenThrowException() {
        assertThrows(NullPointerException.class, () -> accountService.createAccount(null));
    }

    @Test
    public void givenValidAccountNumber_whenGetAccountBalance_thenCallRepository() {
        var accountNbr = 123456L;
        Double accountBalance = 0.0;
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        Map<String, Double> map = Map.of("currentBalance", accountBalance);
        var projection = factory.createProjection(AccountBalanceProjection.class, map);
        doReturn(projection).when(accountRepository).findCurrentBalanceByAccountNbr(accountNbr);
        var accountBalanceProjection = accountService.getAccountBalance(accountNbr);
        verify(accountRepository,times(1)).findCurrentBalanceByAccountNbr(accountNbr);
        assertThat(accountBalanceProjection.getCurrentBalance()).isEqualTo(accountBalance);
    }

    @Test
    public void givenNullAccountNumber_whenGetAccountBalance_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () ->accountService.getAccountBalance(null));
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
        accountService.updateAllAccountBalance(accounts);
        verify(accountRepository, times(1)).updateAccountByAccountNbr(accounts);
    }

    @Test
    public void givenEmptyAccountsList_whenUpdatingAccountBalance_thenThrowException() {
        List<Account> accounts = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () ->accountService.updateAllAccountBalance(accounts));
    }

    @Test
    public void givenNullAccountsList_whenUpdatingAccountBalance_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () ->accountService.updateAllAccountBalance(null));
    }

    @Test
    public void givenInvalidType_whenAddingAccount_thenThrowException() {
        var accountRequest = new AccountRequest();
        accountRequest.setAccountType("Inválido");
        accountRequest.setClientCpf("62368887016");
        assertThrows(IllegalArgumentException.class, () ->accountService.createAccount(accountRequest));
    }

    @Test
    public void givenValidTypeName_whenAddingAccount_thenCreateAccount() {
        var accountRequest = new AccountRequest();
        accountRequest.setAccountType("POUPANÇA");
        accountRequest.setClientCpf("62368887016");
        var clientResponse = Client.builder()
                .cpf("817.672.190-59")
                .id("1a")
                .birthday(LocalDate.of(1996, 4, 1))
                .name("John Doe")
                .build();
        var accountResponse = Account.builder()
                .accountNbr(123456L)
                .id("1a")
                .type(TypeEnum.POUPANCA)
                .client(clientResponse)
                .currentBalance(0.0d)
                .build();
        doReturn(accountResponse).when(accountRepository).save(any(Account.class));
        doReturn(true).when(clientService).existsClient("62368887016");
        var accountNbr = accountService.createAccount(accountRequest);
        verify(accountRepository, times(1)).save(any(Account.class));
        assertThat(accountNbr.getAccountNbr()).isEqualTo(123456L);
    }
    @Test
    public void givenValidType_whenAddingAccount_thenCreateAccount() {
        var accountRequest = new AccountRequest();
        accountRequest.setAccountType("poupanca");
        accountRequest.setClientCpf("62368887016");
        var clientResponse = Client.builder()
                .cpf("62368887016")
                .id("1a")
                .birthday(LocalDate.of(1996, 4, 1))
                .name("John Doe")
                .build();
        var accountResponse = Account.builder()
                .accountNbr(123456L)
                .id("1a")
                .type(TypeEnum.valueOf(accountRequest.getAccountType().toUpperCase()))
                .client(clientResponse)
                .currentBalance(0.0d)
                .build();
        doReturn(accountResponse).when(accountRepository).save(any(Account.class));
        doReturn(true).when(clientService).existsClient("62368887016");
        var accountNbr = accountService.createAccount(accountRequest);
        verify(accountRepository, times(1)).save(any(Account.class));
        assertThat(accountNbr.getAccountNbr()).isEqualTo(123456L);
    }

    @Test
    public void givenNullType_whenAddingAccount_thenThrowException() {
        var accountRequest = new AccountRequest();
        accountRequest.setAccountType(null);
        accountRequest.setClientCpf("62368887016");
        assertThrows(IllegalArgumentException.class, () ->accountService.createAccount(accountRequest));
    }

    @Test
    public void givenNegativeAccountNbr_whenGettingAccountBalance_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () ->accountService.getAccountBalance(-123456L));
    }

    @Test
    public void givenNullAccountNbr_whenGettingAccountBalance_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () ->accountService.getAccountBalance(null));
    }

}

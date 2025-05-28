package br.com.khadijeelzein.accountmanager.repository;
import br.com.khadijeelzein.accountmanager.enums.TypeEnum;
import br.com.khadijeelzein.accountmanager.model.Account;
import br.com.khadijeelzein.accountmanager.model.Client;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountRepositoryIntegrationTest {
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
        accountRepository.deleteAll();
        clientRepository.deleteAll();
    }
    @Test
    public void givenAccountRepository_whenSaveAccount_thenOK() {
        Client client = Client.builder()
                .cpf("21920373098")
                .name("John Doe")
                .birthday(LocalDate.of(1996, 4, 1))
                .build();
        Client createdClient = clientRepository.save(client);
        Account account = Account.builder()
                .accountNbr(1234567L)
                .type(TypeEnum.CORRENTE)
                .currentBalance(0.0d)
                .client(createdClient)
                .build();
        Account createdAccount = accountRepository.save(account);
        assertNotNull(createdAccount);
        assertEquals(createdAccount.getAccountNbr(), account.getAccountNbr());
        assertNotNull(createdAccount.getId());
        assertEquals(createdAccount.getClient().getId(), createdClient.getId());
        assertEquals(createdAccount.getType(), account.getType());
        assertEquals(createdAccount.getCurrentBalance(), account.getCurrentBalance());
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
        var createdClient = clientRepository.save(client);
        var savedAccount = accountRepository.save(account);
        assertNotNull(savedAccount);
        assertEquals(savedAccount.getAccountNbr(), account.getAccountNbr());
        assertEquals(savedAccount.getType(), account.getType());
        assertEquals(savedAccount.getCurrentBalance(), account.getCurrentBalance());
        assertNotNull(savedAccount.getId());
        assertEquals(savedAccount.getClient().getId(), createdClient.getId());
    }

    @Test
    public void givenNullAccountRequest_whenAddingAccount_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> accountRepository.save(null));
    }

    @Test
    public void givenValidAccountNumber_whenGetAccountBalance_thenCallRepository() {
        var accountNbr = 123456L;
        Client client = Client.builder()
                .cpf("21920373098")
                .name("John Doe")
                .birthday(LocalDate.of(1996, 4, 1))
                .build();
        Client createdClient = clientRepository.save(client);
        Account account = Account.builder()
                .accountNbr(123456L)
                .type(TypeEnum.CORRENTE)
                .currentBalance(0.0d)
                .client(createdClient)
                .build();
        accountRepository.save(account);
        var returnedProjection = accountRepository.findCurrentBalanceByAccountNbr(accountNbr);
        assertNotNull(returnedProjection);
        assertEquals(0.0d, returnedProjection.getCurrentBalance());
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
        accountRepository.updateAccountByAccountNbr(accounts);
        var updatedAccount = accountRepository.findById(account1.getId());
        assertNotNull(updatedAccount);
        updatedAccount.ifPresent(
                account ->
                        assertEquals(account.getCurrentBalance(),
                                account1.getCurrentBalance()));
    }

    @Test
    public void givenNullAccountsList_whenUpdatingAccountBalance_thenThrowException() {
        assertThrows(NullPointerException.class, () ->accountRepository.updateAccountByAccountNbr(null));
    }
}
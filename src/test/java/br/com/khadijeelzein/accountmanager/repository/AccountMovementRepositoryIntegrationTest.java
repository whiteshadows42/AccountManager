package br.com.khadijeelzein.accountmanager.repository;

import br.com.khadijeelzein.accountmanager.AccountmanagerApplication;
import br.com.khadijeelzein.accountmanager.dto.AccountMovementRequest;
import br.com.khadijeelzein.accountmanager.enums.MovementTypeEnum;
import br.com.khadijeelzein.accountmanager.mapper.AccountMovementMapper;
import br.com.khadijeelzein.accountmanager.model.AccountMovement;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.PageRequest;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountMovementRepositoryIntegrationTest  {
    @Autowired
    private AccountMovementRepository accountMovementRepository;
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
        accountMovementRepository.deleteAll();
        accountRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    public void givenValidAccountMovement_thenAddAccountMovement(){
        AccountMovementRequest accountMovementRequest = new AccountMovementRequest();
        accountMovementRequest.setAccountNbrDestination(123456L);
        accountMovementRequest.setAccountNbrOrigin(1234567L);
        accountMovementRequest.setAmount(10d);
        accountMovementRequest.setType(MovementTypeEnum.TRANSFERENCIA.name());
        AccountMovement accountMovement = AccountMovementMapper.toAccountMovement(accountMovementRequest);
        var savedAccountMovement = accountMovementRepository.save(accountMovement);
        assertNotNull(savedAccountMovement);
        assertEquals(1234567L, savedAccountMovement.getAccountOrigin());
        assertEquals(123456L, savedAccountMovement.getAccountDestination());
        assertEquals(10d, savedAccountMovement.getAmount());
        assertEquals(MovementTypeEnum.TRANSFERENCIA, savedAccountMovement.getType());
    }

    @Test
    public void givenValidAccountMovementRequest_whenFindingAccountMovementHistory_thenReturnAccount() {
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
        var accountMovementResponse = accountMovementRepository.save(accountMovement);
        var accountMovements = List.of(accountMovementResponse);
        var pageable = PageRequest.of(0, 10);
        var response = accountMovementRepository.findAllByDateAndAccountOriginOrAccountDestination(
                startDate,
                endDate,
                Long.parseLong(accountNbr),
                pageable);
        assertThat(response).isNotNull();
        assertThat(response.getNumberOfElements()).isEqualTo(1);
        assertEquals(accountMovements.get(0).getAccountOrigin(),response.getContent().get(0).getAccountOrigin());
        assertEquals(accountMovements.get(0).getAccountDestination(),response.getContent().get(0).getAccountDestination());
        assertEquals(accountMovements.get(0).getAmount(),response.getContent().get(0).getAmount());
        assertEquals(accountMovements.get(0).getType().name(),response.getContent().get(0).getType());
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
                        Long.parseLong(accountNbr),
                        pageable));
    }

    @Test
    public void givenInvalidDates_whenFindingAccountMovementHistory_thenThrowsException() {
        var accountNbr = String.valueOf(123456L);
        var startDate = LocalDate.now().plusDays(1);
        var endDate = LocalDate.now().minusDays(1);
        var pageable = PageRequest.of(0, 10);
        assertThrows(IllegalArgumentException.class, () ->
                accountMovementRepository.findAllByDateAndAccountOriginOrAccountDestination(
                        startDate,
                        endDate,
                        Long.parseLong(accountNbr),
                        pageable));
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
                        Long.parseLong(null),
                        pageable));
    }

    @Test
    public void givenStartDateNull_whenFindingAccountMovementHistory_thenReturnAccount() {
        var accountNbr = String.valueOf(123456L);
        var endDate = LocalDate.now().plusDays(1);
        var accountMovementRequest = AccountMovement.builder()
                .accountOrigin(Long.parseLong(accountNbr))
                .accountDestination(1234567L)
                .amount(10d)
                .id("1a")
                .type(MovementTypeEnum.TRANSFERENCIA)
                .dateTime(LocalDateTime.now())
                .build();
        var accountMovement = accountMovementRepository.save(accountMovementRequest);
        var accountMovements = List.of(accountMovement);
        var pageable = PageRequest.of(0, 10);
        var response = accountMovementRepository.findAllByDateAndAccountOriginOrAccountDestination(
                null,
                endDate,
                Long.parseLong(accountNbr),
                pageable);
        assertThat(response).isNotNull();
        assertThat(response.getNumberOfElements()).isEqualTo(1);
        assertEquals(accountMovements.get(0).getAccountOrigin(),response.getContent().get(0).getAccountOrigin());
        assertEquals(accountMovements.get(0).getAccountDestination(),response.getContent().get(0).getAccountDestination());
        assertEquals(accountMovements.get(0).getAmount(),response.getContent().get(0).getAmount());
        assertEquals(accountMovements.get(0).getType().name(),response.getContent().get(0).getType());
    }
    @Test
    public void givenEndDateNull_whenFindingAccountMovementHistory_thenReturnAccount() {
        var accountNbr = String.valueOf(123456L);
        var startDate = LocalDate.now().minusDays(1);
        var accountMovementRequest = AccountMovement.builder()
                .accountOrigin(Long.parseLong(accountNbr))
                .accountDestination(1234567L)
                .amount(10d)
                .id("1a")
                .type(MovementTypeEnum.TRANSFERENCIA)
                .dateTime(LocalDateTime.now())
                .build();
        var accountMovement = accountMovementRepository.save(accountMovementRequest);
        var accountMovements = List.of(accountMovement);
        var pageable = PageRequest.of(0, 10);
        var response = accountMovementRepository.findAllByDateAndAccountOriginOrAccountDestination(
                startDate,
                null,
                Long.parseLong(accountNbr),
                pageable);
        assertThat(response).isNotNull();
        assertThat(response.getNumberOfElements()).isEqualTo(1);
        assertEquals(accountMovements.get(0).getAccountOrigin(),response.getContent().get(0).getAccountOrigin());
        assertEquals(accountMovements.get(0).getAccountDestination(),response.getContent().get(0).getAccountDestination());
        assertEquals(accountMovements.get(0).getAmount(),response.getContent().get(0).getAmount());
        assertEquals(accountMovements.get(0).getType().name(),response.getContent().get(0).getType());
    }

    @Test
    public void givenStartDateAfterToday_whenFindingAccountMovementHistory_thenReturnAccount() {
        var accountNbr = 123456L;
        var endDate = LocalDate.now().plusDays(2);
        var startDate = LocalDate.now().plusDays(1);
        var pageable = PageRequest.of(0, 10);
        assertThrows(IllegalArgumentException.class,
                () -> accountMovementRepository.
                        findAllByDateAndAccountOriginOrAccountDestination(startDate,endDate,accountNbr,pageable));
    }
}

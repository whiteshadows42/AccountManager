package br.com.khadijeelzein.accountmanager.repository;

import br.com.khadijeelzein.accountmanager.model.Client;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DuplicateKeyException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientRepositoryIntegrationTest  {
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
        clientRepository.deleteAll();
    }
    @Test
    public void givenClientRepository_whenSaveClient_thenOK() {
        Client client = Client.builder()
                .cpf("21920373098")
                .name("John Doe")
                .birthday(LocalDate.of(1996,4,1))
                .build();
        Client createdClient = clientRepository.save(client);
        assertNotNull(createdClient);
        assertEquals(createdClient.getName(), client.getName());
        assertNotNull(createdClient.getId());
        assertEquals(createdClient.getBirthday(), client.getBirthday());
        assertEquals(createdClient.getCpf(), client.getCpf());
    }


    @Test
    public void givenClientCpfIsDuplicated_thenThrowException() {
        Client client1 = Client.builder()
                .cpf("21920373098")
                .name("John Doe")
                .birthday(LocalDate.of(1996,4,1))
                .build();
        Client client2 = Client.builder()
                .cpf("21920373098")
                .name("Jane Doe")
                .birthday(LocalDate.of(1996,4,1))
                .build();
        clientRepository.save(client1);
        assertThrows(DuplicateKeyException.class, () -> clientRepository.save(client2));
    }

}

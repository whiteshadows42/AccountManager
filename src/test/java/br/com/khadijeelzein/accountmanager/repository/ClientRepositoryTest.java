package br.com.khadijeelzein.accountmanager.repository;


import br.com.khadijeelzein.accountmanager.model.Client;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientRepositoryTest {
    @Mock
    private ClientRepository clientRepository;

    @Test
    public void givenClientIsValid_ThenAddClient() {
        Client client = Client.builder()
                .cpf("21920373098")
                .name("John Doe")
                .birthday(LocalDate.of(1996,4,1))
                .build();
        when(clientRepository.save(client)).thenReturn(client);
        Client savedClient = clientRepository.save(client);
        assertNotNull(savedClient);
        assertEquals("John Doe", savedClient.getName());
        assertEquals(LocalDate.of(1996,4,1), savedClient.getBirthday());
        assertEquals("21920373098", savedClient.getCpf());
        verify(clientRepository,times(1)).save(client);
    }
    @Test
    public void givenClientCpfIsInvalid_thenThrowException() {
        Client client = Client.builder()
                .cpf("123456")
                .name("John Doe")
                .birthday(LocalDate.of(1996,4,1))
                .build();
        when(clientRepository.save(client)).thenThrow(ConstraintViolationException.class);
        assertThrows(ConstraintViolationException.class, () -> clientRepository.save(client));
    }

    @Test
    public void givenClientCpfIsNull_thenThrowException() {
        Client client = Client.builder()
                .cpf(null)
                .name("John Doe")
                .birthday(LocalDate.of(1996,4,1))
                .build();
        when(clientRepository.save(client)).thenThrow(ConstraintViolationException.class);
        assertThrows(ConstraintViolationException.class, () -> clientRepository.save(client));
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
        when(clientRepository.save(client2)).thenThrow(DuplicateKeyException.class);
        assertThrows(DuplicateKeyException.class, () -> clientRepository.save(client2));
    }
    @Test
    public void givenClientNameIsNull_thenThrowException() {
        Client client = Client.builder()
                .cpf("21920373098")
                .name(null)
                .birthday(LocalDate.of(1996,4,1))
                .build();
        when(clientRepository.save(client)).thenThrow(ConstraintViolationException.class);
        assertThrows(ConstraintViolationException.class, () -> clientRepository.save(client));
    }

    @Test
    public void givenClientBirthdayIsNull_thenThrowException() {
        Client client = Client.builder()
                .cpf("21920373098")
                .name("John Doe")
                .birthday(null)
                .build();
        when(clientRepository.save(client)).thenThrow(ConstraintViolationException.class);
        assertThrows(ConstraintViolationException.class, () -> clientRepository.save(client));
    }

    @Test
    public void givenClientIsNull_thenThrowException() {
        when(clientRepository.save(null)).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> clientRepository.save(null));
    }
}

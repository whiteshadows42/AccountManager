package br.com.khadijeelzein.accountmanager.service;

import br.com.khadijeelzein.accountmanager.dto.ClientRequest;
import br.com.khadijeelzein.accountmanager.mapper.ClientMapper;
import br.com.khadijeelzein.accountmanager.model.Client;
import br.com.khadijeelzein.accountmanager.repository.ClientRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    @Test
    public void givenValidClientRequest_whenAddingClient_thenCallRepository() {
        ClientRequest client = new ClientRequest();
        client.setBirthday(LocalDate.of(1996, 4, 1));
        client.setName("John Doe");
        client.setCpf("62368887016");
        var clientResponse = Client.builder()
                .cpf("62368887016")
                .id("1a")
                .birthday(LocalDate.of(1996, 4, 1))
                .name("John Doe")
                .build();
        when(clientRepository.save(ClientMapper.toClient(client))).thenReturn(clientResponse);
        clientService.addClient(client);
        verify(clientRepository,times(1)).save(ClientMapper.toClient(client));
    }
    @Test
    public void givenNullClientRequest_whenAddingClient_thenThrowException() {
        ClientRequest client = null;
        assertThrows(NullPointerException.class, () -> clientService.addClient(client));
    }
    @Test
    public void givenInvalidCpfClientRequest_whenAddingClient_thenThrowException() {
        ClientRequest client = new ClientRequest();
        client.setBirthday(LocalDate.of(1996, 4, 1));
        client.setName("John Doe");
        client.setCpf("12345");
        clientService.addClient(client);
        Set<ConstraintViolation<Client>> violations = validator.validate(ClientMapper.toClient(client));
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void givenInvalidDateAndNameClientRequest_whenAddingClient_thenThrowException() {
        ClientRequest client = new ClientRequest();
        client.setBirthday(null);
        client.setName(null);
        client.setCpf("62368887016");
        assertThrows(IllegalArgumentException.class,() -> clientService.addClient(client));
        Set<ConstraintViolation<Client>> violations = validator.validate(ClientMapper.toClient(client));
        assertThat(violations.size()).isEqualTo(2);
    }

    @Test
    public void givenNullDateClientRequest_whenAddingClient_thenThrowException() {
        ClientRequest client = new ClientRequest();
        client.setBirthday(null);
        client.setName("John Doe");
        client.setCpf("62368887016");
        assertThrows(IllegalArgumentException.class, () -> clientService.addClient(client));
    }
    @Test
    public void givenInvalidNameClientRequest_whenAddingClient_thenThrowException() {
        ClientRequest client = new ClientRequest();
        client.setBirthday(LocalDate.of(1996, 4, 1));
        client.setName(null);
        client.setCpf("62368887016");
        clientService.addClient(client);
        Set<ConstraintViolation<Client>> violations = validator.validate(ClientMapper.toClient(client));
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void givenFormattedCpfClientRequest_whenAddingClient_thenCallRepository() {
        ClientRequest client = new ClientRequest();
        client.setBirthday(LocalDate.of(1996, 4, 1));
        client.setName("John Doe");
        client.setCpf("081.632.850-10");
        var clientResponse = Client.builder()
                .cpf("081.632.850-10")
                .id("1a")
                .birthday(LocalDate.of(1996, 4, 1))
                .name("John Doe")
                .build();
        when(clientRepository.save(ClientMapper.toClient(client))).thenReturn(clientResponse);
        clientService.addClient(client);
        verify(clientRepository,times(1)).save(ClientMapper.toClient(client));
    }

}

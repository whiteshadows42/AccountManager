package br.com.khadijeelzein.accountmanager.mapper;

import br.com.khadijeelzein.accountmanager.dto.ClientRequest;
import br.com.khadijeelzein.accountmanager.model.Client;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {
    public static Client toClient(ClientRequest clientRequest) {
         @Valid Client client = Client.builder()
                .cpf(clientRequest.getCpf().replaceAll("[^0-9]", ""))
                .name(clientRequest.getName())
                .birthday(clientRequest.getBirthday())
                .build();
         return client;
    }
}

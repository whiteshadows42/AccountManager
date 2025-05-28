package br.com.khadijeelzein.accountmanager.service;

import br.com.khadijeelzein.accountmanager.dto.ClientRequest;
import br.com.khadijeelzein.accountmanager.mapper.ClientMapper;
import br.com.khadijeelzein.accountmanager.model.Client;
import br.com.khadijeelzein.accountmanager.repository.ClientRepository;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Getter
public class ClientService implements ClientServiceInterface {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void addClient(@Valid ClientRequest clientRequest) {
        var isValid = checkValidBirthday(clientRequest.getBirthday());

        if (isValid) {
            Client client = ClientMapper.toClient(clientRequest);
            clientRepository.save(client);
        }else{
            throw new IllegalArgumentException(
                    "Data de nascimento não pode ser futura"
            );
        }
    }

    private boolean checkValidBirthday(LocalDate birthday) {
        return birthday!=null && birthday.isBefore(LocalDate.now());
    }

    @Override
    public boolean existsClient(String cpf){
        var exists = clientRepository.existsClientByCpf(cpf.replaceAll("[^0-9]", ""));
        var i = 0;
        return exists;
    }
}

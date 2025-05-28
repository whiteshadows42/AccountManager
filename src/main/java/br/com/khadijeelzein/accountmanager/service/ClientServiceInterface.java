package br.com.khadijeelzein.accountmanager.service;

import br.com.khadijeelzein.accountmanager.dto.ClientRequest;

public interface ClientServiceInterface {
    void addClient(ClientRequest clientRequest);
    boolean existsClient(String cpf);
}

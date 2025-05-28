package br.com.khadijeelzein.accountmanager.repository;

import br.com.khadijeelzein.accountmanager.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends MongoRepository<Client, String> {
    boolean existsClientByCpf(String cpf);
}

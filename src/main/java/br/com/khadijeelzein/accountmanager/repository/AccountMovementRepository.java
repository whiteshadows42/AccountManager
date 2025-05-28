package br.com.khadijeelzein.accountmanager.repository;

import br.com.khadijeelzein.accountmanager.model.Account;
import br.com.khadijeelzein.accountmanager.model.AccountMovement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountMovementRepository extends PagingAndSortingRepository<AccountMovement,String>,MongoRepository<AccountMovement, String>,AccountMovementRepositoryCustom {

}

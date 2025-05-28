package br.com.khadijeelzein.accountmanager.repository;

import br.com.khadijeelzein.accountmanager.dto.AccountBalanceProjection;
import br.com.khadijeelzein.accountmanager.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends MongoRepository<Account, String>,AccountRepositoryCustom {
     AccountBalanceProjection findCurrentBalanceByAccountNbr(Long accountNbr);
     boolean existsByAccountNbr(Long accountNbr);
}
package br.com.khadijeelzein.accountmanager.service;

import br.com.khadijeelzein.accountmanager.dto.AccountBalanceProjection;
import br.com.khadijeelzein.accountmanager.dto.AccountNbrResponse;
import br.com.khadijeelzein.accountmanager.dto.AccountRequest;
import br.com.khadijeelzein.accountmanager.model.Account;

import java.util.List;

public interface AccountServiceInterface {
    AccountNbrResponse createAccount(AccountRequest accountRequest);
    AccountBalanceProjection getAccountBalance(Long accountNbr);
    void updateAllAccountBalance(List<Account> accounts);
    boolean accountExists(Long accountNbr);
}

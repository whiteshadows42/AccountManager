package br.com.khadijeelzein.accountmanager.repository;

import br.com.khadijeelzein.accountmanager.model.Account;

import java.util.List;

public interface AccountRepositoryCustom {
    void updateAccountByAccountNbr(List<Account> accounts);
}

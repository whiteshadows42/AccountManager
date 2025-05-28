package br.com.khadijeelzein.accountmanager.mapper;

import br.com.khadijeelzein.accountmanager.dto.AccountRequest;
import br.com.khadijeelzein.accountmanager.dto.ClientRequest;
import br.com.khadijeelzein.accountmanager.enums.TypeEnum;
import br.com.khadijeelzein.accountmanager.model.Account;
import br.com.khadijeelzein.accountmanager.model.Client;

public class AccountMapper {
    public static Account toAccount(AccountRequest accountRequest){
        return Account.builder()
                .type(TypeEnum.setValueOfEnum(accountRequest.getAccountType().toUpperCase()))
                .client(Client.builder().cpf(accountRequest.getClientCpf()).build())
                .build();
    }
}

package br.com.khadijeelzein.accountmanager.service;

import br.com.khadijeelzein.accountmanager.dto.AccountBalanceProjection;
import br.com.khadijeelzein.accountmanager.dto.AccountNbrResponse;
import br.com.khadijeelzein.accountmanager.dto.AccountRequest;
import br.com.khadijeelzein.accountmanager.enums.TypeEnum;
import br.com.khadijeelzein.accountmanager.mapper.AccountMapper;
import br.com.khadijeelzein.accountmanager.model.Account;
import br.com.khadijeelzein.accountmanager.repository.AccountRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Getter
public class AccountService implements AccountServiceInterface {

    private final AccountRepository accountRepository;
    private final ClientServiceInterface clientService;

    @Autowired
    public AccountService(AccountRepository accountRepository, ClientServiceInterface clientService) {
        this.accountRepository = accountRepository;
        this.clientService = clientService;
    }

    @Override
    public AccountNbrResponse createAccount(AccountRequest accountRequest){
        var isValid = checkAccountRequest(accountRequest);
        if(isValid) {
            Account account = AccountMapper.toAccount(accountRequest);
            account.setAccountNbr(ThreadLocalRandom.current().nextLong(1,Long.MAX_VALUE));
            account.setCurrentBalance(0.0d);
            Account accountResponse = accountRepository.save(account);
            return new AccountNbrResponse(accountResponse.getAccountNbr());
        }
        return null;
    }

    private boolean checkAccountRequest(AccountRequest accountRequest) {
        if(accountRequest.getAccountType()==null) throw new IllegalArgumentException("Tipo não pode ser nulo");
        if(Arrays.stream(TypeEnum.values()).anyMatch(type ->
                type.getType()
                        .contentEquals(accountRequest.getAccountType().toUpperCase())
        ) || Arrays.stream(TypeEnum.values()).anyMatch(type ->
                type.name()
                        .contentEquals(accountRequest.getAccountType().toUpperCase())
        )
        ) {
            if(!clientService.existsClient(accountRequest.getClientCpf()))
                throw new IllegalArgumentException("Cliente Inexistente");
            else return true;
        } else throw new IllegalArgumentException("Tipo Inválido");
    }

    @Override
    public AccountBalanceProjection getAccountBalance(Long accountNbr){
        if(accountNbr!=null && accountNbr>0)
            return accountRepository.findCurrentBalanceByAccountNbr(accountNbr);
        else throw new IllegalArgumentException("Número da conta não pode ser nulo ou menor que 0");
    }

    @Override
    public void updateAllAccountBalance(List<Account> accounts){
        if(accounts!=null && !accounts.isEmpty())
            accountRepository.updateAccountByAccountNbr(accounts);
        else throw new IllegalArgumentException("Contas não podem ser nulas");
    }
    @Override
    public boolean accountExists(Long accountNbr){
        return accountRepository.existsByAccountNbr(accountNbr);
    }
}

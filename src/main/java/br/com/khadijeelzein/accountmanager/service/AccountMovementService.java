package br.com.khadijeelzein.accountmanager.service;

import br.com.khadijeelzein.accountmanager.dto.AccountMovementRequest;
import br.com.khadijeelzein.accountmanager.dto.AccountMovementResponse;
import br.com.khadijeelzein.accountmanager.dto.AccountBalanceProjection;
import br.com.khadijeelzein.accountmanager.enums.MovementTypeEnum;
import br.com.khadijeelzein.accountmanager.mapper.AccountMovementMapper;
import br.com.khadijeelzein.accountmanager.model.Account;
import br.com.khadijeelzein.accountmanager.repository.AccountMovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class AccountMovementService implements AccountMovementServiceInterface {
    private final AccountMovementRepository accountMovementRepository;
    private final AccountServiceInterface accountService;

    @Autowired
    public AccountMovementService(AccountMovementRepository accountMovementRepository, AccountService accountService) {
        this.accountMovementRepository = accountMovementRepository;
        this.accountService = accountService;
    }

    @Override
    public void accountTransfer(AccountMovementRequest accountMovementRequest) {
        var isValid = checkAccountRequest(accountMovementRequest);
        if(isValid) {
            var balanceOrigin = getBalanceFromAccount(accountMovementRequest);
            var balanceDestination = getBalanceFromAccount(accountMovementRequest);
            var newBalanceOrigin = balanceOrigin - accountMovementRequest.getAmount();
            var newBalanceDestination = balanceDestination + accountMovementRequest.getAmount();
            updateAccountBalances(accountMovementRequest, newBalanceOrigin, newBalanceDestination);
            var accountMovement = AccountMovementMapper.toAccountMovement(accountMovementRequest);
            accountMovementRepository.save(accountMovement);
        }
    }

    private void updateAccountBalances(AccountMovementRequest accountMovementRequest,Double newBalanceOrigin, Double newBalanceDestination) {
        Account accountOrigin = Account.builder()
                .accountNbr(accountMovementRequest.getAccountNbrOrigin())
                .currentBalance(newBalanceOrigin)
                .build();
        Account accountDestination = Account.builder().accountNbr(accountMovementRequest.getAccountNbrDestination())
                .currentBalance(newBalanceDestination)
                .build();
       var accounts= List.of(accountOrigin, accountDestination);
       accountService.updateAllAccountBalance(accounts);
    }

    private Double getBalanceFromAccount(AccountMovementRequest accountMovementRequest) {
        AccountBalanceProjection balanceOriginProjection = accountService.getAccountBalance(accountMovementRequest.getAccountNbrOrigin());
        return balanceOriginProjection.getCurrentBalance();
    }

    private boolean checkAccountRequest(AccountMovementRequest accountMovementRequest) {
        if(accountMovementRequest.getType()==null) throw new IllegalArgumentException("Tipo não pode ser nulo");
        if(Arrays.stream(MovementTypeEnum.values()).anyMatch(type ->
                type.getType()
                        .contentEquals(accountMovementRequest.getType().toUpperCase())
        ) || Arrays.stream(MovementTypeEnum.values()).anyMatch(type ->
                type.name()
                        .contentEquals(accountMovementRequest.getType().toUpperCase())
        )){
            if(accountMovementRequest.getAccountNbrOrigin()>0 && accountMovementRequest.getAccountNbrDestination()>0) {
                if (!Objects.equals(accountMovementRequest.getAccountNbrOrigin(), accountMovementRequest.getAccountNbrDestination())) {
                    var existsAccountOrigin = accountService.accountExists(accountMovementRequest.getAccountNbrOrigin());
                    var existsAccountDestination = accountService.accountExists(accountMovementRequest.getAccountNbrDestination());
                    if (!existsAccountOrigin || !existsAccountDestination) {
                        throw new IllegalArgumentException("Contas Inexistente");
                    } else return true;
                } else throw new IllegalArgumentException("Contas Iguais");
            }else throw new IllegalArgumentException("Contas Inválidas");
        } else throw new IllegalArgumentException("Tipo Inválido");
    }

    @Override
    public Page<AccountMovementResponse> accountTransferHistory(String id,
                                                                LocalDate startDate,
                                                                LocalDate endDate,Pageable pageable) {
        var isValid = checkIfDatesAndAccountNumberAreValid(id, startDate, endDate);
        if (isValid) {
                var response = accountMovementRepository
                        .findAllByDateAndAccountOriginOrAccountDestination(
                                startDate,
                                endDate,
                                Long.parseLong(id),
                                pageable);
                return response;
        } return null;
    }

    private boolean checkIfDatesAndAccountNumberAreValid(String id, LocalDate startDate, LocalDate endDate) {

        if(startDate!=null && endDate!=null && startDate.isAfter(endDate) ||
                startDate!= null && startDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Data inicial deve vir antes da final " +
                            "e/ou data inicial não pode ser futura"
            );
        }
        if(Long.parseLong(id)<=0) {
            throw new IllegalArgumentException(
                    "Número de conta deve ser maior que 0");
        }
        return true;
    }
}

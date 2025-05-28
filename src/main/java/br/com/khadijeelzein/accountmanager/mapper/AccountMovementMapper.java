package br.com.khadijeelzein.accountmanager.mapper;

import br.com.khadijeelzein.accountmanager.dto.AccountMovementRequest;
import br.com.khadijeelzein.accountmanager.dto.AccountMovementResponse;
import br.com.khadijeelzein.accountmanager.dto.AccountRequest;
import br.com.khadijeelzein.accountmanager.enums.MovementTypeEnum;
import br.com.khadijeelzein.accountmanager.model.Account;
import br.com.khadijeelzein.accountmanager.model.AccountMovement;
import br.com.khadijeelzein.accountmanager.model.Client;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AccountMovementMapper {
    public static List<AccountMovementResponse> toAccountMovementResponseList(List<AccountMovement> accountMovements){
        List<AccountMovementResponse> accountMovementResponses = new ArrayList<>();
        accountMovements.forEach(accountMovement->{
            accountMovementResponses.add( AccountMovementResponse.builder()
                    .accountOrigin(accountMovement.getAccountOrigin())
                    .accountDestination(accountMovement.getAccountDestination())
                    .amount(accountMovement.getAmount())
                    .dateTime(accountMovement.getDateTime())
                    .type(accountMovement.getType().name())
                    .build());
        });
        return accountMovementResponses;
    }
    public static AccountMovement toAccountMovement(AccountMovementRequest accountMovementRequest){
        return AccountMovement.builder().
                accountOrigin(accountMovementRequest.getAccountNbrOrigin())
                .accountDestination(accountMovementRequest.getAccountNbrDestination())
                .amount(accountMovementRequest.getAmount())
                .type(MovementTypeEnum.setValueOfEnum(accountMovementRequest.getType().toUpperCase()))
                .dateTime(LocalDateTime.
                        now(ZoneId.of("America/Sao_Paulo")))
                .build();
    }
}

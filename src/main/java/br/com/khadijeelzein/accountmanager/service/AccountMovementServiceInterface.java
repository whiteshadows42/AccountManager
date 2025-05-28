package br.com.khadijeelzein.accountmanager.service;

import br.com.khadijeelzein.accountmanager.dto.AccountMovementRequest;
import br.com.khadijeelzein.accountmanager.dto.AccountMovementResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;


public interface AccountMovementServiceInterface {

    void accountTransfer(AccountMovementRequest accountMovementRequest) throws Exception;

    Page<AccountMovementResponse> accountTransferHistory(String id,
                                                         LocalDate startDate,
                                                         LocalDate endDate,
                                                         Pageable pageable);
}

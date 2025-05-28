package br.com.khadijeelzein.accountmanager.repository;

import br.com.khadijeelzein.accountmanager.dto.AccountMovementResponse;
import br.com.khadijeelzein.accountmanager.model.AccountMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;


public interface AccountMovementRepositoryCustom {

    Page<AccountMovementResponse> findAllByDateAndAccountOriginOrAccountDestination(
            LocalDate from, LocalDate to, Long account, Pageable pageable);

}
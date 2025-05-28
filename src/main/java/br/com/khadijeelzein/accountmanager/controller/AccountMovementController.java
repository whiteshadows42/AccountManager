package br.com.khadijeelzein.accountmanager.controller;

import br.com.khadijeelzein.accountmanager.dto.AccountMovementRequest;
import br.com.khadijeelzein.accountmanager.dto.AccountMovementResponse;
import br.com.khadijeelzein.accountmanager.exceptions.ErrorResponse;
import br.com.khadijeelzein.accountmanager.service.AccountMovementServiceInterface;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@Tag(name = "AccountMovementController", description = "Todos os Endpoints relacionados a movimentações bancárias")
public class AccountMovementController {

    private final AccountMovementServiceInterface accountMovementService;

    @Autowired
    public AccountMovementController(AccountMovementServiceInterface accountMovementService) {
        this.accountMovementService = accountMovementService;
    }

    @Tag(name = "accountTransfer",description = "Endpoint responsável por realizar tranferências entre contas")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    @Operation(summary = "Faz transferência entre contas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transferência Realizada",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class)) }),
            @ApiResponse(responseCode = "400", description = "Parâmetro da Requisição Inválido",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro inesperado",content = @Content)
    })
    @PostMapping("/transactions")
    public ResponseEntity<Void> accountTransfer(
            @RequestBody @Valid AccountMovementRequest accountMovementRequest) throws Exception {
        accountMovementService.accountTransfer(accountMovementRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Tag(name = "accountTransferHistory",description = "Endpoint responsável por buscar o histórico de transferências")
    @Operation(summary = "Busca o histórico de transferências entre contas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico encontrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)) }),
            @ApiResponse(responseCode = "400", description = "Parâmetros da Requisição Inválido",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro inesperado",content = @Content),
            @ApiResponse(responseCode = "404", description = "Histórico não encontrado",content = @Content),

    })
    @GetMapping("/accounts/{id}/transactions")
    public ResponseEntity<Page<AccountMovementResponse>> accountTransferHistory(@PathVariable String id,
                                                                                @DateTimeFormat(pattern = "dd/MM/yyyy")
                                                                                @RequestParam(value = "startDate", required=false) LocalDate startDate,
                                                                                @DateTimeFormat(pattern = "dd/MM/yyyy")
                                                                                    @RequestParam(value = "endDate", required=false)
                                                                                             LocalDate endDate,
                                                                                @ParameterObject Pageable pageable) {
        var response = accountMovementService.accountTransferHistory(id,
                startDate,
                endDate,
                pageable);
        if(response == null || response.getContent().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

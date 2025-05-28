package br.com.khadijeelzein.accountmanager.controller;

import br.com.khadijeelzein.accountmanager.dto.AccountBalanceProjection;
import br.com.khadijeelzein.accountmanager.dto.AccountNbrResponse;
import br.com.khadijeelzein.accountmanager.dto.AccountRequest;
import br.com.khadijeelzein.accountmanager.exceptions.ErrorResponse;
import br.com.khadijeelzein.accountmanager.service.AccountService;
import br.com.khadijeelzein.accountmanager.service.AccountServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "AccountController", description = "Todos os Endpoints relacionados a contas bancárias")
@RequestMapping("/accounts")
public class AccountController {

    private final AccountServiceInterface accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Tag(name = "createAccount",description = "Endpoint responsável por criar uma conta bancária")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    @Operation(summary = "Cadastra uma conta bancária")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conta criada",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountNbrResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Parâmetro da Requisição Inválido",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro inesperado",content = @Content)
    })
    @PostMapping
    public ResponseEntity<AccountNbrResponse> createAccount(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Conta a ser criada", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountRequest.class),
                    examples = @ExampleObject(value = "{ \"cliente\": \"Cpf do Cliente\", " +
                            "\"tipo_conta\": \"Tipo da conta bancária\"}")))
            @RequestBody @Valid AccountRequest accountRequest) {
            var response = accountService.createAccount(accountRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Tag(name = "getAccountBalance",description = "Endpoint responsável por pesquisar o saldo atual da conta")
    @Operation(summary = "Retorna o saldo atual pelo número da conta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retornou o saldo atual",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountBalanceProjection.class)) }),
            @ApiResponse(responseCode = "400", description = "Número de conta inválido",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro inesperado",content = @Content),
            @ApiResponse(responseCode = "404", description = "Saldo atual não encontrado",
                    content = @Content) })
    @GetMapping("/{id}/balance")
    public ResponseEntity<Object> getAccountBalance(@PathVariable @NotBlank @NotNull String id) {
        var balance = accountService.getAccountBalance(Long.parseLong(id));
        if(balance != null) return new ResponseEntity<>(balance, HttpStatus.OK);
        var errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,"Saldo não encontrado, conta inexistente");
        return new ResponseEntity<>(
                errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }
}

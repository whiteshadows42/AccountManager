package br.com.khadijeelzein.accountmanager.controller;

import br.com.khadijeelzein.accountmanager.dto.ClientRequest;
import br.com.khadijeelzein.accountmanager.model.Client;
import br.com.khadijeelzein.accountmanager.service.ClientService;
import br.com.khadijeelzein.accountmanager.service.ClientServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "ClientController", description = "Todos os Endpoints relacionados a clientes")
public class ClientController{

    private final ClientServiceInterface clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Tag(name = "addClient",description = "Endpoint responsável por cadastrar cliente")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    @Operation(summary = "Cadastra cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class)) }),
            @ApiResponse(responseCode = "400", description = "Parâmetro da Requisição Inválido",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro inesperado",content = @Content),
            @ApiResponse(responseCode = "409",description = "Cliente já cadastrado previamente",content = @Content)
    })
    @PostMapping("/clients")
    public ResponseEntity<Void> addClient(@Valid @RequestBody ClientRequest client){
        clientService.addClient(client);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

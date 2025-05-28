package br.com.khadijeelzein.accountmanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;


@Data
public class AccountMovementRequest {
    @Positive
    @NotNull
    @JsonProperty("conta_origem")
    private Long accountNbrOrigin;
    @Positive
    @NotNull
    @JsonProperty("conta_destino")
    private Long accountNbrDestination;

    @Positive
    @NotNull
    @JsonProperty("valor")
    private Double amount;

    @NotBlank
    @JsonProperty("tipo")
    private String type;
}

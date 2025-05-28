package br.com.khadijeelzein.accountmanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class AccountMovementResponse {
    @JsonProperty("conta_origem")
    private Long accountOrigin;
    @JsonProperty("conta_destino")
    private Long accountDestination;
    @JsonProperty("valor")
    private Double amount;
    @JsonProperty("tipo")
    private String type;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonProperty("data_hora")
    private LocalDateTime dateTime;
}

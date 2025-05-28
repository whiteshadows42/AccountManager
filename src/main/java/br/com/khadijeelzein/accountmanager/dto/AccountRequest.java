package br.com.khadijeelzein.accountmanager.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
public class AccountRequest {
    @JsonProperty("cliente")
    @NotBlank
    @CPF
    private String clientCpf;

    @JsonProperty("tipo_conta")
    @NotBlank
    private String accountType;

}

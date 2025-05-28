package br.com.khadijeelzein.accountmanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;
import java.time.LocalDate;

@Data
public class ClientRequest {
    @CPF
    private String cpf;
    @JsonProperty("nome_completo")
    @NotBlank
    private String name;
    @JsonProperty("data_nascimento")
    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthday;
}

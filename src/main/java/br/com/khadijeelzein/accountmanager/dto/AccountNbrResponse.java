package br.com.khadijeelzein.accountmanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class AccountNbrResponse {
    @JsonProperty("numero_conta")
    private Long accountNbr;
}

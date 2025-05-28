package br.com.khadijeelzein.accountmanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface AccountBalanceProjection {
    @JsonProperty("saldo_atual")
    Double getCurrentBalance();
}
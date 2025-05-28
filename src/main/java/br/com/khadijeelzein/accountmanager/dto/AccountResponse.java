package br.com.khadijeelzein.accountmanager.dto;

import br.com.khadijeelzein.accountmanager.enums.TypeEnum;
import br.com.khadijeelzein.accountmanager.model.Client;

public class AccountResponse {

    private String id;

    private Long accountNbr;

    private ClientResponse client;

    private TypeEnum type;

    private Double currentBalance;
}

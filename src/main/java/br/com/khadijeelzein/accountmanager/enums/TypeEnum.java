package br.com.khadijeelzein.accountmanager.enums;

import lombok.Getter;

@Getter
public enum TypeEnum {
    CORRENTE(1,"CORRENTE"),
    POUPANCA(2,"POUPANÇA");

    private final String type;
    private final Integer id;

    TypeEnum(int id, String type) {
        this.type = type;
        this.id = id;
    }
    public static TypeEnum setValueOfEnum(String type) {
        if (type.equals("CORRENTE")) {
            return TypeEnum.CORRENTE;
        }
        if (type.equals("POUPANÇA") || type.equals("POUPANCA")) {
            return TypeEnum.POUPANCA;
        }
        throw new IllegalArgumentException("Tipo Inválido");
    }
}

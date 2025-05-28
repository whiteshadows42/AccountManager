package br.com.khadijeelzein.accountmanager.enums;

import lombok.Getter;

@Getter
public enum MovementTypeEnum {
    TRANSFERENCIA(1,"TRANSFERÊNCIA");

    private final String type;
    private final Integer id;

    MovementTypeEnum(Integer id, String type) {
        this.id = id;
        this.type = type;
    }
    public static MovementTypeEnum setValueOfEnum(String type) {
        if (type.equals("TRANSFERÊNCIA") || type.equals("TRANSFERENCIA")) {
            return MovementTypeEnum.TRANSFERENCIA;
        }
        throw new IllegalArgumentException("Tipo Inválido");
    }
}

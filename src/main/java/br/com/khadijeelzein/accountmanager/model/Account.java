package br.com.khadijeelzein.accountmanager.model;

import br.com.khadijeelzein.accountmanager.enums.TypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@Document(collection = "account")
@Builder
public class Account {
    @Id
    private String id;

    @Indexed(unique = true)
    @NotNull
    private Long accountNbr;

    @DocumentReference
    @NotNull
    private Client client;

    @NotNull
    private TypeEnum type;

    @NotNull
    private Double currentBalance;
}

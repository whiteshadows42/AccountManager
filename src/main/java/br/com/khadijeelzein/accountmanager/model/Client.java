package br.com.khadijeelzein.accountmanager.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Data
@Document(collection = "client")
@Builder
public class Client {
    @Id
    private String id;

    @Field("clientCpf")
    @CPF
    @Indexed(unique = true)
    @NotBlank
    private String cpf;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate birthday;
}

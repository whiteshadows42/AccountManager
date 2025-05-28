package br.com.khadijeelzein.accountmanager.model;

import br.com.khadijeelzein.accountmanager.enums.MovementTypeEnum;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "account_movement")
public class AccountMovement {
    @Id
    private String id;

    @Positive
    private Long accountOrigin;

    @Positive
    private Long accountDestination;

    @PositiveOrZero
    private Double amount;

    private MovementTypeEnum type;

    @CreatedDate
    private LocalDateTime dateTime;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;
}

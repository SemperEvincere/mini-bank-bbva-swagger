package com.bbva.minibank.infrastructure.entities;

import com.bbva.minibank.domain.models.enums.TransactionTypeEnum;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

  @Id
  @GeneratedValue(generator = "uuid2")
  UUID id;

  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime timestamp;

  @Enumerated(value = EnumType.STRING)
  private TransactionTypeEnum type;

  @PositiveOrZero
  private BigDecimal amount;

  @NotNull
  private UUID accountNumberFrom;

  @Nullable
  private UUID accountNumberTo;

}

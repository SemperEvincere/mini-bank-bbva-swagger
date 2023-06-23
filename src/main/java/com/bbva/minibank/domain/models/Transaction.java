package com.bbva.minibank.domain.models;

import com.bbva.minibank.domain.models.enums.TransactionTypeEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {

  private UUID id;
  private LocalDateTime createdAt;
  private TransactionTypeEnum type;
  private BigDecimal amount;
  private UUID accountNumberFrom;
  private UUID accountNumberTo;

}

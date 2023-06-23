package com.bbva.minibank.presentation.response.account;

import com.bbva.minibank.domain.models.enums.ClientTypeEnum;
import com.bbva.minibank.domain.models.enums.CurrencyEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@JsonInclude
@Getter
@Setter
public class AccountResponse {

  private UUID id;
  private UUID clientHolderId;
  private BigDecimal balance;
  private CurrencyEnum currency;

}

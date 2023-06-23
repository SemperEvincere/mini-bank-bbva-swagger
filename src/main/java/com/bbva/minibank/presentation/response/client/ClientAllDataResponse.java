package com.bbva.minibank.presentation.response.client;

import com.bbva.minibank.presentation.response.account.AccountDetailsResponse;
import com.bbva.minibank.presentation.response.account.AccountResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@JsonInclude()
@Builder
@Getter
@Setter
public class ClientAllDataResponse {

  private UUID id;
  private LocalDate createDate;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private String address;
  private List<AccountDetailsResponse> accounts;
  private LocalDate updatedAt;


}

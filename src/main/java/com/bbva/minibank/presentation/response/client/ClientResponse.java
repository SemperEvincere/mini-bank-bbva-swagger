package com.bbva.minibank.presentation.response.client;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientResponse {

  private UUID id;
  private LocalDate createDate;
  private String lastName;
  private String firstName;
  private String email;
  private String phone;
  private String address;
  private LocalDate updatedAt;
}

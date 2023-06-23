package com.bbva.minibank.presentation.controllers;

import com.bbva.minibank.application.usecases.account.IAccountFindUseCase;
import com.bbva.minibank.application.usecases.client.*;
import com.bbva.minibank.application.usecases.user.IUserUseCase;
import com.bbva.minibank.domain.models.Account;
import com.bbva.minibank.domain.models.Client;
import com.bbva.minibank.infrastructure.entities.UserEntity;
import com.bbva.minibank.presentation.mappers.AccountPresentationMapper;
import com.bbva.minibank.presentation.mappers.ClientPresentationMapper;
import com.bbva.minibank.presentation.request.client.ClientCreateRequest;
import com.bbva.minibank.presentation.response.account.AccountDetailsResponse;
import com.bbva.minibank.presentation.response.account.AccountResponse;
import com.bbva.minibank.presentation.response.client.ClientAllDataResponse;
import com.bbva.minibank.presentation.response.client.ClientResponse;
import com.bbva.minibank.presentation.response.client.ClientUpdateResponse;
import com.bbva.minibank.presentation.response.errors.ErrorResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

  private final IClientCreateUseCase clientCreateUseCase;
  private final IClientSaveUseCase clientSaveUseCase;
  private final IClientFindByUseCase clientFindByUseCase;
  private final ClientPresentationMapper clientMapper;
  private final AccountPresentationMapper accountMapper;
  private final IAccountFindUseCase accountFindUseCase;
  private final IClientUpdateUseCase clientUpdateUseCase;
  private final IClientDeleteUseCase clientDeleteUseCase;
  private final IUserUseCase userUseCase;

  private static ResponseEntity<ErrorResponse> getErrorResponseEntity(BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      List<String> errors = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());

      ErrorResponse errorResponse = new ErrorResponse("Error de validación", errors);
      return ResponseEntity.badRequest().body(errorResponse);
    }
    return null;
  }

  @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
  public ResponseEntity<?> create(@Valid @RequestBody ClientCreateRequest request,
      BindingResult bindingResult) {
    ResponseEntity<ErrorResponse> errorResponse = getErrorResponseEntity(bindingResult);
    if (errorResponse != null) {
      return errorResponse;
    }
    UserEntity userEntity = userUseCase.findUserById(request.getUserId());
    if (userEntity == null) {
      return ResponseEntity.notFound().build();
    }
    Client client = clientCreateUseCase.create(request, userEntity);
    Client savedClient = clientSaveUseCase.save(client);
    ClientResponse response = clientMapper.domainToResponse(savedClient);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);

  }

  @GetMapping(value = "/", produces = "application/json")
  @ResponseBody
  public List<ClientResponse> getAll() {
    return clientFindByUseCase.getAll().stream().map(clientMapper::domainToResponse).collect(Collectors.toList());
  }

  @GetMapping(value = "/{id}", produces = "application/json")
  @ResponseBody
  public ClientAllDataResponse getOne(@PathVariable("id") UUID id) {
    Optional<Client> clientOptional = clientFindByUseCase.findByIdAndIsActive(id);

    return clientOptional.map(client -> {
      List<Account> accounts = client.getAccounts()
                                     .stream()
                                     .map(accountFindUseCase::findByAccountNumber)
                                     .toList();
      List<AccountDetailsResponse> accountDetailsResponses = accountMapper.domainToDetailResponseList(accounts);
      return clientMapper.domainToAllDataResponse(client, accountDetailsResponses);
    }).orElse(null);
  }

  @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
  public ResponseEntity<?> update(@PathVariable("id") UUID id, @Valid @RequestBody ClientCreateRequest request,
                                     BindingResult bindingResult) {
    ResponseEntity<ErrorResponse> errorResponse = getErrorResponseEntity(bindingResult);
    if (errorResponse != null) {
      return ResponseEntity.badRequest().body(errorResponse);
    }
    Client client = clientFindByUseCase.findById(id).orElse(null);
    if (client == null) {
      return ResponseEntity.notFound().build();
    }
    Client clientUpdated = clientMapper.updateDomainFromRequest(client, request, id);
    Client savedUpdatedClient = clientUpdateUseCase.update(clientUpdated);
    ClientResponse response = clientMapper.domainToResponse(savedUpdatedClient);
    return ResponseEntity.ok(response);

  }
  
  @DeleteMapping(value = "/{id}", produces = "application/json")
  public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
    Client client = clientFindByUseCase.findByIdAndIsActive(id).orElse(null);
    if (client == null) {
      return ResponseEntity.notFound().build();
    }
    clientDeleteUseCase.delete(client);
    return ResponseEntity.ok("Client delete");
  }
  
  @PostMapping(value = "/restore/{id}", produces = "application/json")
  public ResponseEntity<?> restoreDeletedClient(@PathVariable("id") UUID id) {
    Client client = clientFindByUseCase.findById(id).orElse(null);
    if (client == null) {
      return ResponseEntity.notFound().build();
    }
    Client clienteRestored = clientUpdateUseCase.restoreDeletedClient(client);
    ClientResponse response = clientMapper.domainToResponse(clienteRestored);
    return ResponseEntity.ok(response);
  }


}

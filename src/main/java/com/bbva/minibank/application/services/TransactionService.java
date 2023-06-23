package com.bbva.minibank.application.services;

import com.bbva.minibank.application.repository.ITransactionRepository;
import com.bbva.minibank.application.usecases.account.IAccountFindUseCase;
import com.bbva.minibank.application.usecases.account.IAccountOperationsUseCase;
import com.bbva.minibank.application.usecases.account.IAccountUpdateUseCase;
import com.bbva.minibank.application.usecases.client.IClientFindByUseCase;
import com.bbva.minibank.application.usecases.transaction.ITransactionBalanceUseCase;
import com.bbva.minibank.application.usecases.transaction.ITransactionCreateUseCase;
import com.bbva.minibank.application.usecases.transaction.ITransactionFindUseCase;
import com.bbva.minibank.domain.models.Account;
import com.bbva.minibank.domain.models.Client;
import com.bbva.minibank.domain.models.Transaction;
import com.bbva.minibank.domain.models.enums.TransactionTypeEnum;
import com.bbva.minibank.presentation.request.transaction.TransactionCreateRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionBalanceUseCase, ITransactionCreateUseCase,
                                           ITransactionFindUseCase {

  private final ITransactionRepository transactionRepository;
  private final IAccountFindUseCase accountFind;
  private final IAccountUpdateUseCase accountUpdate;
  private final IClientFindByUseCase clientFindBy;
  private final IAccountOperationsUseCase accountOperationsUseCase;

  public Transaction createTransaction(TransactionCreateRequest transactionCreateRequest) {
    return Transaction
            .builder()
            .createdAt(LocalDateTime.now())
            .type(TransactionTypeEnum.valueOf(transactionCreateRequest.getType()))
            .accountNumberFrom(transactionCreateRequest.getIdAccountOrigin().isBlank() ? null : UUID.fromString(transactionCreateRequest.getIdAccountOrigin()))
            .accountNumberTo(transactionCreateRequest.getIdAccountDestination().isBlank() ? null : UUID.fromString(transactionCreateRequest.getIdAccountDestination()))
            .amount(transactionCreateRequest.getAmount())
            .build();
  }

  @Override
  public Transaction deposit(Transaction transaction,
      Client client) {
    if(transaction.getAccountNumberFrom() == null){
      throw new IllegalArgumentException("AccountNumberFrom not must be null");
    }
    // verificar que el cliente existe
    if (clientFindBy.findById(client.getId()).isEmpty()) {
      throw new IllegalArgumentException("Client not found");
    }
    // buscar la cuenta del depositante
    UUID accountClient = clientFindBy.getAccountClient(transaction, client);
    Account accountSaved = accountFind.findByAccountNumber(accountClient);
    // verificar que el cliente sea holder o coholder de la cuenta
    if (accountSaved.getClientHolder().equals(client.getId()) || Objects.requireNonNull(
                                                                                accountSaved.getListSecondsHolders())
                                                                        .contains(client.getId())) {
        // realizar el deposito
      accountSaved.setBalance(accountOperationsUseCase.add(accountSaved.getBalance(), transaction.getAmount()));
    } else {
      throw new IllegalArgumentException("Client not is holder or coholder of account");
    }
    // guardar la transaccion
    if (accountSaved.getTransactions() == null || accountSaved.getTransactions().isEmpty()) {
      accountSaved.setTransactions(new ArrayList<>());
    }
    Transaction transactionSaved = transactionRepository.save(transaction);
    accountSaved.getTransactions().add(transactionSaved);
    accountUpdate.update(accountSaved);
    return transactionSaved;
  }


  @Override
  public Transaction withdraw(Transaction transaction,
      Client client) {
    UUID accountClient = clientFindBy.getAccountClient(transaction, client);
    Account accountSaved = accountFind.findByAccountNumber(accountClient);
    accountSaved.setBalance(accountOperationsUseCase.substract(accountSaved.getBalance(), transaction.getAmount()));
    if (accountSaved.getTransactions() == null || accountSaved.getTransactions().isEmpty()) {
      accountSaved.setTransactions(new ArrayList<>());
    }
    Transaction transactionSaved = transactionRepository.save(transaction);
    accountSaved.getTransactions().add(transactionSaved);
    accountUpdate.update(accountSaved);
    return transactionSaved;
  }

  @Override
  public Transaction transfer(Transaction transaction,
      Client clientSaved) {
    if(transaction.getAccountNumberTo().equals(transaction.getAccountNumberFrom())){
      throw new IllegalArgumentException("Account origin and destination must be different");
    }
    
    UUID accountClient = clientFindBy.getAccountClient(transaction, clientSaved);
    Account accountOrigin = accountFind.findByAccountNumber(accountClient);
    Account accountDestination = accountFind.findByAccountNumber(transaction.getAccountNumberTo());

    if(accountOrigin.getBalance().compareTo(transaction.getAmount()) < 0){
      throw new IllegalArgumentException("Insufficient funds");
    }
    
    if(accountOrigin.getClientHolder().equals(clientSaved.getId()) || Objects.requireNonNull(accountOrigin.getListSecondsHolders()).contains(clientSaved.getId())){
      throw new IllegalArgumentException("Client not is holder or coholder of account origin");
    }
    
    if(!accountOrigin.getCurrency().equals(accountDestination.getCurrency())){
      throw new IllegalArgumentException("Accounts must be in the same currency");
    }
    
    accountOrigin.setBalance(accountOperationsUseCase.substract(accountOrigin.getBalance(), transaction.getAmount()));
    accountDestination.setBalance(accountOperationsUseCase.add(accountDestination.getBalance(), transaction.getAmount()));
    if (accountOrigin.getTransactions() == null || accountOrigin.getTransactions().isEmpty()) {
      accountOrigin.setTransactions(new ArrayList<>());
    }
    if (accountDestination.getTransactions() == null || accountDestination.getTransactions().isEmpty()) {
      accountDestination.setTransactions(new ArrayList<>());
    }
    Transaction transactionSaved = transactionRepository.save(transaction);
    accountOrigin.getTransactions().add(transactionSaved);
    accountDestination.getTransactions().add(transactionSaved);
    accountUpdate.update(accountOrigin);
    accountUpdate.update(accountDestination);

    return transactionSaved;
  }
  
  
  @Override
  public List<Transaction> findAll() {
    return transactionRepository.findAll();
  }
  
  @Override
  public Transaction findById(UUID transactionNumber) {
    return transactionRepository.findById(transactionNumber).orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
  }
  
}

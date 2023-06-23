package com.bbva.minibank.application.services;

import com.bbva.minibank.application.repository.IAccountRepository;
import com.bbva.minibank.application.usecases.account.*;
import com.bbva.minibank.application.usecases.client.IClientUpdateUseCase;
import com.bbva.minibank.domain.models.Account;
import com.bbva.minibank.domain.models.Client;
import com.bbva.minibank.domain.models.enums.CurrencyEnum;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService implements
        IAccountCreateUseCase,
        IAccountFindUseCase,
        IAccountUpdateUseCase,
        IAccountOperationsUseCase,
        IAccountDeleteUseCase {

  private final IAccountRepository accountRepository;
  private final IClientUpdateUseCase clientUpdateUseCase;

  @Override
  public Account create(CurrencyEnum currency,
      Client holder,
      Client secondHolder) {
    Account.AccountBuilder accountBuilder = Account.builder()
            .accountNumber(UUID.randomUUID())
            .currency(currency)
            .creationDate(LocalDate.now())
            .balance(BigDecimal.ZERO)
            .transactions(new ArrayList<>())
            .clientHolder(holder.getId());

    if (secondHolder != null) {
        accountBuilder.listSecondsHolders(List.of(secondHolder.getId()));
    }

    Account account = accountBuilder.build();
    accountRepository.save(account);
    clientUpdateUseCase.addAccount(holder, account);
    if (secondHolder != null) {
        clientUpdateUseCase.addAccount(secondHolder, account);
    }

    return account;
  }

  @Override
  public Account findByAccountNumber(UUID accountNumber) {
    return accountRepository.findByAccountNumber(accountNumber);
  }

  @Override
  public List<Account> findAll() {
    return accountRepository.findAll();
  }

  @Override
  public Account update(Account accountUpdate) {
    return accountRepository.save(accountUpdate);
  }


  @Override
  public BigDecimal substract(BigDecimal balance,
      BigDecimal amount) {
    if(balance.compareTo(amount) < 0){
      throw new IllegalArgumentException("Balance not must be negative");
    }
    return balance.subtract(amount);
  }

  @Override
  public BigDecimal add(BigDecimal balance,
      BigDecimal amount) {
    return balance.add(amount);
  }

  @Override
  public void blockAccount(Account account) {
    account.setLocked(true);
    accountRepository.save(account);
  }
}

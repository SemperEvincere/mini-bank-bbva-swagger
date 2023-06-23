package com.bbva.minibank.infrastructure.repositories;

import com.bbva.minibank.application.repository.IAccountRepository;
import com.bbva.minibank.domain.models.Account;
import com.bbva.minibank.infrastructure.entities.AccountEntity;
import com.bbva.minibank.infrastructure.mappers.AccountEntityMapper;
import com.bbva.minibank.infrastructure.repositories.springdatajpa.IAccountSpringRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements IAccountRepository {

  private final IAccountSpringRepository accountSpringRepository;
  private final AccountEntityMapper accountEntityMapper;

  @Override
  public Account findByAccountNumber(UUID accountNumber) {
    Optional<AccountEntity> accountEntity = accountSpringRepository.findById(accountNumber);
    return accountEntityMapper.entityToDomain(accountEntity.orElse(null));
  }

  @Override
  public void saveAll(List<Account> accountsDefault) {
    List<AccountEntity> accountEntities = accountsDefault.stream().map(accountEntityMapper::domainToEntity).toList();
    accountSpringRepository.saveAll(accountEntities);
  }

  @Override
  public Account save(Account newAccount) {
    AccountEntity accountEntity = accountEntityMapper.domainToEntity(newAccount);
    AccountEntity accountSaved = accountSpringRepository.save(accountEntity);
    return accountEntityMapper.entityToDomain(accountSaved);
  }

  @Override
  public List<Account> findAll() {
    List<AccountEntity> accountEntities = accountSpringRepository.findAll();
    return accountEntities.stream().map(accountEntityMapper::entityToDomain).toList();
  }
}

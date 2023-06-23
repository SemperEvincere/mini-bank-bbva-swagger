package com.bbva.minibank.application.services;

import com.bbva.minibank.application.repository.IClientRepository;
import com.bbva.minibank.application.usecases.client.*;
import com.bbva.minibank.domain.models.Account;
import com.bbva.minibank.domain.models.Client;
import com.bbva.minibank.domain.models.Transaction;
import com.bbva.minibank.infrastructure.entities.UserEntity;
import com.bbva.minibank.infrastructure.mappers.ClientEntityMapper;
import com.bbva.minibank.presentation.mappers.ClientPresentationMapper;
import com.bbva.minibank.presentation.request.client.ClientCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService
    implements IClientUpdateUseCase,
               IClientCreateUseCase,
               IClientSaveUseCase,
               IClientFindByUseCase,
               IClientDeleteUseCase {

    private final IClientRepository clientRepository;
    private final ClientPresentationMapper clientMapper;
    private final UserService userService;
    private final ClientEntityMapper clientEntityMapper;

    public Client create(ClientCreateRequest request, UserEntity userEntity) {
        String email = userEntity.getEmail();
        if (clientRepository.existsByEmailAndLastNameAndFirstName(email, request.getLastName(),
                                                                  request.getFirstName())) {
            throw new RuntimeException("This client already exists");
        }
        Client client = clientMapper.requestToDomain(request, userEntity);
        userEntity.setClient(clientEntityMapper.domainToEntity(client));
        userService.save(userEntity);
        return clientRepository.saveClient(client);
    }
    
    


    @Override
    public Client save(Client client) {
        return clientRepository.saveClient(client);
    }

    @Override
    public List<Client> getAll() {
        return clientRepository.getAll();
    }

    @Override
    public Optional<Client> findById(UUID id) {
        return clientRepository.findById(id);
    }

    public UUID getAccountClient(Transaction transaction,
                                 Client client) {
        return client.getAccounts()
                     .stream()
                     .filter(acc -> acc.equals(transaction.getAccountNumberFrom()))
                     .findFirst()
                     .orElseThrow(() -> new RuntimeException("Account not found"));
    }
    
    @Override
    public Optional<Client> findByIdAndIsActive(UUID id) {
        return clientRepository.findByIdAndIsActive(id);
    }
    
    @Override
    public Client update(Client client) {
        if (!clientRepository.existsById(client.getId())) {
            throw new RuntimeException("This client does not exist");
        }
        return clientRepository.update(client);
    }

    @Override
    public void addAccount(Client client, Account account) {
        clientRepository.addAccount(client, account);
    }
    
    @Override
    public Client restoreDeletedClient(Client client) {
        return clientRepository.restoreDeletedClient(client);
    }
    
    @Override
    public void delete(Client client) {
        clientRepository.delete(client);
    }
}

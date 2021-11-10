package com.zerofiltre.freeland.infra.providers.database.client;

import com.zerofiltre.freeland.domain.client.ClientProvider;
import com.zerofiltre.freeland.domain.client.model.Client;
import com.zerofiltre.freeland.domain.client.model.ClientId;
import com.zerofiltre.freeland.infra.providers.database.client.mapper.ClientJPAMapper;
import com.zerofiltre.freeland.infra.providers.database.client.model.ClientJPA;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientDatabaseProvider implements ClientProvider {

  private final ClientJPARepository repository;
  private final ClientJPAMapper mapper;

  @Override
  public Optional<Client> clientOfId(ClientId clientId) {
    return repository.findBySiren(clientId.getSiren())
        .map(mapper::fromJPA);
  }

  @Override
  public Client registerClient(Client client) {
    ClientJPA clientJPA = mapper.toJPA(client);
    return mapper.fromJPA(repository.save(clientJPA));
  }
}

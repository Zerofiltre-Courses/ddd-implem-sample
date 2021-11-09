package com.zerofiltre.freeland.infra.providers.database.client;

import com.zerofiltre.freeland.domain.client.ClientProvider;
import com.zerofiltre.freeland.domain.client.model.Client;
import com.zerofiltre.freeland.domain.client.model.ClientId;
import com.zerofiltre.freeland.infra.providers.database.client.mapper.ClientJPAMapper;
import com.zerofiltre.freeland.infra.providers.database.client.model.ClientJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientDatabaseProvider implements ClientProvider {

  private final ClientJPARepository repository;
  private final ClientJPAMapper mapper;

  @Override
  public Client clientOfId(ClientId clientId) {
    return mapper.fromJPA(repository.findBySiren(clientId.getSiren()));
  }

  @Override
  public Client registerClient(Client client) {
    ClientJPA clientJPA = mapper.toJPA(client);
    return mapper.fromJPA(repository.save(clientJPA));
  }
}

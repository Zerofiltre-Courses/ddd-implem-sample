package com.zerofiltre.freeland.infra.providers.database.client.mapper;

import com.zerofiltre.freeland.domain.client.model.Client;
import com.zerofiltre.freeland.domain.client.model.ClientId;
import com.zerofiltre.freeland.infra.providers.database.client.ClientJPARepository;
import com.zerofiltre.freeland.infra.providers.database.client.model.ClientJPA;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ClientJPAMapper {

  @Autowired
  private ClientJPARepository clientJPARepository;

  @Mapping(target = "siren", expression = "java(client.getClientId().getSiren())")
  @Mapping(target = "name", expression = "java(client.getClientId().getName())")
  public abstract ClientJPA toJPA(Client client);

  public abstract Client fromJPA(ClientJPA clientJPA);


  @AfterMapping
  Client addClientId(@MappingTarget Client result, ClientJPA clientJPA) {
    ClientId clientId = new ClientId(clientJPA.getSiren(), clientJPA.getName());
    result.setClientId(clientId);
    return result;
  }

  @AfterMapping
  ClientJPA addId(@MappingTarget ClientJPA result, Client client) {
    if (client != null) {
      ClientId clientId = client.getClientId();
      if (clientId != null && clientId.getSiren() != null) {
        return clientJPARepository.findBySiren(clientId.getSiren())
            .map(clientJPA -> {
              result.setId(clientJPA.getId());
              return result;
            }).orElse(result);
      }
    }
    return result;
  }

}

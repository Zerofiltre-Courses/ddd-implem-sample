package com.zerofiltre.freeland.infra.providers.database.client.mapper;

import com.zerofiltre.freeland.domain.client.model.Client;
import com.zerofiltre.freeland.domain.client.model.ClientId;
import com.zerofiltre.freeland.infra.providers.database.client.model.ClientJPA;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class ClientJPAMapper {

  public abstract ClientJPA toJPA(Client client);

  public abstract Client fromJPA(ClientJPA clientJPA);


  @AfterMapping
  Client addClientId(@MappingTarget Client result, ClientJPA clientJPA) {
    ClientId clientId = new ClientId(clientJPA.getSiren(), clientJPA.getName());
    result.setClientId(clientId);
    return result;
  }

}

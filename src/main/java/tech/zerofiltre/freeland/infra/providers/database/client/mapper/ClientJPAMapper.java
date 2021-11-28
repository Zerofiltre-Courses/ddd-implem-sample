package tech.zerofiltre.freeland.infra.providers.database.client.mapper;

import tech.zerofiltre.freeland.domain.Address;
import tech.zerofiltre.freeland.domain.client.model.Client;
import tech.zerofiltre.freeland.domain.client.model.ClientId;
import tech.zerofiltre.freeland.infra.providers.database.client.model.ClientJPA;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class ClientJPAMapper {


  @Mapping(target = "siren", expression = "java(client.getClientId().getSiren())")
  @Mapping(target = "name", expression = "java(client.getClientId().getName())")
  @Mapping(target = "streetNumber", expression = "java(client.getAddress().getStreetNumber())")
  @Mapping(target = "streetName", expression = "java(client.getAddress().getStreetName())")
  @Mapping(target = "city", expression = "java(client.getAddress().getCity())")
  @Mapping(target = "postalCode", expression = "java(client.getAddress().getPostalCode())")
  @Mapping(target = "country", expression = "java(client.getAddress().getCountry())")
  public abstract ClientJPA toJPA(Client client);

  public abstract Client fromJPA(ClientJPA clientJPA);


  @AfterMapping
  Client completeMapping(@MappingTarget Client result, ClientJPA clientJPA) {
    ClientId clientId = new ClientId(clientJPA.getSiren(), clientJPA.getName());
    result.setClientId(clientId);
    Address address = new Address(clientJPA.getStreetNumber(), clientJPA.getStreetName(), clientJPA.getPostalCode(),
        clientJPA.getCity(), clientJPA.getCountry());
    result.setAddress(address);
    return result;
  }


}

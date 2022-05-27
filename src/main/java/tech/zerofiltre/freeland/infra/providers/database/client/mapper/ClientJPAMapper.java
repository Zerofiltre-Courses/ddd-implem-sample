package tech.zerofiltre.freeland.infra.providers.database.client.mapper;

import org.mapstruct.*;
import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.client.model.*;
import tech.zerofiltre.freeland.infra.providers.database.client.model.*;

@Mapper(componentModel = "spring")
public abstract class ClientJPAMapper {


    @Mapping(target = "siren", expression = "java(client.getClientId()!=null?client.getClientId().getSiren():null)")
    @Mapping(target = "name", expression = "java(client.getClientId()!=null?client.getClientId().getName():null)")
    @Mapping(target = "streetNumber", expression = "java(client.getAddress().getStreetNumber())")
    @Mapping(target = "streetName", expression = "java(client.getAddress().getStreetName())")
    @Mapping(target = "city", expression = "java(client.getAddress().getCity())")
    @Mapping(target = "postalCode", expression = "java(client.getAddress().getPostalCode())")
    @Mapping(target = "country", expression = "java(client.getAddress().getCountry())")
    public abstract ClientJPA toJPA(Client client);

    public abstract Client fromJPA(ClientJPA clientJPA);


    @AfterMapping
    Client completeMapping(@MappingTarget Client.ClientBuilder result, ClientJPA clientJPA) {
        ClientId clientId = new ClientId(clientJPA.getSiren(), clientJPA.getName());
        result.clientId(clientId);
        Address address = new Address(clientJPA.getStreetNumber(), clientJPA.getStreetName(), clientJPA.getPostalCode(),
                clientJPA.getCity(), clientJPA.getCountry());
        result.address(address);
        return result.build();
    }


}

package tech.zerofiltre.freeland.infra.entrypoints.rest.client.mapper;

import org.mapstruct.*;
import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.client.model.*;
import tech.zerofiltre.freeland.domain.client.model.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.client.model.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.client.model.*;

@Mapper(componentModel = "spring")
public abstract class ClientVMMapper {


    @Mapping(target = "siren", expression = "java(client.getClientId().getSiren())")
    @Mapping(target = "name", expression = "java(client.getClientId().getName())")
    @Mapping(target = "streetNumber", expression = "java(client.getAddress().getStreetNumber())")
    @Mapping(target = "streetName", expression = "java(client.getAddress().getStreetName())")
    @Mapping(target = "city", expression = "java(client.getAddress().getCity())")
    @Mapping(target = "postalCode", expression = "java(client.getAddress().getPostalCode())")
    @Mapping(target = "country", expression = "java(client.getAddress().getCountry())")
    public abstract ClientVM toVM(Client client);


    public abstract Client fromVM(ClientVM clientVM);

    @AfterMapping
    Client completeMapping(@MappingTarget Client.ClientBuilder result, ClientVM clientVM) {
        ClientId clientId = new ClientId(clientVM.getSiren(), clientVM.getName());
        result.clientId(clientId);
        Address address = new Address(clientVM.getStreetNumber(), clientVM.getStreetName(), clientVM.getPostalCode(),
                clientVM.getCity(), clientVM.getCountry());
        result.address(address);
        return result.build();
    }


}

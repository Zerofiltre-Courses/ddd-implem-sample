package com.zerofiltre.freeland.infra.providers.database.serviceContract.mapper;

import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContract;
import com.zerofiltre.freeland.infra.providers.database.serviceContract.model.ServiceContractJPA;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = WagePortageAgreementJPAMapper.class)
public abstract class ServiceContractJPAMapper {


  /*  @Mappings({
        @Mapping(target = "serviceContractId", qualifiedByName = "toServiceContractId"),
        @Mapping(target = "clientId", qualifiedByName = "toClientId"),
        @Mapping(target = "rate", qualifiedByName = "toRate")
    })*/
  public abstract ServiceContract fromJPA(ServiceContractJPA serviceContractJPA);

  @Mappings({
      @Mapping(source = "contractNumber", target = "serviceContractId.contractNumber"),
      @Mapping(source = "ratePrice", target = "rate.price"),
      @Mapping(source = "rateCurrency", target = "rate.currency"),
      @Mapping(source = "rateFrequency", target = "rate.frequency")
  })
  public abstract ServiceContractJPA toJPA(ServiceContract serviceContract);


 /* @Named("toServiceContractId")
  private ServiceContractId toServiceContractId(String contractNumber) {
    if (contractNumber == null) {
      return null;
    }
    return new ServiceContractId(contractNumber);
  }

  @Named("toClientId")
  private ClientId toClientId(ClientJPA clientJPA) {
    if (clientJPA == null) {
      return null;
    }

    return new ClientId(clientJPA.getSiren(), clientJPA.getName());
  }

  @Named("toRate")
  private Rate toRate(float ratePrice, Currency rateCurrency, Frequency rateFrequency) {
    return new Rate(ratePrice, rateCurrency, rateFrequency);
  }
*/

}

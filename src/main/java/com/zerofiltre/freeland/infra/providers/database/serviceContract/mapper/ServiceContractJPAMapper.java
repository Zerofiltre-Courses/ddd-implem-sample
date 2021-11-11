package com.zerofiltre.freeland.infra.providers.database.serviceContract.mapper;

import com.zerofiltre.freeland.domain.Rate;
import com.zerofiltre.freeland.domain.client.model.ClientId;
import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContract;
import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;
import com.zerofiltre.freeland.infra.providers.database.client.ClientJPARepository;
import com.zerofiltre.freeland.infra.providers.database.client.model.ClientJPA;
import com.zerofiltre.freeland.infra.providers.database.serviceContract.model.ServiceContractJPA;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = WagePortageAgreementJPAMapper.class)
public abstract class ServiceContractJPAMapper {

  @Autowired
  private ClientJPARepository clientJPARepository;

  @Mappings({
      @Mapping(target = "serviceContractId", source = "contractNumber"),
      @Mapping(target = "clientId", source = "client"),
  })
  public abstract ServiceContract fromJPA(ServiceContractJPA serviceContractJPA);


  @AfterMapping
  public void addRate(@MappingTarget ServiceContract result, ServiceContractJPA serviceContractJPA) {
    result.setRate(new Rate(serviceContractJPA.getRatePrice(), serviceContractJPA.getRateCurrency(),
        serviceContractJPA.getRateFrequency()));
  }

  @Mappings({
      @Mapping(target = "contractNumber", expression = "java(serviceContract.getServiceContractId().getContractNumber())"),
      @Mapping(target = "ratePrice", expression = "java(serviceContract.getRate().getPrice())"),
      @Mapping(target = "rateCurrency", expression = "java(serviceContract.getRate().getCurrency())"),
      @Mapping(target = "rateFrequency", expression = "java(serviceContract.getRate().getFrequency())"),
      @Mapping(target = "client", source = "clientId")
  })
  public abstract ServiceContractJPA toJPA(ServiceContract serviceContract);


  ServiceContractId toServiceContractId(Long contractNumber) {
    if (contractNumber == null) {
      return null;
    }
    return new ServiceContractId(contractNumber);
  }

  ClientId toClientId(ClientJPA clientJPA) {
    if (clientJPA == null) {
      return null;
    }
    return new ClientId(clientJPA.getSiren(), clientJPA.getName());
  }

  ClientJPA toClientJPA(ClientId clientId) {
    if (clientId == null) {
      return null;
    }
    return clientJPARepository.findById(clientId.getSiren()).orElse(null);
  }


}

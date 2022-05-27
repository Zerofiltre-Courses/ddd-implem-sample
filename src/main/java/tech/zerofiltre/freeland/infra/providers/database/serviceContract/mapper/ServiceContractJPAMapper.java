package tech.zerofiltre.freeland.infra.providers.database.serviceContract.mapper;

import tech.zerofiltre.freeland.domain.Rate;
import tech.zerofiltre.freeland.domain.client.model.ClientId;
import tech.zerofiltre.freeland.domain.servicecontract.model.ServiceContract;
import tech.zerofiltre.freeland.domain.servicecontract.model.ServiceContractId;
import tech.zerofiltre.freeland.infra.providers.database.client.ClientJPARepository;
import tech.zerofiltre.freeland.infra.providers.database.client.model.ClientJPA;
import tech.zerofiltre.freeland.infra.providers.database.serviceContract.model.ServiceContractJPA;
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
  public void addRate(@MappingTarget ServiceContract.ServiceContractBuilder result, ServiceContractJPA serviceContractJPA) {
    result.rate(new Rate(serviceContractJPA.getRateValue(), serviceContractJPA.getRateCurrency(),
        serviceContractJPA.getRateFrequency()));
  }

  @Mappings({
      @Mapping(target = "contractNumber", expression = "java(serviceContract.getServiceContractId()!=null?serviceContract.getServiceContractId().getContractNumber():null)"),
      @Mapping(target = "rateValue", expression = "java(serviceContract.getRate().getValue())"),
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

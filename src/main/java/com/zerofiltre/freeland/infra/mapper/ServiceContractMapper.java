package com.zerofiltre.freeland.infra.mapper;


import com.zerofiltre.freeland.domain.entities.ServiceContract;
import com.zerofiltre.freeland.infra.dto.ServiceContractDTO;
import org.mapstruct.Mapper;

@Mapper
public abstract class ServiceContractMapper {

  public abstract ServiceContract toLightEntity(ServiceContractDTO serviceContractDTO);

  public ServiceContract toEntity(ServiceContractDTO serviceContractDTO) {
    ServiceContract serviceContract = toLightEntity(serviceContractDTO);
    //custom imple
    return serviceContract;
  }


  public abstract ServiceContractDTO toDTO(ServiceContract serviceContract);

}

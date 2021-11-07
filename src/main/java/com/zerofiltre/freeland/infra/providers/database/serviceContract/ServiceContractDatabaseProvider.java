package com.zerofiltre.freeland.infra.providers.database.serviceContract;

import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContract;
import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;
import com.zerofiltre.freeland.domain.serviceContract.use_cases.ServiceContractProvider;
import com.zerofiltre.freeland.infra.providers.database.serviceContract.mapper.ServiceContractJPAMapper;
import com.zerofiltre.freeland.infra.providers.database.serviceContract.model.ServiceContractJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceContractDatabaseProvider implements ServiceContractProvider {

  private final ServiceContractJPARepository repository;
  private final ServiceContractJPAMapper mapper;


  @Override
  public ServiceContract serviceContractFromId(ServiceContractId serviceContractId) {
    ServiceContractJPA serviceContractJPA = repository.getById(serviceContractId.getContractNumber());
    return mapper.fromJPA(serviceContractJPA);
  }

  @Override
  public ServiceContract registerContract(ServiceContract serviceContract) {
    ServiceContractJPA serviceContractJPA = mapper.toJPA(serviceContract);
    return mapper.fromJPA(repository.save(serviceContractJPA));
  }
}

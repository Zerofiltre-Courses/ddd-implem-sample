package com.zerofiltre.freeland.infra.providers.database.serviceContract;

import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContract;
import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;
import com.zerofiltre.freeland.domain.serviceContract.use_cases.ServiceContractProvider;
import com.zerofiltre.freeland.infra.providers.database.serviceContract.mapper.ServiceContractJPAMapper;
import com.zerofiltre.freeland.infra.providers.database.serviceContract.model.ServiceContractJPA;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ServiceContractDatabaseProvider implements ServiceContractProvider {

  private final ServiceContractJPARepository repository;
  private final ServiceContractJPAMapper mapper;


  @Override
  public Optional<ServiceContract> serviceContractFromId(ServiceContractId serviceContractId) {
    return repository.findById(serviceContractId.getContractNumber())
        .map(mapper::fromJPA);
  }

  @Override
  public ServiceContract registerContract(ServiceContract serviceContract) {
    ServiceContractJPA serviceContractJPA = mapper.toJPA(serviceContract);
    return mapper.fromJPA(repository.save(serviceContractJPA));
  }
}

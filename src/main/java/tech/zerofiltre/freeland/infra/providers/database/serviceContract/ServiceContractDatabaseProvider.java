package tech.zerofiltre.freeland.infra.providers.database.serviceContract;

import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContract;
import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;
import tech.zerofiltre.freeland.application.useCases.serviceContract.ServiceContractProvider;
import tech.zerofiltre.freeland.infra.providers.database.serviceContract.mapper.ServiceContractJPAMapper;
import tech.zerofiltre.freeland.infra.providers.database.serviceContract.model.ServiceContractJPA;
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
  public Optional<ServiceContract> serviceContractOfId(ServiceContractId serviceContractId) {
    return repository.findById(serviceContractId.getContractNumber())
        .map(mapper::fromJPA);
  }

  @Override
  public ServiceContract registerContract(ServiceContract serviceContract) {
    ServiceContractJPA serviceContractJPA = mapper.toJPA(serviceContract);
    return mapper.fromJPA(repository.save(serviceContractJPA));
  }

  @Override
  public void removeServiceContract(ServiceContractId serviceContractId) {
    repository.deleteById(serviceContractId.getContractNumber());
  }
}

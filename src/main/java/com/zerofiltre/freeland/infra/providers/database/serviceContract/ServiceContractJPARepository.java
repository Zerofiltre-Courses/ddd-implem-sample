package com.zerofiltre.freeland.infra.providers.database.serviceContract;

import com.zerofiltre.freeland.infra.providers.database.serviceContract.model.ServiceContractJPA;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceContractJPARepository extends JpaRepository<ServiceContractJPA, Long> {

  Optional<ServiceContractJPA> findByContractNumber(String contractNumber);

}

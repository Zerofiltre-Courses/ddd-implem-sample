package com.zerofiltre.freeland.infra.providers.database.serviceContract;

import com.zerofiltre.freeland.infra.providers.database.serviceContract.model.ServiceContractJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceContractJPARepository extends JpaRepository<ServiceContractJPA, Long> {

}

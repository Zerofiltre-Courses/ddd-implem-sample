package com.zerofiltre.freeland.infra;

import com.zerofiltre.freeland.infra.dto.ServiceContractDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceContractRepository extends JpaRepository<ServiceContractDTO, String>  {


}

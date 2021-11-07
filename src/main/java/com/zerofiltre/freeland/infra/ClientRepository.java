package com.zerofiltre.freeland.infra;

import com.zerofiltre.freeland.infra.dto.ClientDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientDTO, String> {

}

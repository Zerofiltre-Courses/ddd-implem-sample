package com.zerofiltre.freeland.application;

import com.zerofiltre.freeland.infra.dto.ClientDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepositoryEXP extends JpaRepository<ClientDTO, String> {

}

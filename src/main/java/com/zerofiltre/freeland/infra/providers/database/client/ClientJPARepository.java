package com.zerofiltre.freeland.infra.providers.database.client;

import com.zerofiltre.freeland.infra.providers.database.client.model.ClientJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientJPARepository extends JpaRepository<ClientJPA, Long> {

  ClientJPA findBySiren(String siren);
}

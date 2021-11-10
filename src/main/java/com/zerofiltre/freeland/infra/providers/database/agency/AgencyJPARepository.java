package com.zerofiltre.freeland.infra.providers.database.agency;

import com.zerofiltre.freeland.infra.providers.database.agency.model.AgencyJPA;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgencyJPARepository extends JpaRepository<AgencyJPA, Long> {

  Optional<AgencyJPA> findBySiren(String siren);
}

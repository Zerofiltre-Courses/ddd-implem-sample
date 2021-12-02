package tech.zerofiltre.freeland.infra.providers.database.agency;

import tech.zerofiltre.freeland.infra.providers.database.agency.model.AgencyJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgencyJPARepository extends JpaRepository<AgencyJPA, String> {

}

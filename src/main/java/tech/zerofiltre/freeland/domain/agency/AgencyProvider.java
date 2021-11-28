package tech.zerofiltre.freeland.domain.agency;

import tech.zerofiltre.freeland.domain.agency.model.Agency;
import tech.zerofiltre.freeland.domain.agency.model.AgencyId;
import java.util.Optional;

public interface AgencyProvider {

  Optional<Agency> agencyOfId(AgencyId agencyId);

  Agency registerAgency(Agency agency);


}

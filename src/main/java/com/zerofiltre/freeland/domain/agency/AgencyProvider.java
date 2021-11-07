package com.zerofiltre.freeland.domain.agency;

import com.zerofiltre.freeland.domain.agency.model.Agency;
import com.zerofiltre.freeland.domain.agency.model.AgencyId;

public interface AgencyProvider {

  Agency agencyOfId(AgencyId agencyId);


}

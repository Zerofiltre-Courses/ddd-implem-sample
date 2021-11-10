package com.zerofiltre.freeland.infra.providers.database.agency;


import com.zerofiltre.freeland.domain.agency.AgencyProvider;
import com.zerofiltre.freeland.domain.agency.model.Agency;
import com.zerofiltre.freeland.domain.agency.model.AgencyId;
import com.zerofiltre.freeland.infra.providers.database.agency.mapper.AgencyJPAMapper;
import com.zerofiltre.freeland.infra.providers.database.agency.model.AgencyJPA;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AgencyDatabaseProvider implements AgencyProvider {

  private final AgencyJPARepository repository;
  private final AgencyJPAMapper mapper;

  @Override
  public Optional<Agency> agencyOfId(AgencyId AgencyId) {
    return repository.findBySiren(AgencyId.getSiren())
        .map(mapper::fromJPA);
  }

  @Override
  public Agency registerAgency(Agency Agency) {
    AgencyJPA AgencyJPA = mapper.toJPA(Agency);
    return mapper.fromJPA(repository.save(AgencyJPA));
  }

}

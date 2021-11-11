package com.zerofiltre.freeland.infra.providers.database.serviceContract;

import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreementId;
import com.zerofiltre.freeland.domain.serviceContract.use_cases.WagePortageAgreementProvider;
import com.zerofiltre.freeland.infra.providers.database.serviceContract.mapper.WagePortageAgreementJPAMapper;
import com.zerofiltre.freeland.infra.providers.database.serviceContract.model.WagePortageAgreementJPA;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WagePortageAgreementDatabaseProvider implements WagePortageAgreementProvider {

  private final WagePortageAgreementJPARepository repository;
  private final WagePortageAgreementJPAMapper mapper;

  @Override
  public Optional<WagePortageAgreement> wagePortageAgreementOfId(WagePortageAgreementId wagePortageAgreementId) {
    return repository.findById(wagePortageAgreementId.getAgreementNumber())
        .map(mapper::fromJPA);
  }

  @Override
  public WagePortageAgreement registerWagePortageAgreement(WagePortageAgreement wagePortageAgreement) {
    WagePortageAgreementJPA wagePortageAgreementJPA = mapper.toJPA(wagePortageAgreement);
    return mapper.fromJPA(repository.save(wagePortageAgreementJPA));
  }
}

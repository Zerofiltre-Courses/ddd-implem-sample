package com.zerofiltre.freeland.infra.providers.database.serviceContract;

import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreementId;
import com.zerofiltre.freeland.domain.serviceContract.use_cases.WagePortageAgreementProvider;
import com.zerofiltre.freeland.infra.providers.database.serviceContract.mapper.WagePortageAgreementJPAMapper;
import com.zerofiltre.freeland.infra.providers.database.serviceContract.model.WagePortageAgreementJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WagePortageAgreementDatabaseProvider implements WagePortageAgreementProvider {

  private final WagePortageAgreementJPARepository repository;
  private final WagePortageAgreementJPAMapper mapper;

  @Override
  public WagePortageAgreement wagePortageAgreementOfId(WagePortageAgreementId wagePortageAgreementId) {
    return mapper.fromJPA(repository.findByAgreementNumber(wagePortageAgreementId.getAgreementNumber()));
  }

  @Override
  public WagePortageAgreement registerWagePortageAgreement(WagePortageAgreement wagePortageAgreement) {
    WagePortageAgreementJPA wagePortageAgreementJPA = mapper.toJPA(wagePortageAgreement);
    return mapper.fromJPA(repository.save(wagePortageAgreementJPA));
  }
}

package tech.zerofiltre.freeland.infra.providers.database.serviceContract;

import tech.zerofiltre.freeland.domain.servicecontract.model.WagePortageAgreement;
import tech.zerofiltre.freeland.domain.servicecontract.model.WagePortageAgreementId;
import tech.zerofiltre.freeland.domain.servicecontract.WagePortageAgreementProvider;
import tech.zerofiltre.freeland.infra.providers.database.serviceContract.mapper.WagePortageAgreementJPAMapper;
import tech.zerofiltre.freeland.infra.providers.database.serviceContract.model.WagePortageAgreementJPA;
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

  @Override
  public void removeWagePortageAgreement(WagePortageAgreementId wagePortageAgreementId) {
    repository.deleteById(wagePortageAgreementId.getAgreementNumber());
  }


}

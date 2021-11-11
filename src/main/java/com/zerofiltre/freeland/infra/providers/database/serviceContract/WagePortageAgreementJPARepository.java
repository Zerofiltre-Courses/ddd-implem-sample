package com.zerofiltre.freeland.infra.providers.database.serviceContract;

import com.zerofiltre.freeland.infra.providers.database.serviceContract.model.WagePortageAgreementJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WagePortageAgreementJPARepository extends JpaRepository<WagePortageAgreementJPA, Long> {

}

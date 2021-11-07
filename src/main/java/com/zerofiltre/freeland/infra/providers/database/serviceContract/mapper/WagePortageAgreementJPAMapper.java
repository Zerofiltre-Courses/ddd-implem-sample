package com.zerofiltre.freeland.infra.providers.database.serviceContract.mapper;

import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;
import com.zerofiltre.freeland.infra.providers.database.serviceContract.model.WagePortageAgreementJPA;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class WagePortageAgreementJPAMapper {

  abstract WagePortageAgreementJPA toJPA(WagePortageAgreement wagePortageAgreement);

  abstract WagePortageAgreement fromJPA(WagePortageAgreementJPA wagePortageAgreementJPA);


}

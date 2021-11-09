package com.zerofiltre.freeland.infra.providers.database.serviceContract.mapper;

import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreementId;
import com.zerofiltre.freeland.infra.providers.database.serviceContract.model.WagePortageAgreementJPA;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class WagePortageAgreementJPAMapper {


  @Mapping(target = "wagePortageAgreementId", source = "agreementNumber")
  public abstract WagePortageAgreement fromJPA(WagePortageAgreementJPA wagePortageAgreementJPA);

  @Mapping(target = "agreementNumber", expression = "java(wagePortageAgreement.getWagePortageAgreementId().getAgreementNumber())")
  public abstract WagePortageAgreementJPA toJPA(WagePortageAgreement wagePortageAgreement);

  WagePortageAgreementId toWagePortageAgreementId(String agreementNumber) {
    if (agreementNumber == null) {
      return null;
    }
    return new WagePortageAgreementId(agreementNumber);
  }
}

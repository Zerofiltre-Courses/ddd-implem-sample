package com.zerofiltre.freeland.domain.serviceContract.model;

import java.util.Objects;

public class WagePortageAgreementId {

  private final String agreementNumber;

  public WagePortageAgreementId(String agreementNumber) {
    this.agreementNumber = agreementNumber;
  }

  public String getAgreementNumber() {
    return agreementNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WagePortageAgreementId that = (WagePortageAgreementId) o;
    return agreementNumber.equals(that.agreementNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(agreementNumber);
  }
}

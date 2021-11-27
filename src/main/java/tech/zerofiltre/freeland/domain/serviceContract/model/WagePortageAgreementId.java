package tech.zerofiltre.freeland.domain.serviceContract.model;

import java.util.Objects;

public class WagePortageAgreementId {

  private final Long agreementNumber;

  public WagePortageAgreementId(Long agreementNumber) {
    this.agreementNumber = agreementNumber;
  }

  public Long getAgreementNumber() {
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

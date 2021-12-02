package tech.zerofiltre.freeland.domain.serviceContract.model;

import java.util.Date;
import tech.zerofiltre.freeland.domain.Rate.Currency;
import tech.zerofiltre.freeland.domain.Rate.Frequency;

public class ServiceContractStarted extends ServiceContractEvent {

  public ServiceContractStarted() {
  }

  public ServiceContractStarted(long serviceContractNumber, String clientName, String clientSiren,
      String freelancerName, String freelancerSiren, String agencyName, String agencySiren, float rateValue,
      Frequency rateFrequency,
      Currency rateCurrency, float serviceFeesRate, Date startDate) {
    super(serviceContractNumber, clientName, clientSiren, freelancerName, freelancerSiren, agencyName, agencySiren,
        rateValue, rateFrequency, rateCurrency, serviceFeesRate, startDate);
  }
}

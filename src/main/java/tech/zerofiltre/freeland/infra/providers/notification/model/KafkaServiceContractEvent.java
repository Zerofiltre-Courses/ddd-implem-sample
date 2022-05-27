package tech.zerofiltre.freeland.infra.providers.notification.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import tech.zerofiltre.freeland.domain.*;

import java.util.*;

@ToString
@Getter
public class KafkaServiceContractEvent {

    private final long serviceContractNumber;
    private final String clientName;
    private final String clientSiren;
    private final String freelancerName;
    private final String freelancerSiren;
    private final String agencyName;
    private final String agencySiren;
    private final float rateValue;
    private final Rate.Frequency rateFrequency;
    private final Rate.Currency rateCurrency;
    private final float serviceFeesRate;
    private final Date startDate;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public KafkaServiceContractEvent(@JsonProperty("serviceContractNumber") long serviceContractNumber, @JsonProperty("clientName") String clientName, @JsonProperty("clientSiren") String clientSiren, @JsonProperty("freelancerName") String freelancerName, @JsonProperty("freelancerSiren") String freelancerSiren, @JsonProperty("agencyName") String agencyName, @JsonProperty("agencySiren") String agencySiren, @JsonProperty("rateValue") float rateValue, @JsonProperty("rateFrequency") Rate.Frequency rateFrequency, @JsonProperty("rateCurrency") Rate.Currency rateCurrency, @JsonProperty("serviceFeesRate") float serviceFeesRate, @JsonProperty("startDate") Date startDate) {
        this.serviceContractNumber = serviceContractNumber;
        this.clientName = clientName;
        this.clientSiren = clientSiren;
        this.freelancerName = freelancerName;
        this.freelancerSiren = freelancerSiren;
        this.agencyName = agencyName;
        this.agencySiren = agencySiren;
        this.rateValue = rateValue;
        this.rateFrequency = rateFrequency;
        this.rateCurrency = rateCurrency;
        this.serviceFeesRate = serviceFeesRate;
        this.startDate = startDate;
    }
}

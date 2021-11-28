package tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract.model;

import lombok.*;
import tech.zerofiltre.freeland.domain.Rate.Currency;
import tech.zerofiltre.freeland.domain.Rate.*;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceContractVM {

    private long contractNumber;

    private long wagePortageAgreementNumber;

    private String clientSiren;

    private float rateValue;
    private Currency rateCurrency;
    private Frequency rateFrequency;

    private String terms;
    private Date startDate;
    private Date endDate;

}

package tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract.model;

import lombok.*;
import tech.zerofiltre.freeland.domain.Rate.Currency;
import tech.zerofiltre.freeland.domain.Rate.*;

import javax.validation.constraints.*;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceContractVM {

    @NotNull
    private long contractNumber;

    @NotNull
    private long wagePortageAgreementNumber;

    @NotEmpty
    private String clientSiren;

    @NotNull
    private float rateValue;
    @NotNull
    private Currency rateCurrency;
    @NotNull
    private Frequency rateFrequency;

    @NotEmpty
    private String terms;
    private Date startDate;
    private Date endDate;

}

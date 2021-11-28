package tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract.model;

import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WagePortageAgreementVM {

    private long agreementNumber;

    private String freelancerSiren;

    private String agencySiren;

    private float serviceFeesRate;
    private String terms;
    private Date startDate;
    private Date endDate;
}

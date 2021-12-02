package tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract.model;

import lombok.*;

import javax.validation.constraints.*;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WagePortageAgreementVM {

    private long agreementNumber;

    @NotEmpty
    private String freelancerSiren;

    @NotEmpty
    private String agencySiren;

    @NotNull
    private float serviceFeesRate;

    @NotEmpty
    private String terms;


    private Date startDate;

    private Date endDate;
}

package tech.zerofiltre.freeland.application.servicecontract;

import tech.zerofiltre.freeland.domain.servicecontract.model.*;

import java.util.*;

public class WagePortageAgreementStopper {


    public void stop(WagePortageAgreement wagePortageAgreement) throws StopWagePortageAgreementException {
        WagePortageAgreementId wagePortageAgreementId = wagePortageAgreement.getWagePortageAgreementId();
        Optional<WagePortageAgreement> optionalRegisteredWagePortageAgreement =
                wagePortageAgreement.of(wagePortageAgreementId);

        optionalRegisteredWagePortageAgreement.ifPresent(WagePortageAgreement::remove);
        optionalRegisteredWagePortageAgreement.orElseThrow(() -> new StopWagePortageAgreementException(
                "The agreement " + wagePortageAgreementId.getAgreementNumber() + " might have already been deleted"));
    }

}

package tech.zerofiltre.freeland.infra.entrypoints.rest.agency;

import lombok.*;
import org.springframework.web.bind.annotation.*;
import tech.zerofiltre.freeland.domain.agency.*;
import tech.zerofiltre.freeland.domain.agency.model.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.agency.mapper.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.agency.model.*;

@RestController
@RequestMapping("agency")
@RequiredArgsConstructor
public class AgencyController {

    private final AgencyVMMapper agencyVMMapper;
    private final AgencyProvider agencyProvider;

    @PostMapping
    public AgencyVM registerAgency(@RequestBody AgencyVM agencyVM) {
        Agency agency = agencyProvider.registerAgency(agencyVMMapper.fromVM(agencyVM));
        return agencyVMMapper.toVM(agency);
    }
}

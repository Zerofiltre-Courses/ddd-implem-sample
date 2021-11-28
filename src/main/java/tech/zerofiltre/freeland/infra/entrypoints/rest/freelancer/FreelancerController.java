package tech.zerofiltre.freeland.infra.entrypoints.rest.freelancer;

import lombok.*;
import org.springframework.web.bind.annotation.*;
import tech.zerofiltre.freeland.domain.freelancer.*;
import tech.zerofiltre.freeland.domain.freelancer.model.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.freelancer.mapper.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.freelancer.model.*;

@RestController
@RequiredArgsConstructor
public class FreelancerController {

    private final FreelancerVMMapper freelancerVMMapper;
    private final FreelancerProvider freelancerProvider;

    @PostMapping
    FreelancerVM registerAgency(@RequestBody FreelancerVM agencyVM) {
        Freelancer freelancer = freelancerProvider.registerFreelancer(freelancerVMMapper.fromVM(agencyVM));
        return freelancerVMMapper.toVM(freelancer);
    }
}

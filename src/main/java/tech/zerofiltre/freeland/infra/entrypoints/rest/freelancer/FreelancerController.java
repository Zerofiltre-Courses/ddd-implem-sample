package tech.zerofiltre.freeland.infra.entrypoints.rest.freelancer;

import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import tech.zerofiltre.freeland.domain.freelancer.*;
import tech.zerofiltre.freeland.domain.freelancer.model.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.freelancer.mapper.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.freelancer.model.*;

import javax.validation.*;

@RestController
@RequestMapping("freelancer")
@RequiredArgsConstructor
public class FreelancerController {

    private final FreelancerVMMapper freelancerVMMapper;
    private final FreelancerProvider freelancerProvider;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public FreelancerVM registerAgency(@RequestBody @Valid FreelancerVM agencyVM) {
        Freelancer freelancer = freelancerProvider.registerFreelancer(freelancerVMMapper.fromVM(agencyVM));
        return freelancerVMMapper.toVM(freelancer);
    }
}

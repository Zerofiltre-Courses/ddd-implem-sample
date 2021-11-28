package tech.zerofiltre.freeland.domain.freelancer;

import tech.zerofiltre.freeland.domain.freelancer.model.Freelancer;
import tech.zerofiltre.freeland.domain.freelancer.model.FreelancerId;
import java.util.Optional;

public interface FreelancerProvider {

  Optional<Freelancer> freelancerOfId(FreelancerId freelancerId);

  Freelancer registerFreelancer(Freelancer freelancer);

}

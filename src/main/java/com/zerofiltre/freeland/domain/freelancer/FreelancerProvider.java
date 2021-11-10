package com.zerofiltre.freeland.domain.freelancer;

import com.zerofiltre.freeland.domain.freelancer.model.Freelancer;
import com.zerofiltre.freeland.domain.freelancer.model.FreelancerId;
import com.zerofiltre.freeland.infra.providers.database.freelancer.model.FreelancerJPA;
import java.util.Optional;

public interface FreelancerProvider {

  Optional<FreelancerJPA> freelancerOfId(FreelancerId freelancerId);

  Freelancer registerFreelancer(Freelancer freelancer);

}

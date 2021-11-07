package com.zerofiltre.freeland.domain.freelancer;

import com.zerofiltre.freeland.domain.freelancer.model.Freelancer;
import com.zerofiltre.freeland.domain.freelancer.model.FreelancerId;

public interface FreelancerProvider {

  Freelancer freelancerOfId(FreelancerId freelancerId);

  Freelancer registerFreelancer(Freelancer freelancer);

}

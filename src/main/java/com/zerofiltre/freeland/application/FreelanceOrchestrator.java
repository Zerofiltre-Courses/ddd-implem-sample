package com.zerofiltre.freeland.application;

import com.zerofiltre.freeland.domain.Agency;
import com.zerofiltre.freeland.domain.Freelancer;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class FreelanceOrchestrator {

  private final Agency agency;

  public FreelanceOrchestrator(Agency agency) {
    this.agency = agency;
  }

  public Set<Freelancer> freelancers() {
    return agency.freelancers();
  }

  public Freelancer addFreelancer(Freelancer freelancer){
    return agency.addFreelancer(freelancer);
  }
}

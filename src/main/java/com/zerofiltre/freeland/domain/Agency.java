package com.zerofiltre.freeland.domain;

import java.util.Set;


public interface Agency {

  Set<Freelancer> freelancers();

  Freelancer addFreelancer(Freelancer freelancer);

}

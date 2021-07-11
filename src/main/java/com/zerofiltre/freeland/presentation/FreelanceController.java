package com.zerofiltre.freeland.presentation;

import com.zerofiltre.freeland.application.FreelanceOrchestrator;
import com.zerofiltre.freeland.domain.Freelancer;
import java.util.Set;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/freelance")
public class FreelanceController {

  private FreelanceOrchestrator freelanceOrchestrator;

  public FreelanceController(FreelanceOrchestrator freelanceOrchestrator) {
    this.freelanceOrchestrator = freelanceOrchestrator;
  }

  @GetMapping
  public Set<Freelancer> all() {
    return freelanceOrchestrator.freelancers();
  }

  @PostMapping
  public Freelancer add(@RequestBody Freelancer freelancer) {
    return freelanceOrchestrator.addFreelancer(freelancer);
  }
}

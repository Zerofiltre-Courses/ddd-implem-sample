package tech.zerofiltre.freeland.infra.providers.database.freelancer;


import tech.zerofiltre.freeland.domain.freelancer.FreelancerProvider;
import tech.zerofiltre.freeland.domain.freelancer.model.Freelancer;
import tech.zerofiltre.freeland.domain.freelancer.model.FreelancerId;
import tech.zerofiltre.freeland.infra.providers.database.freelancer.mapper.FreelancerJPAMapper;
import tech.zerofiltre.freeland.infra.providers.database.freelancer.model.FreelancerJPA;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FreelancerDatabaseProvider implements FreelancerProvider {

  private final FreelancerJPARepository repository;
  private final FreelancerJPAMapper mapper;

  @Override
  public Optional<Freelancer> freelancerOfId(FreelancerId freelancerId) {
    return repository.findById(freelancerId.getSiren())
        .map(mapper::fromJPA);
  }

  @Override
  public Freelancer registerFreelancer(Freelancer freelancer) {
    FreelancerJPA freelancerJPA = mapper.toJPA(freelancer);
    return mapper.fromJPA(repository.save(freelancerJPA));
  }
}

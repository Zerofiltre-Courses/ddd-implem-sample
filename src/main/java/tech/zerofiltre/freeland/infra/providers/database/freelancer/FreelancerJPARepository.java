package tech.zerofiltre.freeland.infra.providers.database.freelancer;

import tech.zerofiltre.freeland.infra.providers.database.freelancer.model.FreelancerJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreelancerJPARepository extends JpaRepository<FreelancerJPA, String> {

}

package com.zerofiltre.freeland.infra.providers.database.freelancer;

import com.zerofiltre.freeland.infra.providers.database.freelancer.model.FreelancerJPA;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreelancerJPARepository extends JpaRepository<FreelancerJPA, Long> {

  Optional<FreelancerJPA> findBySiren(String siren);
}

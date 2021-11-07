package com.zerofiltre.freeland.infra;

import com.zerofiltre.freeland.infra.dto.FreelancerDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreelancerRepository extends JpaRepository<FreelancerDTO, String> {

}

package com.zerofiltre.freeland.infrastructure;

import com.zerofiltre.freeland.domain.Agency;
import com.zerofiltre.freeland.domain.Freelancer;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class JPAAgency implements Agency {

  private ProfileRepository profileRepository;
  private ProfileMapper profileMapper;

  public JPAAgency(ProfileRepository profileRepository,
      ProfileMapper profileMapper) {
    this.profileRepository = profileRepository;
    this.profileMapper = profileMapper;
  }

  @Override
  public Set<Freelancer> freelancers() {
    Set<Profile> profiles = new HashSet<>(profileRepository.findAll());
    return profileMapper.toFreelances(profiles);
  }

  @Override
  public Freelancer addFreelancer(Freelancer freelancer) {
    return profileMapper.toFreelance(profileRepository.save(profileMapper.toProfile(freelancer)));
  }
}

package com.zerofiltre.freeland.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerofiltre.freeland.infrastructure.JPAAgency;
import com.zerofiltre.freeland.infrastructure.Profile;
import com.zerofiltre.freeland.infrastructure.ProfileMapper;
import com.zerofiltre.freeland.infrastructure.ProfileRepository;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import({JPAAgency.class})
public class AgencyTest {

  public static final String TEST_NAME = "Carl";
  @Autowired
  private JPAAgency jpaAgency;

  private Agency agency;

  @MockBean
  private ProfileRepository profileRepository;

  @MockBean
  private ProfileMapper profileMapper;

  @PostConstruct
  void init() {
    agency = jpaAgency;
  }

  @Test
  void freelancers_mustCallProfileRepository() {

    //when
    agency.freelancers();

    //then
    verify(profileRepository, times(1)).findAll();
    verify(profileMapper, times(1)).toFreelances(anySet());
  }

  @Test
  void givenFreelancersInAgency_freelancers_mustReturnASetOfFreelancer() {

    Profile profile = new Profile();
    profile.setId(23);
    profile.setName(TEST_NAME);

    Freelancer freelancer = new Freelancer();
    freelancer.setId(23);
    freelancer.setName(TEST_NAME);

    List<Profile> expectedProfiles = Collections.singletonList(profile);
    Set<Freelancer> expectedFreelancers = Collections.singleton(freelancer);
    when(profileRepository.findAll()).thenReturn(expectedProfiles);
    when(profileMapper.toFreelances(anySet())).thenReturn(expectedFreelancers);

    //when
    Set<Freelancer> freelancers = agency.freelancers();

    //assert
    assertThat(freelancers.size()).isNotEqualTo(0);
    assertThat(freelancers.stream().anyMatch(f -> 23 == f.getId() && TEST_NAME.equals(f.getName()))).isTrue();
  }

  @Test
  void givenAFreelance_addFreelance_mustCallProfileRepositorySave() {

    Freelancer freelancer = new Freelancer();
    freelancer.setName(TEST_NAME);

    Profile profile = new Profile();
    profile.setName(TEST_NAME);

    when(profileMapper.toProfile(freelancer)).thenReturn(profile);
    when(profileRepository.save(profile)).thenAnswer(invocationOnMock -> {
      Profile profile1 = invocationOnMock.getArgument(0);
      profile1.setId(1);
      return profile1;
    });
    when(profileMapper.toFreelance(profile)).thenAnswer(invocationOnMock -> {
      Freelancer result = new Freelancer();
      result.setId(1);
      result.setName(TEST_NAME);
      return result;
    });

    //when
    Freelancer newFreelancer = agency.addFreelancer(freelancer);

    //assert
    assertThat(newFreelancer.getId()).isEqualTo(1);
    verify(profileMapper, times(1)).toFreelance(profile);
    verify(profileMapper, times(1)).toProfile(freelancer);
    verify(profileRepository, times(1)).save(profile);


  }

}

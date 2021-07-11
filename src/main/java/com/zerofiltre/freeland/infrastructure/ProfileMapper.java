package com.zerofiltre.freeland.infrastructure;

import com.zerofiltre.freeland.domain.Freelancer;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ProfileMapper {

  public abstract Freelancer toFreelance(Profile profile);

  public abstract Set<Freelancer> toFreelances(Set<Profile> profiles);

  public abstract Profile toProfile(Freelancer freelancer);
}

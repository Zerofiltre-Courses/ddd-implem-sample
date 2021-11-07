package com.zerofiltre.freeland.infra.mapper;

import com.zerofiltre.freeland.domain.entities.Client;
import com.zerofiltre.freeland.infra.dto.ClientDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ClientMapper {

  public abstract ClientDTO toDTO(Client client);

  public abstract Client toEntity(ClientDTO clientDTO);
}

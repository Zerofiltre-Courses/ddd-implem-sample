package com.zerofiltre.freeland.infra.providers.database.serviceContract;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContract;
import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;
import com.zerofiltre.freeland.infra.providers.database.serviceContract.mapper.ServiceContractJPAMapper;
import com.zerofiltre.freeland.infra.providers.database.serviceContract.model.ServiceContractJPA;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ServiceContractDatabaseProviderTest {

  public static final Long CONTRACT_NUMBER = 12L;
  private ServiceContractDatabaseProvider serviceContractDatabaseProvider;

  @Mock
  private ServiceContractJPARepository repository;

  @Mock
  private ServiceContractJPAMapper mapper;

  @BeforeEach
  void init() {
    serviceContractDatabaseProvider = new ServiceContractDatabaseProvider(repository, mapper);
  }

  @Test
  void serviceContractFromId_mustCallTheProperMethods() {
    //arrange
    ServiceContractId serviceContractId = new ServiceContractId(CONTRACT_NUMBER);
    ServiceContractJPA serviceContractJPA = new ServiceContractJPA();
    when(repository.findById(any())).thenReturn(Optional.of(serviceContractJPA));

    //act
    serviceContractDatabaseProvider.serviceContractOfId(serviceContractId);

    //assert
    verify(repository, times(1)).findById(CONTRACT_NUMBER);
    verify(mapper, times(1)).fromJPA(serviceContractJPA);

  }

  @Test
  void registerContract_mustCallTheProperMethods() {
    //arrange
    ServiceContractJPA serviceContractJPA = new ServiceContractJPA();
    ServiceContract serviceContract = new ServiceContract();
    when(mapper.toJPA(serviceContract)).thenReturn(serviceContractJPA);

    when(mapper.fromJPA(any())).thenReturn(serviceContract);

    when(repository.save(any())).thenReturn(serviceContractJPA);

    //act
    serviceContractDatabaseProvider.registerContract(serviceContract);

    //assert
    verify(mapper, times(1)).toJPA(any());

    verify(repository, times(1)).save(any());

    verify(mapper, times(1)).fromJPA(any());


  }
}
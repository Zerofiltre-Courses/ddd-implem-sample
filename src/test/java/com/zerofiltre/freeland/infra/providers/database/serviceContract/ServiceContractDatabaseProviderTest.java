package com.zerofiltre.freeland.infra.providers.database.serviceContract;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContract;
import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;
import com.zerofiltre.freeland.infra.providers.database.serviceContract.mapper.ServiceContractJPAMapper;
import com.zerofiltre.freeland.infra.providers.database.serviceContract.model.ServiceContractJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ServiceContractDatabaseProviderTest {

  public static final String CONTRACT_NUMBER = "xxxxxx";
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
    when(repository.getById(any())).thenReturn(serviceContractJPA);

    //act
    serviceContractDatabaseProvider.serviceContractFromId(serviceContractId);

    //assert
    verify(repository, times(1)).findByContractNumber(CONTRACT_NUMBER);
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
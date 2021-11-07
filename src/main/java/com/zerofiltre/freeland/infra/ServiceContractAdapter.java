package com.zerofiltre.freeland.infra;

import com.zerofiltre.freeland.application.ServiceContractFacade;
import com.zerofiltre.freeland.domain.entities.Client;
import com.zerofiltre.freeland.domain.entities.Freelancer;
import com.zerofiltre.freeland.domain.entities.ServiceContract;
import com.zerofiltre.freeland.infra.dto.ClientDTO;
import com.zerofiltre.freeland.infra.dto.FreelancerDTO;
import com.zerofiltre.freeland.infra.dto.ServiceContractDTO;
import com.zerofiltre.freeland.infra.mapper.ClientMapper;
import com.zerofiltre.freeland.infra.mapper.FreelancerMapper;
import com.zerofiltre.freeland.infra.mapper.ServiceContractMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ServiceContractAdapter implements ServiceContractFacade {


  private final ServiceContractRepository serviceContractRepository;
  private final ClientRepository clientRepository;
  private final FreelancerRepository freelancerRepository;
  private final ServiceContractMapper serviceContractMapper;
  private final ClientMapper clientMapper;
  private final FreelancerMapper freelancerMapper;


  @Override
  public void startServiceContract() {
    Client client = new Client();
    ClientDTO clientDTO = clientMapper.toDTO(client);
    Freelancer freelancer = new Freelancer();
    FreelancerDTO freelancerDTO = freelancerMapper.toDTO(freelancer);

    clientRepository.save(clientDTO);
    freelancerRepository.save(freelancerDTO);

    ServiceContract serviceContract = new ServiceContract();
    //fill service contract with data from client and freelancer

    ServiceContractDTO serviceContractDTO = serviceContractMapper.toDTO(serviceContract);

    serviceContractRepository.save(serviceContractDTO);


  }

  @Override
  public void stopServiceContract() {

  }
}

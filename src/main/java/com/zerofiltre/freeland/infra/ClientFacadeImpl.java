package com.zerofiltre.freeland.infra;

import com.zerofiltre.freeland.domain.entities.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientFacadeImpl implements ClientFacade {

  @Override
  public Client save() {
    System.out.println("Saving client");
    return new Client();
  }
}

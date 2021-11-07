package com.zerofiltre.freeland.domain.client;

import com.zerofiltre.freeland.domain.client.model.Client;
import com.zerofiltre.freeland.domain.client.model.ClientId;

public interface ClientProvider {

  Client clientOfId(ClientId clientId);

  Client registerClient(Client client);

}

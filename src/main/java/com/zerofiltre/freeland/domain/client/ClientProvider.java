package com.zerofiltre.freeland.domain.client;

import com.zerofiltre.freeland.domain.client.model.Client;
import com.zerofiltre.freeland.domain.client.model.ClientId;
import java.util.Optional;

public interface ClientProvider {

  Optional<Client> clientOfId(ClientId clientId);

  Client registerClient(Client client);

}

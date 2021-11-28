package tech.zerofiltre.freeland.infra.providers.database.client;

import tech.zerofiltre.freeland.infra.providers.database.client.model.ClientJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientJPARepository extends JpaRepository<ClientJPA, String> {

}

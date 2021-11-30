package tech.zerofiltre.freeland.infra.entrypoints.rest.client;

import lombok.*;
import org.springframework.web.bind.annotation.*;
import tech.zerofiltre.freeland.domain.client.*;
import tech.zerofiltre.freeland.domain.client.model.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.client.mapper.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.client.model.*;

@RestController
@RequestMapping("client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientVMMapper clientVMMapper;
    private final ClientProvider clientProvider;

    @PostMapping
    public ClientVM registerClient(@RequestBody ClientVM clientVM) {
        Client client = clientProvider.registerClient(clientVMMapper.fromVM(clientVM));
        return clientVMMapper.toVM(client);
    }
}

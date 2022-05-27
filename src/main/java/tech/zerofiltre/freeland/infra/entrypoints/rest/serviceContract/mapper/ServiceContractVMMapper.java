package tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.*;
import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.client.model.*;
import tech.zerofiltre.freeland.domain.servicecontract.model.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract.model.*;
import tech.zerofiltre.freeland.infra.providers.database.client.*;
import tech.zerofiltre.freeland.infra.providers.database.serviceContract.*;
import tech.zerofiltre.freeland.infra.providers.database.serviceContract.mapper.*;

@Mapper(componentModel = "spring")
public abstract class ServiceContractVMMapper {

    @Autowired
    private ClientJPARepository clientJPARepository;

    @Autowired
    private WagePortageAgreementJPARepository wagePortageAgreementJPARepository;

    @Autowired
    private WagePortageAgreementJPAMapper wagePortageAgreementJPAMapper;


    @Mapping(target = "serviceContractId", source = "contractNumber")
    @Mapping(target = "clientId", source = "clientSiren")
    @Mapping(target = "wagePortageAgreement", source = "wagePortageAgreementNumber")
    public abstract ServiceContract fromVM(ServiceContractVM serviceContractVM);


    @AfterMapping
    public void addRate(@MappingTarget ServiceContract.ServiceContractBuilder result, ServiceContractVM serviceContractVM) {
        result.rate(new Rate(serviceContractVM.getRateValue(), serviceContractVM.getRateCurrency(),
                serviceContractVM.getRateFrequency()));
    }

    @Mapping(target = "contractNumber", expression = "java(serviceContract.getServiceContractId().getContractNumber())")
    @Mapping(target = "rateValue", expression = "java(serviceContract.getRate().getValue())")
    @Mapping(target = "rateCurrency", expression = "java(serviceContract.getRate().getCurrency())")
    @Mapping(target = "rateFrequency", expression = "java(serviceContract.getRate().getFrequency())")
    @Mapping(target = "wagePortageAgreementNumber", source = "wagePortageAgreement")
    @Mapping(target = "clientSiren", source = "clientId")
    public abstract ServiceContractVM toVM(ServiceContract serviceContract);


    ServiceContractId toServiceContractId(long contractNumber) {
        return new ServiceContractId(contractNumber);
    }

    ClientId toClientId(String clientSiren) {
        if (clientSiren == null) {
            return null;
        }
        return new ClientId(clientSiren, null);
    }

    String toClientSiren(ClientId clientId) {
        if (clientId == null) {
            return null;
        }
        return clientId.getSiren();
    }

    WagePortageAgreement toWagePortageAgreement(long wagePortageAgreementNumber) {
        return wagePortageAgreementJPARepository.findById(wagePortageAgreementNumber)
                .map(wagePortageAgreementJPA -> wagePortageAgreementJPAMapper.fromJPA(wagePortageAgreementJPA))
                .orElse(null);
    }

    long toWagePortageAgreementNumber(WagePortageAgreement wagePortageAgreement) {
        if (wagePortageAgreement == null)
            return 0;
        WagePortageAgreementId wagePortageAgreementId = wagePortageAgreement.getWagePortageAgreementId();
        if (wagePortageAgreementId == null)
            return 0;
        return wagePortageAgreementId.getAgreementNumber();
    }


}

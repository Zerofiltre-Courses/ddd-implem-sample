package com.zerofiltre.freeland.infra.providers.notification;

import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContractEvent;
import com.zerofiltre.freeland.domain.serviceContract.useCases.serviceContract.ServiceContractNotifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceContractKafkaNotifier implements ServiceContractNotifier {

  private final KafkaTemplate<String, ServiceContractEvent> kafkaTemplate;

  @Override
  public void notify(ServiceContractEvent serviceContractEvent) {
    ListenableFuture<SendResult<String, ServiceContractEvent>> future = kafkaTemplate
        .send("service-contract-topic", serviceContractEvent);

    future.addCallback(new ListenableFutureCallback<>() {

      @Override
      public void onSuccess(SendResult<String, ServiceContractEvent> result) {
        log.info("Message [{}] delivered with offset {}",
            serviceContractEvent.toString(),
            result.getRecordMetadata().offset());
      }

      @Override
      public void onFailure(Throwable ex) {
        log.warn("Unable to deliver message [{}]. {}",
            serviceContractEvent.toString(),
            ex.getMessage());
      }
    });
  }
}


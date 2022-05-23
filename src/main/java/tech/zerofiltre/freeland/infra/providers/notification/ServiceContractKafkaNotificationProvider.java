package tech.zerofiltre.freeland.infra.providers.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContractEvent;
import tech.zerofiltre.freeland.application.useCases.serviceContract.ServiceContractNotificationProvider;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceContractKafkaNotificationProvider implements ServiceContractNotificationProvider {

  private final KafkaTemplate<String, ServiceContractEvent> kafkaTemplate;

  @Value("${service-contract.topic}")
  private String topic;

  @Override
  public void notify(ServiceContractEvent serviceContractEvent) {
    ListenableFuture<SendResult<String, ServiceContractEvent>> future = kafkaTemplate
        .send(topic, serviceContractEvent);

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


package tech.zerofiltre.freeland.infra.providers.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import tech.zerofiltre.freeland.domain.servicecontract.model.ServiceContractEvent;
import tech.zerofiltre.freeland.domain.servicecontract.ServiceContractNotificationProvider;
import tech.zerofiltre.freeland.infra.providers.notification.mapper.*;
import tech.zerofiltre.freeland.infra.providers.notification.model.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceContractKafkaNotificationProvider implements ServiceContractNotificationProvider {

  private final KafkaTemplate<String, KafkaServiceContractEvent> kafkaTemplate;

  @Value("${service-contract.topic}")
  private String topic;

  private final KafkaServiceContractEventMapper mapper;


  @Override
  public void notify(ServiceContractEvent serviceContractEvent) {
    KafkaServiceContractEvent payload = mapper.toKafka(serviceContractEvent);
    ListenableFuture<SendResult<String, KafkaServiceContractEvent>> future = kafkaTemplate
        .send(topic, payload);

    future.addCallback(new ListenableFutureCallback<>() {

      @Override
      public void onSuccess(SendResult<String, KafkaServiceContractEvent> result) {
        log.info("Message [{}] delivered with offset {}",
            payload.toString(),
            result.getRecordMetadata().offset());
      }

      @Override
      public void onFailure(Throwable ex) {
        log.warn("Unable to deliver message [{}]. {}",
            payload.toString(),
            ex.getMessage());
      }
    });
  }
}


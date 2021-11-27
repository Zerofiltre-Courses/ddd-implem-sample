package tech.zerofiltre.freeland.infra.providers.notification;

import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContractEvent;

@Slf4j
@Component
public class TestServiceContractKafkaListener {

  private final CountDownLatch latch = new CountDownLatch(1);

  private String payload = null;

  @KafkaListener(topics = "${service-contract.topic}", groupId = "${spring.kafka.consumer.group-id}")
  public void receive(ConsumerRecord<String, ServiceContractEvent> consumerRecord) {
    log.info("received payload='{}'", consumerRecord.toString());
    setPayload(consumerRecord.toString());
    latch.countDown();
  }

  public CountDownLatch getLatch() {
    return latch;
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }
}

package tech.zerofiltre.freeland.domain.serviceContract.useCases.serviceContract;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContractEvent;
import tech.zerofiltre.freeland.infra.providers.notification.TestServiceContractKafkaListener;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@TestPropertySource(properties = "service-contract.topic=embedded-test-topic")
@Disabled("Permission denied on file system")
class ServiceContractKafkaNotifierIT {

  @Autowired
  private TestServiceContractKafkaListener listener;

  @Autowired
  private ServiceContractNotifier producer;

  @Test
  public void givenEmbeddedKafkaBroker_whenSendingToSimpleProducer_thenMessageReceived()
      throws Exception {
    producer.notify(new ServiceContractEvent());
    listener.getLatch().await(10000, TimeUnit.MILLISECONDS);

    assertThat(listener.getLatch().getCount()).isEqualTo(0L);
    assertThat(listener.getPayload()).contains("embedded-test-topic");
  }
}
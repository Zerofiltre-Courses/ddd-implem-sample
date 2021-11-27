package tech.zerofiltre.freeland.domain.serviceContract.useCases.serviceContract;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContractEvent;
import tech.zerofiltre.freeland.domain.serviceContract.useCases.serviceContract.ServiceContractKafkaNotifierLiveIT.BrokerPropertiesOverrideInitializer;
import tech.zerofiltre.freeland.infra.providers.notification.TestServiceContractKafkaListener;

@SpringBootTest
@DirtiesContext
@Testcontainers
@ContextConfiguration(initializers = BrokerPropertiesOverrideInitializer.class)
class ServiceContractKafkaNotifierLiveIT {

  @Container
  public static KafkaContainer kafka =
      new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

  @Autowired
  private ServiceContractNotifier producer;

  @Autowired
  private TestServiceContractKafkaListener listener;

  @Test
  public void givenEmbeddedKafkaBroker_whenSendingToSimpleProducer_thenMessageReceived()
      throws Exception {
    producer.notify(new ServiceContractEvent());
    listener.getLatch().await(10000, TimeUnit.MILLISECONDS);

    assertThat(listener.getLatch().getCount()).isEqualTo(0L);
    assertThat(listener.getPayload()).contains("container-test-topic");
  }

  static class BrokerPropertiesOverrideInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {


    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
          configurableApplicationContext,
          "spring.kafka.bootstrap-servers=" + kafka.getBootstrapServers());

      TestPropertySourceUtils.addPropertiesFilesToEnvironment(
          configurableApplicationContext, "classpath:application.properties");
    }
  }

}

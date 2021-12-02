package tech.zerofiltre.freeland;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tech.zerofiltre.freeland.infra.providers.notification.ServiceContractKafkaNotificationProvider;

@SpringBootTest
@EnableAutoConfiguration(exclude = KafkaAutoConfiguration.class)
class FreelandTests {

  @MockBean
  ServiceContractKafkaNotificationProvider serviceContractKafkaNotifier;

  @Test
  void contextLoads() {

  }


}

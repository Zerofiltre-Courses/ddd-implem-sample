package com.zerofiltre.freeland.infra.providers.notification.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
class KafkaTopicConfig {

  @Bean
  public NewTopic serviceContractTopic() {
    return TopicBuilder.name("service-contract-topic").build();
  }
}


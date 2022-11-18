package cn.lico.geek.config;

import cn.lico.geek.config.properties.GeekKafkaTopicProperties;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author meteor
 */
@Configuration
public class KafkaInitialConfig {

    private GeekKafkaTopicProperties topicProperties;

    @Autowired
    public KafkaInitialConfig(GeekKafkaTopicProperties topicProperties) {
        this.topicProperties = topicProperties;
    }

    /**
     * 创建DataItemChangeTopic topic
     * @return
     */
    @Bean
    public NewTopic initialDataItemChangeTopic() {
        return new NewTopic(topicProperties.getDataItemChange(),16, (short) 1 );
    }

    /**
     * 创建UserAction topic
     * @return
     */
    @Bean
    public NewTopic initialUserActionTopic() {
        return new NewTopic(topicProperties.getUserAction(),16, (short) 1 );
    }
}

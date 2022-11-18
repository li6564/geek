package cn.lico.geek.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author meteor
 */
@Data
@Component
@ConfigurationProperties(prefix = "geek.kafka.topics")
public class GeekKafkaTopicProperties {
    /**
     * 数据条目更新
     */
    private String dataItemChange = "topic.geek.dataitem.change";

    /**
     * 用户相关操作
     */
    private String userAction = "topic.geek.useraction";
}

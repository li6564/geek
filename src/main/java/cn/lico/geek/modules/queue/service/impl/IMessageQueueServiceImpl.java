package cn.lico.geek.modules.queue.service.impl;

import cn.lico.geek.core.queue.message.DataItemChangeMessage;
import cn.lico.geek.modules.queue.service.IMessageQueueService;
import cn.lico.geek.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author：linan
 * @Date：2022/11/17 22:34
 */
@Service
@Slf4j
public class IMessageQueueServiceImpl implements IMessageQueueService {
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendDataItemChangeMessage(DataItemChangeMessage dataItemChangeMessage) {
        try {
            kafkaTemplate.send("topic1", JsonUtils.toJson(dataItemChangeMessage));
            System.out.println("kafka 发送消息"+dataItemChangeMessage.toString());
            log.debug("发送kafka消息：{}", dataItemChangeMessage.toString());
        }catch (Exception e){
            e.printStackTrace();
            log.error("Kafka消息发送失败：{}", e.getMessage());
        }
    }
}

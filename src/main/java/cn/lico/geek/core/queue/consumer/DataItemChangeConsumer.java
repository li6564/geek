package cn.lico.geek.core.queue.consumer;

import cn.lico.geek.core.listener.DataItemChangeListener;
import cn.lico.geek.core.queue.message.DataItemChangeMessage;
import cn.lico.geek.core.queue.message.DataItemChangeType;
import cn.lico.geek.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 数据更新监听器
 * 监听本系统所有的数据更新 操作：更新操作包括
 * @Author：linan
 * @Date：2022/11/17 21:16
 */
@Component
@Slf4j
public class DataItemChangeConsumer implements TopicConsumer, ApplicationContextAware {
    private Collection<DataItemChangeListener> listeners;

    @Override
    @KafkaListener(topics = "topic1")
    public void onMessage(ConsumerRecord<?, String> record) {

        Optional<String> kafkaMessage = Optional.ofNullable(record.value());
        if(kafkaMessage.isPresent()){
            DataItemChangeMessage dataItemChangeMessage;
            try {
                dataItemChangeMessage = JsonUtils.toBean(kafkaMessage.get(),DataItemChangeMessage.class);
                log.debug("接收到kafka消息：{}", dataItemChangeMessage.toString());
                // 调用每个监听器对应的消息处理函数
                DataItemChangeType changeType =dataItemChangeMessage.getChangeType();
                for (DataItemChangeListener listener : listeners) {
                    try {
                        switch (changeType) {
                            case ADD:
                                listener.onDataItemAdd(dataItemChangeMessage);
                                break;
                            case DELETE:
                                listener.onDataItemDelete(dataItemChangeMessage);
                                break;
                            case UPDATE:
                                listener.onDataItemUpdate(dataItemChangeMessage);
                                break;
                            case USER_NICK_UPDATE:
                                listener.onUserNickUpdate(dataItemChangeMessage);
                                break;
                            case USER_AVATAR_UPDATE:
                                listener.onUserAvatarUpdate(dataItemChangeMessage);
                                break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        log.error(e.getMessage());
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                log.error("DataItemUpdateMessage consume failed:{}", e.getMessage());
            }
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,DataItemChangeListener> listenerMap = applicationContext.getBeansOfType(DataItemChangeListener.class);
        if (!CollectionUtils.isEmpty(listenerMap)){
            this.listeners = listenerMap.values();
        }else {
            this.listeners = List.of();
        }
    }
}

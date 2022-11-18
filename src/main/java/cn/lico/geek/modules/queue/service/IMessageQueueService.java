package cn.lico.geek.modules.queue.service;

import cn.lico.geek.core.queue.message.DataItemChangeMessage;

/**
 * @Author：linan
 * @Date：2022/11/17 22:33
 */
public interface IMessageQueueService {
    /**
     * 发送搜索条目更新消息
     * @param dataItemChangeMessage
     */
    void sendDataItemChangeMessage(DataItemChangeMessage dataItemChangeMessage);
}

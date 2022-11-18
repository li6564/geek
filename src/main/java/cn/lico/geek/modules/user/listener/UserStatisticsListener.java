package cn.lico.geek.modules.user.listener;

import cn.lico.geek.core.listener.DataItemChangeListener;
import cn.lico.geek.core.queue.message.DataItemChangeMessage;
import cn.lico.geek.core.queue.message.DataItemChangeType;
import cn.lico.geek.core.queue.message.DataItemType;
import cn.lico.geek.modules.user.Service.UserStatisticsService;
import cn.lico.geek.modules.user.entity.UserStatistics;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author：linan
 * @Date：2022/11/17 21:51
 */
@Component
@Slf4j
public class UserStatisticsListener implements DataItemChangeListener {
    @Autowired
    private UserStatisticsService userStatisticsService;


    /**
     * 博客浏览量改变或回答被采纳
     * @param dataItemChangeMessage
     */
    @Override
    public void onDataItemAdd(DataItemChangeMessage dataItemChangeMessage){
        if (dataItemChangeMessage.getItemType().equals(DataItemType.BLOG_VIEW_NUM)){
            onDataChange(dataItemChangeMessage);
        }else if (dataItemChangeMessage.getItemType().equals(DataItemType.BLOG)){
            //发布博客事件增加博客数量
            onDataChange(dataItemChangeMessage);
        }

    }

    /**
     * 更新用户统计信息
     * @param dataItemChangeMessage
     */
    private void onDataChange(DataItemChangeMessage dataItemChangeMessage){
        DataItemType itemType = dataItemChangeMessage.getItemType();
        String itemId = dataItemChangeMessage.getItemId();
        String operatorId = dataItemChangeMessage.getOperatorId();
        //更新博客发布量
        if (itemType.equals(DataItemType.BLOG)){
            //新增博客发布量
            if (dataItemChangeMessage.getChangeType().equals(DataItemChangeType.ADD)){
                UserStatistics userStatistics = userStatisticsService.getById(operatorId);
                Integer blogNum = userStatistics.getBlogNum();
                userStatistics.setBlogNum(blogNum+1);
                userStatisticsService.updateById(userStatistics);
            }
        }
    }
}

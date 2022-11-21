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
     * 博客浏览量改变或博客数量新增或新增关注
     * @param dataItemChangeMessage
     */
    @Override
    public void onDataItemAdd(DataItemChangeMessage dataItemChangeMessage){
        if (dataItemChangeMessage.getItemType().equals(DataItemType.BLOG_VIEW_NUM)){
            System.out.println("执行了123111");
            onDataChange(dataItemChangeMessage);

        }else if (dataItemChangeMessage.getItemType().equals(DataItemType.BLOG)){
            //发布博客事件增加博客数量
            onDataChange(dataItemChangeMessage);
        }else if (dataItemChangeMessage.getItemType().equals(DataItemType.USER)){
            //新增关注数量
            onDataChange(dataItemChangeMessage);
        }else if (dataItemChangeMessage.getItemType().equals(DataItemType.POST)){
            //新增动态发布量
            onDataChange(dataItemChangeMessage);
        }
    }

    /**
     * 取消关注或删除博客
     * @param dataItemChangeMessage
     */
    @Override
    public void onDataItemDelete(DataItemChangeMessage dataItemChangeMessage){
        //用户粉丝数-1，用户关注数-1
        if (dataItemChangeMessage.getItemType().equals(DataItemType.USER)){
            onDataChange(dataItemChangeMessage);
        }else if (dataItemChangeMessage.getItemType().equals(DataItemType.POST)){
            //动态发布量-1
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
        DataItemChangeType changeType = dataItemChangeMessage.getChangeType();
        //更新博客发布量
        if (itemType.equals(DataItemType.BLOG)){
            //新增博客发布量
            if (changeType.equals(DataItemChangeType.ADD)){
                UserStatistics userStatistics = userStatisticsService.getById(operatorId);
                Integer blogNum = userStatistics.getBlogNum();
                userStatistics.setBlogNum(blogNum+1);
                userStatisticsService.updateById(userStatistics);
            }
        //更新博客浏览量
        }else if (itemType.equals(DataItemType.BLOG_VIEW_NUM)){
            //博客浏览量+1
            if (changeType.equals(DataItemChangeType.ADD)){
                UserStatistics userStatistics = userStatisticsService.getById(operatorId);
                userStatistics.setBlogViewNum(userStatistics.getBlogViewNum()+1);
                userStatisticsService.updateById(userStatistics);
            }
        //更新用户粉丝数和关注数
        }else if (itemType.equals(DataItemType.USER)){
            if (changeType.equals(DataItemChangeType.ADD)){
                //更新粉丝数
                UserStatistics userStatistics = userStatisticsService.getById(itemId);
                userStatistics.setFansNum(userStatistics.getFansNum()+1);
                userStatisticsService.updateById(userStatistics);
                //更新关注数
                UserStatistics userStatistics1 = userStatisticsService.getById(operatorId);
                userStatistics1.setFollowedNum(userStatistics1.getFollowedNum()+1);
                userStatisticsService.updateById(userStatistics1);
            }else if (changeType.equals(DataItemChangeType.DELETE)){
                //更新粉丝数
                UserStatistics userStatistics = userStatisticsService.getById(itemId);
                userStatistics.setFansNum(userStatistics.getFansNum()-1);
                userStatisticsService.updateById(userStatistics);
                //更新关注数
                UserStatistics userStatistics1 = userStatisticsService.getById(operatorId);
                userStatistics1.setFollowedNum(userStatistics1.getFollowedNum()-1);
                userStatisticsService.updateById(userStatistics1);
            }
        }else if (itemType.equals(DataItemType.POST)){
            //新增动态发布数
            if (changeType.equals(DataItemChangeType.ADD)){
                UserStatistics userStatistics = userStatisticsService.getById(operatorId);
                userStatistics.setPostNum(userStatistics.getPostNum()+1);
                userStatisticsService.updateById(userStatistics);
            }else if (changeType.equals(DataItemChangeType.DELETE)){
                //动态数-1
                UserStatistics userStatistics = userStatisticsService.getById(operatorId);
                userStatistics.setPostNum(userStatistics.getPostNum()-1);
                userStatisticsService.updateById(userStatistics);
            }
        }
    }
}

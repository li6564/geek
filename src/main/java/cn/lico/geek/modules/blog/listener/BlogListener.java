package cn.lico.geek.modules.blog.listener;

import cn.lico.geek.core.listener.DataItemChangeListener;
import cn.lico.geek.core.queue.message.DataItemChangeMessage;
import cn.lico.geek.core.queue.message.DataItemChangeType;
import cn.lico.geek.core.queue.message.DataItemType;
import cn.lico.geek.modules.blog.entity.Blog;
import cn.lico.geek.modules.blog.service.BlogService;
import cn.lico.geek.modules.notice.entity.Notice;
import cn.lico.geek.modules.notice.service.NoticeService;
import cn.lico.geek.modules.user.Service.UserStatisticsService;
import cn.lico.geek.modules.user.entity.UserStatistics;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author：linan
 * @Date：2022/11/17 21:51
 */
@Component
@Slf4j
public class BlogListener implements DataItemChangeListener {
    @Autowired
    private BlogService blogService;

    /**
     * 更新博客用户名
     * @param dataItemChangeMessage
     */
    @Override
    public void onUserNickUpdate(DataItemChangeMessage dataItemChangeMessage){
        if (dataItemChangeMessage.getItemType().equals(DataItemType.USER)){
            onDataChange(dataItemChangeMessage);
        }
    }


    /**
     * 更新博客用户名
     * @param dataItemChangeMessage
     */
    private void onDataChange(DataItemChangeMessage dataItemChangeMessage){
        DataItemType itemType = dataItemChangeMessage.getItemType();
        String itemId = dataItemChangeMessage.getItemId();
        String operatorId = dataItemChangeMessage.getOperatorId();
        String businessUid = dataItemChangeMessage.getBusinessUid();
        String userUid = dataItemChangeMessage.getUserUid();
        DataItemChangeType changeType = dataItemChangeMessage.getChangeType();
        //更新Blog作者名
        if (itemType.equals(DataItemType.USER)){
            if (changeType.equals(DataItemChangeType.USER_NICK_UPDATE)){
                LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Blog::getUserUid,itemId)
                        .eq(Blog::getStatus,1);
                List<Blog> list = blogService.list(queryWrapper);
                for (Blog blog : list) {
                    blog.setAuthor(userUid);
                }
            }
        }
    }
}

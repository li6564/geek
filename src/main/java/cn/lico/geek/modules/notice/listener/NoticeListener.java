package cn.lico.geek.modules.notice.listener;

import cn.lico.geek.core.listener.DataItemChangeListener;
import cn.lico.geek.core.queue.message.DataItemChangeMessage;
import cn.lico.geek.core.queue.message.DataItemChangeType;
import cn.lico.geek.core.queue.message.DataItemType;
import cn.lico.geek.modules.notice.entity.Notice;
import cn.lico.geek.modules.notice.service.NoticeService;
import cn.lico.geek.modules.question.entity.Question;
import cn.lico.geek.modules.user.entity.UserStatistics;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author：linan
 * @Date：2022/11/27 18:54
 */
@Component
@Slf4j
public class NoticeListener implements DataItemChangeListener {
    @Autowired
    private NoticeService noticeService;

    @Override
    public void onDataItemAdd(DataItemChangeMessage dataItemChangeMessage){
        if (dataItemChangeMessage.getItemType().equals(DataItemType.BLOG_COMMENT)){
            onDataChange(dataItemChangeMessage);
        }else if (dataItemChangeMessage.getItemType().equals(DataItemType.QUESTION_PRAISE)){
            onDataChange(dataItemChangeMessage);
        }else if (dataItemChangeMessage.getItemType().equals(DataItemType.BLOG_PRAISE)){
            onDataChange(dataItemChangeMessage);
        }else if (dataItemChangeMessage.getItemType().equals(DataItemType.POST_PRAISE)){
            onDataChange(dataItemChangeMessage);
        }
    }

    private void onDataChange(DataItemChangeMessage dataItemChangeMessage) {
        String itemId = dataItemChangeMessage.getItemId();
        String operatorId = dataItemChangeMessage.getOperatorId();
        DataItemChangeType changeType = dataItemChangeMessage.getChangeType();
        DataItemType itemType = dataItemChangeMessage.getItemType();
        String businessUid = dataItemChangeMessage.getBusinessUid();
        String userUid = dataItemChangeMessage.getUserUid();
        if (itemType.equals(DataItemType.BLOG_COMMENT)){ //博客评论消息通知
            if (changeType.equals(DataItemChangeType.ADD)){
                Notice notice = new Notice();
                notice.setBusinessUid(itemId).setUserUid(operatorId).setNoticeType(1).setBusinessType(1);
                System.out.println(notice.toString());
                noticeService.save(notice);
            }
        }else if (itemType.equals(DataItemType.QUESTION_PRAISE)){ //问答点赞消息通知
            if (changeType.equals(DataItemChangeType.ADD)){
                Notice notice = new Notice();
                notice.setNoticeType(3).setBusinessUid(businessUid).setUserUid(userUid).setBusinessType(7);
                noticeService.save(notice);
            }
        }else if(itemType.equals(DataItemType.BLOG_PRAISE)){  //博客点赞消息通知
            if (changeType.equals(DataItemChangeType.ADD)){
                Notice notice = new Notice();
                notice.setNoticeType(3).setBusinessType(6).setUserUid(userUid).setBusinessUid(businessUid);
                noticeService.save(notice);
            }
        }else if (itemType.equals(DataItemType.POST_PRAISE)){ //动态点赞消息通知
            if (changeType.equals(DataItemChangeType.ADD)){
                Notice notice = new Notice();
                notice.setNoticeType(3).setBusinessType(14).setUserUid(userUid).setBusinessUid(businessUid);
                noticeService.save(notice);
            }
        }
    }
}

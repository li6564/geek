package cn.lico.geek.modules.question.listener;

import cn.lico.geek.core.listener.DataItemChangeListener;
import cn.lico.geek.core.queue.message.DataItemChangeMessage;
import cn.lico.geek.core.queue.message.DataItemChangeType;
import cn.lico.geek.core.queue.message.DataItemType;
import cn.lico.geek.modules.question.entity.Question;
import cn.lico.geek.modules.question.service.QuestionService;
import cn.lico.geek.modules.user.Service.UserStatisticsService;
import cn.lico.geek.modules.user.entity.UserStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author：linan
 * @Date：2022/11/23 17:47
 */
@Component
@Slf4j
public class QuestionListener implements DataItemChangeListener {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserStatisticsService userStatisticsService;

    @Override
    public void onDataItemAdd(DataItemChangeMessage dataItemChangeMessage){
        //问答回答次数+1
        if (dataItemChangeMessage.getItemType().equals(DataItemType.QUESTION_REPLY)){
            onDataChange(dataItemChangeMessage);
        }
    }

    private void onDataChange(DataItemChangeMessage dataItemChangeMessage) {
        String itemId = dataItemChangeMessage.getItemId();
        String operatorId = dataItemChangeMessage.getOperatorId();
        DataItemChangeType changeType = dataItemChangeMessage.getChangeType();
        DataItemType itemType = dataItemChangeMessage.getItemType();
        if (itemType.equals(DataItemType.QUESTION_REPLY)){
            if (changeType.equals(DataItemChangeType.ADD)){
                //问答回答次数+1
                Question question = questionService.getById(itemId);
                question.setReplyCount(question.getReplyCount()+1);
                questionService.updateById(question);
                //用户回答次数+1
                UserStatistics userStatistics = userStatisticsService.getById(operatorId);
                userStatistics.setQuestionReplyNum(userStatistics.getQuestionReplyNum()+1);
                userStatisticsService.updateById(userStatistics);
            }

        }
    }

}

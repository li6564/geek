package cn.lico.geek.modules.user.Service.Impl;

import cn.lico.geek.core.queue.message.DataItemChangeMessage;
import cn.lico.geek.core.queue.message.DataItemChangeType;
import cn.lico.geek.core.queue.message.DataItemType;
import cn.lico.geek.modules.queue.service.IMessageQueueService;
import cn.lico.geek.modules.user.Service.UserPraiseRecordService;
import cn.lico.geek.modules.user.entity.UserPraiseRecord;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jdk.jfr.DataAmount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author：linan
 * @Date：2022/11/14 14:45
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserPraiseCountTest {
    @Autowired
    private UserPraiseRecordService userPraiseRecordService;

    @Autowired
    private IMessageQueueService messageQueueService;

    @Test
    public void sendMessage(){
        DataItemChangeMessage dataItemChangeMessage = new DataItemChangeMessage();
        dataItemChangeMessage.setItemType(DataItemType.USER);
        dataItemChangeMessage.setChangeType(DataItemChangeType.ADD);
        messageQueueService.sendDataItemChangeMessage(dataItemChangeMessage);
    }

    @Test
    public void getCount(){
        LambdaQueryWrapper<UserPraiseRecord> queryWrapper = new LambdaQueryWrapper<>();
        String s = "aa6e71e0c243b921ca657a900b4b442f";
        queryWrapper.eq(UserPraiseRecord::getResourceUid,s)
                .eq(UserPraiseRecord::getPraiseType,1)
                .eq(UserPraiseRecord::getStatus,1);
        int count = userPraiseRecordService.count(queryWrapper);
        System.out.println(count+"**********");
    }
}

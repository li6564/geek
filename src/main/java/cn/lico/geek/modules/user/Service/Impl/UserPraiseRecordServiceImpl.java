package cn.lico.geek.modules.user.Service.Impl;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.emuns.AppHttpCodeEnum;
import cn.lico.geek.core.queue.message.DataItemChangeMessage;
import cn.lico.geek.core.queue.message.DataItemChangeType;
import cn.lico.geek.core.queue.message.DataItemType;
import cn.lico.geek.modules.blog.form.BlogPraiseCountForm;
import cn.lico.geek.modules.blog.vo.BlogPraiseCountVo;
import cn.lico.geek.modules.queue.service.IMessageQueueService;
import cn.lico.geek.modules.user.Service.UserPraiseRecordService;
import cn.lico.geek.modules.user.entity.UserPraiseRecord;
import cn.lico.geek.modules.user.mapper.UserPraiseRecordMapper;
import cn.lico.geek.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Author：linan
 * @Date：2022/11/14 14:07
 */
@Service
public class UserPraiseRecordServiceImpl extends ServiceImpl<UserPraiseRecordMapper, UserPraiseRecord> implements UserPraiseRecordService {
    @Autowired
    private IMessageQueueService messageQueueService;

    /**
     * 获取当前用户关于博客的点赞信息
     * @param blogPraiseCountForm
     * @return
     */
    @Override
    public ResponseResult getPraiseCount(BlogPraiseCountForm blogPraiseCountForm) {
        BlogPraiseCountVo praiseRecordVo = new BlogPraiseCountVo();
        if (SecurityUtils.isLogin()){
            //获取当前用户的uid
            String userId = SecurityUtils.getUserId();
            //判断用户是否为当前博客点过赞
            LambdaQueryWrapper<UserPraiseRecord> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserPraiseRecord::getUserUid,userId);
            queryWrapper.eq(UserPraiseRecord::getResourceUid,blogPraiseCountForm.getResourceUid());
            queryWrapper.eq(UserPraiseRecord::getStatus,1);
            UserPraiseRecord praiseRecord = getOne(queryWrapper);
            //如果没有给点过赞则设置isPraise为false
            if (Objects.isNull(praiseRecord)){
                praiseRecordVo.setIsPraise(false);
            }else {
                //否则设置为true
                praiseRecordVo.setIsPraise(true);
            }
        }else {
            praiseRecordVo.setIsPraise(false);
        }

        //获取当前博客点赞的数量
        LambdaQueryWrapper<UserPraiseRecord> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(UserPraiseRecord::getResourceUid,blogPraiseCountForm.getResourceUid());
        queryWrapper1.eq(UserPraiseRecord::getResourceType,blogPraiseCountForm.getResourceType());
        queryWrapper1.eq(UserPraiseRecord::getStatus,1);
        int count = count(queryWrapper1);
        praiseRecordVo.setPraiseCount(count);

        return new ResponseResult(praiseRecordVo);
    }

    /**
     * 博客点赞
     * @param blogPraiseCountForm
     * @return
     */
    @Override
    public ResponseResult praise(BlogPraiseCountForm blogPraiseCountForm) {
        //获取当前用户uid
        String userId = SecurityUtils.getUserId();
        //如果之前点过赞则将状态字段设置为1即可
        LambdaQueryWrapper<UserPraiseRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPraiseRecord::getUserUid,userId);
        queryWrapper.eq(UserPraiseRecord::getResourceUid,blogPraiseCountForm.getResourceUid());
        UserPraiseRecord praiseRecord = getOne(queryWrapper);
        if (Objects.nonNull(praiseRecord)){
            praiseRecord.setStatus(1);
            updateById(praiseRecord);
        }else {
            //设置点赞记录信息
            UserPraiseRecord userPraiseRecord = new UserPraiseRecord();
            userPraiseRecord.setResourceType(blogPraiseCountForm.getResourceType());
            userPraiseRecord.setUserUid(userId);
            userPraiseRecord.setResourceUid(blogPraiseCountForm.getResourceUid());
            //存入数据库
            save(userPraiseRecord);
        }
        //如果点赞类型为2即问答模块点赞，则发送消息
//        if ("2".equals(blogPraiseCountForm.getResourceType())){
//            DataItemChangeMessage dataItemChangeMessage = new DataItemChangeMessage();
//            dataItemChangeMessage.setOperatorId(userId);
//            dataItemChangeMessage.setChangeType(DataItemChangeType.ADD);
//            dataItemChangeMessage.setItemType(DataItemType.QUESTION);
//            dataItemChangeMessage.setItemId(blogPraiseCountForm.getResourceUid());
//            messageQueueService.sendDataItemChangeMessage(dataItemChangeMessage);
//        }
        return new ResponseResult("点赞成功！",AppHttpCodeEnum.SUCCESS.getMsg());
    }

    /**
     * 取消点赞
     * @param blogPraiseCountForm
     * @return
     */
    @Override
    public ResponseResult cancelPraise(BlogPraiseCountForm blogPraiseCountForm) {
        //获取当前用户id
        String userId = SecurityUtils.getUserId();
        //删除点赞记录
        LambdaQueryWrapper<UserPraiseRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPraiseRecord::getResourceUid,blogPraiseCountForm.getResourceUid());
        queryWrapper.eq(UserPraiseRecord::getUserUid,userId);
        queryWrapper.eq(UserPraiseRecord::getResourceType,blogPraiseCountForm.getResourceType());

        UserPraiseRecord praiseRecord = getOne(queryWrapper);
        //设置状态为0
        praiseRecord.setStatus(0);
        updateById(praiseRecord);
        return new ResponseResult("取消成功！",AppHttpCodeEnum.SUCCESS.getMsg());
    }
}

package cn.lico.geek.modules.user.Service.Impl;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.emuns.AppHttpCodeEnum;
import cn.lico.geek.core.queue.message.DataItemChangeMessage;
import cn.lico.geek.core.queue.message.DataItemChangeType;
import cn.lico.geek.core.queue.message.DataItemType;
import cn.lico.geek.modules.queue.service.IMessageQueueService;
import cn.lico.geek.modules.user.Service.UserWatchService;
import cn.lico.geek.modules.user.dto.UserWatchDto;
import cn.lico.geek.modules.user.entity.UserWatch;
import cn.lico.geek.modules.user.mapper.UserWatchMapper;
import cn.lico.geek.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Author：linan
 * @Date：2022/11/13 22:33
 */
@Service
public class UserWatchServiceImpl extends ServiceImpl<UserWatchMapper, UserWatch> implements UserWatchService {
    @Autowired
    private IMessageQueueService messageQueueService;

    /**
     * 判断当前用户是否关注了博主
     * @param toUserUid
     * @return
     */
    @Override
    public UserWatchDto checkCurrentUserWatch(String toUserUid) {
        //获取当前用户id
        String userId = SecurityUtils.getUserId();
        //判断是否关注
        LambdaQueryWrapper<UserWatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserWatch::getToUserUid,toUserUid);
        queryWrapper.eq(UserWatch::getUserUid,userId);
        queryWrapper.eq(UserWatch::getStatus,1);
        UserWatch userWatch = getOne(queryWrapper);

        UserWatchDto userWatchDto = new UserWatchDto();
        userWatchDto.setCode("success");
        if (Objects.isNull(userWatch)){
            userWatchDto.setData(0);
        }else {
            userWatchDto.setData(1);
        }
        return userWatchDto;
    }

    /**
     * 关注操作
     * @param toUserUid
     * @return
     */
    @Override
    public ResponseResult addUserWatch(String toUserUid) {
        //获取当前用户id
        String userId = SecurityUtils.getUserId();
        //判断之前是否关注过，如果关注过则修改status字段为1 即可
        LambdaQueryWrapper<UserWatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserWatch::getToUserUid,toUserUid);
        queryWrapper.eq(UserWatch::getUserUid,userId);
        UserWatch userWatch = getOne(queryWrapper);
        UserWatch userWatch1 = new UserWatch();
        if (Objects.nonNull(userWatch)){
            userWatch.setStatus(1);
            updateById(userWatch);
        }else {//没有关注过则新增关注记录
            userWatch1.setUserUid(userId);
            userWatch1.setToUserUid(toUserUid);
            save(userWatch1);
        }
        //发送关注消息
        DataItemChangeMessage dataItemChangeMessage = new DataItemChangeMessage();
        dataItemChangeMessage.setItemId(toUserUid);
        dataItemChangeMessage.setItemType(DataItemType.USER);
        dataItemChangeMessage.setChangeType(DataItemChangeType.ADD);
        dataItemChangeMessage.setOperatorId(userId);
        if (Objects.nonNull(userWatch)){
            dataItemChangeMessage.setBusinessUid(userWatch.getUid());
        }else {
            dataItemChangeMessage.setBusinessUid(userWatch1.getUid());
        }
        dataItemChangeMessage.setUserUid(toUserUid);
        messageQueueService.sendDataItemChangeMessage(dataItemChangeMessage);
        return new ResponseResult("关注成功", AppHttpCodeEnum.SUCCESS.getMsg());

    }

    /**
     * 取消关注
     * @param toUserUid
     * @return
     */
    @Override
    public ResponseResult deleteUserWatch(String toUserUid) {
        //获取当前用户id
        String userId = SecurityUtils.getUserId();
        //获取关注记录，并设置status为0
        LambdaQueryWrapper<UserWatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserWatch::getToUserUid,toUserUid);
        queryWrapper.eq(UserWatch::getUserUid,userId);
        UserWatch userWatch = getOne(queryWrapper);
        userWatch.setStatus(0);
        updateById(userWatch);
        //发送取消关注消息
        DataItemChangeMessage dataItemChangeMessage = new DataItemChangeMessage();
        dataItemChangeMessage.setItemId(toUserUid);
        dataItemChangeMessage.setItemType(DataItemType.USER);
        dataItemChangeMessage.setChangeType(DataItemChangeType.DELETE);
        dataItemChangeMessage.setOperatorId(userId);
        messageQueueService.sendDataItemChangeMessage(dataItemChangeMessage);

        return new ResponseResult("取消关注成功",AppHttpCodeEnum.SUCCESS.getMsg());
    }
}

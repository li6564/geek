package cn.lico.geek.modules.user.Service.Impl;

import cn.lico.geek.common.dto.PageDTO;
import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.emuns.AppHttpCodeEnum;
import cn.lico.geek.core.queue.message.DataItemChangeMessage;
import cn.lico.geek.core.queue.message.DataItemChangeType;
import cn.lico.geek.core.queue.message.DataItemType;
import cn.lico.geek.modules.blog.vo.BlogInfoUser;
import cn.lico.geek.modules.queue.service.IMessageQueueService;
import cn.lico.geek.modules.user.Service.UserService;
import cn.lico.geek.modules.user.Service.UserWatchService;
import cn.lico.geek.modules.user.dto.UserWatchDto;
import cn.lico.geek.modules.user.dto.UserWatchListDto;
import cn.lico.geek.modules.user.entity.User;
import cn.lico.geek.modules.user.entity.UserWatch;
import cn.lico.geek.modules.user.form.UserWatchListForm;
import cn.lico.geek.modules.user.mapper.UserWatchMapper;
import cn.lico.geek.utils.BeanCopyUtils;
import cn.lico.geek.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Author：linan
 * @Date：2022/11/13 22:33
 */
@Service
public class UserWatchServiceImpl extends ServiceImpl<UserWatchMapper, UserWatch> implements UserWatchService {
    @Autowired
    private IMessageQueueService messageQueueService;

    @Autowired
    private UserService userService;

    /**
     * 判断当前用户是否关注了博主
     *
     * @param toUserUid
     * @return
     */
    @Override
    public UserWatchDto checkCurrentUserWatch(String toUserUid) {
        //获取当前用户id
        String userId = SecurityUtils.getUserId();
        //判断是否关注
        LambdaQueryWrapper<UserWatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserWatch::getToUserUid, toUserUid);
        queryWrapper.eq(UserWatch::getUserUid, userId);
        queryWrapper.eq(UserWatch::getStatus, 1);
        UserWatch userWatch = getOne(queryWrapper);

        UserWatchDto userWatchDto = new UserWatchDto();
        userWatchDto.setCode("success");
        if (Objects.isNull(userWatch)) {
            userWatchDto.setData(0);
        } else {
            userWatchDto.setData(1);
        }
        return userWatchDto;
    }

    /**
     * 关注操作
     *
     * @param toUserUid
     * @return
     */
    @Override
    public ResponseResult addUserWatch(String toUserUid) {
        //获取当前用户id
        String userId = SecurityUtils.getUserId();
        //判断之前是否关注过，如果关注过则修改status字段为1 即可
        LambdaQueryWrapper<UserWatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserWatch::getToUserUid, toUserUid);
        queryWrapper.eq(UserWatch::getUserUid, userId);
        UserWatch userWatch = getOne(queryWrapper);
        UserWatch userWatch1 = new UserWatch();
        if (Objects.nonNull(userWatch)) {
            userWatch.setStatus(1);
            updateById(userWatch);
        } else {//没有关注过则新增关注记录
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
        if (Objects.nonNull(userWatch)) {
            dataItemChangeMessage.setBusinessUid(userWatch.getUid());
        } else {
            dataItemChangeMessage.setBusinessUid(userWatch1.getUid());
        }
        dataItemChangeMessage.setUserUid(toUserUid);
        messageQueueService.sendDataItemChangeMessage(dataItemChangeMessage);
        return new ResponseResult("关注成功", AppHttpCodeEnum.SUCCESS.getMsg());

    }

    /**
     * 取消关注
     *
     * @param toUserUid
     * @return
     */
    @Override
    public ResponseResult deleteUserWatch(String toUserUid) {
        //获取当前用户id
        String userId = SecurityUtils.getUserId();
        //获取关注记录，并设置status为0
        LambdaQueryWrapper<UserWatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserWatch::getToUserUid, toUserUid);
        queryWrapper.eq(UserWatch::getUserUid, userId);
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

        return new ResponseResult("取消关注成功", AppHttpCodeEnum.SUCCESS.getMsg());
    }

    /**
     * 获取指定用户的粉丝或者关注列表
     *
     * @param userWatchListForm
     * @return
     */
    @Override
    public ResponseResult getUserWatchList(UserWatchListForm userWatchListForm) {
        //获取指定用户的粉丝或者关注者
        LambdaQueryWrapper<UserWatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(userWatchListForm.getToUserUid()),UserWatch::getToUserUid, userWatchListForm.getToUserUid())
                .eq(Objects.nonNull(userWatchListForm.getUserUid()),UserWatch::getUserUid,userWatchListForm.getUserUid())
                .eq(UserWatch::getStatus,1);

        queryWrapper.orderByDesc(UserWatch::getCreateTime);
        //进行分页查询
        Page<UserWatch> page = new Page<>(userWatchListForm.getCurrentPage(), userWatchListForm.getPageSize());
        page(page, queryWrapper);
        List<UserWatchListDto> userWatchListDtos = BeanCopyUtils.copyBeanList(page.getRecords(), UserWatchListDto.class);
        if (userWatchListDtos.size() > 0) {
            for (UserWatchListDto userWatchListDto : userWatchListDtos) {
                if (Objects.nonNull(userWatchListForm.getToUserUid())){
                    getFansInfo(userWatchListDto);
                }else {
                    getWatchList(userWatchListDto);
                }

                //判断当前操作者是否登录
                if (SecurityUtils.isLogin()) {
                    LambdaQueryWrapper<UserWatch> queryWrapper1 = new LambdaQueryWrapper<>();
                    queryWrapper1.eq(UserWatch::getUserUid, SecurityUtils.getUserId())
                            .eq(Objects.nonNull(userWatchListForm.getToUserUid()), UserWatch::getToUserUid, userWatchListDto.getUser().getUid())
                            .eq(Objects.nonNull(userWatchListForm.getUserUid()), UserWatch::getToUserUid, userWatchListDto.getUser().getUid())
                            .eq(UserWatch::getStatus, 1);
                    UserWatch userWatch = getOne(queryWrapper1);
                    if (Objects.isNull(userWatch)) {
                        userWatchListDto.setWatchStatus(0);
                    } else {
                        userWatchListDto.setWatchStatus(1);
                    }
                } else {
                    //如果没登陆则设置关注状态为0
                    userWatchListDto.setWatchStatus(0);
                }
            }

        }
        PageDTO<UserWatchListDto> pageDTO = new PageDTO<>();
        pageDTO.setRecords(userWatchListDtos);
        pageDTO.setCurrent((int) page.getCurrent());
        pageDTO.setSize((int) page.getSize());
        pageDTO.setTotal((int) page.getTotal());
        return new ResponseResult(pageDTO);
}

    //填充粉丝信息
    private void getFansInfo(UserWatchListDto userWatchListDto) {
        //获取粉丝uid
        String userUid = userWatchListDto.getUserUid();
        //根据uid进行查询
        User user = userService.getById(userUid);
        BlogInfoUser blogInfoUser = BeanCopyUtils.copyBean(user, BlogInfoUser.class);
        userWatchListDto.setUser(blogInfoUser);
    }
    //填充关注者列表
    private void getWatchList(UserWatchListDto userWatchListDto){
        //获取关注者id
        String toUserUid = userWatchListDto.getToUserUid();
        //根据uid进行查询
        User user = userService.getById(toUserUid);
        BlogInfoUser blogInfoUser = BeanCopyUtils.copyBean(user, BlogInfoUser.class);
        userWatchListDto.setUser(blogInfoUser);
    }
}

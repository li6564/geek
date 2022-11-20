package cn.lico.geek.modules.user.Service.Impl;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.user.Service.UserWatchService;
import cn.lico.geek.modules.user.dto.UserWatchDto;
import cn.lico.geek.modules.user.entity.UserWatch;
import cn.lico.geek.modules.user.mapper.UserWatchMapper;
import cn.lico.geek.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Author：linan
 * @Date：2022/11/13 22:33
 */
@Service
public class UserWatchServiceImpl extends ServiceImpl<UserWatchMapper, UserWatch> implements UserWatchService {
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
}

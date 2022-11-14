package cn.lico.geek.modules.user.Service.Impl;

import cn.lico.geek.modules.user.Service.UserWatchService;
import cn.lico.geek.modules.user.entity.UserWatch;
import cn.lico.geek.modules.user.mapper.UserWatchMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Author：linan
 * @Date：2022/11/13 22:33
 */
@Service
public class UserWatchServiceImpl extends ServiceImpl<UserWatchMapper, UserWatch> implements UserWatchService {
}

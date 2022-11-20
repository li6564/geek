package cn.lico.geek.modules.user.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.user.Service.UserWatchService;
import cn.lico.geek.modules.user.dto.UserWatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：linan
 * @Date：2022/11/18 17:11
 */
@RestController
@RequestMapping("/userwatch")
public class UserWatchApi {
    @Autowired
    private UserWatchService userWatchService;

    @PostMapping("/checkCurrentUserWatch")
    public UserWatchDto checkCurrentUserWatch(String toUserUid){
        return userWatchService.checkCurrentUserWatch(toUserUid);
    }
}

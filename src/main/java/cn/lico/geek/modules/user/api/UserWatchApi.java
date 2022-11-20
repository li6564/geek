package cn.lico.geek.modules.user.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.user.Service.UserWatchService;
import cn.lico.geek.modules.user.dto.UserWatchDto;
import cn.lico.geek.modules.user.form.ToUserUidForm;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author：linan
 * @Date：2022/11/18 17:11
 */
@RestController
@RequestMapping("/userwatch")
public class UserWatchApi {
    @Autowired
    private UserWatchService userWatchService;

    /**
     * 判断当前用户是否关注博主
     * @param toUserUid
     * @return
     */
    @PostMapping("/checkCurrentUserWatch")
    public UserWatchDto checkCurrentUserWatch(@RequestBody ToUserUidForm toUserUid){
        return userWatchService.checkCurrentUserWatch(toUserUid.getToUserUid());
    }

    /**
     * 关注操作
     * @param toUserUid
     * @return
     */
    @PostMapping("/addUserWatch")
    public ResponseResult addUserWatch(@RequestBody ToUserUidForm toUserUid){
        return userWatchService.addUserWatch(toUserUid.getToUserUid());
    }

    /**
     * 取消关注
     * @param toUserUid
     * @return
     */
    @PostMapping("/deleteUserWatch")
    public ResponseResult deleteUserWatch(@RequestBody ToUserUidForm toUserUid){
        return userWatchService.deleteUserWatch(toUserUid.getToUserUid());
    }
}

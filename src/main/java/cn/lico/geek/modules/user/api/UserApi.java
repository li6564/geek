package cn.lico.geek.modules.user.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.user.Service.UserService;
import cn.lico.geek.modules.user.Service.UserStatisticsService;
import cn.lico.geek.modules.user.form.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：linan
 * @Date：2022/11/12 17:48
 */
@RestController
@RequestMapping("/user")
public class UserApi {
    @Autowired
    private UserService userService;

    @Autowired
    private UserStatisticsService userStatisticsService;

    @PostMapping("/getUserTopN")
    public ResponseResult getUserTopN(@RequestBody PageForm pageForm){
        return userStatisticsService.getUserTopN(pageForm);
    }
}

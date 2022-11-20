package cn.lico.geek.modules.user.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.user.Service.LocalLoginService;
import cn.lico.geek.modules.user.Service.UserService;
import cn.lico.geek.modules.user.Service.UserStatisticsService;
import cn.lico.geek.modules.user.form.LoginForm;
import cn.lico.geek.modules.user.form.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private LocalLoginService localLoginService;

    @Autowired
    private UserStatisticsService userStatisticsService;

    /**
     * 获得社区精英列表
     * @param pageForm
     * @return
     */
    @PostMapping("/getUserTopN")
    public ResponseResult getUserTopN(@RequestBody PageForm pageForm){
        return userStatisticsService.getUserTopN(pageForm);
    }

    /**
     * 用户账号密码登录
     * @param loginForm
     * @return
     */
    @PostMapping("/login")
    public ResponseResult localLogin(@RequestBody LoginForm loginForm){
        return localLoginService.login(loginForm);
    }

    /**
     *
     * @param token
     * @return
     */
    @GetMapping("/verify/{token}")
    public ResponseResult authVerify(@PathVariable("token") String token){
        return userService.authVerify(token);
    }

    /**
     * 退出登录
     * @return
     */
    @PostMapping("/logout")
    public ResponseResult logout(){
        return localLoginService.logout();
    }
}

package cn.lico.geek.modules.user.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.user.Service.LocalLoginService;
import cn.lico.geek.modules.user.Service.UserService;
import cn.lico.geek.modules.user.Service.UserStatisticsService;
import cn.lico.geek.modules.user.form.LoginForm;
import cn.lico.geek.modules.user.form.PageForm;
import cn.lico.geek.modules.user.form.PasswordUpdateForm;
import cn.lico.geek.modules.user.form.UserRegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author：linan
 * @Date：2022/11/12 17:48
 */
@RestController
@RequestMapping("/userAuth")
public class UserAuthApi {
    @Autowired
    private UserService userService;

    @Autowired
    private LocalLoginService localLoginService;

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

    @PostMapping("/updateUserPassword")
    public ResponseResult updateUserPassword(@RequestBody PasswordUpdateForm passwordUpdateForm){
        return userService.updateUserPwd(passwordUpdateForm.getOldPwd(),passwordUpdateForm.getNewPwd());
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody UserRegisterForm userRegisterForm){
        return userService.register(userRegisterForm);
    }
}

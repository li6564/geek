package cn.lico.geek.modules.user.Service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.user.form.LoginForm;

/**
 * @Author：linan
 * @Date：2022/11/14 15:53
 */
public interface LocalLoginService {

    public ResponseResult login(LoginForm loginForm);

    public ResponseResult logout();
}

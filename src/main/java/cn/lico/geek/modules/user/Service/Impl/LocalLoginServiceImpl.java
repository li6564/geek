package cn.lico.geek.modules.user.Service.Impl;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.emuns.AppHttpCodeEnum;
import cn.lico.geek.modules.user.Service.LocalLoginService;
import cn.lico.geek.modules.user.form.LoginForm;
import cn.lico.geek.modules.user.form.UserDetailsForm;
import cn.lico.geek.utils.JwtUtil;
import cn.lico.geek.utils.RedisCache;
import cn.lico.geek.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author：linan
 * @Date：2022/11/14 15:54
 */
@Service
public class LocalLoginServiceImpl implements LocalLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(LoginForm loginForm) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginForm.getUserName(),loginForm.getPassWord());
        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);
        }catch (Exception e){
            return new ResponseResult("用户名或密码错误！",AppHttpCodeEnum.ERROR.getMsg());
        }
        UserDetailsForm userDetails = (UserDetailsForm) authenticate.getPrincipal();
        //若userDetails 为空  则登录失败
        if (Objects.isNull(userDetails)){
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getMsg());

        }
        //获取用户id
        String uid = userDetails.getUser().getUid();
        //用jwt进行加密生成token
        String token = JwtUtil.createJWT(uid);
        //将 userDetails 对象存入redis以id为健
        redisCache.setCacheObject("login"+uid,userDetails,7, TimeUnit.DAYS);
        //将token返回给前端
        Map<String,String> map = new HashMap<>();
        map.put("token",token);
        return new ResponseResult(map);
    }

    /**
     * 退出登录
     * @return
     */
    @Override
    public ResponseResult logout() {
        String uid = SecurityUtils.getUserId();
        boolean flag = redisCache.deleteObject("login" + uid);
        return ResponseResult.okResult();
    }
}

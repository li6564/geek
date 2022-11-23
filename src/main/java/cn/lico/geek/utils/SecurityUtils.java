package cn.lico.geek.utils;


import cn.lico.geek.modules.user.form.UserDetailsForm;
import com.aliyuncs.utils.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * @Author
 */
public class SecurityUtils
{

    /**
     * 获取用户
     **/
    public static UserDetailsForm getLoginUser()
    {
        return (UserDetailsForm) getAuthentication().getPrincipal();
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Boolean isAdmin(){
        String id = getLoginUser().getUser().getUid();
        return StringUtils.isNotEmpty(id);
    }
    public static Boolean isLogin(){
        return !"anonymousUser".equals(getAuthentication().getPrincipal());
    }

    public static String getUserId() {
        return getLoginUser().getUser().getUid();
    }
}
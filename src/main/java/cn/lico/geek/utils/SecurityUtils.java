package cn.lico.geek.utils;


import cn.lico.geek.modules.user.form.UserForm;
import com.aliyuncs.utils.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Author
 */
public class SecurityUtils
{

    /**
     * 获取用户
     **/
    public static UserForm getLoginUser()
    {
        return (UserForm) getAuthentication().getPrincipal();
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

    public static String getUserId() {
        return getLoginUser().getUser().getUid();
    }
}
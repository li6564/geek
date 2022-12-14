package cn.lico.geek.modules.user.Service;


import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.user.entity.User;
import cn.lico.geek.modules.user.exception.UserServiceException;
import cn.lico.geek.modules.user.form.PageForm;
import cn.lico.geek.modules.user.form.UserBlogForm;
import cn.lico.geek.modules.user.form.UserRegisterForm;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {


    ResponseResult authVerify(String token);

    ResponseResult getUserByUid(String userUid) throws UserServiceException;

    ResponseResult getUserCenterByUid(String adminUid);

    ResponseResult editUser(User user);


    ResponseResult updateUserPwd(String oldPwd, String newPwd);

    ResponseResult register(UserRegisterForm userRegisterForm);
}

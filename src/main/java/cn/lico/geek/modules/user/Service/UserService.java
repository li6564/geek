package cn.lico.geek.modules.user.Service;


import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.user.entity.User;
import cn.lico.geek.modules.user.form.PageForm;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {


    ResponseResult authVerify(String token);
}

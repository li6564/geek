package cn.lico.geek.modules.moment.service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.moment.entity.UserMoment;
import cn.lico.geek.modules.moment.form.UserMomentListForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author：linan
 * @Date：2022/11/20 17:10
 */
public interface UserMomentService extends IService<UserMoment> {
    ResponseResult getUserMomentList(UserMomentListForm userMomentListForm);
}

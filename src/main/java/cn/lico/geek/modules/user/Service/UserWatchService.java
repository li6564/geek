package cn.lico.geek.modules.user.Service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.user.dto.UserWatchDto;
import cn.lico.geek.modules.user.entity.UserWatch;
import cn.lico.geek.modules.user.form.UserWatchListForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author：linan
 * @Date：2022/11/13 22:32
 */
public interface UserWatchService extends IService<UserWatch> {
    UserWatchDto checkCurrentUserWatch(String toUserUid);

    ResponseResult addUserWatch(String toUserUid);

    ResponseResult deleteUserWatch(String toUserUid);

    ResponseResult getUserWatchList(UserWatchListForm userWatchListForm);
}

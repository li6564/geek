package cn.lico.geek.modules.user.Service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.user.entity.UserStatistics;
import cn.lico.geek.modules.user.form.PageForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author：linan
 * @Date：2022/11/13 14:40
 */
public interface UserStatisticsService extends IService<UserStatistics> {
    ResponseResult getUserTopN(PageForm pageForm);
}

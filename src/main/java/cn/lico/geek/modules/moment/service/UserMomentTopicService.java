package cn.lico.geek.modules.moment.service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.moment.entity.UserMomentTopic;
import cn.lico.geek.modules.moment.form.UserMomentTopicPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author：linan
 * @Date：2022/11/20 16:26
 */
public interface UserMomentTopicService extends IService<UserMomentTopic> {
    ResponseResult getUserMomentTopicList(UserMomentTopicPage userMomentTopicPage);
}

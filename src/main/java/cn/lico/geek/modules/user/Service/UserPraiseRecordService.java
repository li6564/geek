package cn.lico.geek.modules.user.Service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.blog.form.BlogPraiseCountForm;
import cn.lico.geek.modules.user.entity.UserPraiseRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author：linan
 * @Date：2022/11/14 14:07
 */
public interface UserPraiseRecordService extends IService<UserPraiseRecord> {
    ResponseResult getPraiseCount(BlogPraiseCountForm blogPraiseCountForm);

    ResponseResult praise(BlogPraiseCountForm blogPraiseCountForm);

    ResponseResult cancelPraise(BlogPraiseCountForm blogPraiseCountForm);
}

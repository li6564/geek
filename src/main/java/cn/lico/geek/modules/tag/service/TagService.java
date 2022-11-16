package cn.lico.geek.modules.tag.service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.blog.form.PageVo;
import cn.lico.geek.modules.tag.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author：linan
 * @Date：2022/11/12 12:55
 */
public interface TagService extends IService<Tag> {

    public ResponseResult getHotTag();

    ResponseResult getBlogTagList(PageVo pageVo);
}

package cn.lico.geek.modules.blog.service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.blog.entity.BlogSort;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author：linan
 * @Date：2022/11/11 15:11
 */
public interface BlogSortService extends IService<BlogSort> {

    public ResponseResult getHotBlogSort();
}

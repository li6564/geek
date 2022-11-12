package cn.lico.geek.modules.blog.service.Impl;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.emuns.AppHttpCodeEnum;
import cn.lico.geek.modules.blog.entity.BlogSort;
import cn.lico.geek.modules.blog.mapper.BlogSortMapper;
import cn.lico.geek.modules.blog.service.BlogSortService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Author：linan
 * @Date：2022/11/11 15:11
 */
@Service
public class BlogSortImpl extends ServiceImpl<BlogSortMapper, BlogSort> implements BlogSortService {
    /**
     * 查询所有的博客分类
     * @return
     */
    @Override
    public ResponseResult getHotBlogSort() {
        List<BlogSort> list = list();
        return new ResponseResult(list);
    }
}

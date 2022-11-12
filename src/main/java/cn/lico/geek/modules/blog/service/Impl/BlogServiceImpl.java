package cn.lico.geek.modules.blog.service.Impl;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.blog.entity.Blog;
import cn.lico.geek.modules.blog.mapper.BlogMapper;
import cn.lico.geek.modules.blog.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Author：linan
 * @Date：2022/11/8 11:56
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
    /**
     * 根据文章等级进行推荐展示
     * @param currentPage
     * @param pageSize
     * @param level
     * @param useSort
     * @return
     */
    @Override
    public ResponseResult getBlogByLevel(Integer currentPage, Integer pageSize, Integer level, Integer useSort) {
        return null;
    }
}

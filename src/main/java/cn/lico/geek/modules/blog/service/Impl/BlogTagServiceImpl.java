package cn.lico.geek.modules.blog.service.Impl;

import cn.lico.geek.modules.blog.entity.BlogSort;
import cn.lico.geek.modules.blog.entity.BlogTag;
import cn.lico.geek.modules.blog.mapper.BlogSortMapper;
import cn.lico.geek.modules.blog.mapper.BlogTagMapper;
import cn.lico.geek.modules.blog.service.BlogTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Author：linan
 * @Date：2022/11/12 13:57
 */
@Service
public class BlogTagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag> implements BlogTagService {
}

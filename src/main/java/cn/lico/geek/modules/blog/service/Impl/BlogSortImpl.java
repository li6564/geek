package cn.lico.geek.modules.blog.service.Impl;

import cn.lico.geek.common.dto.PageDTO;
import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.emuns.AppHttpCodeEnum;
import cn.lico.geek.modules.blog.entity.BlogSort;
import cn.lico.geek.modules.blog.form.PageVo;
import cn.lico.geek.modules.blog.mapper.BlogSortMapper;
import cn.lico.geek.modules.blog.service.BlogSortService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
        LambdaQueryWrapper<BlogSort> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogSort::getStatus,1);
        List<BlogSort> list = list(queryWrapper);
        return new ResponseResult(list);
    }

    /**
     * 创建博客时查询所有的博客分类
     * @param pageVo
     */
    @Override
    public ResponseResult getBlogSortList(PageVo pageVo) {
        LambdaQueryWrapper<BlogSort> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogSort::getStatus,1);
        Page<BlogSort> page = new Page<>(pageVo.getCurrentPage(),pageVo.getPageSize());
        page(page,queryWrapper);
        PageDTO<BlogSort> pageDTO = new PageDTO<>(page);
        return new ResponseResult(page);
    }
}

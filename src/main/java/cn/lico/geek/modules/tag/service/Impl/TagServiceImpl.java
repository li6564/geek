package cn.lico.geek.modules.tag.service.Impl;

import cn.lico.geek.common.dto.PageDTO;
import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.constant.RedisConstants;
import cn.lico.geek.modules.blog.entity.BlogTag;
import cn.lico.geek.modules.blog.form.PageVo;
import cn.lico.geek.modules.tag.entity.Tag;
import cn.lico.geek.modules.tag.mapper.TagMapper;
import cn.lico.geek.modules.tag.service.TagService;
import cn.lico.geek.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author：linan
 * @Date：2022/11/12 12:56
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper,Tag> implements TagService {
    @Autowired
    private RedisCache redisCache;

    /**
     * 获取热门标签
     * @return
     */
    @Override
    public ResponseResult getHotTag() {
        //现在redis中进行查询，如果存在直接返回
        List<Object> cacheList = redisCache.getCacheList(RedisConstants.HOT_TAGS);
        if (cacheList.size()>0){
            return new ResponseResult(cacheList);
        }
        //根据标签引用量进行排序
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Tag::getClickCount);
        //判断标签是否有效
        queryWrapper.eq(Tag::getStatus,1);
        //根据分页来获取前20个标签
        Page<Tag> page = new Page<>(1,20);
        page(page,queryWrapper);
        //在redis中缓存热门标签，并设置失效时间
        redisCache.setCacheList(RedisConstants.HOT_TAGS,page.getRecords());
        redisCache.expire(RedisConstants.HOT_TAGS,7, TimeUnit.DAYS);

        return new ResponseResult(page.getRecords());
    }

    /**
     * 获取全部有效的标签
     * @param pageVo
     * @return
     */
    @Override
    public ResponseResult getBlogTagList(PageVo pageVo) {
        //判断标签是否有效
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getStatus,1);
        Page<Tag> page = new Page<>(pageVo.getCurrentPage(),pageVo.getPageSize());
        page(page,queryWrapper);

        PageDTO<Tag> pageDTO = new PageDTO<>(page);
        return new ResponseResult(pageDTO);
    }
}

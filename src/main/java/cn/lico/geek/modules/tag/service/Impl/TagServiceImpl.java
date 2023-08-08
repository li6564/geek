package cn.lico.geek.modules.tag.service.Impl;

import cn.lico.geek.common.dto.PageDTO;
import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.constant.RedisConstants;
import cn.lico.geek.modules.blog.entity.BlogTag;
import cn.lico.geek.modules.blog.form.PageVo;
import cn.lico.geek.modules.tag.entity.Tag;
import cn.lico.geek.modules.tag.mapper.TagMapper;
import cn.lico.geek.modules.tag.service.TagService;
import cn.lico.geek.utils.CacheData;
import cn.lico.geek.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author：linan
 * @Date：2022/11/12 12:56
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper,Tag> implements TagService {
    @Autowired
    private RedisCache redisCache;

    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    /**
     * 获取热门标签
     * @return
     */
    @Override
    public ResponseResult getHotTag() {
        //1.先在redis中进行查询缓存
        CacheData<Tag> tagCacheData = redisCache.getCacheObject(RedisConstants.HOT_TAGS);
        //2.判断是否存在
        if (tagCacheData == null){
            //3.不存在直接返回
            return null;
        }
        //4.命中，判断是否过期
        LocalDateTime expireTime = tagCacheData.getExpireTime();
        //4.1.未过期，直接返回缓存信息
        if (expireTime.isAfter(LocalDateTime.now())){
            return new ResponseResult(tagCacheData);
        }
        //4.2.过期，需要重建缓存
        //5.缓存重建
        //5.1.获取互斥锁
        boolean isLock = redisCache.tryLock(RedisConstants.LOCK_TAG, RedisConstants.LOCK_TAG_TTL);
        //5.2. 判断是否获取锁成功
        if (isLock){
            //5.3如果成功，开启独立线程重建缓存
            try {
                //重建缓存
                CACHE_REBUILD_EXECUTOR.submit(()->{
                    buildTagCache(20L);
                });
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                redisCache.unlock(RedisConstants.LOCK_TAG);
            }
        }
        //5.4.返回过期信息
        return new ResponseResult(tagCacheData);
    }



    public void buildTagCache(Long expireTime){
        //根据标签引用量进行排序
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Tag::getClickCount);
        //判断标签是否有效
        queryWrapper.eq(Tag::getStatus,1);
        //根据分页来获取前20个标签
        Page<Tag> page = new Page<>(1,20);
        page(page,queryWrapper);
        //在redis中缓存热门标签，并设置失效时间
        CacheData<Tag> cacheData = new CacheData<Tag>();
        cacheData.setData(page.getRecords());
        cacheData.setExpireTime(LocalDateTime.now().plusSeconds(expireTime));
        redisCache.setCacheObject(RedisConstants.HOT_TAGS,cacheData);
        //redisCache.expire(RedisConstants.HOT_TAGS,7, TimeUnit.DAYS);

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

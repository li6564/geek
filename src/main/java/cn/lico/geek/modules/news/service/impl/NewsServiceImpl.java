package cn.lico.geek.modules.news.service.impl;

import cn.lico.geek.common.dto.PageDTO;
import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.news.dto.NewsListDto;
import cn.lico.geek.modules.news.entity.News;
import cn.lico.geek.modules.news.enums.NewsErrorCode;
import cn.lico.geek.modules.news.exception.NewsServiceException;
import cn.lico.geek.modules.news.mapper.NewsMapper;
import cn.lico.geek.modules.news.service.NewsService;
import cn.lico.geek.utils.BeanCopyUtils;
import cn.lico.geek.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author：linan
 * @Date：2022/11/30 19:42
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {
    @Autowired
    private RedisCache redisCache;

    /**
     * 获取资讯列表
     * @return
     */
    @Override
    public ResponseResult getlist() {
        LambdaQueryWrapper<News> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(News::getStatus,1);
        queryWrapper.orderByDesc(News::getCreateTime);
        //进行分页查询
        Page page = new Page(1,16);
        page(page,queryWrapper);

        PageDTO<News> pageDTO = new PageDTO<>();
        pageDTO.setRecords(page.getRecords());
        pageDTO.setSize((int)page.getSize());
        pageDTO.setTotal((int)page.getTotal());
        pageDTO.setCurrent((int)page.getCurrent());
        return new ResponseResult(pageDTO);
    }

    /**
     * 根据id获取资讯详情
     * @param oid
     * @param isLazy
     * @return
     */
    @Override
    public ResponseResult getNewsByUid(String remoteHost,Integer oid, Integer isLazy) throws NewsServiceException{
        LambdaQueryWrapper<News> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(News::getOid,oid);
        queryWrapper.eq(News::getStatus,1);
        News news = getOne(queryWrapper);
        if (Objects.nonNull(news)){
            Object cacheObject = redisCache.getCacheObject(news.getUid() + remoteHost);
            if (Objects.isNull(cacheObject)){
                redisCache.setCacheObject(news.getUid()+remoteHost,"1",60*60, TimeUnit.SECONDS);
                news.setClickCount(news.getClickCount()+1);
                updateById(news);
            }
            return new ResponseResult(news);
        }else {
            throw new NewsServiceException(NewsErrorCode.NEWS_NOT_FOUND);
        }
    }
}

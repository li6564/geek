package cn.lico.geek.modules.news.service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.news.entity.News;
import cn.lico.geek.modules.news.exception.NewsServiceException;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author：linan
 * @Date：2022/11/30 19:42
 */
public interface NewsService extends IService<News> {
    ResponseResult getlist();

    ResponseResult getNewsByUid(String remoteHost,Integer oid, Integer isLazy) throws NewsServiceException;
}

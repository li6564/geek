package cn.lico.geek.core.aspect;

import cn.lico.geek.modules.blog.entity.Blog;
import cn.lico.geek.modules.blog.service.BlogService;
import cn.lico.geek.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.Set;

/**
 * @Author：linan
 * @Date：2022/11/19 17:07
 */
@Aspect
@Component
@Slf4j
public class BrowseTimesAspect {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private BlogService blogService;
    /**
     * 切入点
     */
    @Pointcut("@annotation(cn.lico.geek.core.annotation.BrowseTimes)")
    public void checkPoint(){
    }

    /**
     * 执行指定方法后记录浏览次数
     * @param joinPoint
     */
    @Before(value = "checkPoint()")
    public void browseTimes(JoinPoint joinPoint){
        //获取请求主机的IP地址
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String remoteHost = requestAttributes.getRequest().getRemoteHost();
        //根据Ip 地址在 redis查询结果
        Object cacheObject = redisCache.getCacheObject(remoteHost);
        //如果查询结果为空，则新增记录到redids中，并且将该博客浏览量加1
        if (Objects.isNull(cacheObject)){
            //新增记录到redids中
            redisCache.setCacheObject(remoteHost,"1");
            //获取当前方法请求参数,即博客oid
            int oid = (int)joinPoint.getArgs()[0];
            //将博客浏览量+1
            LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Blog::getOid,oid);
            Blog blog = blogService.getOne(queryWrapper);
            blog.setClickCount(blog.getClickCount()+1);
            blogService.updateById(blog);
        }

    }
}

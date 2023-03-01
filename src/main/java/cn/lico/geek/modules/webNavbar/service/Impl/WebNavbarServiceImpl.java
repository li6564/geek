package cn.lico.geek.modules.webNavbar.service.Impl;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.constant.RedisConstants;
import cn.lico.geek.modules.webNavbar.entity.WebNavbar;
import cn.lico.geek.modules.webNavbar.mapper.WebNavbarMapper;
import cn.lico.geek.modules.webNavbar.service.WebNavbarService;
import cn.lico.geek.modules.webNavbar.vo.WebNavbarVo;
import cn.lico.geek.utils.BeanCopyUtils;
import cn.lico.geek.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author：linan
 * @Date：2022/11/13 19:51
 */
@Service
public class WebNavbarServiceImpl extends ServiceImpl<WebNavbarMapper, WebNavbar> implements WebNavbarService {
    @Autowired
    private RedisCache redisCache;

    /**
     * 获取导航栏列表
     * @param isShow
     * @return
     */
    @Override
    public ResponseResult getWebNavbarList(Integer isShow) {
        List<Object> cacheList = redisCache.getCacheList(RedisConstants.WEB_NAVBAR);
        if (cacheList.size()>0){
            return new ResponseResult(cacheList);
        }

        LambdaQueryWrapper<WebNavbar> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WebNavbar::getStatus,1);
        //判断导航栏是否可展示
        queryWrapper.eq(WebNavbar::getIsShow,isShow);
        //判断是否为一级菜单
        queryWrapper.eq(WebNavbar::getParentUid,"-1");
        queryWrapper.orderByDesc(WebNavbar::getSort);
        //获取一级菜单
        List<WebNavbar> list = list(queryWrapper);
        //转换为webNavbarVos
        List<WebNavbarVo> webNavbarVos = BeanCopyUtils.copyBeanList(list, WebNavbarVo.class);
        //给一级菜单添加子菜单
        for (WebNavbarVo webNavbarVo : webNavbarVos) {
            LambdaQueryWrapper<WebNavbar> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(WebNavbar::getParentUid,webNavbarVo.getUid());
            queryWrapper1.eq(WebNavbar::getIsShow,isShow);
            List<WebNavbar> childWebNavbarList = list(queryWrapper1);
            if (Objects.nonNull(childWebNavbarList)){
                webNavbarVo.setChildWebNavbar(childWebNavbarList);
            }
        }
        //在redis中缓存导航栏信息并设置有效日期
        redisCache.setCacheList(RedisConstants.WEB_NAVBAR,webNavbarVos);
        redisCache.expire(RedisConstants.WEB_NAVBAR,8, TimeUnit.DAYS);
        return new ResponseResult(webNavbarVos);
    }
}

package cn.lico.geek.modules.moment.service.impl;

import cn.lico.geek.common.dto.PageDTO;
import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.moment.entity.UserMomentTopic;
import cn.lico.geek.modules.moment.form.UserMomentTopicPage;
import cn.lico.geek.modules.moment.mapper.UserMomentTopicMapper;
import cn.lico.geek.modules.moment.service.UserMomentTopicService;
import cn.lico.geek.modules.moment.vo.UserMomentTopicVo;
import cn.lico.geek.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author：linan
 * @Date：2022/11/20 16:26
 */
@Service
public class UserMomentTopicServiceImpl extends ServiceImpl<UserMomentTopicMapper,UserMomentTopic> implements UserMomentTopicService {
    @Override
    public ResponseResult getUserMomentTopicList(UserMomentTopicPage userMomentTopicPage) {
        //根据分页查询动态主题
        LambdaQueryWrapper<UserMomentTopic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserMomentTopic::getStatus,1);
        queryWrapper.eq(UserMomentTopic::getIsPublish,1);
        Page<UserMomentTopic> page = new Page<>(userMomentTopicPage.getCurrentPage(),userMomentTopicPage.getPageSize());
        page(page,queryWrapper);
        //将查询结果转为userMomentTopicVo
        List<UserMomentTopicVo> userMomentTopicVos = BeanCopyUtils.copyBeanList(page.getRecords(), UserMomentTopicVo.class);
        //封装返回结果到PageDto中
        PageDTO<UserMomentTopicVo> pageDTO = new PageDTO<>();
        pageDTO.setRecords(userMomentTopicVos);
        pageDTO.setSize((int)page.getSize());
        pageDTO.setTotal((int)page.getTotal());
        pageDTO.setCurrent((int)page.getCurrent());
        return new ResponseResult(pageDTO);
    }
}

package cn.lico.geek.modules.user.Service.Impl;

import cn.lico.geek.common.dto.PageDTO;
import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.user.Service.UserService;
import cn.lico.geek.modules.user.Service.UserStatisticsService;
import cn.lico.geek.modules.user.entity.User;
import cn.lico.geek.modules.user.entity.UserStatistics;
import cn.lico.geek.modules.user.form.PageForm;
import cn.lico.geek.modules.user.mapper.UserStatisticsMapper;
import cn.lico.geek.modules.user.vo.UserTopVo;
import cn.lico.geek.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author：linan
 * @Date：2022/11/13 14:41
 */
@Service
public class UserStatisticsServiceImpl extends ServiceImpl<UserStatisticsMapper,UserStatistics> implements UserStatisticsService {

    @Autowired
    private UserService userService;

    /**
     * 获取社区精英
     * @param pageForm
     * @return
     */
    @Override
    public ResponseResult getUserTopN(PageForm pageForm) {

        LambdaQueryWrapper<UserStatistics> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(UserStatistics::getBlogNum,UserStatistics::getPostNum,UserStatistics::getQuestionNum);

        Page<UserStatistics> page = new Page<>(pageForm.getCurrentPage(),pageForm.getPageSize());
        page(page,queryWrapper);

        List<UserTopVo> userTopVos = BeanCopyUtils.copyBeanList(page.getRecords(), UserTopVo.class);

        //给userTopVo填充信息

        for (UserTopVo userTopVo : userTopVos) {
            User user = userService.getById(userTopVo.getUid());
            userTopVo.setNickName(user.getNickName());
            userTopVo.setPhotoUrl(user.getPhotoUrl());
            userTopVo.setUserTag(user.getUserTag());
        }
        PageDTO<UserTopVo> pageDTO = new PageDTO<>();
        pageDTO.setRecords(userTopVos);
        pageDTO.setSize((int)page.getSize());
        pageDTO.setTotal((int)page.getTotal());
        pageDTO.setCurrent((int)page.getCurrent());

        return new ResponseResult(pageDTO);
    }
}

package cn.lico.geek.modules.user.Service.Impl;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.user.Service.UserService;
import cn.lico.geek.modules.user.Service.UserStatisticsService;
import cn.lico.geek.modules.user.entity.User;
import cn.lico.geek.modules.user.entity.UserStatistics;
import cn.lico.geek.modules.user.form.PageForm;
import cn.lico.geek.modules.user.mapper.UserMapper;
import cn.lico.geek.utils.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{
    /**
     *根据token获取用户信息
     * @param token
     * @return
     */
    @Override
    public ResponseResult authVerify(String token) {
        Claims claims = null;
        //解析token
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String uid = claims.getSubject();
        //根据uid获取用户
        User user = getById(uid);

        return new ResponseResult(user);
    }
}

package cn.lico.geek.modules.user.Service.Impl;

import cn.lico.geek.modules.user.Service.UserService;
import cn.lico.geek.modules.user.entity.User;
import cn.lico.geek.modules.user.enums.LoginErrorCode;
import cn.lico.geek.modules.user.exception.UserDetailsServiceException;
import cn.lico.geek.modules.user.form.UserDetailsForm;
import cn.lico.geek.modules.user.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Author：linan
 * @Date：2022/11/14 16:16
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 根据用户名判断用户是否存在
     * @param s
     * @return
     * @throws UsernameNotFoundException
     */
    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println("查询用户");
        //根据用户名查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,s);
        User user = userMapper.selectOne(queryWrapper);
        //判断用户是否存在，如果不存在则抛出异常
        if (Objects.isNull(user)){
            throw new UserDetailsServiceException(LoginErrorCode.USER_NAME_NOT_FOUND);
        }

        return new UserDetailsForm(user);
    }
}

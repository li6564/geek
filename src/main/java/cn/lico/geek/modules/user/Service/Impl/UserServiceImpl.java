package cn.lico.geek.modules.user.Service.Impl;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.emuns.AppHttpCodeEnum;
import cn.lico.geek.core.queue.message.DataItemChangeMessage;
import cn.lico.geek.core.queue.message.DataItemChangeType;
import cn.lico.geek.core.queue.message.DataItemType;
import cn.lico.geek.modules.blog.service.BlogService;
import cn.lico.geek.modules.queue.service.IMessageQueueService;
import cn.lico.geek.modules.user.Service.UserService;
import cn.lico.geek.modules.user.Service.UserStatisticsService;
import cn.lico.geek.modules.user.Service.UserWatchService;
import cn.lico.geek.modules.user.dto.UserCenterDto;
import cn.lico.geek.modules.user.entity.User;
import cn.lico.geek.modules.user.entity.UserStatistics;
import cn.lico.geek.modules.user.entity.UserWatch;
import cn.lico.geek.modules.user.enums.UserErrorCode;
import cn.lico.geek.modules.user.exception.UserServiceException;
import cn.lico.geek.modules.user.form.UserRegisterForm;
import cn.lico.geek.modules.user.mapper.UserMapper;
import cn.lico.geek.modules.user.vo.UserVo;
import cn.lico.geek.utils.BeanCopyUtils;
import cn.lico.geek.utils.JwtUtil;
import cn.lico.geek.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{
    @Autowired
    private UserStatisticsService userStatisticsService;

    @Autowired
    private UserWatchService userWatchService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IMessageQueueService messageQueueService;

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

    /**
     * 根据用户id获取用户信息
     * @param userUid
     * @return
     */
    @Override
    public ResponseResult getUserByUid(String userUid) throws UserServiceException{
        //根据条件查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUid,userUid);
        queryWrapper.eq(User::getStatus,1);
        User user = getOne(queryWrapper);
        if (Objects.isNull(user)){
            throw new UserServiceException(UserErrorCode.USER_NOT_FOUND);
        }else {
            //将用户信息转化为userCenterVo
            UserVo userVo = BeanCopyUtils.copyBean(user, UserVo.class);
            //填充userCenterVo 信息
            //判断是否关注了用户
            if (!SecurityUtils.isLogin()){
                userVo.setIsWatchUser(false);
            }else {
                String userId = SecurityUtils.getUserId();
                LambdaQueryWrapper<UserWatch> queryWrapper1 = new LambdaQueryWrapper<>();
                queryWrapper1.eq(UserWatch::getUserUid,userId)
                        .eq(UserWatch::getToUserUid,userUid)
                        .eq(UserWatch::getStatus,1);
                UserWatch userWatch = userWatchService.getOne(queryWrapper1);
                if (Objects.nonNull(userWatch)){
                    userVo.setIsWatchUser(true);
                }else {
                    userVo.setIsWatchUser(false);
                }
            }
            //填充用户统计信息
            LambdaQueryWrapper<UserStatistics> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(UserStatistics::getUid,userUid);
            UserStatistics userStatistics = userStatisticsService.getOne(queryWrapper1);
            userVo.setBlogPublishCount(userStatistics.getBlogNum()).setBlogVisitCount(userStatistics.getBlogViewNum())
                    .setUserFollowCount(userStatistics.getFansNum()).setUserWatchCount(userStatistics.getFollowedNum())
                    .setUserMomentCount(userStatistics.getPostNum());

            return new ResponseResult(userVo);
        }

    }

    /**
     * 获取用户统计信息
     * @param adminUid
     * @return
     */
    @Override
    public ResponseResult getUserCenterByUid(String adminUid) {
        LambdaQueryWrapper<UserStatistics> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserStatistics::getUid,adminUid);

        UserStatistics userStatistics = userStatisticsService.getOne(queryWrapper);
        UserCenterDto userCenterDto = new UserCenterDto();
        userCenterDto.setBlogCount(userStatistics.getBlogNum()).setFollowCount(userStatistics.getFansNum())
                .setWatchCount(userStatistics.getFollowedNum()).setMomentCount(userStatistics.getPostNum())
                .setQuestionCount(userStatistics.getQuestionNum());
        return new ResponseResult(userCenterDto);
    }

    /**
     * 编辑用户资料
     * @param user
     * @return
     */
    @Override
    public ResponseResult editUser(User user) {
        boolean flag = updateById(user);
        if (flag){
            //发送用户昵称改变消息
            DataItemChangeMessage dataItemChangeMessage = new DataItemChangeMessage();
            dataItemChangeMessage.setItemId(user.getUid()).setItemType(DataItemType.USER).setChangeType(DataItemChangeType.USER_NICK_UPDATE)
                    .setUserUid(user.getNickName());
            messageQueueService.sendDataItemChangeMessage(dataItemChangeMessage);
            return new ResponseResult("更新成功！", AppHttpCodeEnum.SUCCESS.getMsg());
        }else {
            return new ResponseResult("更新失败！",AppHttpCodeEnum.ERROR.getMsg());
        }

    }

    /**
     * 修改密码
     * @param oldPwd
     * @param newPwd
     * @return
     */
    @Override
    public ResponseResult updateUserPwd(String oldPwd, String newPwd) {
        String userId = SecurityUtils.getUserId();
        User user = getById(userId);
        boolean flag = passwordEncoder.matches(oldPwd, user.getPassWord());
        if (!flag){
            return new ResponseResult("密码不正确！",AppHttpCodeEnum.ERROR.getMsg());
        }else {
            String newPassword = passwordEncoder.encode(newPwd);
            user.setPassWord(newPassword);
            updateById(user);
        }
        return new ResponseResult("修改成功！",AppHttpCodeEnum.SUCCESS.getMsg());
    }

    /**
     * 用户注册
     * @param userRegisterForm
     * @return
     */
    @Override
    public ResponseResult register(UserRegisterForm userRegisterForm) {
        //先判断当前用户名是否存在，如果存在返回注册失败
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userRegisterForm.getUserName());
        queryWrapper.eq(User::getStatus,1);
        User user = getOne(queryWrapper);
        if (Objects.nonNull(user)){
            return new ResponseResult("注册失败,用户名已存在!",AppHttpCodeEnum.ERROR.getMsg());
        }
        User user1 = new User();
        user1.setUserName(userRegisterForm.getUserName()).setEmail(userRegisterForm.getEmail())
                .setNickName(userRegisterForm.getNickName());
        String encode = passwordEncoder.encode(userRegisterForm.getPassWord());
        user1.setPassWord(encode);
        save(user1);
        //创建用户统计信息
        UserStatistics userStatistics = new UserStatistics();
        userStatistics.setUid(user1.getUid());
        userStatisticsService.save(userStatistics);
        //发送用户注册消息
        DataItemChangeMessage dataItemChangeMessage = new DataItemChangeMessage();
        dataItemChangeMessage.setItemId(user1.getUid()).setChangeType(DataItemChangeType.USER_REGISTER).setItemType(DataItemType.USER);
        messageQueueService.sendDataItemChangeMessage(dataItemChangeMessage);
        return new ResponseResult("注册成功！",AppHttpCodeEnum.SUCCESS.getMsg());
    }

}

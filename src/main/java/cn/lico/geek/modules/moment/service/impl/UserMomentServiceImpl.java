package cn.lico.geek.modules.moment.service.impl;

import cn.lico.geek.common.dto.PageDTO;
import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.emuns.AppHttpCodeEnum;
import cn.lico.geek.core.queue.message.DataItemChangeMessage;
import cn.lico.geek.core.queue.message.DataItemChangeType;
import cn.lico.geek.core.queue.message.DataItemType;
import cn.lico.geek.modules.blog.vo.BlogInfoUser;
import cn.lico.geek.modules.moment.entity.MomentPhoto;
import cn.lico.geek.modules.moment.entity.MomentTopic;
import cn.lico.geek.modules.moment.entity.UserMoment;
import cn.lico.geek.modules.moment.entity.UserMomentTopic;
import cn.lico.geek.modules.moment.form.UserMomentAddForm;
import cn.lico.geek.modules.moment.form.UserMomentDeleteForm;
import cn.lico.geek.modules.moment.form.UserMomentListForm;
import cn.lico.geek.modules.moment.mapper.UserMomentMapper;
import cn.lico.geek.modules.moment.service.MomentPhotoService;
import cn.lico.geek.modules.moment.service.MomentTopicService;
import cn.lico.geek.modules.moment.service.UserMomentService;
import cn.lico.geek.modules.moment.service.UserMomentTopicService;
import cn.lico.geek.modules.moment.vo.PraiseInfo;
import cn.lico.geek.modules.moment.vo.UserMomentVo;
import cn.lico.geek.modules.queue.service.IMessageQueueService;
import cn.lico.geek.modules.user.Service.UserPraiseRecordService;
import cn.lico.geek.modules.user.Service.UserService;
import cn.lico.geek.modules.user.Service.UserWatchService;
import cn.lico.geek.modules.user.entity.User;
import cn.lico.geek.modules.user.entity.UserPraiseRecord;
import cn.lico.geek.modules.user.entity.UserWatch;
import cn.lico.geek.utils.BeanCopyUtils;
import cn.lico.geek.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author：linan
 * @Date：2022/11/20 17:11
 */
@Service
public class UserMomentServiceImpl extends ServiceImpl<UserMomentMapper, UserMoment> implements UserMomentService {
    @Autowired
    private MomentPhotoService momentPhotoService;

    @Autowired
    private MomentTopicService momentTopicService;

    @Autowired
    private UserWatchService userWatchService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserPraiseRecordService userPraiseRecordService;

    @Autowired
    private UserMomentTopicService userMomentTopicService;

    @Autowired
    private IMessageQueueService messageQueueService;

    /**
     * 获取动态内容
     * @param userMomentListForm
     * @return
     */
    @Override
    public ResponseResult getUserMomentList(UserMomentListForm userMomentListForm) {
        LambdaQueryWrapper<UserMoment> queryWrapper = new LambdaQueryWrapper<>();
        //判断是否按照分类标签来查找动态
        if (Objects.nonNull(userMomentListForm.getTopicUids())&&userMomentListForm.getTopicUids().length()>0){
            LambdaQueryWrapper<MomentTopic> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(MomentTopic::getTopicUid,userMomentListForm.getTopicUids());
            //获取动态列表
            List<MomentTopic> momentTopics = momentTopicService.list(queryWrapper1);
            List<String> userMomentIds = new ArrayList<>();
            for (MomentTopic momentTopic : momentTopics) {
                userMomentIds.add(momentTopic.getMomentUid());
            }
            if (userMomentIds.size()>0){
                queryWrapper.in(UserMoment::getUid,userMomentIds);
            }
        }
        //判断是否查看关注动态
        if (Objects.nonNull(userMomentListForm.getOrderBy())&&userMomentListForm.getOrderBy().length()>0){
            //获取当前用户id
            String userId = SecurityUtils.getUserId();
            LambdaQueryWrapper<UserWatch> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(UserWatch::getUserUid,userId);
            queryWrapper1.eq(UserWatch::getStatus,1);
            List<UserWatch> list = userWatchService.list(queryWrapper1);
            List<String> Ids = new ArrayList<>();
            for (UserWatch userWatch : list) {
                Ids.add(userWatch.getToUserUid());
            }
            if (Objects.isNull(Ids)||Ids.size() == 0){
                queryWrapper.eq(UserMoment::getUserUid,"-1");
            }else {
                queryWrapper.in(UserMoment::getUserUid,Ids);
            }
        }
        //进行排序
        queryWrapper.orderByDesc(UserMoment::getCreateTime);

        //分页查询
        Page<UserMoment> page = new Page<>(userMomentListForm.getCurrentPage(),userMomentListForm.getPageSize());
        page(page,queryWrapper);
        //将结果转为UserMomentVo
        List<UserMomentVo> userMomentVos = BeanCopyUtils.copyBeanList(page.getRecords(), UserMomentVo.class);
        //填充信息
        for (UserMomentVo userMomentVo : userMomentVos) {
            //填充user信息
            getUserInfo(userMomentVo.getUserUid(),userMomentVo);
            //填充点赞信息
            getPraiseInfo(userMomentVo.getUid(),userMomentVo);
            //填充话题信息
            getTopicInfo(userMomentVo.getUid(),userMomentVo);
            //填充图片列表‘
            getPhotoList(userMomentVo.getUid(),userMomentVo);

        }
        PageDTO<UserMomentVo> pageDTO = new PageDTO<>();
        pageDTO.setRecords(userMomentVos);
        pageDTO.setCurrent((int)page.getCurrent());
        pageDTO.setTotal((int)page.getTotal());
        pageDTO.setSize((int)page.getSize());
        return new ResponseResult(pageDTO);
    }

    /**
     *发布动态
     * @param userMomentAddForm
     * @return
     */
    @Override
    public ResponseResult addUserMoment(UserMomentAddForm userMomentAddForm) {
        //获取当前用户id
        String userId = SecurityUtils.getUserId();

        UserMoment userMoment = new UserMoment();
        userMoment.setContent(userMomentAddForm.getContent());
        userMoment.setIsPublish(1);
        userMoment.setUrl(userMomentAddForm.getUrl());
        userMoment.setUrlInfo(userMomentAddForm.getUrlInfo());
        userMoment.setUserUid(userId);
        //保存动态
        save(userMoment);
        //获取动态uid
        String uid = userMoment.getUid();
        //获取话题列表
        String[] topicList = userMomentAddForm.getTopicUids().split(",");
        //保存动态话题表
        for (String s : topicList) {
            MomentTopic momentTopic = new MomentTopic();
            momentTopic.setTopicUid(s);
            momentTopic.setMomentUid(uid);
            momentTopicService.save(momentTopic);
        }

        //获取 图片链接链表
        String[] photoUrlList = userMomentAddForm.getPictureUrl().split(",");
        //保存动态链接表
        for (String s : photoUrlList) {
            MomentPhoto momentPhoto = new MomentPhoto();
            momentPhoto.setMomentUid(uid);
            momentPhoto.setPhotoUrl(s);
            momentPhotoService.save(momentPhoto);
        }
        //

        //发送新增动态消息
        DataItemChangeMessage dataItemChangeMessage = new DataItemChangeMessage();
        dataItemChangeMessage.setOperatorId(userId);
        dataItemChangeMessage.setChangeType(DataItemChangeType.ADD);
        dataItemChangeMessage.setItemType(DataItemType.POST);
        messageQueueService.sendDataItemChangeMessage(dataItemChangeMessage);

        return new ResponseResult("发表动态成功", AppHttpCodeEnum.SUCCESS.getMsg());
    }

    /**
     * 删除动态
     * @param userMomentDeleteForm
     * @return
     */
    @Override
    public ResponseResult deleteBatch(UserMomentDeleteForm userMomentDeleteForm) {
        //获取当前用户的id
        String userId = SecurityUtils.getUserId();

        //获取要删除动态的uid
        String uid = userMomentDeleteForm.getUid();
        //获取要删除的动态记录
        UserMoment userMoment = getById(uid);
        //删除动态
        userMoment.setStatus(0);
        updateById(userMoment);
        //删除动态对应的话题
        LambdaQueryWrapper<MomentTopic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MomentTopic::getMomentUid,uid);
        momentTopicService.remove(queryWrapper);
        //删除动态的图片
        LambdaQueryWrapper<MomentPhoto> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(MomentPhoto::getMomentUid,uid);
        momentPhotoService.remove(queryWrapper1);
        //发送删除动态消息
        DataItemChangeMessage dataItemChangeMessage = new DataItemChangeMessage();
        dataItemChangeMessage.setOperatorId(userId);
        dataItemChangeMessage.setChangeType(DataItemChangeType.DELETE);
        dataItemChangeMessage.setItemType(DataItemType.POST);
        messageQueueService.sendDataItemChangeMessage(dataItemChangeMessage);

        return new ResponseResult("删除成功",AppHttpCodeEnum.SUCCESS.getMsg());
    }

    /**
     * 填充动态图片
     * @param uid
     * @param userMomentVo
     */
    private void getPhotoList(String uid, UserMomentVo userMomentVo) {
        LambdaQueryWrapper<MomentPhoto> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MomentPhoto::getMomentUid,uid);
        List<MomentPhoto> momentPhotoList = momentPhotoService.list(queryWrapper);
        List<String> photoList = new ArrayList<>();
        if (Objects.nonNull(momentPhotoList)){
            for (MomentPhoto momentPhoto : momentPhotoList) {
                photoList.add(momentPhoto.getPhotoUrl());
            }
        }
        userMomentVo.setPhotoList(photoList);
    }

    /**
     * 填充话题信息
     * @param uid
     * @param userMomentVo
     */
    private void getTopicInfo(String uid, UserMomentVo userMomentVo) {
        LambdaQueryWrapper<MomentTopic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MomentTopic::getMomentUid,userMomentVo.getUid());
        List<MomentTopic> momentTopicList = momentTopicService.list(queryWrapper);
        List<UserMomentTopic> userMomentTopicList = new ArrayList<>();
        if (Objects.nonNull(momentTopicList)){
            for (MomentTopic momentTopic : momentTopicList) {
                LambdaQueryWrapper<UserMomentTopic> queryWrapper1 = new LambdaQueryWrapper<>();
                queryWrapper1.eq(UserMomentTopic::getUid,momentTopic.getTopicUid());
                UserMomentTopic topic = userMomentTopicService.getOne(queryWrapper1);
                userMomentTopicList.add(topic);
            }
        }
        userMomentVo.setUserMomentTopicList(userMomentTopicList);
    }

    /**
     * 填充点赞信息
     * @param uid
     * @param userMomentVo
     */
    private void getPraiseInfo(String uid, UserMomentVo userMomentVo) {
        //获取当前用户id
        String userId = SecurityUtils.getUserId();
        PraiseInfo praiseInfo = new PraiseInfo();
        LambdaQueryWrapper<UserPraiseRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPraiseRecord::getResourceUid,uid);
        queryWrapper.eq(UserPraiseRecord::getStatus,1);
        int count = userPraiseRecordService.count(queryWrapper);
        queryWrapper.eq(UserPraiseRecord::getUserUid,userId);
        UserPraiseRecord userPraiseRecord = userPraiseRecordService.getOne(queryWrapper);
        //设置点赞数量
        praiseInfo.setPraiseCount(count);
        //如果点赞记录不为空
        if (Objects.nonNull(userPraiseRecord)){
            praiseInfo.setPraise(true);

        }else {
            praiseInfo.setPraise(false);
        }
        userMomentVo.setPraiseInfo(praiseInfo);
    }

    /**
     * 根据用户id获取用户信息
     * @param userUid
     */
    private void getUserInfo(String userUid,UserMomentVo userMomentVo) {
        User user = userService.getById(userUid);
        //转换为需要的形式
        BlogInfoUser infoUser = BeanCopyUtils.copyBean(user, BlogInfoUser.class);
        userMomentVo.setUser(infoUser);
    }
}

package cn.lico.geek.modules.project.service.impl;

import cn.lico.geek.common.dto.PageDTO;
import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.blog.vo.BlogInfoUser;
import cn.lico.geek.modules.blog.vo.BlogPraiseCountVo;
import cn.lico.geek.modules.project.entity.Project;
import cn.lico.geek.modules.project.entity.ProjectCategory;
import cn.lico.geek.modules.project.mapper.ProjectMapper;
import cn.lico.geek.modules.project.service.ProjectCategoryService;
import cn.lico.geek.modules.project.service.ProjectService;
import cn.lico.geek.modules.project.vo.ProjectVo;
import cn.lico.geek.modules.user.Service.UserPraiseRecordService;
import cn.lico.geek.modules.user.Service.UserService;
import cn.lico.geek.modules.user.entity.User;
import cn.lico.geek.modules.user.entity.UserPraiseRecord;
import cn.lico.geek.utils.BeanCopyUtils;
import cn.lico.geek.utils.RedisCache;
import cn.lico.geek.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author：linan
 * @Date：2023/2/19 15:25
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {
    @Autowired
    private UserService userService;

    @Autowired
    private UserPraiseRecordService userPraiseRecordService;

    @Autowired
    private ProjectCategoryService projectCategoryService;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult queryPage(Integer currentPage, Integer pageSize, String orderByDescColumn, Integer projectTagUid) {
        LambdaQueryWrapper<Project> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Project::getStatus,1);
        if (Objects.nonNull(projectTagUid)&&projectTagUid!=1){
            queryWrapper.eq(Project::getProjectCategoryId,projectTagUid);
        }
        if (orderByDescColumn.length()==0||Objects.isNull(orderByDescColumn)||"create_time".equals(orderByDescColumn)){
            queryWrapper.orderByDesc(Project::getCreateTime);
        }else {
            queryWrapper.orderByDesc(Project::getClickcount);
        }
        Page<Project> page = new Page<>(currentPage,pageSize);
        page(page,queryWrapper);
        List<Project> projectList = page.getRecords();
        List<ProjectVo> projectVos = BeanCopyUtils.copyBeanList(projectList, ProjectVo.class);
        for (ProjectVo projectVo : projectVos) {
            setUserInfo(projectVo.getUseruid(),projectVo);

            if (SecurityUtils.isLogin()){
                setPraiseInfo(SecurityUtils.getUserId(),projectVo);
            }else {
                setPraiseInfo("-1",projectVo);
            }

            setProjectCategoryList(projectVo);

        }

        PageDTO<ProjectVo> pageDTO = new PageDTO<>();
        pageDTO.setRecords(projectVos);
        pageDTO.setSize((int)page.getSize());
        pageDTO.setTotal((int)page.getTotal());
        pageDTO.setCurrent((int)page.getCurrent());
        return new ResponseResult(pageDTO);
    }

    @Override
    public ResponseResult getProjectByOid(Integer oid, Integer isLazy, String remoteHost) {
        LambdaQueryWrapper<Project> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Project::getOid,oid)
                .eq(Project::getStatus,1);
        Project project = getOne(queryWrapper);
        Object cacheObject = redisCache.getCacheObject(oid+remoteHost);
        //如果查询结果为空，则新增记录到redids中，并且将该博客浏览量加1
        if (Objects.isNull(cacheObject)){
            //新增记录到redids中
            redisCache.setCacheObject(oid+remoteHost,"1",60*60, TimeUnit.SECONDS);
            //将博客浏览量+1
            project.setClickcount(project.getClickcount()+1);
            updateById(project);
        }
        ProjectVo projectVo = BeanCopyUtils.copyBean(project, ProjectVo.class);
        setUserInfo(projectVo.getUseruid(),projectVo);

        if (SecurityUtils.isLogin()){
            setPraiseInfo(SecurityUtils.getUserId(),projectVo);
        }else {
            setPraiseInfo("-1",projectVo);
        }

        setProjectCategoryList(projectVo);
        return new ResponseResult(projectVo);
    }

    @Override
    public ResponseResult addProject(Project project) {
        String userId = SecurityUtils.getUserId();
        project.setUseruid(userId);
        project.setProjectCategoryId(2);
        save(project);
        return ResponseResult.okResult();
    }



    private void setUserInfo(String userid,ProjectVo projectVo){
        User user = userService.getById(userid);
        BlogInfoUser blogInfoUser = BeanCopyUtils.copyBean(user, BlogInfoUser.class);
        projectVo.setUser(blogInfoUser);
    }

    private void setPraiseInfo(String userid,ProjectVo projectVo){
        LambdaQueryWrapper<UserPraiseRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPraiseRecord::getResourceUid,projectVo.getUid())
                .eq(UserPraiseRecord::getUserUid,userid)
                .eq(UserPraiseRecord::getStatus,1);
        int count = userPraiseRecordService.count(queryWrapper);
        BlogPraiseCountVo blogPraiseCountVo = new BlogPraiseCountVo();
        if (count>0){
            blogPraiseCountVo.setIsPraise(true);
        }else{
            blogPraiseCountVo.setIsPraise(false);
        }

        LambdaQueryWrapper<UserPraiseRecord> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(UserPraiseRecord::getResourceUid,projectVo.getUid())
                .eq(UserPraiseRecord::getStatus,1);
        int count1 = userPraiseRecordService.count(queryWrapper1);
        blogPraiseCountVo.setPraiseCount(count1);

        projectVo.setPraiseInfo(blogPraiseCountVo);
    }

    private void setProjectCategoryList(ProjectVo projectVo){
        Integer projectCategoryId = projectVo.getProjectCategoryId();

        LambdaQueryWrapper<ProjectCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectCategory::getUid,projectCategoryId)
                .eq(ProjectCategory::getStatus,1);
        List<ProjectCategory> list = new ArrayList<>();
        ProjectCategory projectCategory = projectCategoryService.getOne(queryWrapper);
        list.add(projectCategory);
        projectVo.setProjectCategoryList(list);
    }
}

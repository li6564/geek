package cn.lico.geek.modules.project.service.impl;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.project.entity.ProjectCategory;
import cn.lico.geek.modules.project.mapper.ProjectCategoryMapper;
import cn.lico.geek.modules.project.service.ProjectCategoryService;
import cn.lico.geek.modules.project.vo.ProjectCategoryVo;
import cn.lico.geek.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Author：linan
 * @Date：2023/2/19 14:26
 */
@Service
public class ProjectCategoryServiceImpl extends ServiceImpl<ProjectCategoryMapper, ProjectCategory> implements ProjectCategoryService{
    @Override
    public ResponseResult getAllTagList() {
        LambdaQueryWrapper<ProjectCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectCategory::getStatus,1)
                .orderByDesc(ProjectCategory::getSort);
        List<ProjectCategory> list = list(queryWrapper);
        List<ProjectCategoryVo> projectCategoryVos = BeanCopyUtils.copyBeanList(list, ProjectCategoryVo.class);
//        for (ProjectCategoryVo projectCategoryVo : projectCategoryVos) {
//            if (projectCategoryVo.getParentUid().length() == 0 || Objects.isNull(projectCategoryVo.getParentUid())){
//                    projectCategoryVo.setChildren(null);
//            }
//        }
        return new ResponseResult(projectCategoryVos);
    }
}

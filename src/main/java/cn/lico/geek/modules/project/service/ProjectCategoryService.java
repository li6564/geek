package cn.lico.geek.modules.project.service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.project.entity.ProjectCategory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author：linan
 * @Date：2023/2/19 14:25
 */
public interface ProjectCategoryService extends IService<ProjectCategory> {
    ResponseResult getAllTagList();
}

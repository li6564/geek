package cn.lico.geek.modules.project.service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.project.entity.Project;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author：linan
 * @Date：2023/2/19 15:25
 */
public interface ProjectService extends IService<Project> {
    ResponseResult queryPage(Integer currentPage, Integer pageSize, String orderByDescColumn, Integer projectTagUid);

    ResponseResult addProject(Project project);

    ResponseResult getProjectByOid(Integer oid, Integer isLazy, String remoteHost);
}

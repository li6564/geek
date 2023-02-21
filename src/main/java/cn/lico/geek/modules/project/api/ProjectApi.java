package cn.lico.geek.modules.project.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.project.entity.Project;
import cn.lico.geek.modules.project.form.ProjectForm;
import cn.lico.geek.modules.project.service.ProjectCategoryService;
import cn.lico.geek.modules.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Author：linan
 * @Date：2023/2/19 14:29
 */
@RestController
@RequestMapping("/project")
public class ProjectApi {
    @Autowired
    private ProjectCategoryService projectCategoryService;

    @Autowired
    private ProjectService projectService;

    @PostMapping("/getAllTagList")
    public ResponseResult getAllTagList(){
        return projectCategoryService.getAllTagList();
    }

    @PostMapping("/queryPage")
    public ResponseResult queryPage(@RequestBody ProjectForm projectForm){
        return projectService.queryPage(projectForm.getCurrentPage(),projectForm.getPageSize(),projectForm.getOrderByDescColumn(),projectForm.getProblemTagUid());
    }

    @PostMapping("/add")
    public ResponseResult addProject(@RequestBody Project project){
        System.out.println(project.toString());
        return projectService.addProject(project);
    }

    @PostMapping("/getTypeList")
    public ResponseResult getTypeList(){
        return projectCategoryService.getAllTagList();
    }

    /**
     * 根据项目oid获取项目
     * @param oid
     * @param isLazy
     * @return
     */
    @PostMapping("/getProject")
    public ResponseResult getProjectByOid(@RequestParam Integer oid,
                                       @RequestParam Integer isLazy){
        //获取请求主机的IP地址
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String remoteHost = requestAttributes.getRequest().getRemoteHost();

        return projectService.getProjectByOid(oid,isLazy,remoteHost);
    }
}

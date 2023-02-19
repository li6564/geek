package cn.lico.geek.modules.project.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.project.form.ProjectForm;
import cn.lico.geek.modules.project.service.ProjectCategoryService;
import cn.lico.geek.modules.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return projectService.queryPage(projectForm.getCurrentPage(),projectForm.getPageSize(),projectForm.getOrderByDescColumn(),projectForm.getProjectTagUid());
    }
}

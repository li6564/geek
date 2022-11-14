package cn.lico.geek.modules.tag.api;

/**
 * @Author：linan
 * @Date：2022/11/13 15:49
 */

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger.web.TagsSorter;

@RestController
@RequestMapping("/tag")
public class TagApi {
    @Autowired
    private TagService tagService;


    @GetMapping("/getHotTag")
    public ResponseResult getHotTag(){
        return tagService.getHotTag();
    }
}

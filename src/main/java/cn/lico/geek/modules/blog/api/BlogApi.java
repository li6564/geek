package cn.lico.geek.modules.blog.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.emuns.AppHttpCodeEnum;
import cn.lico.geek.modules.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：linan
 * @Date：2022/11/8 11:37
 */
@RestController
@RequestMapping("/index")
public class BlogApi {
    @Autowired
    private BlogService blogService;

//    @GetMapping("/getBlogByLevel")
//    public ResponseResult getBlogByLevel(@RequestParam Integer currentPage,
//                                         @RequestParam Integer pageSize,
//                                         @RequestParam Integer level,
//                                         @RequestParam Integer useSort){
//        try {
//            return blogService.getBlogByLevel(currentPage,pageSize,level,useSort);
//        }catch (Exception e){
//            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
//        }

//    }
}

package cn.lico.geek.modules.blog.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.blog.service.BlogSortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：linan
 * @Date：2022/11/11 15:38
 */
@RestController
@RequestMapping("/blogsort")
public class BlogSortApi {

    @Autowired
    private BlogSortService blogSortService;

    @GetMapping("/getHotBlogSort")
    public ResponseResult getHotBlogSort(){
       return blogSortService.getHotBlogSort();
    }
}

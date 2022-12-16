package cn.lico.geek.modules.search.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.blog.service.BlogService;
import cn.lico.geek.modules.search.form.SearchForm;
import cn.lico.geek.modules.search.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author：linan
 * @Date：2022/12/4 14:42
 */
@RestController
@RequestMapping("/search")
public class SearchApi {
    @Autowired
    private ISearchService searchService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/elasticSearchAgg")
    public ResponseResult elasticSearchAgg(@RequestParam Integer currentPage,
                                           @RequestParam Integer pageSize,
                                           @RequestParam String keywords,
                                           @RequestParam(value = "resourceType",required = false) String resourceType){
        // spring的分页是从0开始的，对输入的页码减1后可以统一分页规则
        return searchService.searchPage(currentPage-1,pageSize,keywords,resourceType);
    }

    /**
     * 根据标签UID查询博客列表
     * @param tagUid
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/searchBlogByTag")
    public ResponseResult searchBlogByTag(@RequestParam String tagUid,
                                          @RequestParam Integer currentPage,
                                          @RequestParam Integer pageSize){
        return blogService.searchBlogByTag(tagUid,currentPage,pageSize);
    }

    /**
     * 根据博客分类查询博客
     * @param blogSortUid
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/searchBlogBySort")
    public ResponseResult searchBlogBySort(@RequestParam String blogSortUid,
                                           @RequestParam Integer currentPage,
                                           @RequestParam Integer pageSize){
        return blogService.searchBlogBySort(blogSortUid,currentPage,pageSize);
    }
}
package cn.lico.geek.modules.webNavbar.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.webNavbar.service.WebNavbarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：linan
 * @Date：2022/11/13 19:52
 */
@RestController
@RequestMapping("/index")
public class WebNavbarApi {
    @Autowired
    private WebNavbarService webNavbarService;

    /**
     * 获取导航栏列表
     * @param isShow
     * @return
     */
    @GetMapping("/getWebNavbar")
    public ResponseResult getWebNavbarList(@RequestParam Integer isShow){
        return webNavbarService.getWebNavbarList(isShow);
    }
}

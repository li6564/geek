package cn.lico.geek.modules.webNavbar.service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.webNavbar.entity.WebNavbar;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author：linan
 * @Date：2022/11/13 19:51
 */
public interface WebNavbarService extends IService<WebNavbar> {
    ResponseResult getWebNavbarList(Integer isShow);
}

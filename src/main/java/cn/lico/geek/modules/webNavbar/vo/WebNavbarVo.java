package cn.lico.geek.modules.webNavbar.vo;

import cn.lico.geek.modules.webNavbar.entity.WebNavbar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author：linan
 * @Date：2022/11/13 19:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebNavbarVo {
    private String uid;

    private String name;

    private Integer navbarLevel;

    private String summary;

    private String parentUid;

    private String url;

    private String icon;

    private Integer isShow;

    private Integer isJumpExternalUrl;

    private Integer sort;

    private Integer status;

    private List<WebNavbar> childWebNavbar;

    private Date createTime;


    private Date updateTime;
}

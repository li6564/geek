package cn.lico.geek.modules.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author：linan
 * @Date：2022/11/16 14:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogInfoUser {
    private String nickName;

    private Integer gender;

    private String occupation;

    private String summary;

    private Integer userTag;

    private Integer loadingValid;

    private Integer credits;

    private String photoUrl;

    private Integer blogPublishCount;

    private Integer blogVisitCount;

    private String backgroundFileUrl;

    private Integer userLevel;

    private String uid;

    private Integer status;

    private Date createTime;



}

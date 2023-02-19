package cn.lico.geek.modules.blog.vo;

import cn.lico.geek.modules.blog.entity.BlogSort;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author：linan
 * @Date：2022/11/12 12:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewBlogVo {

    private Integer oid;

    private String uid;

    private String title;

    private String summary;

    private Integer clickCount;

    private Integer collectCount;

    private Integer praiseCount;

    private Date createTime;

    private String userUid;

    private String outsideLink;

    //类型【0 博客， 1：推广】
    private Integer type;

    private String photoList;

    private NewBlogUserVo user;

    private BlogSort blogSort;

    private List tagList;


}

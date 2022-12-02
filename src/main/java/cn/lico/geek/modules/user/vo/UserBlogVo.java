package cn.lico.geek.modules.user.vo;

import cn.lico.geek.modules.blog.entity.BlogSort;
import cn.lico.geek.modules.blog.vo.NewBlogUserVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @Author：linan
 * @Date：2022/12/2 16:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserBlogVo {

    private Integer oid;

    private String uid;

    private String title;

    private String summary;

    private Integer clickCount;

    private Integer collectCount;

    private Integer praiseCount;

    private Date createTime;

    private String outsideLink;

    //类型【0 博客， 1：推广】
    private Integer type;

    private String author;

    private String photoList;

    private BlogSort blogSort;

    private List tagList;
}

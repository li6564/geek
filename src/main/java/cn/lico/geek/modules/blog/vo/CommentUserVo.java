package cn.lico.geek.modules.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author：linan
 * @Date：2022/11/16 16:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentUserVo {

    private String nickName;

    private Integer gender;

    private Integer userTag;

    private String photoUrl;

    private String uid;

    private Integer status;

    private Date createTime;
}

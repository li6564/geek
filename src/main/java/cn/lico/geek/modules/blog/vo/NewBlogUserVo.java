package cn.lico.geek.modules.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：linan
 * @Date：2022/11/12 12:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewBlogUserVo {

    private String nickName;

    private Integer userTag;

    private String photoUrl;
}

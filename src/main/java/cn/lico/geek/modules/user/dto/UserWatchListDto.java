package cn.lico.geek.modules.user.dto;

import cn.lico.geek.modules.blog.vo.BlogInfoUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Author：linan
 * @Date：2022/12/2 17:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserWatchListDto {
    private String userUid;

    private String toUserUid;

    private Integer isAdmin = 0;

    private BlogInfoUser user;

    private Integer watchStatus;

    private String uid;

    private Integer status;

    private Date createTime;

    private Date updateTime;


}

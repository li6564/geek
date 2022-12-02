package cn.lico.geek.modules.user.form;

import lombok.Data;

/**
 * @Author：linan
 * @Date：2022/12/2 16:49
 */
@Data
public class UserBlogForm {
    private Integer currentPage;

    private String orderByDescColumn;

    private Integer pageSize;

    private String userUid;
}

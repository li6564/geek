package cn.lico.geek.modules.user.form;

import lombok.Data;

/**
 * @Author：linan
 * @Date：2022/12/2 17:42
 */
@Data
public class UserWatchListForm {
    private Integer currentPage;

    private Integer pageSize;

    private String userUid;

    private String toUserUid;
}

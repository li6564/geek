package cn.lico.geek.modules.moment.form;

import lombok.Data;

/**
 * @Author：linan
 * @Date：2022/11/20 16:59
 */
@Data
public class UserMomentListForm {
    private Integer currentPage;

    //查询条件
    private String orderBy;
    //排序规则
    private String orderByDescColumn;

    private Integer pageSize;

    private String topicUids;

    private String uid;

    private String userUid;
}

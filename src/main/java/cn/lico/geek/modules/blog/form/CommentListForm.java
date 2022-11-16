package cn.lico.geek.modules.blog.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：linan
 * @Date：2022/11/16 16:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentListForm {
    private String blogUid;

    private Integer currentPage;

    private Integer pageSize;

    private String source;
}

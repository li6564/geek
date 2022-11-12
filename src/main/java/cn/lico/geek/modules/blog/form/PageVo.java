package cn.lico.geek.modules.blog.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：linan
 * @Date：2022/11/12 12:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVo {
    private String blogSortUid;

    private Integer currentPage;

    private String orderBy;

    private String orderByDescColumn;

    private Integer pageSize;
}

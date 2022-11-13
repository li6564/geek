package cn.lico.geek.modules.user.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：linan
 * @Date：2022/11/12 17:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageForm {
    private Integer currentPage;

    private Integer pageSize;

    private boolean refresh;
}

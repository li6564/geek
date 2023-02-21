package cn.lico.geek.modules.project.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：linan
 * @Date：2023/2/19 15:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectForm {
    private Integer currentPage;

    private Integer pageSize;

    private String orderByDescColumn;

    private Integer problemTagUid;
}

package cn.lico.geek.modules.question.form;

import lombok.Data;

/**
 * @Author：linan
 * @Date：2022/11/21 20:35
 */
@Data
public class QuestionListForm {
    private Integer currentPage;

    private Integer pageSize;
    //排序根据
    private String orderByDescColumn;
    //模块：最新（newQuestion），最热（hotQuestion），待回答（waitQuestion）
    private String methodType;


}

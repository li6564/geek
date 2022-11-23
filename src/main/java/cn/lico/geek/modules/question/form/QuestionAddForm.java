package cn.lico.geek.modules.question.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：linan
 * @Date：2022/11/23 16:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAddForm {

    private String content;

    private String questionTagUid;

    private String questionTemplateUid;

    private String summary;

    private String title;


}

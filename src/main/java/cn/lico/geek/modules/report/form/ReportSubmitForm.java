package cn.lico.geek.modules.report.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：linan
 * @Date：2023/3/1 19:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportSubmitForm {
    private String content;

    private String reportContent;

    private String reportContentUid;

    private String reportType;

    private String reportUserUid;

    private String violationType;
}

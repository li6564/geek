package cn.lico.geek.modules.notice.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：linan
 * @Date：2022/11/25 15:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeListForm {
    private Integer currentPage;

    private Integer noticeType;

    private Integer pageSize;
}

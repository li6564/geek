package cn.lico.geek.modules.moment.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：linan
 * @Date：2022/11/20 16:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMomentTopicPage {

    private Integer pageSize;

    private Integer currentPage;
}

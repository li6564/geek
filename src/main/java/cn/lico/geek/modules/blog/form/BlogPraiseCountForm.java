package cn.lico.geek.modules.blog.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：linan
 * @Date：2022/11/18 15:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogPraiseCountForm {
    private String resourceType;

    private String resourceUid;
}

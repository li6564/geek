package cn.lico.geek.modules.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：linan
 * @Date：2022/11/18 15:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogPraiseCountVo {
    private Boolean isPraise;

    private Integer praiseCount;
}

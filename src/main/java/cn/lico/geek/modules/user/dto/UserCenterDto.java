package cn.lico.geek.modules.user.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author：linan
 * @Date：2022/12/2 16:10
 */
@Data
@Accessors(chain = true)
public class UserCenterDto {
    private Integer questionCount;

    private Integer blogCount;

    private Integer followCount;

    private Integer momentCount;

    private Integer watchCount;
}

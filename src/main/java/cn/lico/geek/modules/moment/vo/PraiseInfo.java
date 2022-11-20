package cn.lico.geek.modules.moment.vo;

import lombok.Data;

/**
 * @Author：linan
 * @Date：2022/11/20 18:26
 */
@Data
public class PraiseInfo {
    private boolean isTread;

    private Integer treadCount;

    private boolean isPraise;

    private Integer praiseCount;
}

package cn.lico.geek.modules.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author：linan
 * @Date：2022/11/25 14:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserReceiveNoticeCountDto {
    private Integer userReceiveMessageCount;

    private Integer userReceivePraiseCount;

    private Integer userReceiveCommentCount;

    private Integer userReceiveSystemCount;

    private Integer userReceiveWatchCount;

    private Integer userReceiveCollectCount;
}

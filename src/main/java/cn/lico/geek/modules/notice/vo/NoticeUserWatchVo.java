package cn.lico.geek.modules.notice.vo;

import cn.lico.geek.modules.blog.vo.BlogInfoUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：linan
 * @Date：2022/11/25 16:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeUserWatchVo {
    private BlogInfoUser user;
}

package cn.lico.geek.modules.user.dto;

import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

/**
 * @Author：linan
 * @Date：2022/11/18 17:19
 */
@Data
public class UserWatchDto {
    //1表示已关注，0表示未关注
    private Integer data;

    private String code;
}

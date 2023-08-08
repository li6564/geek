package cn.lico.geek.utils;

import cn.lico.geek.modules.tag.entity.Tag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author：linan
 * @Date：2023/8/8 17:15
 */
@Data
public class CacheData<T> {
    private List<T> data;
    private LocalDateTime expireTime;
}

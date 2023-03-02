package cn.lico.geek.modules.blog.form;

import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

/**
 * @Author：linan
 * @Date：2023/3/2 10:20
 */
@Data
public class PublishForm {
    private String isPublish;

    private String uid;
}

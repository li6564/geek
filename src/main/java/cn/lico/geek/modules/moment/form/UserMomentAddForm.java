package cn.lico.geek.modules.moment.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;

/**
 * @Author：linan
 * @Date：2022/11/21 17:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMomentAddForm {
    //动态内容
    private String content;

    //图片地址
    private String pictureUrl;

    //话题uid
    private String topicUids;

    //链接
    private String url;

    //网站信息
    private String urlInfo;
}

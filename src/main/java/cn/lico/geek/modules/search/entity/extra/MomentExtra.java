package cn.lico.geek.modules.search.entity.extra;

import cn.lico.geek.modules.search.entity.UserInfo;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @Author：linan
 * @Date：2022/12/3 16:19
 */
@Data
public class MomentExtra {
    @Field(index = false,type = FieldType.Integer)
    private Integer oid;

    @Field(index = false,type = FieldType.Integer)
    private Integer commentCount;

    private UserInfo user;
}

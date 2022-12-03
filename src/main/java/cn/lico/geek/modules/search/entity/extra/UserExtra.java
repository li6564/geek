package cn.lico.geek.modules.search.entity.extra;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @Author：linan
 * @Date：2022/12/3 16:20
 */
@Data
public class UserExtra {
    @Field(type = FieldType.Keyword)
    private String nickName;

    @Field(index = false,type = FieldType.Integer)
    private Integer gender;

    @Field(index = false,type = FieldType.Text)
    private String occupation;

    @Field(index = false,type = FieldType.Integer)
    private Integer userTag;

    @Field(index = false,type = FieldType.Integer)
    private Integer blogPublishCount;

//    private Integer commentPublishCount;

//    private Integer praiseCount;

    @Field(index = false,type = FieldType.Boolean)
    private boolean userWatchStatus;

    @Field(index = false,type = FieldType.Integer)
    private Integer userMomentCount;

//    private Integer userLevel;
}

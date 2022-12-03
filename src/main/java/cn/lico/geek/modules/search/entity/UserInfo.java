package cn.lico.geek.modules.search.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @Author：linan
 * @Date：2022/12/3 17:06
 */
@Data
public class UserInfo {
    @Field(type = FieldType.Keyword)
    private String nickName;

    @Field(index = false,type = FieldType.Integer)
    private Integer gender;

    @Field(index = false,type = FieldType.Text)
    private String occupation;

    @Field(index = false,type = FieldType.Integer)
    private Integer userTag;

    @Field(index = false,type = FieldType.Text)
    private String photoUrl;

    @Field(index = false,type = FieldType.Text)
    private String uid;

    @Field(index = false,type = FieldType.Integer)
    private Integer status;

    @Field(index = false,type = FieldType.Date)
    private Date createTime;
}

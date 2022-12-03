package cn.lico.geek.modules.search.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @Author：linan
 * @Date：2022/12/3 17:14
 */
@Data
public class QuestionTag {
    @Field(type = FieldType.Keyword)
    private String name;

    @Field(index = false,type = FieldType.Text)
    private String summary;

    @Field(index = false,type = FieldType.Integer)
    private Integer clickCount;

    @Field(index = false,type = FieldType.Integer)
    private Integer sort;

    @Field(index = false,type = FieldType.Text)
    private String uid;

    @Field(index = false,type = FieldType.Integer)
    private Integer status;
}

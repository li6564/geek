package cn.lico.geek.modules.search.entity.extra;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @Author：linan
 * @Date：2022/12/3 16:19
 */
@Data
public class BlogExtra {
    @Field(index = false,type = FieldType.Integer)
    private Integer oid;

    @Field(type = FieldType.Keyword)
    private String blogSortName;

    @Field(index = false,type = FieldType.Text)
    private String blogSortUid;

    //作者
    @Field(type = FieldType.Keyword)
    private String author;

    @Field(index = false,type = FieldType.Text)
    private String authorName;

    //类型【0 博客， 1：推广】
    @Field(index = false,type = FieldType.Integer)
    private Integer type;

    //是否是特权文章（0：普通文章，1 ：特权文章）
    @Field(index = false,type = FieldType.Integer)
    private Integer isVip;
}

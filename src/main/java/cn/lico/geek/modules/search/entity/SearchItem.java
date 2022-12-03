package cn.lico.geek.modules.search.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @Author：linan
 * @Date：2022/12/3 15:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "search_item")
public class SearchItem<T>{
    @Id
    private String uid;

    @Field(index = false,type = FieldType.Text)
    private String resourceType;

    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer="ik_max_word")
    private String title;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String summary;

    //博客内容
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String content;

    @Field(index = false,type = FieldType.Integer)
    private Integer clickCount;

    @Field(index = false,type = FieldType.Integer)
    private Integer collectCount;

    //是否开启评论(0:否 1:是)
    @Field(index = false,type = FieldType.Integer)
    private Integer openComment;

    //投稿用户UID
    @Field(index = false,type = FieldType.Text)
    private String userUid;

    @Field(index = false,type = FieldType.Text)
    private String photoUrl;

    @Field(index = false,type = FieldType.Integer)
    private Integer status;

    @Field(index = false,type = FieldType.Date)
    private Date createTime;

    @Field(index = false,type = FieldType.Date)
    private Date updateTime;

    private T extra;
}

package cn.lico.geek.modules.search.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
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
@Document(indexName = "test3_search_items")
public class SearchItem<T>{

    public SearchItem(String uid, String resourceType) {
        this.uid = uid;
        this.resourceType = resourceType;
    }

    @Id
    private String uid;

    @Field(type = FieldType.Keyword)
    private String resourceType;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
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

//    //是否开启评论(0:否 1:是)
//    @Field(index = false,type = FieldType.Integer)
//    private Integer openComment;

    //投稿用户UID
    @Field(index = false,type = FieldType.Text)
    private String userUid;

    @Field(index = false,type = FieldType.Text)
    private String photoUrl;

//    @Field(index = false,type = FieldType.Integer)
//    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Field(index = false,type = FieldType.Date,format = DateFormat.custom,pattern = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_second")
    private Date createTime;

//    @Field(index = false,type = FieldType.Date)
//    private Date updateTime;

    private T extra;
}

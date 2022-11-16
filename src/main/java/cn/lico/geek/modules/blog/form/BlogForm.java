package cn.lico.geek.modules.blog.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author：linan
 * @Date：2022/11/15 21:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogForm {
//
//    private String uid;


    //文章所属类别id
    private String blogSortUid;

    //博客内容
    private String content;

    //
    private String fileUid;


    //是否发布：0：否，1：是
    private String isPublish;

    //简介
    private String summary;

    //所属标签id列表
    private String tagUid;

    //标题
    private String title;

    //标题图
    private List<String> photoList;

}

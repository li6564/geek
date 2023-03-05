package cn.lico.geek.modules.blog.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：linan
 * @Date：2022/11/12 13:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_blog_tag")
public class BlogTag {
    @TableId(value = "uid",type = IdType.AUTO)
    private Integer uid;

    private String blogId;

    private String tagId;

    private Integer status;
}

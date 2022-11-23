package cn.lico.geek.modules.question.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (QuestionTags)表实体类 问答标签中间表
 *
 * @author makejava
 * @since 2022-11-23 16:17:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_question_tags")
public class QuestionTags{
    //uid
    @TableId(value = "uid",type = IdType.ASSIGN_UUID)
    private String uid;
    //问答id
    private String questionUid;
    //问答标签id
    private String tagUid;

}


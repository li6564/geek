package cn.lico.geek.modules.moment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (MomentTopic)表实体类
 *
 * @author makejava
 * @since 2022-11-20 18:38:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_moment_topic")
public class MomentTopic{
    //id
    @TableId(value = "uid",type = IdType.ASSIGN_UUID)
    private String uid;
    //动态id
    private String momentUid;
    //主题id
    private String topicUid;

}


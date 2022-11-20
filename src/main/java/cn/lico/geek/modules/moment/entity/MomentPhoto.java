package cn.lico.geek.modules.moment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (MomentPhoto)表实体类
 *
 * @author makejava
 * @since 2022-11-20 18:38:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_moment_photo")
public class MomentPhoto{
    //图片id
    @TableId(value = "file_uid",type = IdType.ASSIGN_UUID)
    private String fileUid;
    //动态id
    private String momentUid;
    //图片链接
    private String photoUrl;

}


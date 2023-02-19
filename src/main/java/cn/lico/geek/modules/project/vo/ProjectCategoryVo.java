package cn.lico.geek.modules.project.vo;

import cn.lico.geek.modules.project.entity.ProjectCategory;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author：linan
 * @Date：2023/2/19 14:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCategoryVo {

    private Integer uid;

    private String parentUid;

    private String name;

    private Integer status;

    private Integer sort;

    private String icon;

    private List<ProjectCategory> children;

    private Date createTime;

    private Date updateTime;
}

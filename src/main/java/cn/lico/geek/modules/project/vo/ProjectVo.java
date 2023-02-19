package cn.lico.geek.modules.project.vo;

import cn.lico.geek.modules.blog.vo.BlogInfoUser;
import cn.lico.geek.modules.blog.vo.BlogPraiseCountVo;
import cn.lico.geek.modules.project.entity.ProjectCategory;
import cn.lico.geek.modules.user.vo.UserVo;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author：linan
 * @Date：2023/2/19 15:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectVo {
    private String uid;

    private Integer oid;
    //项目名称
    private String name;
    //项目简介
    private String summary;
    //项目描述
    private String content;

    private Integer clickcount;

    private Integer collectcount;
    //发布者id
    private String useruid;

    private Integer projectCategoryId;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private BlogInfoUser user;

    private BlogPraiseCountVo praiseInfo;

    private List<ProjectCategory> projectCategoryList;
}

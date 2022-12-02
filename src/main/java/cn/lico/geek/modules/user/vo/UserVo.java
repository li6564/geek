package cn.lico.geek.modules.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Author：linan
 * @Date：2022/12/2 14:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserVo {
    private String nickName;

    private Integer gender;
    //职业
    private String occupation;

    private String summary;

    private Integer userTag;
    //是否通过加载校验【0 未通过，1 已通过】
    private Integer loadingValid;
    //积分
    private Integer credits;

    private String backgroundFileUrl;

    //个人头像
    private String photoUrl;

    private Boolean isWatchUser;

    private Integer blogPublishCount;

    private Integer blogVisitCount;

    private Integer userWatchCount;

    private Integer userFollowCount;

    private Integer userMomentCount;

//    private Integer userLevel;

//    private String userIpPossession;

    private String uid;

    private Integer status;

    private Date createTime;

}

package cn.lico.geek.modules.user.vo;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：linan
 * @Date：2022/11/12 17:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTopVo {

    private String uid;

    private String nickName;

    private Integer userTag;

    private String photoUrl;

    private Integer blogNum;

    private Integer questionNum;

    private Integer postNum;
}

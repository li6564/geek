package cn.lico.geek.modules.user.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：linan
 * @Date：2022/12/9 12:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterForm {
    private String userName;

    private String nickName;

    private String email;

    private String passWord;
}

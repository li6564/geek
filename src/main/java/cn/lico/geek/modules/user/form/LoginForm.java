package cn.lico.geek.modules.user.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：linan
 * @Date：2022/11/14 15:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {
    private String userName;

    private String passWord;
}

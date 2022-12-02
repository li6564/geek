package cn.lico.geek.modules.user.form;

import lombok.Data;

/**
 * @Author：linan
 * @Date：2022/12/2 21:55
 */
@Data
public class PasswordUpdateForm {
    private String oldPwd;

    private String newPwd;
}

package cn.lico.geek.common.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 分页表单
 * @author linan
 */
@Data
public class PageForm {

    @NotNull(message = "请传入参数page")
    private Integer page;

    @NotNull(message = "请传入参数size")
    private Integer size;
}

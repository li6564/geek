package cn.lico.geek.modules.search.form;

import lombok.Data;

/**
 * @Author：linan
 * @Date：2022/12/4 14:44
 */
@Data
public class SearchForm {
    private Integer currentPage;

    private Integer pageSize;

    private String keywords;

    private String resourceType;
}

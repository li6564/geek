package cn.lico.geek.common.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 分页结果返回
 * @author linan
 */
@Data
public class PageDTO<T> {

    /**
     * 总页数
     */
    private long totalPage;

    /**
     * 总记录数
     */
    private long totalRecords;

    /**
     * 每页数据量
     */
    private long pageSize;

    /**
     * 数据
     */
    private List<T> dataList;

    public PageDTO() {
    }

    public PageDTO(IPage<T> page) {
        setDataList(page.getRecords());
        setTotalPage(page.getPages());
        setTotalRecords(page.getTotal());
        setPageSize(page.getSize());
    }

    public PageDTO(Page<T> page) {
        setDataList(page.getContent());
        setTotalPage(page.getTotalPages());
        setTotalRecords(page.getTotalElements());
        setPageSize(page.getSize());
    }
}
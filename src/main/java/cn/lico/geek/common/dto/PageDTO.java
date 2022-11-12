package cn.lico.geek.common.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页结果返回
 * @author linan    
 */
@Data
public class PageDTO<T> {

//    /**
//     * 总页数
//     */
//    private long totalPage;

    /**
     * 总记录数
     */
    private Integer total;

    /**
     * 每页数据量
     */
    private Integer size;

    /**
     * 数据
     */
    private List<T> records;

    /**
     * 当前页数
     */
    private Integer current;

    private boolean optimizeCountSql = true;

    private boolean isSearchCount = true;

    private boolean hitCount = false;

    private List orders = new ArrayList();

    public PageDTO() {
    }

    public PageDTO(IPage<T> page) {
        setRecords(page.getRecords());
        //setTotalPage(page.getPages());
        setTotal((int)page.getTotal());
        setSize((int)page.getSize());
        setCurrent((int)page.getCurrent());
    }

    public PageDTO(Page<T> page) {
        setRecords(page.getContent());
        //setTotalPage(page.getTotalPages());
        setTotal((int)page.getTotalElements());
        setSize(page.getSize());
    }
}
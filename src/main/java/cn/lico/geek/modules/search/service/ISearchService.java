package cn.lico.geek.modules.search.service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.queue.message.DataItemType;
import cn.lico.geek.modules.search.entity.SearchItem;

import java.util.Optional;

/**
 * @Author：linan
 * @Date：2022/12/4 12:33
 */
public interface ISearchService{
    /**
     * 添加搜索条目
     * @param dataItemType
     * @param itemId
     */
    void saveSearchItem(DataItemType dataItemType, String itemId);

    /**
     * 删除搜索条目
     * @param dataItemType
     * @param itemId
     */
    void deleteSearchItem(DataItemType dataItemType, String itemId);

    /**
     * 更新搜索条目
     * @param dataItemType
     * @param itemId
     */
    void updateSearchItem(DataItemType dataItemType, String itemId);

    /**
     * 查找搜索条目
     * @param dataItemType
     * @param itemId
     * @return
     */
    Optional<SearchItem> getSearchItem(DataItemType dataItemType, String itemId);

    /**
     * 根据关键词分页搜索
     * @param currentPage
     * @param pageSize
     * @param keywords
     * @param resourceType
     * @return
     */
    ResponseResult searchPage(int currentPage, Integer pageSize, String keywords, String resourceType);
}

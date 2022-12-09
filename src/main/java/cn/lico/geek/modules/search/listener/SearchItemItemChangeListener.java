package cn.lico.geek.modules.search.listener;

import cn.lico.geek.core.listener.DataItemChangeListener;
import cn.lico.geek.core.queue.message.DataItemChangeMessage;
import cn.lico.geek.core.queue.message.DataItemType;
import cn.lico.geek.modules.search.service.ISearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author meteor
 */
@Component
@Slf4j
public class SearchItemItemChangeListener implements DataItemChangeListener {

    private ISearchService searchService;

    @Autowired
    public SearchItemItemChangeListener(ISearchService searchService) {
        this.searchService = searchService;
    }

    @Override
    public void onDataItemAdd(DataItemChangeMessage dataItemChangeMessage) throws Exception {
        System.out.println("es添加博客");
        searchService.saveSearchItem(dataItemChangeMessage.getItemType(), dataItemChangeMessage.getItemId());
    }

    @Override
    public void onDataItemUpdate(DataItemChangeMessage dataItemChangeMessage) throws Exception {
        searchService.updateSearchItem(dataItemChangeMessage.getItemType(), dataItemChangeMessage.getItemId());
    }

    @Override
    public void onDataItemDelete(DataItemChangeMessage dataItemChangeMessage) throws Exception {
        searchService.deleteSearchItem(dataItemChangeMessage.getItemType(), dataItemChangeMessage.getItemId());
    }

    @Override
    public void onDataItemRegister(DataItemChangeMessage dataItemChangeMessage){
        System.out.println("es 添加用户");
        searchService.saveSearchItem(dataItemChangeMessage.getItemType(), dataItemChangeMessage.getItemId());
    }
}

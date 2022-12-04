package cn.lico.geek.modules.search.repository;

import cn.lico.geek.modules.search.entity.SearchItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author linan
 */
@Repository
public interface ISearchRepository extends ElasticsearchRepository<SearchItem, String> {
}

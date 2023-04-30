package qrm.repository;

import qrm.repository.options.PagingInfo;
import qrm.repository.options.QueryOptions;
import qrm.repository.options.SortingInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface BaseRepository<T> {

    T fetchById(long id);

    List<T> searchByIds(Collection<Long> ids, PagingInfo pagingInfo);

    List<T> searchByIds(Collection<Long> ids, SortingInfo sortInfo);

    List<T> searchByIds(Collection<Long> ids, SortingInfo sortInfo, PagingInfo pagingInfo);

    List<T> searchByIds(Collection<Long> ids, QueryOptions queryOptions);

    List<T> search(Map<String, Object> parameters, PagingInfo pagingInfo);

    List<T> search(Map<String, Object> parameters, SortingInfo sortInfo);

    List<T> search(Map<String, Object> parameters, SortingInfo sortInfo, PagingInfo pagingInfo);

    List<T> search(Map<String, Object> parameters, QueryOptions queryOptions);
}

package qrm.repository.options;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class QueryOptions {

    public static final QueryOptions EMPTY = new QueryOptions(PagingInfo.EMPTY, SortingInfo.EMPTY);

    private final PagingInfo pagingInfo;
    private final SortingInfo sortingInfo;
}

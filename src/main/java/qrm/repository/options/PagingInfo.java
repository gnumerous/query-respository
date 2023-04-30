package qrm.repository.options;

import lombok.Builder;
import lombok.Getter;

@Builder
public class PagingInfo {

    public static final PagingInfo EMPTY = new PagingInfo(-1, -1);

    @Getter private final int pageStart;
    @Getter private final int pageSize;
}

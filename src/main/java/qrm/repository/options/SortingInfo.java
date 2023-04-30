package qrm.repository.options;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Builder
public class SortingInfo {

    public static final SortingInfo EMPTY = new SortingInfo(List.of());

    @Getter @Singular private final List<FieldOption>  options;

    @Builder
    public static class FieldOption {
        private final String fieldName;
        private final String direction;
    }
}

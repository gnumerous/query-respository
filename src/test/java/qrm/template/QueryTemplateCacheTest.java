package qrm.template;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class QueryTemplateCacheTest {

    private static QueryTemplateCache queries;

    @BeforeAll
    static void startup() throws IOException {
        queries = QueryTemplateCache.getInstance();
        queries.init();
    }

    @Test
    void replaceMultiple() {
        String validQuery = queries.getQuery("basic_query", Map.of("value1", "Reginald", "field1", "author"));
        System.out.println(validQuery);
        assertThat(validQuery).contains("author");
        assertThat(validQuery).contains("Reginald");
    }

    @Test
    void templateNotFound_exception() {
        assertThrows(RuntimeException.class, () -> queries.getQuery("not_found_query",
                Map.of("value1", "Reginald", "field1", "author")));
    }

    @Test
    void queryWithoutReplacements_exception() {
        assertThrows(RuntimeException.class, () -> queries.getQuery("basic_query", Map.of()));
    }

    @Test
    void queryWithoutReplacements_okay() {
        String validQuery = queries.getQuery("static_query", Map.of());
        assertThat(validQuery).doesNotContain("${");
    }
}

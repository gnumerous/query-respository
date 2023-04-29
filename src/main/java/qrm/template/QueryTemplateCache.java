package qrm.template;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class QueryTemplateCache {

    private static final String QUERIES_EQT = "/queries/queries.eqt";
    private static final String openReplacementRegex = "\\$\\{";
    private static final String closeReplacementRegex = "\\}";
    private static final String continuationRegex = closeReplacementRegex + "|" + openReplacementRegex;

    private static final QueryTemplateCache instance = new QueryTemplateCache();

    private Map<String, String> queryTexts;

    public static QueryTemplateCache getInstance() {
        return instance;
    }

    private QueryTemplateCache() {
        // this.queryTexts = new HashMap<>();
    }

    void init() throws IOException {

        try (InputStream is = QueryTemplateCache.class.getResourceAsStream(QUERIES_EQT)) {
            String data = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            String[] parts = data.split("=+");
            Map<String, String> tmpMap = new HashMap<>();
            Stream.of(parts).map(String::trim).filter(txt -> !txt.isEmpty()).forEach(txt -> {
                String[] subparts = txt.replace("name:", "").split("text:");
                tmpMap.put(subparts[0].trim(), subparts[1].trim());
            });
            queryTexts = Collections.unmodifiableMap(tmpMap);
        }
        // System.out.println(queryTexts);
    }

    public Optional<String> getTemplate(String templateName) {
        return Optional.ofNullable(queryTexts.get(templateName));
    }

    public String getQuery(String templateName, Map<String, String> substitutions) throws NoSuchElementException {
        String template = getTemplate(templateName)
                .orElseThrow(() -> new RuntimeException(String.format("template '%s' not found", templateName)));

        if (substitutions.isEmpty()) {
            if (template.contains("${")) {
                throw new RuntimeException(String.format("incomplete substitutions for template '%s'", templateName));
            }
            return template;
        }
        String patternString = String.format("%s%s%s", openReplacementRegex,
                String.join(continuationRegex, substitutions.keySet()), closeReplacementRegex);
        // System.out.printf("pattern: %s%n", patternString);
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(template);

        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String found = matcher.group(0);
            String replacement = substitutions.get(found.substring(2, found.length() - 1));
            // System.out.printf("replacing %s with %s%n", found, replacement);
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);

        // System.out.println(sb.toString());
        return sb.toString();
    }
}

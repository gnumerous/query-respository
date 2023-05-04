package qrm.repository;

import org.h2.jdbc.JdbcResultSet;
import org.h2.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxyBuilder<R, T> {

    public R createProxy(Class<R> repo, Class<T> type) {
        return (R) Proxy.newProxyInstance(repo.getClassLoader(),
                new Class[]{repo},
                new ProxyHandler<R, T>(repo, type));
    }

    private static class ProxyHandler<R, T> implements InvocationHandler {
        private static final List<Map<String, Object>> TESTDATA = List.of(
                Map.of("id", 100, "first_name", "Henry", "last_name", "Oh"),
                Map.of("id", 101, "first_name", "Carl", "last_name", "Yastremski"),
                Map.of("id", 102, "first_name", "Clark", "last_name", "Kent"));

        private final Class<R> repo;
        private final Class<T> type;

        ProxyHandler(Class<R> repo, Class<T> type) {
            this.repo = repo;
            this.type = type;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Map<String, String> properties = new HashMap<>();

            properties.putAll(getClassQueryAnnotation(repo));
            properties.putAll(getMethodQueryAnnotation(method));

            AtomicReference<Object> response = new AtomicReference<>();
            // final String t = this.type.getName();
            if ((method.getReturnType() == List.class) || (method.getReturnType() == Collection.class)) {
                response.set(new ArrayList<>());
            } else if (method.getReturnType() == Set.class) {
                response.set(new HashSet<>());
            }

            System.out.println(properties);
            if (properties.containsKey("dataType")) {
                try {
                    Class<?> returnType = Class.forName(properties.get("dataType").replace(".class", ""));
                    if (response.get() instanceof Collection) {
                        for (int idx = 0; idx < TESTDATA.size(); idx++) {
                            ((Collection) response.get()).add(buildInstance(idx));
                        }
                    } else {
                        response.set(buildInstance(0));
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

//            gotit.ifPresent(annotation -> {
//                Map<String, String> map = regexParse(annotation.toString());
//                System.out.println(map);
//                if (map.containsKey("dataType")) {
//                    try {
//                        Class<?> returnType = Class.forName(map.get("dataType").replace(".class", ""));
//                        if (response.get() instanceof Collection) {
//                            for (int idx = 0; idx < TESTDATA.size(); idx++) {
//                                ((Collection) response.get()).add(buildInstance(idx));
//                            }
//                        } else {
//                            response.set(returnType);
//                        }
//                    } catch (ClassNotFoundException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            });

            return response.get();
        }

        //         private static
        private static Map<String, String> getClassQueryAnnotation(Class<?> repo) {
            Optional<Annotation> optAnnotation = Arrays.stream(repo.getDeclaredAnnotations())
                    .peek(annotation -> {
                        System.out.println("name: " + annotation.getClass().getName());
                    })
                    .filter(annotation -> annotation.toString().contains("@qrm.repository.annotation.Query")).findFirst();
            return optAnnotation.isPresent() ? regexParse(optAnnotation.toString()) : new HashMap<>();
        }

        private static Map<String, String> getMethodQueryAnnotation(Method method) {
            Map<String, String> result = new HashMap<>();

            Arrays.stream(method.getDeclaredAnnotations())
                    .peek(annotation -> {
                        System.out.println("name: " + annotation.getClass().getName());
                    })
                    .filter(annotation -> annotation.toString().contains("@qrm.repository.annotation.Query")).findFirst()
                    .ifPresent(gotit -> putAll(result, regexParse(gotit.toString())));

            Arrays.stream(method.getAnnotations())
                    .peek(annotation -> {
                        System.out.println("name: " + annotation.getClass().getName());
                    })
                    .filter(annotation -> annotation.toString().contains("@qrm.repository.annotation.Query")).findFirst()
                    .ifPresent(gotit -> putAll(result, regexParse(gotit.toString())));
            return result;
        }

        private String buildDefaultQueryFromType() {
            return "select id, first_name, last_name from employee where id = ${id}";
        }

        private static void putAll(Map<String, String> result, Map<String, String> parsed) {
            parsed.forEach((k, v) -> {
                if ((k.equals("dataType") && v.equals("void.class")) ||
                        (k.equals("query") && v.equals(""))){
                    System.out.printf("ignoring %s, %s%n", k, v);
                } else {
                    System.out.printf("putting %s, %s to map%n", k, v);
                    result.put(k, v);
                }
            });
        }

        private T buildInstance(int idx) {
            try {
                Constructor<T> constructor = type.getDeclaredConstructor();
                T instance = constructor.newInstance();
                Field[] fields = type.getDeclaredFields();
                fields[0].setAccessible(true);
                fields[1].setAccessible(true);
                fields[2].setAccessible(true);

                fields[0].setInt(instance, (int) TESTDATA.get(idx).get("id"));
                fields[1].set(instance, TESTDATA.get(idx).get("first_name"));
                fields[2].set(instance, TESTDATA.get(idx).get("last_name"));
                String s = "";

                return instance;
            } catch (Exception ex) {
                throw new RuntimeException("blew up!", ex);
            }
        }

        private static Map<String, String> regexParse(String query) {
            // String query = "@repository.Query(text=\"select * from employee where id = :id\", dataType=repository.SomeData.class)";
            Pattern pattern = Pattern.compile("text=\".*\"|dataType=[\\.\\w]+");
            Matcher matcher = pattern.matcher(query);
            Map<String, String> map = new HashMap<>();
            while (matcher.find()) {
                String found = matcher.group(0);
                int idx = found.indexOf("=");
                String key = found.substring(0, idx);
                String value = found.substring(idx + 1).trim();
                System.out.println(key + "|" + value);
                if (!StringUtils.isNullOrEmpty(value) && !value.equals("void.class")) {
                    map.put(key, value);
                }
            }
            return map;
        }

    }

}

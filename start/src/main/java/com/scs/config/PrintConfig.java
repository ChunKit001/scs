package com.scs.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ConditionalOnProperty(name = "print.config", havingValue = "true")
@Slf4j
//@Component
@AllArgsConstructor
public class PrintConfig implements CommandLineRunner {
    private static final String APPLICATION_CONFIG = "applicationConfig";

    private final ConfigurableEnvironment springEnv;

    @Override
    public void run(String... args) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("");
        if (resource == null) {
            return;
        }
        String s = resource.getPath();

        log.debug("classpath:{}", s);
        log.debug("start to print environment variables");
        MutablePropertySources propSrcs = springEnv.getPropertySources();
        // 获取所有配置 key -> [source, key, value]
        Map<String, String[]> props = propSrcs.stream()
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> (EnumerablePropertySource<?>) ps)
                .flatMap(ps -> {
                    String psName = ps.getName();
                    if (psName.startsWith(APPLICATION_CONFIG)) {
                        psName = psName.substring(31);
                        psName = psName.substring(0, psName.length() - 1);
                    }
                    final String fpsName = psName;
                    return Arrays.stream(ps.getPropertyNames())
                            .map(ppName -> new String[] { fpsName, ppName, springEnv.getProperty(ppName) });
                })
                .distinct()
                .sorted(Comparator.comparing(pArr -> pArr[1]))
                .collect(Collectors.toMap(pArr -> pArr[1], Function.identity(), (pArr1, pArr2) -> pArr1));

        // key 和 value 之间的最小间隙
        int interval = 20;
        int max01 = props.values().stream()
                .map(pArr -> pArr[0])
                .max(Comparator.comparingInt(String::length))
                .orElse("")
                .length();
        int max12 = props.values().stream()
                .map(pArr -> pArr[1])
                .max(Comparator.comparingInt(String::length))
                .orElse("")
                .length();

        // 打印
        String propStr = props.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    String[] pArr = e.getValue();
                    int i01 = max01 - pArr[0].length() + interval;
                    int i12 = max12 - pArr[1].length() + interval;
                    String p01 = String.join("", Collections.nCopies(i01, " "));
                    String p12 = String.join("", Collections.nCopies(i12, " "));
                    return pArr[0] + p01 + pArr[1] + p12 + pArr[2];
                })
                .reduce((s1, s2) -> s1 + "\n" + s2)
                .orElse("");
        log.debug("\n" + propStr);
        log.debug("environment variables printed");
    }
}

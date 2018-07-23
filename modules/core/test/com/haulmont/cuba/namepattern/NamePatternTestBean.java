package com.haulmont.cuba.namepattern;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Rushan Zagidullin
 * @since 17.07.2018
 */
@Component("cuba_NamePatternTestBean")
public class NamePatternTestBean {
    public int sum(Integer... values) {
        return Optional.ofNullable(values)
                .map(ints -> Arrays.stream(ints)
                        .filter(Objects::nonNull)
                        .mapToInt(Integer::intValue)
                        .sum())
                .orElse(0);
    }

    public String concat(String... values) {
        return Optional.ofNullable(values)
                .map(strings -> Arrays.stream(strings)
                        .collect(Collectors.joining()))
                .orElse("");
    }
}

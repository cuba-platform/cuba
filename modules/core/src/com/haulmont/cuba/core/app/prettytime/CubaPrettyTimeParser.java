/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.app.prettytime;

import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.Resources;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 */
@Component(CubaPrettyTimeParser.NAME)
public class CubaPrettyTimeParser {

    public static final String NAME = "cuba_CubaPrettyTimeParser";

    @Inject
    protected ServerConfig serverConfig;

    @Inject
    protected Resources resources;

    private Map<String, String> replacements = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
        StrTokenizer tokenizer = new StrTokenizer(serverConfig.getPrettyTimeProperties());
        try {
            for (String fileName : tokenizer.getTokenArray()) {
                InputStream stream = null;

                try {
                    stream = resources.getResourceAsStream(fileName);
                    if (stream == null) {
                        throw new IllegalStateException("Resource is not found: " + fileName);
                    }
                    InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8.name());
                    Properties properties = new Properties() {
                        private Set orderedKeySet = new LinkedHashSet();

                        @Override
                        public Set<String> stringPropertyNames() {
                            return orderedKeySet;
                        }

                        @Override
                        public synchronized Object put(Object key, Object value) {
                            orderedKeySet.add(key);
                            return super.put(key, value);
                        }
                    };
                    properties.load(reader);
                    for (String key : properties.stringPropertyNames()) {
                        String value = properties.getProperty(key);
                        replacements.put(key, value);
                    }
                } finally {
                    IOUtils.closeQuietly(stream);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Date parse(String input) {
        String formattedValue = " " + input.toLowerCase().replaceAll("\\s+", " ") + " ";
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            formattedValue = formattedValue.replace(" " + entry.getKey() + " ", " " + entry.getValue() + " ");
        }
        formattedValue = formattedValue.trim();
        List<Date> dates = new PrettyTimeParser().parse(formattedValue);
        if (dates.isEmpty()) {
            return null;
        }
        if (formattedValue.contains(" minus ") && dates.size() == 2) {
            Long time1 = dates.get(0).getTime();
            Long time2 = dates.get(1).getTime();
            Long difference = time2 - time1;
            Long resultTime = time1 - difference;
            return new Date(resultTime);
        }
        return dates.get(dates.size() - 1);
    }
}
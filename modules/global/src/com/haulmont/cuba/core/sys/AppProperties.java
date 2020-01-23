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
 */

package com.haulmont.cuba.core.sys;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * INTERNAL.
 * Provides access to file-based application properties.
 */
public class AppProperties {

    public static final Pattern SEPARATOR_PATTERN = Pattern.compile("\\s");

    private AppComponents appComponents;

    private Map<String, String> properties = new ConcurrentHashMap<>();

    private Map<String, String> systemProperties = new ConcurrentHashMap<>();

    // Temporary support for deprecated properties: the second element has priority
    private static final Multimap<String, String> DEPRECATED_PROPERTIES = ArrayListMultimap.create();

    static {
        DEPRECATED_PROPERTIES.put("cuba.connectionUrlList", "cuba.connectionUrl");
        DEPRECATED_PROPERTIES.put("cuba.entityLog.enabled", "cuba.security.EntityLog.enabled"); // 6.1
        DEPRECATED_PROPERTIES.put("cuba.cluster.messageSendingThreadPoolSize", "cuba.clusterMessageSendingThreadPoolSize"); // 6.1
        DEPRECATED_PROPERTIES.put("reporting.entityTreeModelMaxDepth", "cuba.reporting.entityTreeModelMaxDeep"); // 6.1
        DEPRECATED_PROPERTIES.put("reporting.office.docx.useOfficeForDocumentConversion", "reporting.openoffice.docx.useOfficeForDocumentConversion");
        DEPRECATED_PROPERTIES.put("reporting.office.docx.useOfficeForDocumentConversion", "reporting.openoffice.docx.useOfficeForPdfConversion"); // 6.7
        DEPRECATED_PROPERTIES.put("cuba.maxUploadSizeMb", "cuba.client.maxUploadSizeMb"); // 6.1
        DEPRECATED_PROPERTIES.put("cuba.gui.systemInfoScriptsEnabled", "cuba.systemInfoScriptsEnabled"); // 6.1
        DEPRECATED_PROPERTIES.put("cuba.gui.manualScreenSettingsSaving", "cuba.manualScreenSettingsSaving"); // 6.1
        DEPRECATED_PROPERTIES.put("cuba.gui.showIconsForPopupMenuActions", "cuba.showIconsForPopupMenuActions"); // 6.1
        DEPRECATED_PROPERTIES.put("cuba.gui.tableShortcut.insert", "cuba.gui.tableInsertShortcut"); // 6.1
        DEPRECATED_PROPERTIES.put("cuba.gui.tableShortcut.add", "cuba.gui.tableAddShortcut"); // 6.1
        DEPRECATED_PROPERTIES.put("cuba.gui.tableShortcut.remove", "cuba.gui.tableRemoveShortcut"); // 6.1
        DEPRECATED_PROPERTIES.put("cuba.gui.tableShortcut.edit", "cuba.gui.tableEditShortcut"); // 6.1
        DEPRECATED_PROPERTIES.put("reporting.parameterPrototypeQueryLimit", "reporting.parameterPrototype.queryLimit"); // 6.1
        DEPRECATED_PROPERTIES.put("reporting.office.*", "reporting.openoffice.*"); // 6.1
        DEPRECATED_PROPERTIES.put("reporting.*", "cuba.reporting.*"); // 6.1
        DEPRECATED_PROPERTIES.put("fts.*", "cuba.fts.*"); // 6.1
        DEPRECATED_PROPERTIES.put("charts.*", "cuba.charts.*"); // 6.1
        DEPRECATED_PROPERTIES.put("cuba.amazonS3.*", "cuba.amazon.s3.*"); // 6.1
    }

    public AppProperties(AppComponents appComponents) {
        this.appComponents = appComponents;
        initSystemProperties();
    }

    public synchronized void initSystemProperties() {
        systemProperties.clear();
        for (String name : System.getProperties().stringPropertyNames()) {
            systemProperties.put(name, System.getProperty(name));
        }
    }

    /**
     * @return all property names defined in the set of {@code app.properties} files and exported by the app components
     */
    public String[] getPropertyNames() {
        Set<String> namesSet = new HashSet<>();
        for (AppComponent appComponent : appComponents.getComponents()) {
            namesSet.addAll(appComponent.getPropertyNames());
        }
        namesSet.addAll(properties.keySet());

        List<String> list = new ArrayList<>(namesSet);
        list.sort(Comparator.naturalOrder());
        return list.toArray(new String[0]);
    }

    /**
     * Get property value defined in the set of {@code app.properties} files.
     *
     * @param key property key
     * @return property value or null if the key is not found
     */
    @Nullable
    public String getProperty(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Null key passed as parameter");
        }

        for (Map.Entry<String, Collection<String>> entry : DEPRECATED_PROPERTIES.asMap().entrySet()) {
            String newProperty = entry.getKey();
            Collection<String> deprecatedProperties = entry.getValue();

            if (newProperty.endsWith("*")) {
                String deprecatedProperty = deprecatedProperties.stream().findFirst().orElse(null);
                if (deprecatedProperty != null) {
                    String substring1 = newProperty.substring(0, newProperty.length() - 1);
                    String substring2 = deprecatedProperty.substring(0, deprecatedProperty.length() - 1);
                    if (key.startsWith(substring1)) {
                        return getActualProperty(key, Collections.singletonList(substring2 + key.substring(substring1.length())));
                    }
                    if (key.startsWith(substring2)) {
                        return getActualProperty(substring1 + key.substring(substring2.length()), Collections.singletonList(key));
                    }
                }
            }
            if (newProperty.equals(key) || deprecatedProperties.contains(key)) {
                return getActualProperty(newProperty, deprecatedProperties);
            }
        }
        return getSystemOrAppProperty(key);
    }

    @Nullable
    private String getActualProperty(String newProperty, Collection<String> deprecatedProperties) {
        for (String property : deprecatedProperties) {
            String value = getSystemOrAppProperty(property);
            if (value != null) {
                return value;
            }
        }
        return getSystemOrAppProperty(newProperty);
    }

    @Nullable
    private String getSystemOrAppProperty(String key) {
        String value = getSystemValue(key);
        if (StringUtils.isEmpty(value)) {
            value = properties.get(key);
        }

        if (value != null) {
            // escaped +
            if (value.startsWith("\\+")) {
                return handleInterpolation(value.substring(1));
            }

            // not +
            if (!value.startsWith("+")) {
                return handleInterpolation(value);
            }

            List<String> values = new LinkedList<>();

            // +
            String cleanValue = value.substring(1);
            int index = 0;
            for (String valuePart : split(cleanValue)) {
                if (!values.contains(valuePart)) {
                    values.add(index, valuePart);
                    index++;
                }
            }
            getValuesFromAppComponents(key, values);
            if (values.isEmpty()) {
                return null;
            }
            return handleInterpolation(Joiner.on(" ").join(values));

        } else {
            List<String> values = new LinkedList<>();
            getValuesFromAppComponents(key, values);
            if (values.isEmpty()) {
                return null;
            }
            return handleInterpolation(Joiner.on(" ").join(values));
        }
    }

    private String getSystemValue(String key) {
        String value = systemProperties.get(key);

        if (StringUtils.isEmpty(value)) {
            value = System.getenv(key);
            if (StringUtils.isEmpty(value)
                    && !Boolean.parseBoolean(properties.get("cuba.disableUppercaseEnvironmentProperties"))) {
                value = System.getenv(key.replace('.', '_').toUpperCase());
            }
        }

        return value;
    }

    private String handleInterpolation(String value) {
        StringSubstitutor substitutor = new StringSubstitutor(key -> {
            String property = getSystemOrAppProperty(key);
            return property != null ? property : System.getProperty(key);
        });
        return substitutor.replace(value);
    }

    private void getValuesFromAppComponents(String key, List<String> values) {
        List<AppComponent> components = appComponents.getComponents();
        ListIterator<AppComponent> iterator = components.listIterator(components.size());

        int index;
        while (iterator.hasPrevious()) {
            AppComponent component = iterator.previous();

            String compValue = component.getProperty(key);
            if (StringUtils.isNotEmpty(compValue)) {
                if (component.isAdditiveProperty(key)) {
                    index = 0;
                    for (String valuePart : split(compValue)) {
                        if (!values.contains(valuePart)) {
                            values.add(index, valuePart);
                            index++;
                        }
                    }
                } else {
                    values.add(0, compValue);
                    // we found overwrite, stop iteration
                    break;
                }
            }
        }
    }

    private Iterable<String> split(String compValue) {
        return Splitter.on(SEPARATOR_PATTERN).omitEmptyStrings().split(compValue);
    }

    /**
     * Set property value. The new value will be accessible at the runtime through {@link #getProperty(String)} and
     * {@link #getPropertyNames()}, but will not be saved in any <code>app.properties</code> file and will be lost
     * after the application restart.
     *
     * @param key   property key
     * @param value property value. If null, the property will be removed.
     */
    public void setProperty(String key, @Nullable String value) {
        if (value == null)
            properties.remove(key);
        else
            properties.put(key, value);
    }
}
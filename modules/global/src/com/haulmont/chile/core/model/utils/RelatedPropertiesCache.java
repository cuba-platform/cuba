/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.chile.core.model.utils;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;

import javax.annotation.Nullable;
import java.util.*;

import static com.haulmont.cuba.core.global.MetadataTools.PERSISTENT_ANN_NAME;

public class RelatedPropertiesCache {
    private final Map<String, Set<String>> propertiesMap = new HashMap<>();

    public RelatedPropertiesCache(MetaClass metaClass) {
        Objects.requireNonNull(metaClass, "metaClass is null");

        for (MetaProperty metaProperty : metaClass.getProperties()) {

            if (metaProperty.isReadOnly() && isNotPersistent(metaClass, metaProperty)) {
                Collection<String> relatedProperties = getRelatedProperties(metaProperty);

                for (String relatedProperty : relatedProperties) {
                    Set<String> relatedReadOnlyProperties = propertiesMap
                            .computeIfAbsent(relatedProperty, k -> new HashSet<>());
                    relatedReadOnlyProperties.add(metaProperty.getName());
                }
            }
        }
    }

    @Nullable
    public Set<String> getRelatedReadOnlyProperties(String propertyName) {
        return propertiesMap.get(propertyName);
    }

    private boolean isNotPersistent(MetaClass metaClass, MetaProperty metaProperty) {
        return !isPersistent(metaClass, metaProperty);
    }

    private boolean isPersistent(MetaClass metaClass, MetaProperty metaProperty) {
        return isPersistent(metaClass) && Boolean.TRUE.equals(metaProperty.getAnnotations().get(PERSISTENT_ANN_NAME));
    }

    private boolean isPersistent(MetaClass metaClass) {
        return Boolean.TRUE.equals(metaClass.getAnnotations().get(PERSISTENT_ANN_NAME))
                && metaClass.getJavaClass().isAnnotationPresent(javax.persistence.Entity.class);
    }

    private List<String> getRelatedProperties(MetaProperty metaProperty) {
        String relatedProperties = (String) metaProperty.getAnnotations().get("relatedProperties");
        List<String> result = Collections.emptyList();
        if (relatedProperties != null) {
            result = Arrays.asList(relatedProperties.split(","));
        }
        return result;
    }
}

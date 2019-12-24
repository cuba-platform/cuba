/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.chile.core.model.MetadataObject;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.EntityStates;
import com.haulmont.cuba.core.global.Metadata;
import org.eclipse.persistence.internal.queries.EntityFetchGroup;
import org.eclipse.persistence.queries.FetchGroup;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
public class FetchGroupUtils {

    public static FetchGroup suggestFetchGroup(Entity entity, Metadata metadata, EntityStates entityStates) {
        Set<String> attributes = metadata.getClassNN(entity.getClass()).getProperties().stream()
                .filter(metaProperty ->
                        !metaProperty.getRange().isClass() || entityStates.isLoaded(entity, metaProperty.getName()))
                .map(MetadataObject::getName)
                .collect(Collectors.toSet());
        return new CubaEntityFetchGroup(new EntityFetchGroup(attributes));
    }

    public static FetchGroup mergeFetchGroups(@Nullable FetchGroup first, @Nullable FetchGroup second) {
        Set<String> attributes = new HashSet<>();
        if (first != null)
            attributes.addAll(getFetchGroupAttributes(first));
        if (second != null)
            attributes.addAll(getFetchGroupAttributes(second));
        return new CubaEntityFetchGroup(new EntityFetchGroup(attributes));
    }

    public static Collection<String> getFetchGroupAttributes(FetchGroup fetchGroup) {
        Set<String> result = new HashSet<>();
        traverseFetchGroupAttributes(result, fetchGroup, "");
        return result;
    }

    private static void traverseFetchGroupAttributes(Set<String> set, FetchGroup fetchGroup, String prefix) {
        for (String attribute : fetchGroup.getAttributeNames()) {
            FetchGroup group = fetchGroup.getGroup(attribute);
            if (group != null) {
                traverseFetchGroupAttributes(set, group, prefix + attribute + ".");
            } else {
                set.add(prefix + attribute);
            }
        }
    }
}

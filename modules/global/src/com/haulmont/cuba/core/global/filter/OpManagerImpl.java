/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.core.global.filter;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.entity.Entity;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.*;
import java.util.*;

import static com.haulmont.cuba.core.global.filter.Op.*;

@Component(OpManager.NAME)
public class OpManagerImpl implements OpManager {

    @Inject
    protected MetadataTools metadataTools;
    @Inject
    protected Metadata metadata;

    protected static final List<Class> dateTimeClasses = ImmutableList.of(Date.class, LocalDate.class, LocalDateTime.class,
            OffsetDateTime.class);
    protected static final List<Class> timeClasses = ImmutableList.of(LocalTime.class, OffsetTime.class);

    @Override
    public EnumSet<Op> availableOps(Class javaClass) {
        if (String.class.equals(javaClass))
            return EnumSet.of(EQUAL, IN, NOT_IN, NOT_EQUAL, CONTAINS, DOES_NOT_CONTAIN, NOT_EMPTY, STARTS_WITH, ENDS_WITH);

        else if (dateTimeClasses.contains(javaClass))
            return EnumSet.of(EQUAL, IN, NOT_IN, NOT_EQUAL, GREATER, GREATER_OR_EQUAL, LESSER, LESSER_OR_EQUAL, NOT_EMPTY, DATE_INTERVAL);

        else if (timeClasses.contains(javaClass))
            return EnumSet.of(EQUAL, NOT_EQUAL, GREATER, GREATER_OR_EQUAL, LESSER, LESSER_OR_EQUAL, NOT_EMPTY, DATE_INTERVAL);

        else if (Number.class.isAssignableFrom(javaClass))
            return EnumSet.of(EQUAL, IN, NOT_IN, NOT_EQUAL, GREATER, GREATER_OR_EQUAL, LESSER, LESSER_OR_EQUAL, NOT_EMPTY);

        else if (Boolean.class.equals(javaClass))
            return EnumSet.of(EQUAL, NOT_EQUAL, NOT_EMPTY);

        else if (UUID.class.equals(javaClass)
                || Enum.class.isAssignableFrom(javaClass)
                || Entity.class.isAssignableFrom(javaClass))
            return EnumSet.of(EQUAL, IN, NOT_IN, NOT_EQUAL, NOT_EMPTY);

        else
            throw new UnsupportedOperationException("Unsupported java class: " + javaClass);
    }

    @Override
    public EnumSet<Op> availableOpsForCollectionDynamicAttribute() {
        return EnumSet.of(CONTAINS, DOES_NOT_CONTAIN, NOT_EMPTY);
    }

    @Override
    public EnumSet<Op> availableOps(MetaClass metaClass, MetaProperty metaProperty) {
        Class javaClass = metaProperty.getJavaType();
        if (String.class.equals(javaClass) && metadataTools.isLob(metaProperty)) {
            String storeName = metadata.getTools().getStoreName(metaClass);
            PersistenceManagerService persistenceManagerService = AppBeans.get(PersistenceManagerService.class);
            if (!persistenceManagerService.supportsLobSortingAndFiltering(storeName)) {
                return EnumSet.of(CONTAINS, DOES_NOT_CONTAIN, NOT_EMPTY, STARTS_WITH, ENDS_WITH);
            }
        }
        return availableOps(javaClass);
    }
}
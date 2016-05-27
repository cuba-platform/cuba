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

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.cuba.core.entity.Entity;

import com.haulmont.cuba.core.global.filter.Op;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.EnumSet;
import java.util.UUID;

import static com.haulmont.cuba.core.global.filter.Op.*;

@Component(OpManager.NAME)
public class OpManagerImpl implements OpManager {

    @Override
    public EnumSet<Op> availableOps(Class javaClass) {
        if (String.class.equals(javaClass))
            return EnumSet.of(EQUAL, IN, NOT_IN, NOT_EQUAL, CONTAINS, DOES_NOT_CONTAIN, NOT_EMPTY, STARTS_WITH, ENDS_WITH);

        else if (Date.class.isAssignableFrom(javaClass)
                || Number.class.isAssignableFrom(javaClass))
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
}
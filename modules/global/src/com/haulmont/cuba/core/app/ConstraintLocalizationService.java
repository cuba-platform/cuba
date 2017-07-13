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

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.security.entity.ConstraintOperationType;
import com.haulmont.cuba.security.entity.LocalizedConstraintMessage;

import javax.annotation.Nullable;

/**
 * Constraint localization support service.
 */
public interface ConstraintLocalizationService {

    String NAME = "cuba_ConstraintLocalizationService";

    /**
     * Tries to find an instance of {@link LocalizedConstraintMessage} by given entity name and operation type.
     *
     * @param entityName    the entity name
     * @param operationType the operation type
     * @return an instance of {@link LocalizedConstraintMessage} with given entity name and operation type
     * or null if nothing found.
     */
    @Nullable
    LocalizedConstraintMessage findLocalizedConstraintMessage(String entityName,
                                                              ConstraintOperationType operationType);
}
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

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.security.entity.ConstraintOperationType;
import com.haulmont.cuba.security.entity.LocalizedConstraintMessage;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;

@Service(ConstraintLocalizationService.NAME)
public class ConstraintLocalizationServiceBean implements ConstraintLocalizationService {

    @Inject
    private DataManager dataManager;

    @Nullable
    @Override
    public LocalizedConstraintMessage findLocalizedConstraintMessage(String entityName,
                                                                     ConstraintOperationType operationType) {
        Preconditions.checkNotNullArgument(entityName);
        Preconditions.checkNotNullArgument(operationType);

        LoadContext<LocalizedConstraintMessage> loadContext = new LoadContext<>(LocalizedConstraintMessage.class);
        loadContext.setQueryString("select e from sec$LocalizedConstraintMessage e " +
                "where e.entityName = :name and e.operationType = :type")
                .setParameter("name", entityName)
                .setParameter("type", operationType);

        List<LocalizedConstraintMessage> localizations = dataManager.loadList(loadContext);

        if (CollectionUtils.isEmpty(localizations)) {
            return null;
        } else if (localizations.size() == 1) {
            return localizations.get(0);
        } else {
            throw new IllegalStateException("Several entities with the same 'entity name/operation type' combination");
        }
    }
}
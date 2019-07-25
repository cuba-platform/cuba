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

package com.haulmont.cuba.core.app;

import com.google.common.base.Preconditions;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TransactionalDataManager;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.contracts.Id;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.Stores;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(BulkEditorDataService.NAME)
public class BulkEditorDataServiceBean implements BulkEditorDataService {

    @Inject
    protected DataManager dataManager;
    @Inject
    protected TransactionalDataManager txDataManager;

    @Inject
    protected MetadataTools metadataTools;
    @Inject
    protected Persistence persistence;

    @Override
    public List<Entity> reload(LoadDescriptor ld) {
        if (metadataTools.hasCompositePrimaryKey(ld.getMetaClass())) {
            return loadItemsWithCompositeKey(ld);
        }

        return loadItemsWithDirectKey(ld);
    }

    @SuppressWarnings("unchecked")
    protected List<Entity> loadItemsWithCompositeKey(LoadDescriptor ld) {
        Preconditions.checkNotNull(ld.getMetaClass(), "metaClass is null");

        TransactionalDataManager secureDataManager = txDataManager.secure();
        String storeName = metadataTools.getStoreName(ld.getMetaClass());
        if (storeName == null) {
            storeName = Stores.MAIN;
        }

        List<Entity> items;
        try (Transaction tx = persistence.createTransaction(storeName)) {

            // for composite key we can only load with N queries, since IN operator is not supported for composite keys
            items = ld.getSelectedItems().stream()
                    .map(item ->
                            secureDataManager.load(Id.of(item))
                                    .view(ld.getView())
                                    .softDeletion(false)
                                    .dynamicAttributes(ld.isLoadDynamicAttributes())
                                    .optional()
                    )
                    .filter(Optional::isPresent)
                    .map(o -> (Entity) o.get())
                    .collect(Collectors.toList());

            tx.commit();
        }
        return items;
    }

    protected List<Entity> loadItemsWithDirectKey(LoadDescriptor ld) {
        List<Object> ids = ld.getSelectedItems().stream()
                .map(Entity::getId)
                .collect(Collectors.toList());

        LoadContext<Entity> lc = new LoadContext<>(ld.getMetaClass());
        lc.setSoftDeletion(false);
        lc.setIds(ids);
        lc.setView(ld.getView());
        lc.setLoadDynamicAttributes(ld.isLoadDynamicAttributes());

        return dataManager.secure().loadList(lc);
    }
}
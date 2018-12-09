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

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TransactionalDataManager;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.contracts.Id;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.MetadataTools;
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
        TransactionalDataManager secureDataManager = txDataManager.secure();

        List<Entity> items;
        try (Transaction tx = persistence.createTransaction()) {

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
        LoadContext.Query query = new LoadContext.Query(
                String.format("select e from %s e where e.%s in :ids", ld.getMetaClass(),
                        metadataTools.getPrimaryKeyName(ld.getMetaClass())));

        List<Object> ids = ld.getSelectedItems().stream()
                .map(Entity::getId)
                .collect(Collectors.toList());
        query.setParameter("ids", ids);

        LoadContext<Entity> lc = new LoadContext<>(ld.getMetaClass());
        lc.setSoftDeletion(false);
        lc.setQuery(query);
        lc.setView(ld.getView());
        lc.setLoadDynamicAttributes(ld.isLoadDynamicAttributes());

        return dataManager.secure().loadList(lc);
    }
}
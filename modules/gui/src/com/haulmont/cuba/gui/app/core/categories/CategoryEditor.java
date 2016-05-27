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

package com.haulmont.cuba.gui.app.core.categories;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.data.DataSupplier;
import org.apache.commons.lang.BooleanUtils;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CategoryEditor extends AbstractEditor<Category> {
    @Inject
    protected DataSupplier dataSupplier;

    @Inject
    protected Metadata metadata;

    @Inject
    protected MessageTools messageTools;

    @Inject
    private LookupField entityType;

    @Inject
    private CheckBox isDefault;

    protected Category category;

    @Override
    protected void postInit() {
        category = getItem();
        initEntityTypeField();
        initIsDefaultCheckbox();
    }

    protected void initEntityTypeField() {
        final ExtendedEntities extendedEntities = metadata.getExtendedEntities();

        Map<String, Object> options = new TreeMap<>();//the map sorts meta classes by the string key
        for (MetaClass metaClass : metadata.getTools().getAllPersistentMetaClasses()) {
            options.put(messageTools.getDetailedEntityCaption(metaClass), metaClass);
        }
        entityType.setOptionsMap(options);

        if (category.getEntityType() != null) {
            entityType.setValue(extendedEntities.getEffectiveMetaClass(
                    extendedEntities.getEffectiveClass(category.getEntityType())));
        }

        entityType.addValueChangeListener(e -> {
            MetaClass metaClass = (MetaClass) e.getValue();
            MetaClass originalClass = extendedEntities.getOriginalMetaClass(metaClass);
            category.setEntityType(originalClass == null ? metaClass.getName() : originalClass.getName());
        });
    }

    protected void initIsDefaultCheckbox() {
        isDefault.setValue(BooleanUtils.isTrue(category.getIsDefault()));
        isDefault.addValueChangeListener(e -> {
            if (Boolean.TRUE.equals(e.getValue())) {
                LoadContext categoriesContext = new LoadContext(category.getClass());
                LoadContext.Query query = categoriesContext.setQueryString("select c from sys$Category c where c.entityType= :entityType and not c.id=:id");
                categoriesContext.setView("category.defaultEdit");
                query.setParameter("entityType", category.getEntityType());
                query.setParameter("id", category.getId());
                List<Category> categories = dataSupplier.loadList(categoriesContext);
                for (Category cat : categories) {
                    cat.setIsDefault(false);
                }
                CommitContext commitContext = new CommitContext(categories);
                dataSupplier.commit(commitContext);
                category.setIsDefault(true);
            } else if (Boolean.FALSE.equals(e.getValue())) {
                category.setIsDefault(false);
            }
        });
    }
}
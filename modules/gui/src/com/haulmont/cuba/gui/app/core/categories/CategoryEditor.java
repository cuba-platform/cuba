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
import com.haulmont.cuba.core.entity.HasUuid;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.LookupField;
import org.apache.commons.lang.BooleanUtils;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CategoryEditor extends AbstractEditor<Category> {
    @Inject
    protected DataManager dataManager;
    @Inject
    protected Metadata metadata;
    @Inject
    protected MessageTools messageTools;

    @Inject
    protected GroupBoxLayout localizedGroupBox;

    protected LocalizedNameFrame localizedFrame;

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected LookupField entityType;
    @Inject
    protected CheckBox isDefault;

    protected Category category;

    @Override
    protected void postInit() {
        category = getItem();
        initEntityTypeField();
        initIsDefaultCheckbox();
        initLocalizedFrame();
    }

    protected void initLocalizedFrame() {
        if (globalConfig.getAvailableLocales().size() > 1) {
            localizedGroupBox.setVisible(true);
            localizedFrame = (LocalizedNameFrame) openFrame(localizedGroupBox, "localizedNameFrame");
            localizedFrame.setWidth("100%");
            localizedFrame.setHeight(AUTO_SIZE);
            localizedFrame.setValue(category.getLocaleNames());
        }
    }

    protected void initEntityTypeField() {
        final ExtendedEntities extendedEntities = metadata.getExtendedEntities();

        Map<String, Object> options = new TreeMap<>();//the map sorts meta classes by the string key
        for (MetaClass metaClass : metadata.getTools().getAllPersistentMetaClasses()) {
            if (metadata.getTools().hasCompositePrimaryKey(metaClass) && !HasUuid.class.isAssignableFrom(metaClass.getJavaClass())) {
                continue;
            }
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
                LoadContext<Category> lc = new LoadContext<>(Category.class)
                        .setView("category.defaultEdit");
                lc.setQueryString("select c from sys$Category c where c.entityType = :entityType and not c.id = :id")
                        .setParameter("entityType", category.getEntityType())
                        .setParameter("id", category.getId());
                List<Category> result = dataManager.loadList(lc);
                for (Category cat : result) {
                    cat.setIsDefault(false);
                }
                CommitContext commitCtx = new CommitContext(result);
                dataManager.commit(commitCtx);
                category.setIsDefault(true);
            } else if (Boolean.FALSE.equals(e.getValue())) {
                category.setIsDefault(false);
            }
        });
    }

    @Override
    protected boolean preCommit() {
        if (localizedFrame != null) {
            category.setLocaleNames(localizedFrame.getValue());
        }
        return super.preCommit();
    }
}
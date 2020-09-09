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

package com.haulmont.cuba.gui.components.actions;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.Actions;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.actions.picker.ClearAction;
import com.haulmont.cuba.gui.actions.picker.LookupAction;
import com.haulmont.cuba.gui.actions.picker.OpenAction;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.value.DatasourceValueSource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Contains utility methods used by GUI actions.
 */
@Component(GuiActionSupport.NAME)
public class GuiActionSupport {

    public static final String NAME = "cuba_GuiActionSupport";

    @Inject
    protected ViewRepository viewRepository;
    @Inject
    protected EntityStates entityStates;
    @Inject
    protected DynamicAttributesGuiTools dynamicAttributesGuiTools;

    @Inject
    protected MetadataTools metadataTools;
    @Inject
    protected Actions actions;

    /**
     * Returns an entity reloaded with the view of the target datasource if it is wider than the set of attributes
     * that is loaded in the given entity instance. The entity is also reloaded if the target datasource requires
     * dynamic attributes and the entity instance does not contain them.
     */
    public Entity reloadEntityIfNeeded(Entity entity, Datasource targetDatasource) {
        boolean needDynamicAttributes = false;
        boolean dynamicAttributesAreLoaded = true;
        if (entity instanceof BaseGenericIdEntity) {
            BaseGenericIdEntity e = (BaseGenericIdEntity) entity;
            dynamicAttributesAreLoaded = e.getDynamicAttributes() != null;
            needDynamicAttributes = targetDatasource.getLoadDynamicAttributes();
        }

        View view = targetDatasource.getView();
        if (view == null) {
            view = viewRepository.getView(entity.getClass(), View.LOCAL);
        }

        if (!entityStates.isLoadedWithView(entity, view)) {
            entity = targetDatasource.getDsContext().getDataSupplier().reload(entity, view, null, needDynamicAttributes);
        } else if (needDynamicAttributes && !dynamicAttributesAreLoaded) {
            dynamicAttributesGuiTools.reloadDynamicAttributes((BaseGenericIdEntity) entity);
        }
        return entity;
    }

    /**
     * Adds actions specified in {@link Lookup} annotation on entity attribute to the given PickerField.
     *
     * @param pickerField field
     * @return true if actions have been added
     */
    public boolean createActionsByMetaAnnotations(PickerField pickerField) {
        ValueSource valueSource = pickerField.getValueSource();
        if (!(valueSource instanceof EntityValueSource)) {
            return false;
        }

        EntityValueSource entityValueSource = (EntityValueSource) pickerField.getValueSource();
        MetaPropertyPath mpp = entityValueSource.getMetaPropertyPath();
        if (mpp == null) {
            return false;
        }

        String[] actionIds = (String[]) metadataTools
                .getMetaAnnotationAttributes(mpp.getMetaProperty().getAnnotations(), Lookup.class)
                .get("actions");
        if (actionIds != null && actionIds.length > 0) {
            for (String actionId : actionIds) {
                createActionById(pickerField, actionId);
            }
            return true;
        }
        return false;
    }

    public void createActionById(PickerField<?> pickerField, String actionId) {
        if (isInLegacyScreen(pickerField)) {
            // in legacy screens
            for (PickerField.ActionType actionType : PickerField.ActionType.values()) {
                if (actionType.getId().equals(actionId.trim())) {
                    pickerField.addAction(actionType.createAction(pickerField));
                    break;
                }
            }
        } else {
            switch (actionId) {
                case "lookup":
                    pickerField.addAction(actions.create(LookupAction.ID));
                    break;

                case "open":
                    pickerField.addAction(actions.create(OpenAction.ID));
                    break;

                case "clear":
                    pickerField.addAction(actions.create(ClearAction.ID));
                    break;

                default:
                    LoggerFactory.getLogger(GuiActionSupport.class)
                            .warn("Unsupported PickerField action type " + actionId);
                    break;
            }
        }
    }

    protected boolean isInLegacyScreen(PickerField<?> pickerField) {
        return pickerField.getFrame() != null
                && pickerField.getFrame().getFrameOwner() instanceof LegacyFrame
                || pickerField.getValueSource() instanceof DatasourceValueSource;
    }
}
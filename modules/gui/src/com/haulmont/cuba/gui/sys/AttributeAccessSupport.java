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

package com.haulmont.cuba.gui.sys;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.MetadataObject;
import com.haulmont.cuba.client.AttributeAccessUpdater;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SecurityState;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component.Editable;
import com.haulmont.cuba.gui.components.ComponentContainer;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.components.data.HasValueSource;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.value.DatasourceValueSource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.EmbeddedDatasource;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.Screen;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Bean of the GUI layer that applies attribute access rules to a frame or screen.
 */
@Component(AttributeAccessSupport.NAME)
public class AttributeAccessSupport {

    public static final String NAME = "cuba_AttributeAccessSupport";

    @Inject
    protected AttributeAccessUpdater attributeAccessUpdater;

    @Inject
    protected Security security;

    @Inject
    private MetadataTools metadataTools;

    /**
     * Apply attribute access rules to a given frame. It means that all components bound to datasources will adjust
     * their visible/read-only/required state according to security state of entity instances contained in the datasources.
     *
     * @param frameOwner frame or screen
     * @param reset      whether to reset the components to the default state specified by role-based security and model
     *                   annotations. If you invoke this method to apply attribute access to already opened screen, set
     *                   the parameter to true, but keep in mind that previous programmatic changes in the components
     *                   visible/read-only/required state will be lost.
     */
    public void applyAttributeAccess(FrameOwner frameOwner, boolean reset) {
        ComponentContainer componentContainer;
        if (frameOwner instanceof Screen) {
            componentContainer = ((Screen) frameOwner).getWindow();
        } else {
            componentContainer = (Window) frameOwner;
        }
        ComponentsHelper.walkComponents(componentContainer, (component, name) -> visitComponent(component, reset));
    }

    /**
     * Apply attribute access rules to a given frame according to the state of the given entities. All passed entities
     * will be sent to the middleware and their security state will be recalculated. All screen components
     * bound to datasources will adjust their visible/read-only/required state according to security state of entity
     * instances contained in the datasources.
     *
     * @param entities   list of instances that should recalculate their security state
     * @param frameOwner frame or screen
     * @param reset      whether to reset the components to the default state specified by role-based security and model
     *                   annotations. If you invoke this method to apply attribute access to already opened screen, set
     *                   the parameter to true, but keep in mind that previous programmatic changes in the components
     *                   visible/read-only/required state will be lost.
     */
    public void applyAttributeAccess(FrameOwner frameOwner, boolean reset, Entity... entities) {
        for (Entity entity : entities) {
            attributeAccessUpdater.updateAttributeAccess(entity);
        }
        applyAttributeAccess(frameOwner, reset);
    }

    protected void visitComponent(com.haulmont.cuba.gui.components.Component component, boolean reset) {
        if (!(component instanceof HasValueSource)) {
            return;
        }

        ValueSource valueSource = ((HasValueSource) component).getValueSource();
        if (!(valueSource instanceof EntityValueSource)) {
            return;
        }

        EntityValueSource entityValueSource = (EntityValueSource) valueSource;

        MetaPropertyPath propertyPath = entityValueSource.getMetaPropertyPath();
        if (valueSource.getState() != BindingState.ACTIVE || propertyPath == null) {
            return;
        }

        if (reset) {
            component.setVisible(security.isEntityAttrReadPermitted(entityValueSource.getEntityMetaClass(), propertyPath.toString()));

            if (component instanceof Editable) {
                ((Editable) component).setEditable(security.isEntityAttrUpdatePermitted(entityValueSource.getEntityMetaClass(), propertyPath.toString()));
            }
            if (component instanceof Field) {
                ((Field) component).setRequired(propertyPath.getMetaProperty().isMandatory());
            }
        }

        Entity item = entityValueSource.getItem();
        ComponentState componentState = calculateComponentState(item, propertyPath);
        if (metadataTools.isEmbeddable(item.getMetaClass()) && entityValueSource instanceof DatasourceValueSource) {
            Datasource ds = ((DatasourceValueSource) entityValueSource).getDatasource();
            if (ds instanceof EmbeddedDatasource) {
                Datasource masterDs = ((EmbeddedDatasource) ds).getMaster();
                item = masterDs.getItem();
                componentState = calculateComponentState(item,
                        metadataTools.resolveMetaPropertyPath(masterDs.getMetaClass(), ((EmbeddedDatasource) ds).getProperty().getName() + "." + propertyPath));
            }
        }

        if (componentState.hidden) {
            component.setVisible(false);
        }
        if (componentState.readOnly) {
            if (component instanceof Editable) {
                ((Editable) component).setEditable(false);
            }
        }
        if (component instanceof Field) {
            if (componentState.required && ((Field) component).isEditable() && component.isVisibleRecursive()) {
                ((Field) component).setRequired(true);
            }
        }
    }

    protected ComponentState calculateComponentState(Entity entity, MetaPropertyPath propertyPath) {
        ComponentState componentState = new ComponentState();
        if (propertyPath == null) {
            return componentState;
        }
        MetaProperty[] metaProperties = propertyPath.getMetaProperties();
        for (int i = 0; i < metaProperties.length; i++) {
            MetaProperty metaProperty = metaProperties[i];
            boolean isLastProperty = i == metaProperties.length - 1;

            SecurityState securityState = getSecurityState(entity);
            String name;
            if (metadataTools.isEmbedded(metaProperty)) {
                name = getEmbeddedAttrName(metaProperties, i);
                componentState = applySecurityState(componentState, securityState, name, isLastProperty);
            }
            name = metaProperty.getName();
            componentState = applySecurityState(componentState, securityState, name, isLastProperty);

            if (!isLastProperty) {
                entity = entity.getValue(name);
                if (entity == null) {
                    break;
                }
            }
        }

        return componentState;
    }

    protected ComponentState applySecurityState(ComponentState componentState, SecurityState securityState, String name, boolean isLastProperty) {
        if (securityState != null) {
            componentState.hidden = test(componentState.hidden, securityState.getHiddenAttributes(), name);
            componentState.readOnly = test(componentState.readOnly, securityState.getReadonlyAttributes(), name);
            if (isLastProperty) {
                componentState.required = test(componentState.required, securityState.getRequiredAttributes(), name);
            }
        }
        return componentState;
    }

    protected String getEmbeddedAttrName(MetaProperty[] metaProperties, int startIndex) {
        List<String> embeddedAttrPath = Arrays.stream(metaProperties)
                .map(MetadataObject::getName)
                .collect(Collectors.toList())
                .subList(startIndex, metaProperties.length);
        return String.join(".", embeddedAttrPath);
    }

    protected SecurityState getSecurityState(Entity entity) {
        return BaseEntityInternalAccess.supportsSecurityState(entity) ? BaseEntityInternalAccess.getSecurityState(entity) : null;
    }

    protected boolean test(boolean value, Collection<String> attributes, String name) {
        return value || attributes != null && attributes.contains(name);
    }

    protected static class ComponentState {
        boolean hidden = false;
        boolean readOnly = false;
        boolean required = false;
    }
}

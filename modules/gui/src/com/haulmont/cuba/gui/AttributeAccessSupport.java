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

package com.haulmont.cuba.gui;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.client.AttributeAccessUpdater;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SecurityState;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.components.Component.Editable;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.EntityValueSource;
import com.haulmont.cuba.gui.components.data.HasValueBinding;
import com.haulmont.cuba.gui.components.data.ValueSource;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collection;

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

    /**
     * Apply attribute access rules to a given frame. It means that all components bound to datasources will adjust
     * their visible/read-only/required state according to security state of entity instances contained in the datasources.
     *
     * @param frame frame or screen
     * @param reset whether to reset the components to the default state specified by role-based security and model
     *              annotations. If you invoke this method to apply attribute access to already opened screen, set
     *              the parameter to true, but keep in mind that previous programmatic changes in the components
     *              visible/read-only/required state will be lost.
     */
    public void applyAttributeAccess(Frame frame, boolean reset) {
        ComponentsHelper.walkComponents(frame, (component, name) -> {
            visitComponent(component, reset);
        });
    }

    /**
     * Apply attribute access rules to a given frame according to the state of the given entities. All passed entities
     * will be sent to the middleware and their security state will be recalculated. All screen components
     * bound to datasources will adjust their visible/read-only/required state according to security state of entity
     * instances contained in the datasources.
     *
     * @param entities list of instances that should recalculate their security state
     * @param frame frame or screen
     * @param reset whether to reset the components to the default state specified by role-based security and model
     *              annotations. If you invoke this method to apply attribute access to already opened screen, set
     *              the parameter to true, but keep in mind that previous programmatic changes in the components
     *              visible/read-only/required state will be lost.
     */
    public void applyAttributeAccess(Frame frame, boolean reset, Entity... entities) {
        for (Entity entity : entities) {
            attributeAccessUpdater.updateAttributeAccess(entity);
        }
        applyAttributeAccess(frame, reset);
    }

    protected void visitComponent(com.haulmont.cuba.gui.components.Component component, boolean reset) {
        if (!(component instanceof HasValueBinding)) {
            return;
        }

        ValueSource valueSource = ((HasValueBinding) component).getValueSource();
        if (!(valueSource instanceof EntityValueSource)) {
            return;
        }

        EntityValueSource entityValueSource = (EntityValueSource) valueSource;

        MetaPropertyPath propertyPath = entityValueSource.getMetaPropertyPath();
        if (valueSource.getState() != BindingState.ACTIVE || propertyPath == null) {
            return;
        }

        if (reset) {
            component.setVisible(security.isEntityAttrReadPermitted(entityValueSource.getMetaClass(), propertyPath.toString()));

            if (component instanceof Editable) {
                ((Editable) component).setEditable(security.isEntityAttrUpdatePermitted(entityValueSource.getMetaClass(), propertyPath.toString()));
            }
            if (component instanceof Field) {
                ((Field) component).setRequired(propertyPath.getMetaProperty().isMandatory());
            }
        }

        ComponentState componentState = calculateComponentState(entityValueSource.getItem(), propertyPath);
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
        MetaProperty[] metaProperties = propertyPath.getMetaProperties();
        ComponentState componentState = new ComponentState();
        for (int i = 0; i < metaProperties.length; i++) {
            MetaProperty metaProperty = metaProperties[i];
            String name = metaProperty.getName();
            SecurityState securityState = getSecurityState(entity);
            if (securityState != null) {
                componentState.hidden = test(componentState.hidden, securityState.getHiddenAttributes(), name);
                componentState.readOnly = test(componentState.readOnly, securityState.getReadonlyAttributes(), name);
                if (i == metaProperties.length - 1) {
                    componentState.required = test(componentState.required, securityState.getRequiredAttributes(), name);
                }
            }
            if (i != metaProperties.length - 1) {
                entity = entity.getValue(name);
                if (entity == null) {
                    break;
                }
            }
        }
        return componentState;
    }

    protected SecurityState getSecurityState(Entity entity) {
        if (entity instanceof BaseGenericIdEntity) {
            return BaseEntityInternalAccess.getSecurityState((BaseGenericIdEntity) entity);
        } else {
            return null;
        }
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

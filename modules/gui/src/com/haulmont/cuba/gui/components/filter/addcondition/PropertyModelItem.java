/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.global.UserSession;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Property node.
 * <p/>
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class PropertyModelItem implements ModelItem {

    private MetaProperty metaProperty;
    private AbstractDescriptorBuilder descriptorBuilder;
    private AbstractConditionDescriptor descriptor;
    private ModelItem parent;

    PropertyModelItem(ModelItem parent, MetaProperty metaProperty,
                      AbstractConditionDescriptor descriptor, AbstractDescriptorBuilder descriptorBuilder) {
        this.parent = parent;
        this.metaProperty = metaProperty;
        this.descriptorBuilder = descriptorBuilder;
        if (descriptor != null) {
            this.descriptor = descriptor;
        } else {
            StringBuilder name = new StringBuilder(metaProperty.getName());
            StringBuilder caption = new StringBuilder(MessageUtils.getPropertyCaption(metaProperty));
            ModelItem item = parent;
            while (item != null && item instanceof PropertyModelItem) {
                name.insert(0, ((PropertyModelItem) item).metaProperty.getName() + ".");
                caption.insert(0, MessageUtils.getPropertyCaption(((PropertyModelItem) item).metaProperty) + ".");
                item = item.getParent();
            }
            this.descriptor = descriptorBuilder.buildPropertyConditionDescriptor(name.toString(), caption.toString());
        }
    }

    @Override
    public ModelItem getParent() {
        return parent;
    }

    @Override
    @Nonnull
    public List<ModelItem> getChildren() {
        if (metaProperty.getRange().isClass()) {
            List<ModelItem> list = new ArrayList<>();

            UserSession userSession = UserSessionProvider.getUserSession();

            for (MetaProperty property : metaProperty.getRange().asClass().getProperties()) {
                // check permissions
                if (userSession.isEntityAttrPermitted(property.getDomain(), property.getName(), EntityAttrAccess.VIEW)) {
                    // exclude not localized properties (they are usually not for end user) and ToMany
                    if (MessageUtils.hasPropertyCaption(property) && !property.getRange().getCardinality().isMany()) {
                        list.add(new PropertyModelItem(this, property, null, descriptorBuilder));
                    }
                }
            }
            Collections.sort(list, new ModelItemComparator());
            return list;
        } else
            return Collections.emptyList();
    }

    @Override
    public String getCaption() {
        return MessageUtils.getPropertyCaption(metaProperty);
    }

    @Override
    public AbstractConditionDescriptor getDescriptor() {
        return descriptor;
    }
}

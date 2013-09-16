/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Property node
 *
 * @author krivopustov
 * @version $Id$
 */
public class PropertyModelItem implements ModelItem {

    private MetaProperty metaProperty;
    private AbstractDescriptorBuilder descriptorBuilder;
    private AbstractConditionDescriptor descriptor;
    private ModelItem parent;
    private List<ModelItem> modelItems;

    PropertyModelItem(ModelItem parent, MetaProperty metaProperty,
                      AbstractConditionDescriptor descriptor, AbstractDescriptorBuilder descriptorBuilder) {
        this.parent = parent;
        this.metaProperty = metaProperty;
        this.descriptorBuilder = descriptorBuilder;
        if (descriptor != null) {
            this.descriptor = descriptor;
        } else {
            StringBuilder name = new StringBuilder(metaProperty.getName());
            StringBuilder caption = new StringBuilder(AppBeans.get(MessageTools.class).getPropertyCaption(metaProperty));
            ModelItem item = parent;
            while (item != null && item instanceof PropertyModelItem) {
                name.insert(0, ((PropertyModelItem) item).metaProperty.getName() + ".");
                caption.insert(0, AppBeans.get(MessageTools.class).getPropertyCaption(((PropertyModelItem) item).metaProperty) + ".");
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
        if (modelItems == null) {
            if (metaProperty.getRange().isClass()) {
                modelItems = new ArrayList<>();

                ModelPropertiesFilter propertiesFilter = new ModelPropertiesFilter();

                for (MetaProperty property : metaProperty.getRange().asClass().getProperties()) {
                    if (propertiesFilter.isPropertyFilterAllowed(property))
                        modelItems.add(new PropertyModelItem(this, property, null, descriptorBuilder));
                }
                Collections.sort(modelItems, new ModelItemComparator());
            } else
                modelItems = Collections.emptyList();
        }
        return modelItems;
    }

    @Override
    public String getCaption() {
        return AppBeans.get(MessageTools.class).getPropertyCaption(metaProperty);
    }

    @Override
    public AbstractConditionDescriptor getDescriptor() {
        return descriptor;
    }
}
/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.AbstractFilterEditor;
import com.haulmont.cuba.gui.components.filter.AbstractPropertyConditionDescriptor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Root of properties branch.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class RootPropertyModelItem implements ModelItem {

    private MetaClass metaClass;
    private List<AbstractConditionDescriptor> propertyDescriptors;
    private AbstractDescriptorBuilder descriptorBuilder;

    public RootPropertyModelItem(MetaClass metaClass, List<AbstractConditionDescriptor> propertyDescriptors,
                          AbstractDescriptorBuilder descriptorBuilder) {
        this.metaClass = metaClass;
        this.propertyDescriptors = propertyDescriptors;
        this.descriptorBuilder = descriptorBuilder;
    }

    @Override
    public ModelItem getParent() {
        return null;
    }

    @Nonnull
    @Override
    public List<ModelItem> getChildren() {
        List<ModelItem> list = new ArrayList<ModelItem>();

        for (AbstractConditionDescriptor descriptor : propertyDescriptors) {
            if (descriptor instanceof AbstractPropertyConditionDescriptor) {
                MetaPropertyPath mpp = metaClass.getPropertyPath(descriptor.getName());
                MetaProperty metaProperty = mpp.getMetaProperties()[0];
                list.add(new PropertyModelItem(null, metaProperty, descriptor, descriptorBuilder));
            }
        }

        Collections.sort(list, new ModelItemComparator());
        return list;
    }

    @Override
    public String getCaption() {
        return MessageProvider.getMessage(AbstractFilterEditor.MESSAGES_PACK, "NewConditionDlg.attributes");
    }

    @Override
    public AbstractConditionDescriptor getDescriptor() {
        return null;
    }
}

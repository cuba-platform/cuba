/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.AbstractFilterEditor;
import com.haulmont.cuba.gui.components.filter.AbstractPropertyConditionDescriptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Root of properties branch.
 *
 * @author krivopustov
 * @version $Id$
 */
public class RootPropertyModelItem implements ModelItem {

    protected Log log = LogFactory.getLog(getClass());

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
        List<ModelItem> list = new ArrayList<>();

        ModelPropertiesFilter modelPropertiesFilter = new ModelPropertiesFilter();

        for (AbstractConditionDescriptor descriptor : propertyDescriptors) {
            if (descriptor instanceof AbstractPropertyConditionDescriptor) {
                MetaPropertyPath mpp = metaClass.getPropertyPath(descriptor.getName());
                if (mpp == null) {
                    log.error("Invalid property name: " + descriptor.getName());
                    continue;
                }
                MetaProperty metaProperty = mpp.getMetaProperty();

                if (modelPropertiesFilter.isPropertyFilterAllowed(metaProperty))
                    list.add(new PropertyModelItem(null, metaProperty, descriptor, descriptorBuilder));
            }
        }

        Collections.sort(list, new ModelItemComparator());
        return list;
    }

    @Override
    public String getCaption() {
        return AppBeans.get(Messages.class).getMessage(AbstractFilterEditor.MESSAGES_PACK, "NewConditionDlg.attributes");
    }

    @Override
    public AbstractConditionDescriptor getDescriptor() {
        return null;
    }
}
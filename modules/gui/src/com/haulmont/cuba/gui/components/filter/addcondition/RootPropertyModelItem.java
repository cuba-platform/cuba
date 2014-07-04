/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
import java.util.Map;

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
    private Map<AbstractConditionDescriptor, String> descriptorMessages;
    private AbstractDescriptorBuilder descriptorBuilder;

    private List<ModelItem> modelItems;

    public RootPropertyModelItem(MetaClass metaClass, List<AbstractConditionDescriptor> propertyDescriptors,
                                 Map<AbstractConditionDescriptor, String> descriptorMessages, AbstractDescriptorBuilder descriptorBuilder) {
        this.metaClass = metaClass;
        this.propertyDescriptors = propertyDescriptors;
        this.descriptorMessages = descriptorMessages;
        this.descriptorBuilder = descriptorBuilder;
    }

    @Override
    public ModelItem getParent() {
        return null;
    }

    @Nonnull
    @Override
    public List<ModelItem> getChildren() {
        if (modelItems == null) {
            modelItems = new ArrayList<>();

            ModelPropertiesFilter modelPropertiesFilter = new ModelPropertiesFilter();

            for (AbstractConditionDescriptor descriptor : propertyDescriptors) {
                if (descriptor instanceof AbstractPropertyConditionDescriptor) {
                    MetaPropertyPath mpp = metaClass.getPropertyPath(descriptor.getName());
                    if (mpp == null) {
                        log.error("Invalid property name: " + descriptor.getName());
                        continue;
                    }

                    MetaProperty metaProperty = mpp.getMetaProperty();
                    if (modelPropertiesFilter.isPropertyFilterAllowed(metaProperty)) {
                        modelItems.add(new PropertyModelItem(null, metaProperty, descriptorMessages, descriptor, descriptorBuilder));
                    }
                }
            }

            Collections.sort(modelItems, new ModelItemComparator());
        }
        return modelItems;
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
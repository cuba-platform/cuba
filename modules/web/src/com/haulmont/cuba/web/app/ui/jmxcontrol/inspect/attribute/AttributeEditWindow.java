/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.jmxcontrol.inspect.attribute;

import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.GridLayout;
import com.haulmont.cuba.web.app.ui.jmxcontrol.util.AttributeEditor;
import com.haulmont.cuba.web.jmx.JmxControlAPI;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanAttribute;
import org.apache.commons.lang.ObjectUtils;

import javax.inject.Inject;

/**
 * @author budarov
 * @version $Id$
 */
public class AttributeEditWindow extends AbstractEditor<ManagedBeanAttribute> {

    protected AttributeEditor valueHolder;

    @Inject
    protected JmxControlAPI jmxControlAPI;

    @Inject
    protected GridLayout valueContainer;

    @Override
    protected void postInit() {
        ManagedBeanAttribute mba = getItem();
        final String type = mba.getType();

        valueHolder = new AttributeEditor(this, type, mba.getValue(), true);

        valueContainer.add(valueHolder.getComponent(), 1, 0);

        if (mba.getName() != null) {
            setCaption(formatMessage("editAttribute.title.format", mba.getName()));
        }
    }

    @Override
    public void commitAndClose() {
        if (assignValue()) {
            close(COMMIT_ACTION_ID, true);
        }
    }

    private boolean assignValue() {
        ManagedBeanAttribute mba = getItem();

        Object oldValue = mba.getValue();
        try {
            Object newValue = valueHolder != null ? valueHolder.getAttributeValue(false) : null;
            if (newValue != null) {
                if (!ObjectUtils.equals(mba.getValue(), newValue)) {
                    mba.setValue(newValue);
                    jmxControlAPI.saveAttributeValue(mba);
                }
                return true;
            }
        } catch (Exception e) {
            getDialogParams().setWidth(640);
            showMessageDialog(String.format(getMessage("editAttribute.exception"), mba.getName()),
                    e.getClass().getCanonicalName() + " " + e.getMessage() + "\n",
                    MessageType.WARNING);
            mba.setValue(oldValue);
            return false;
        }
        showNotification(getMessage("editAttribute.conversionError"), NotificationType.HUMANIZED);
        return false;
    }
}
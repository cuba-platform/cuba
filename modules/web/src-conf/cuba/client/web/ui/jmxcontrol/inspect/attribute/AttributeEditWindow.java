/*
 * Copyright (c) 2008-2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Alexander Budarov
 * Created: 23.08.2010 12:42:53
 * $Id$
 */

package cuba.client.web.ui.jmxcontrol.inspect.attribute;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.jmxcontrol.app.JmxControlService;
import com.haulmont.cuba.jmxcontrol.entity.ManagedBeanAttribute;
import cuba.client.web.ui.jmxcontrol.util.AttributeEditor;
import org.apache.commons.lang.ObjectUtils;

public class AttributeEditWindow extends AbstractEditor {
    private static final long serialVersionUID = -994735809393128156L;

    private AttributeEditor valueHolder;

    public AttributeEditWindow(IFrame frame) {
        super(frame);
    }

    @Override
    public void setItem(Entity item) {
        super.setItem(item);

        ManagedBeanAttribute mba = (ManagedBeanAttribute) getItem();
        final String type = mba.getType();

        GridLayout layout = getComponent("valueContainer");
        valueHolder = new AttributeEditor(this, type, mba.getValue());

        layout.add(valueHolder.getComponent(), 1, 0);
    }

    @Override
    public void commitAndClose() {
        if (assignValue()) {
            super.commitAndClose();
        }
    }

    private boolean assignValue() {
        ManagedBeanAttribute mba = (ManagedBeanAttribute) getItem();

        try {
            Object newValue = valueHolder != null ? valueHolder.getAttributeValue() : null;
            if (newValue != null) {
                if (!ObjectUtils.equals(mba.getValue(), newValue)) {
                    mba.setValue(newValue);
                    JmxControlService jcs = ServiceLocator.lookup(JmxControlService.NAME);
                    jcs.saveAttributeValue(mba);
                }
                return true;
            }
        }
        catch (Exception e) {
            showNotification(String.format(getMessage("editAttribute.exception"), e.getMessage()),
                    IFrame.NotificationType.HUMANIZED);
            return false;
        }
        showNotification(getMessage("editAttribute.conversionError"), IFrame.NotificationType.HUMANIZED);
        return false;
    }

}

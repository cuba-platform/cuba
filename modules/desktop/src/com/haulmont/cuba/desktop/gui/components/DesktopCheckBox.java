/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import org.apache.commons.lang.BooleanUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopCheckBox extends DesktopAbstractComponent<JCheckBox> implements CheckBox {

    private Datasource datasource;
    private MetaProperty metaProperty;
    private MetaPropertyPath metaPropertyPath;

    private boolean updatingInstance;
    private boolean required;

    public DesktopCheckBox() {
        impl = new JCheckBox();
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setRequiredMessage(String msg) {
    }

    public <T> T getValue() {
        return (T) (Boolean) impl.isSelected();
    }

    public void setValue(Object value) {
        impl.setSelected((Boolean) value);
        updateInstance();
    }

    public void addListener(ValueListener listener) {
    }

    public void removeListener(ValueListener listener) {
    }

    public void addValidator(Validator validator) {
    }

    public void removeValidator(Validator validator) {
    }

    public boolean isValid() {
        return true;
    }

    public void validate() throws ValidationException {
    }

    public Datasource getDatasource() {
        return datasource;
    }

    public MetaProperty getMetaProperty() {
        return metaProperty;
    }

    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        final MetaClass metaClass = datasource.getMetaClass();
        metaPropertyPath = metaClass.getPropertyPath(property);
        try {
            metaProperty = metaPropertyPath.getMetaProperty();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Metaproperty name is possibly wrong: " + property, e);
        }

        datasource.addListener(
                new DsListenerAdapter() {
                    @Override
                    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                        if (updatingInstance)
                            return;
                        Boolean value = InstanceUtils.getValueEx((Instance) item, metaPropertyPath.getPath());
                        impl.setSelected(BooleanUtils.isTrue(value));
                    }

                    @Override
                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                        if (updatingInstance)
                            return;
                        if (property.equals(metaPropertyPath.toString())) {
                            impl.setSelected(BooleanUtils.isTrue((Boolean) value));
                        }
                    }
                }
        );

        impl.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        updateInstance();
                    }
                }
        );

        setRequired(metaProperty.isMandatory());
    }

    public String getCaption() {
        return impl.getText();
    }

    public void setCaption(String caption) {
        impl.setText(caption);
    }

    public String getDescription() {
        return null;
    }

    public void setDescription(String description) {
    }

    public boolean isEditable() {
        return impl.isEnabled();
    }

    public void setEditable(boolean editable) {
        impl.setEnabled(editable);
    }

    private void updateInstance() {
        if (updatingInstance || datasource == null || metaPropertyPath == null)
            return;

        updatingInstance = true;
        try {
            boolean value = impl.isSelected();
            InstanceUtils.setValueEx((Instance) datasource.getItem(), metaPropertyPath.getPath(), value);
        } finally {
            updatingInstance = false;
        }
    }

}

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
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.vcl.Picker;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import org.apache.commons.lang.ObjectUtils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopPickerField
    extends DesktopAbstractField<Picker>
    implements PickerField
{
    protected CaptionMode captionMode = CaptionMode.ITEM;
    protected String captionProperty;

    protected Datasource<Entity> datasource;
    protected MetaProperty metaProperty;
    protected MetaPropertyPath metaPropertyPath;

    protected MetaClass metaClass;

    protected Object prevValue;
    protected String prevTextValue;

    private boolean editable = true;

    protected java.util.List<Action> actionsOrder = new LinkedList<Action>();
    private String caption;
    private boolean updatingInstance;

    private Object nullValue = new Object();

    public DesktopPickerField() {
        impl = new Picker();
    }

    public DesktopPickerField(Picker picker) {
        impl = picker;
    }

    @Override
    public CaptionMode getCaptionMode() {
        return captionMode;
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
        this.captionMode = captionMode;
    }

    @Override
    public String getCaptionProperty() {
        return captionProperty;
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;
    }

    @Override
    public MetaClass getMetaClass() {
        Datasource ds = getDatasource();
        if (ds != null) {
            return metaProperty.getRange().asClass();
        } else {
            return metaClass;
        }
    }

    @Override
    public void setMetaClass(MetaClass metaClass) {
        Datasource ds = getDatasource();
        if (ds != null)
            throw new IllegalStateException("Datasource is not null");
        this.metaClass = metaClass;
    }

    @Override
    public LookupAction addLookupAction() {
        LookupAction action = new LookupAction(this);
        addAction(action);
        return action;
    }

    @Override
    public ClearAction addClearAction() {
        ClearAction action = new ClearAction(this);
        addAction(action);
        return action;
    }

    @Override
    public OpenAction addOpenAction() {
        OpenAction action = new OpenAction(this);
        addAction(action);
        return action;
    }

    @Override
    public void addFieldListener(final FieldListener listener) {
        final JTextField field = (JTextField) impl.getEditor();
        field.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                fireFieldListener(listener, field.getText());
            }
        });

        field.addKeyListener(new KeyAdapter() {

            protected final int ENTER_CODE = 10;

            @Override
            public void keyPressed(KeyEvent e) {
                if (ENTER_CODE == e.getKeyCode()) {
                    fireFieldListener(listener, field.getText());
                }
            }
        });
    }

    private void fireFieldListener(FieldListener listener, String fieldText) {
        if (!(ObjectUtils.equals(prevTextValue,fieldText))) {
            prevValue = nullValue;
            prevTextValue = fieldText;
            listener.actionPerformed(fieldText, getValue());
        }
    }

    @Override
    public void setFieldEditable(boolean editable) {
        if (isEditable())
            ((JTextField) impl.getEditor()).setEditable(editable);
    }

    @Override
    public <T> T getValue() {
        if ((datasource != null) && (metaPropertyPath != null)) {
            return (T) datasource.getItem().getValue(metaProperty.getName());
        } else
            return (T) prevValue;
    }

    @Override
    public void setValue(Object value) {
        if (!ObjectUtils.equals(prevValue, value)) {
            updateInstance(value);
            updateComponent(value);
            fireChangeListeners(value);
        }
    }

    private void updateInstance(Object value) {
        if (updatingInstance)
            return;

        if (ObjectUtils.equals(prevValue, value))
            return;

        updatingInstance = true;
        try {
            if (datasource != null && metaProperty != null && datasource.getItem() != null)
                InstanceUtils.setValueEx(datasource.getItem(), metaPropertyPath.getPath(), value);
        } finally {
            updatingInstance = false;
        }
    }

    @Override
    public Datasource getDatasource() {
        return datasource;
    }

    @Override
    public MetaProperty getMetaProperty() {
        return metaProperty;
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        if (datasource == null) {
            setValue(null);
            return;
        }

        final MetaClass metaClass = datasource.getMetaClass();
        metaPropertyPath = metaClass.getPropertyPath(property);
        metaProperty = metaPropertyPath.getMetaProperty();

        datasource.addListener(
                new DsListenerAdapter() {
                    @Override
                    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                        if (updatingInstance)
                            return;

                        Object value = InstanceUtils.getValueEx(item, metaPropertyPath.getPath());
                        updateComponent(value);
                        fireChangeListeners(value);
                    }

                    @Override
                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                        if (updatingInstance)
                            return;

                        if (property.equals(metaProperty.getName())) {
                            updateComponent(value);
                            fireChangeListeners(value);
                        }
                    }
                }
        );

        if ((datasource.getState() == Datasource.State.VALID) && (datasource.getItem() != null)) {
            Object newValue = InstanceUtils.getValueEx(datasource.getItem(), metaPropertyPath.getPath());
            updateComponent(newValue);
            fireChangeListeners();
        }
    }

    private void fireChangeListeners() {
        fireChangeListeners(getValue());
    }

    private void fireChangeListeners(Object newValue) {
        if (!ObjectUtils.equals(prevValue, newValue)) {
            fireValueChanged(prevValue, newValue);
            prevValue = newValue;
        }
    }

    protected void updateComponent(Object value) {
        String text;

        if (value == null) {
            text = "";
        } else {
            if (value instanceof Instance) {
                if (captionMode.equals(CaptionMode.ITEM)) {
                    text = ((Instance) value).getInstanceName();
                } else {
                    text = ((Instance) value).getValueEx(captionProperty);
                }
            } else {
                text = value.toString();
            }
        }

        impl.setValue(text);
        prevTextValue=text;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String getDescription() {
        return impl.getEditor().getToolTipText();
    }

    @Override
    public void setDescription(String description) {
        impl.getEditor().setToolTipText(description);
        DesktopToolTipManager.getInstance().registerTooltip(impl.getEditor());
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
        for (Action action : actionsOrder) {
            if (action instanceof StandardAction)
                ((StandardAction) action).setEditable(isEditable());
        }
        if (!editable && impl.getEditor() instanceof JTextComponent) {
            ((JTextComponent) impl.getEditor()).setEditable(editable);
        }
    }

    @Override
    public void addAction(Action action) {
        actionsOrder.add(action);
        DesktopButton dButton = new DesktopButton();
        dButton.setAction(action);
        impl.addButton(dButton.getImpl());
        // apply Editable after action owner is set
        if (action instanceof StandardAction)
            ((StandardAction) action).setEditable(isEditable());
    }

    @Override
    public void removeAction(Action action) {
        actionsOrder.remove(action);
        if (action.getOwner() != null && action.getOwner() instanceof DesktopButton) {
            JButton button = ((DesktopButton) action.getOwner()).getImpl();
            impl.removeButton(button);
        }
    }

    public Collection<Action> getActions() {
        return Collections.unmodifiableCollection(actionsOrder);
    }

    public Action getAction(String id) {
        for (Action action : getActions()) {
            if (ObjectUtils.equals(action.getId(), id)) {
                return action;
            }
        }
        return null;
    }
}

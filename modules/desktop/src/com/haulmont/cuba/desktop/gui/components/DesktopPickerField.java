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
import com.haulmont.cuba.desktop.sys.vcl.Picker;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import org.apache.commons.lang.ObjectUtils;

import javax.swing.*;
import java.util.*;

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

    protected Object value;

    private boolean editable = true;

    protected java.util.List<Action> actionsOrder = new LinkedList<Action>();

    public DesktopPickerField() {
        impl = new Picker();
        addLookupAction();
        addClearAction();
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
    public <T> T getValue() {
        if (datasource == null) {
            return (T) value;
        } else {
            datasource.getItem().getValue(metaProperty.getName());
            return null;
        }

    }

    @Override
    public void setValue(Object value) {
        if (datasource == null) {
            Object oldValue = this.value;
            this.value = value;
            if (!ObjectUtils.equals(oldValue, value))
                fireValueChanged(oldValue, value);
        } else {
            datasource.getItem().setValue(metaProperty.getName(), value);
        }
        updateText(value);
    }

    @Override
    public void validate() throws ValidationException {
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

        final MetaClass metaClass = datasource.getMetaClass();
        metaPropertyPath = metaClass.getPropertyPath(property);
        metaProperty = metaPropertyPath.getMetaProperty();

        datasource.addListener(
                new DsListenerAdapter() {
                    @Override
                    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                        Object prevValue = prevItem == null ? null : InstanceUtils.getValueEx(prevItem, metaPropertyPath.getPath());
                        Object value = InstanceUtils.getValueEx(item, metaPropertyPath.getPath());
                        updateText(value);
                        fireValueChanged(prevValue, value);
                    }

                    @Override
                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                        if (property.equals(metaProperty.getName())) {
                            updateText(value);
                            fireValueChanged(prevValue, value);
                        }
                    }
                }
        );
    }

    protected void updateText(Object value) {
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
    }

    @Override
    public String getCaption() {
        return null;
    }

    @Override
    public void setCaption(String caption) {
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
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

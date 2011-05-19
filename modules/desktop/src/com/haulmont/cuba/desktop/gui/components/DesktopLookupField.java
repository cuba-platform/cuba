/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;

import javax.swing.*;
import java.util.List;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopLookupField
    extends DesktopAbstractComponent<JPanel>
    implements LookupField
{
    public DesktopLookupField() {
        impl = new JPanel(new java.awt.FlowLayout());
        impl.setBorder(BorderFactory.createLineBorder(java.awt.Color.gray));
        impl.add(new JLabel("TODO: lookupField"));
    }

    @Override
    public Object getNullOption() {
        return null;
    }

    @Override
    public void setNullOption(Object nullOption) {
    }

    @Override
    public FilterMode getFilterMode() {
        return null;
    }

    @Override
    public void setFilterMode(FilterMode mode) {
    }

    @Override
    public boolean isNewOptionAllowed() {
        return false;
    }

    @Override
    public void setNewOptionAllowed(boolean newOptionAllowed) {
    }

    @Override
    public NewOptionHandler getNewOptionHandler() {
        return null;
    }

    @Override
    public void setNewOptionHandler(NewOptionHandler newOptionHandler) {
    }

    @Override
    public void disablePaging() {
    }

    @Override
    public boolean isMultiSelect() {
        return false;
    }

    @Override
    public void setMultiSelect(boolean multiselect) {
    }

    @Override
    public CaptionMode getCaptionMode() {
        return null;
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
    }

    @Override
    public String getCaptionProperty() {
        return null;
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
    }

    @Override
    public String getDescriptionProperty() {
        return null;
    }

    @Override
    public void setDescriptionProperty(String descProperty) {
    }

    @Override
    public CollectionDatasource getOptionsDatasource() {
        return null;
    }

    @Override
    public void setOptionsDatasource(CollectionDatasource datasource) {
    }

    @Override
    public List getOptionsList() {
        return null;
    }

    @Override
    public void setOptionsList(List optionsList) {
    }

    @Override
    public Map<String, Object> getOptionsMap() {
        return null;
    }

    @Override
    public void setOptionsMap(Map<String, Object> map) {
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public void setRequired(boolean required) {
    }

    @Override
    public void setRequiredMessage(String msg) {
    }

    @Override
    public <T> T getValue() {
        return null;
    }

    @Override
    public void setValue(Object value) {
    }

    @Override
    public void addListener(ValueListener listener) {
    }

    @Override
    public void removeListener(ValueListener listener) {
    }

    @Override
    public void addValidator(Validator validator) {
    }

    @Override
    public void removeValidator(Validator validator) {
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void validate() throws ValidationException {
    }

    @Override
    public Datasource getDatasource() {
        return null;
    }

    @Override
    public MetaProperty getMetaProperty() {
        return null;
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
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
        return false;
    }

    @Override
    public void setEditable(boolean editable) {
    }
}

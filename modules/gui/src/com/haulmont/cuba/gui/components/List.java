package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.Set;

public interface List  extends Component, Component.BelongToFrame, Component.ActionsOwner {
    boolean isMultiSelect();
    void setMultiSelect(boolean multiselect);

    <T> T getSingleSelected();
    Set getSelected();

    CollectionDatasource getDatasource();
}

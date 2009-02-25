package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.core.entity.Entity;

import java.util.Set;
import java.util.Collection;

public interface List  extends Component, Component.BelongToFrame, Component.ActionsOwner {
    boolean isMultiSelect();
    void setMultiSelect(boolean multiselect);

    <T extends Entity> T getSingleSelected();
    Set getSelected();

    void setSelected(Entity item);
    void setSelected(Collection<Entity> items);

    CollectionDatasource getDatasource();

    void refresh();
}

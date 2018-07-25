package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.CheckBoxGroup;

public class CheckBoxGroupLoader extends AbstractOptionsBaseLoader<CheckBoxGroup> {

    @Override
    public void createComponent() {
        resultComponent = (CheckBoxGroup) factory.createComponent(CheckBoxGroup.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        loadOrientation(resultComponent, element);
        loadCaptionProperty(resultComponent, element);

        loadOptionsEnum(resultComponent, element);
        loadTabIndex(resultComponent, element);
    }
}

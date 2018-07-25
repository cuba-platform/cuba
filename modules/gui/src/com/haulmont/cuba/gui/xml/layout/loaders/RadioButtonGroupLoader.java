package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.RadioButtonGroup;

public class RadioButtonGroupLoader extends AbstractOptionsBaseLoader<RadioButtonGroup> {

    @Override
    public void createComponent() {
        resultComponent = (RadioButtonGroup) factory.createComponent(RadioButtonGroup.NAME);
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

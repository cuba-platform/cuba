package com.haulmont.cuba.gui.components;

import java.util.Set;

public interface CheckBoxGroup<V>
        extends OptionsField<Set<V>, V>, LookupComponent, Component.Focusable, HasOrientation {
    String NAME = "checkBoxGroup";

}

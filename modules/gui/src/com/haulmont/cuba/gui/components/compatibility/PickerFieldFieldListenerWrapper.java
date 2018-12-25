package com.haulmont.cuba.gui.components.compatibility;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.PickerField;

import java.util.function.Consumer;

@Deprecated
public class PickerFieldFieldListenerWrapper<E extends Entity>
        implements Consumer<PickerField.FieldValueChangeEvent<E>> {

    protected final PickerField.FieldListener listener;

    public PickerFieldFieldListenerWrapper(PickerField.FieldListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PickerFieldFieldListenerWrapper that = (PickerFieldFieldListenerWrapper) o;

        return listener.equals(that.listener);
    }

    @Override
    public int hashCode() {
        return listener.hashCode();
    }

    @Override
    public void accept(PickerField.FieldValueChangeEvent<E> event) {
        listener.actionPerformed(event.getText(), event.getPrevValue());
    }
}

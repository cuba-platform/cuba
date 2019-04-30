/*
 * Copyright (c) 2008-2019 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package spec.cuba.web.components.composite.components.stepper;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.CssLayout;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.web.gui.components.CompositeComponent;
import com.haulmont.cuba.web.gui.components.CompositeDescriptor;
import com.haulmont.cuba.web.gui.components.CompositeWithCaption;
import com.haulmont.cuba.web.gui.components.CompositeWithContextHelp;
import com.haulmont.cuba.web.gui.components.CompositeWithHtmlCaption;
import com.haulmont.cuba.web.gui.components.CompositeWithHtmlDescription;
import com.haulmont.cuba.web.gui.components.CompositeWithIcon;
import com.haulmont.cuba.web.widgets.CubaTextField;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;

import java.util.Collection;
import java.util.function.Consumer;

@CompositeDescriptor("stepper-field.xml")
public class TestStepperField extends CompositeComponent<CssLayout> implements Field<Integer>,
        CompositeWithCaption, CompositeWithHtmlCaption, CompositeWithHtmlDescription,
        CompositeWithIcon, CompositeWithContextHelp {

    public static final String NAME = "testStepperField";

    /* Nested Components */
    private TextField<Integer> valueField;
    private Button upBtn;
    private Button downBtn;

    @Override
    protected void setComposition(CssLayout composition) {
        super.setComposition(composition);

        valueField = getInnerComponent("stepper_valueField");
        CubaTextField cubaTextField = valueField.unwrap(CubaTextField.class);
        cubaTextField.addShortcutListener(createAdjustmentShortcut(ShortcutAction.KeyCode.ARROW_UP, 1));
        cubaTextField.addShortcutListener(createAdjustmentShortcut(ShortcutAction.KeyCode.ARROW_DOWN, -1));

        upBtn = getInnerComponent("stepper_upBtn");
        downBtn = getInnerComponent("stepper_downBtn");

        upBtn.addClickListener(clickEvent -> updateValue(1));
        downBtn.addClickListener(clickEvent -> updateValue(-1));
    }

    private ShortcutListener createAdjustmentShortcut(int keyCode, int adjustment) {
        return new ShortcutListener(null, keyCode, (int[]) null) {
            @Override
            public void handleAction(Object sender, Object target) {
                updateValue(adjustment);
            }
        };
    }

    private void updateValue(int adjustment) {
        Integer currentValue = getValue();
        setValue(currentValue != null ? currentValue + adjustment : adjustment);
    }

    @Override
    public boolean isRequired() {
        return valueField.isRequired();
    }

    @Override
    public void setRequired(boolean required) {
        valueField.setRequired(required);
        getCompositionNN().setRequiredIndicatorVisible(required);
    }

    @Override
    public String getRequiredMessage() {
        return valueField.getRequiredMessage();
    }

    @Override
    public void setRequiredMessage(String msg) {
        valueField.setRequiredMessage(msg);
    }

    @Override
    public void addValidator(Consumer<? super Integer> validator) {
        valueField.addValidator(validator);
    }

    @Override
    public void removeValidator(Consumer<Integer> validator) {
        valueField.removeValidator(validator);
    }

    @Override
    public Collection<Consumer<Integer>> getValidators() {
        return valueField.getValidators();
    }

    @Override
    public boolean isEditable() {
        return valueField.isEditable();
    }

    @Override
    public void setEditable(boolean editable) {
        valueField.setEditable(editable);
        upBtn.setEnabled(editable);
        downBtn.setEnabled(editable);
    }

    @Override
    public Integer getValue() {
        return valueField.getValue();
    }

    @Override
    public void setValue(Integer value) {
        valueField.setValue(value);
    }

    @Override
    public Subscription addValueChangeListener(Consumer<ValueChangeEvent<Integer>> listener) {
        return valueField.addValueChangeListener(listener);
    }

    @Override
    public void removeValueChangeListener(Consumer<ValueChangeEvent<Integer>> listener) {
        valueField.removeValueChangeListener(listener);
    }

    @Override
    public boolean isValid() {
        return valueField.isValid();
    }

    @Override
    public void validate() throws ValidationException {
        valueField.validate();
    }

    @Override
    public void setValueSource(ValueSource<Integer> valueSource) {
        valueField.setValueSource(valueSource);
        getCompositionNN().setRequiredIndicatorVisible(valueField.isRequired());
    }

    @Override
    public ValueSource<Integer> getValueSource() {
        return valueField.getValueSource();
    }
}

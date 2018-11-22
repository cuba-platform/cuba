/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.gui.components.MaskedField;
import com.haulmont.cuba.gui.components.data.ConversionException;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.web.gui.components.util.ShortcutListenerDelegate;
import com.haulmont.cuba.web.widgets.CubaMaskedTextField;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static com.google.common.base.Strings.emptyToNull;
import static com.google.common.base.Strings.nullToEmpty;

public class WebMaskedField<V> extends WebV8AbstractField<CubaMaskedTextField, String, V> implements MaskedField<V> {

    protected static final char PLACE_HOLDER = '_';

    protected final static List<Character> maskSymbols
            = Arrays.asList('#', 'U', 'L', '?', 'A', '*', 'H', 'h', '~');

    protected ShortcutListener enterShortcutListener;
    protected String nullRepresentation;

    protected Datatype<V> datatype;

    public WebMaskedField() {
        this.component = createTextFieldImpl();

        attachValueChangeListener(component);
    }

    @Override
    public void setMask(String mask) {
        component.setMask(mask);
        updateNullRepresentation();
    }

    protected void updateNullRepresentation() {
        StringBuilder valueBuilder = new StringBuilder();
        String mask = getMask();

        for (int i = 0; i < mask.length(); i++) {
            char c = mask.charAt(i);
            if (c == '\'') {
                valueBuilder.append(mask.charAt(++i));
            } else if (maskSymbols.contains(c)) {
                valueBuilder.append(PLACE_HOLDER);
            } else {
                valueBuilder.append(c);
            }
        }
        nullRepresentation = valueBuilder.toString();
    }

    protected String getNullRepresentation() {
        return nullRepresentation;
    }

    @Override
    public boolean isEmpty() {
        return MaskedField.super.isEmpty()
                || Objects.equals(getValue(), getNullRepresentation());
    }

    @Override
    public String getMask() {
        return component.getMask();
    }

    @Override
    public void setValueMode(ValueMode mode) {
        component.setMaskedMode(mode == ValueMode.MASKED);
    }

    @Override
    public ValueMode getValueMode() {
        return component.isMaskedMode() ? ValueMode.MASKED : ValueMode.CLEAR;
    }

    @Override
    public boolean isSendNullRepresentation() {
        return component.isSendNullRepresentation();
    }

    @Override
    public void setSendNullRepresentation(boolean sendNullRepresentation) {
        component.setSendNullRepresentation(sendNullRepresentation);
    }

    @Override
    public String getRawValue() {
        return component.getValue();
    }

    @Override
    public Datatype<V> getDatatype() {
        return datatype;
    }

    @Override
    public void setDatatype(Datatype<V> datatype) {
        this.datatype = datatype;
    }

    @Override
    protected String convertToPresentation(V modelValue) throws ConversionException {
        if (datatype != null) {
            return nullToEmpty(datatype.format(modelValue));
        }

        if (valueBinding != null
                && valueBinding.getSource() instanceof EntityValueSource) {
            EntityValueSource entityValueSource = (EntityValueSource) valueBinding.getSource();
            Range range = entityValueSource.getMetaPropertyPath().getRange();
            if (range.isDatatype()) {
                Datatype<V> propertyDataType = range.asDatatype();
                return nullToEmpty(propertyDataType.format(modelValue));
            }
        }

        return nullToEmpty(super.convertToPresentation(modelValue));
    }

    @Override
    protected V convertToModel(String componentRawValue) throws ConversionException {
        String value = emptyToNull(componentRawValue);

        if (datatype != null) {
            try {
                return datatype.parse(value);
            } catch (ParseException e) {
                // vaadin8 localized message
                throw new ConversionException("Unable to convert value", e);
            }
        }

        if (valueBinding != null
                && valueBinding.getSource() instanceof EntityValueSource) {
            EntityValueSource entityValueSource = (EntityValueSource) valueBinding.getSource();
            Datatype<V> propertyDataType = entityValueSource.getMetaPropertyPath().getRange().asDatatype();
            try {
                return propertyDataType.parse(value);
            } catch (ParseException e) {
                // vaadin8 localized message
                throw new ConversionException("Unable to convert value", e);
            }
        }

        return super.convertToModel(value);
    }

//    vaadin8
//    @Override
    protected CubaMaskedTextField createTextFieldImpl() {
        return new CubaMaskedTextField();
    }

    @Override
    public void setCursorPosition(int position) {
        component.setCursorPosition(position);
    }

    @Override
    public void selectAll() {
        component.selectAll();
    }

    @Override
    public void setSelectionRange(int pos, int length) {
        component.setSelection(pos, length);
    }

    @Override
    public Subscription addEnterPressListener(Consumer<EnterPressEvent> listener) {
        if (enterShortcutListener == null) {
            enterShortcutListener = new ShortcutListenerDelegate("enter", KeyCode.ENTER, null)
                    .withHandler((sender, target) -> {
                        EnterPressEvent event = new EnterPressEvent(WebMaskedField.this);
                        publish(EnterPressEvent.class, event);
                    });
            component.addShortcutListener(enterShortcutListener);
        }

        getEventHub().subscribe(EnterPressEvent.class, listener);

        return () -> removeEnterPressListener(listener);
    }

    @Override
    public void removeEnterPressListener(Consumer<EnterPressEvent> listener) {
        unsubscribe(EnterPressEvent.class, listener);

        if (enterShortcutListener != null
                && !hasSubscriptions(EnterPressEvent.class)) {
            component.removeShortcutListener(enterShortcutListener);
            enterShortcutListener = null;
        }
    }

    @Override
    public void focus() {
        component.focus();
    }

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }

    @Override
    public void commit() {
        super.commit();
    }

    @Override
    public void discard() {
        super.discard();
    }

    @Override
    public boolean isBuffered() {
        return super.isBuffered();
    }

    @Override
    public void setBuffered(boolean buffered) {
        super.setBuffered(buffered);
    }

    @Override
    public boolean isModified() {
        return super.isModified();
    }
}
/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.web.widgets.CubaLabel;
import com.haulmont.cuba.web.widgets.CubaTextField;
import com.haulmont.cuba.web.widgets.CurrencyLabelPosition;
import com.vaadin.server.*;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.v7.ui.CustomField;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;

public class CubaCurrencyField extends CustomField {
    protected static final String CURRENCYFIELD_STYLENAME = "c-currencyfield";
    protected static final String CURRENCYFIELD_LAYOUT_STYLENAME = "c-currencyfield-layout";
    protected static final String CURRENCY_STYLENAME = "c-currencyfield-currency";
    protected static final String CURRENCYFIELD_TEXT_STYLENAME = "c-currencyfield-text";

    protected static final String CURRENCY_VISIBLE = "currency-visible";
    protected static final String IE9_INPUT_WRAP_STYLENAME = "ie9-input-wrap";

    protected CssLayout container;
    protected CssLayout ie9InputWrapper;
    protected CubaTextField textField;
    protected CubaLabel currencyLabel;

    protected String currency;
    protected boolean showCurrencyLabel = true;
    protected CurrencyLabelPosition currencyLabelPosition = CurrencyLabelPosition.RIGHT;

    protected Datatype datatype = Datatypes.get("decimal");

    public CubaCurrencyField(CubaTextField textField) {
        this.textField = textField;

        init();

        initTextField();
        initCurrencyLabel();

        initLayout();

        updateCurrencyLabelVisibility();
    }

    protected void init() {
        setPrimaryStyleName(CURRENCYFIELD_STYLENAME);
        setSizeUndefined();
    }

    protected void initTextField() {
        textField.setWidth("100%");

        textField.addStyleName(CURRENCYFIELD_TEXT_STYLENAME);

//        vaadin8
        /*textField.setValidationVisible(false);
        textField.setShowBufferedSourceException(false);
        textField.setShowErrorForDisabledState(false);
        textField.setNullRepresentation(StringUtils.EMPTY);
        textField.setConverter(new StringToDatatypeConverter(datatype));*/

        textField.addValueChangeListener(event -> markAsDirty());
    }

    protected void initCurrencyLabel() {
        currencyLabel = new CubaLabel();
        // allows to set to the table-cell element width by content
        currencyLabel.setWidth("1px");
        currencyLabel.setHeight("100%");
        currencyLabel.addStyleName(CURRENCY_STYLENAME);
    }

    protected void initLayout() {
        container = new CssLayout();
        container.setSizeFull();
        container.setPrimaryStyleName(CURRENCYFIELD_LAYOUT_STYLENAME);

        container.addComponent(currencyLabel);

        if (useWrapper()) {
            ie9InputWrapper = new CssLayout(textField);
            ie9InputWrapper.setSizeFull();
            ie9InputWrapper.setPrimaryStyleName(IE9_INPUT_WRAP_STYLENAME);

            container.addComponent(ie9InputWrapper);
        } else {
            container.addComponent(textField);
        }

        setFocusDelegate(textField);
    }

    protected boolean useWrapper() {
        Page current = Page.getCurrent();
        if (current != null) {
            WebBrowser browser = current.getWebBrowser();
            return browser != null &&
                    (browser.isIE() && browser.getBrowserMajorVersion() <= 10 || browser.isSafari());
        } else {
            return false;
        }
    }

    public boolean getShowCurrencyLabel() {
        return showCurrencyLabel;
    }

    public void setShowCurrencyLabel(boolean showCurrency) {
        this.showCurrencyLabel = showCurrency;

        updateCurrencyLabelVisibility();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
        currencyLabel.setValue(currency);

        updateCurrencyLabelVisibility();
    }

    protected void updateCurrencyLabelVisibility() {
        boolean currencyVisible = StringUtils.isNotEmpty(currency) && showCurrencyLabel;

        currencyLabel.setVisible(currencyVisible);

        if (currencyVisible) {
            container.addStyleName(CURRENCY_VISIBLE);
        } else {
            container.removeStyleName(CURRENCY_VISIBLE);
        }
    }

    public CurrencyLabelPosition getCurrencyLabelPosition() {
        return currencyLabelPosition;
    }

    public void setCurrencyLabelPosition(CurrencyLabelPosition currencyLabelPosition) {
        container.removeStyleName(this.currencyLabelPosition.name().toLowerCase());
        this.currencyLabelPosition = currencyLabelPosition;
        container.addStyleName(currencyLabelPosition.name().toLowerCase());

        container.removeComponent(currencyLabel);
        if (CurrencyLabelPosition.LEFT == currencyLabelPosition) {
            container.addComponent(currencyLabel, 0);
        } else {
            container.addComponent(currencyLabel, 1);
        }
    }

    @Override
    protected Component initContent() {
        return container;
    }

    @Override
    public Class getType() {
        return Object.class;
    }

    @Override
    public void focus() {
        textField.focus();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        textField.setTabIndex(tabIndex);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);

        textField.setReadOnly(readOnly);
    }

    @Override
    public void setRequired(boolean required) {
        textField.setRequiredIndicatorVisible(required);

        markAsDirty();
    }

    @Override
    public boolean isRequired() {
        return textField.isRequiredIndicatorVisible();
    }

    @Override
    public void setRequiredError(String requiredMessage) {
//        vaadin8
//        textField.setRequiredError(requiredMessage);

        markAsDirty();
    }

    @Override
    public String getRequiredError() {
        //        vaadin8
//        return textField.getRequiredError();

        return null;
    }

    @Override
    public ErrorMessage getErrorMessage() {
        ErrorMessage superError = super.getErrorMessage();
        if (!textField.isReadOnly() && textField.isRequiredIndicatorVisible() && textField.isEmpty()) {
            ErrorMessage error = AbstractErrorMessage.getErrorMessageForException(
                    new com.vaadin.v7.data.Validator.EmptyValueException(getRequiredError()));
            if (error != null) {
                return new CompositeErrorMessage(superError, error);
            }
        }
        return superError;
    }

    public void setTextField(CubaTextField textField) {
        this.textField = textField;
    }

    public void setDatatype(Datatype datatype) {
        this.datatype = datatype;

        try {
            Object parsedValue = datatype.parse(textField.getValue(), getLocale());

//            vaadin8
//            textField.setConverter(new StringToDatatypeConverter(datatype));
//            textField.setConvertedValue(parsedValue);
        } catch (ParseException e) {
            String message = String.format("Value %s cannot be parsed as %s datatype",
                    textField.getValue(), datatype);
            throw new RuntimeException(message, e);
        }
    }

    public Datatype getDatatype() {
        return datatype;
    }
}
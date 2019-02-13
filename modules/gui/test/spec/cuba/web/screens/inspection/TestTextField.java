/*
 * Copyright (c) 2008-2018 Haulmont.
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

package spec.cuba.web.screens.inspection;

import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.SizeUnit;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.icons.Icons;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;

public class TestTextField implements TextField<String> {
    private Consumer<TextChangeEvent> listener;

    @Override
    public String getRawValue() {
        return null;
    }

    @Override
    public void commit() {

    }

    @Override
    public void discard() {

    }

    @Override
    public boolean isBuffered() {
        return false;
    }

    @Override
    public void setBuffered(boolean buffered) {

    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public Component getParent() {
        return null;
    }

    @Override
    public void setParent(Component parent) {

    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {

    }

    @Override
    public boolean isResponsive() {
        return false;
    }

    @Override
    public void setResponsive(boolean responsive) {

    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void setVisible(boolean visible) {

    }

    @Override
    public boolean isVisibleRecursive() {
        return false;
    }

    @Override
    public boolean isEnabledRecursive() {
        return false;
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public SizeUnit getHeightSizeUnit() {
        return null;
    }

    @Override
    public void setHeight(String height) {

    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public SizeUnit getWidthSizeUnit() {
        return null;
    }

    @Override
    public void setWidth(String width) {

    }

    @Override
    public Alignment getAlignment() {
        return null;
    }

    @Override
    public void setAlignment(Alignment alignment) {

    }

    @Override
    public String getStyleName() {
        return null;
    }

    @Override
    public void setStyleName(String styleName) {

    }

    @Override
    public void addStyleName(String styleName) {

    }

    @Override
    public void removeStyleName(String styleName) {

    }

    @Override
    public <X> X unwrap(Class<X> internalComponentClass) {
        return null;
    }

    @Override
    public <X> X unwrapComposition(Class<X> internalCompositionClass) {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public boolean isDescriptionAsHtml() {
        return false;
    }

    @Override
    public void setDescriptionAsHtml(boolean descriptionAsHtml) {

    }

    @Override
    public String getCaption() {
        return null;
    }

    @Override
    public void setCaption(String caption) {

    }

    @Override
    public boolean isCaptionAsHtml() {
        return false;
    }

    @Override
    public void setCaptionAsHtml(boolean captionAsHtml) {

    }

    @Override
    public void focus() {

    }

    @Override
    public int getTabIndex() {
        return 0;
    }

    @Override
    public void setTabIndex(int tabIndex) {

    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public void setRequired(boolean required) {

    }

    @Override
    public String getRequiredMessage() {
        return null;
    }

    @Override
    public void setRequiredMessage(String msg) {

    }

    @Override
    public void addValidator(Consumer validator) {

    }

    @Override
    public void removeValidator(Consumer validator) {

    }

    @Override
    public Collection getValidators() {
        return Collections.emptyList();
    }

    @Override
    public Frame getFrame() {
        return null;
    }

    @Override
    public void setFrame(Frame frame) {

    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public void setEditable(boolean editable) {

    }

    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public void setIcon(String icon) {

    }

    @Override
    public void setIconFromSet(Icons.Icon icon) {

    }

    @Override
    public String getContextHelpText() {
        return null;
    }

    @Override
    public void setContextHelpText(String contextHelpText) {

    }

    @Override
    public boolean isContextHelpTextHtmlEnabled() {
        return false;
    }

    @Override
    public void setContextHelpTextHtmlEnabled(boolean enabled) {

    }

    @Override
    public Consumer<ContextHelpIconClickEvent> getContextHelpIconClickHandler() {
        return null;
    }

    @Override
    public void setContextHelpIconClickHandler(Consumer<ContextHelpIconClickEvent> handler) {

    }

    @Override
    public void setDatatype(Datatype<String> datatype) {

    }

    @Override
    public Datatype<String> getDatatype() {
        return null;
    }

    @Override
    public Function<String, String> getFormatter() {
        return null;
    }

    @Override
    public void setFormatter(Function<? super String, String> formatter) {

    }

    @Override
    public String getInputPrompt() {
        return null;
    }

    @Override
    public void setInputPrompt(String inputPrompt) {

    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public void setValue(String value) {

    }

    @Override
    public Subscription addValueChangeListener(Consumer<ValueChangeEvent<String>> listener) {
        return null;
    }

    @Override
    public void removeValueChangeListener(Consumer<ValueChangeEvent<String>> listener) {

    }

    @Override
    public boolean isTrimming() {
        return false;
    }

    @Override
    public void setTrimming(boolean trimming) {

    }

    @Override
    public int getMaxLength() {
        return 0;
    }

    @Override
    public void setMaxLength(int maxLength) {

    }

    @Override
    public void setCursorPosition(int position) {

    }

    @Override
    public CaseConversion getCaseConversion() {
        return null;
    }

    @Override
    public void setCaseConversion(CaseConversion caseConversion) {

    }

    @Override
    public void selectAll() {

    }

    @Override
    public void setSelectionRange(int pos, int length) {

    }

    @Override
    public Subscription addTextChangeListener(Consumer<TextChangeEvent> listener) {
        this.listener = listener;
        return () -> {};
    }

    @Override
    public void removeTextChangeListener(Consumer<TextChangeEvent> listener) {

    }

    @Override
    public int getTextChangeTimeout() {
        return 0;
    }

    @Override
    public void setTextChangeTimeout(int timeout) {

    }

    @Override
    public TextChangeEventMode getTextChangeEventMode() {
        return null;
    }

    @Override
    public void setTextChangeEventMode(TextChangeEventMode mode) {

    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void validate() {

    }

    @Override
    public void setValueSource(ValueSource<String> valueSource) {

    }

    @Override
    public ValueSource<String> getValueSource() {
        return null;
    }

    public Consumer<TextChangeEvent> getListener() {
        return listener;
    }

    @Override
    public Subscription addEnterPressListener(Consumer<EnterPressEvent> listener) {
        return null;
    }

    @Override
    public void removeEnterPressListener(Consumer<EnterPressEvent> listener) {

    }

    @Override
    public void setHtmlName(String htmlName) {
    }

    @Override
    public String getHtmlName() {
        return null;
    }

    @Override
    public void setConversionErrorMessage(String conversionErrorMessage) {
    }

    @Override
    public String getConversionErrorMessage() {
        return null;
    }
}
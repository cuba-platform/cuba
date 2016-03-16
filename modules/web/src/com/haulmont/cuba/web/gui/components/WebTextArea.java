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

import com.haulmont.cuba.web.toolkit.ui.CubaTextArea;
import com.vaadin.ui.TextArea;

/**
 */
public class WebTextArea
        extends
            WebAbstractTextArea<TextArea>
        implements
            com.haulmont.cuba.gui.components.TextArea {

    @Override
    protected TextArea createTextFieldImpl() {
        return new CubaTextArea();
    }

    @Override
    public void setCursorPosition(int position) {
        component.setCursorPosition(position);
    }

    @Override
    public String getInputPrompt() {
        return component.getInputPrompt();
    }

    @Override
    public void setInputPrompt(String inputPrompt) {
        component.setInputPrompt(inputPrompt);
    }

    @Override
    public boolean isWordwrap() {
        return component.isWordwrap();
    }

    @Override
    public void setWordwrap(boolean wordwrap) {
        component.setWordwrap(wordwrap);
    }
}
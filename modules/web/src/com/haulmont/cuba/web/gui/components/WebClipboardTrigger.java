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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.ClipboardTrigger;
import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.web.gui.WebAbstractFacet;
import com.haulmont.cuba.web.widgets.CubaCopyButtonExtension;

import java.util.function.Consumer;

import static com.haulmont.cuba.web.widgets.CubaCopyButtonExtension.browserSupportCopy;
import static com.haulmont.cuba.web.widgets.CubaCopyButtonExtension.copyWith;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public class WebClipboardTrigger extends WebAbstractFacet implements ClipboardTrigger {

    protected TextInputField<?> input;
    protected Button button;

    @Override
    public void setInput(TextInputField<?> input) {
        this.input = input;

        checkInitialized();
    }

    @Override
    public TextInputField<?> getInput() {
        return input;
    }

    @Override
    public void setButton(Button button) {
        if (this.button != null) {
            disableExtension(this.button);
        }

        this.button = button;

        checkInitialized();
    }

    @Override
    public Button getButton() {
        return button;
    }

    @Override
    public boolean isSupportedByWebBrowser() {
        return browserSupportCopy();
    }

    @Override
    public Subscription addCopyListener(Consumer<CopyEvent> listener) {
        return getEventHub().subscribe(CopyEvent.class, listener);
    }

    protected void disableExtension(Button button) {
        com.vaadin.ui.Button vButton = button.unwrap(com.vaadin.ui.Button.class);

        vButton.getExtensions().stream()
                .filter(e -> e instanceof CubaCopyButtonExtension)
                .findFirst()
                .ifPresent(vButton::removeExtension);
    }

    protected void checkInitialized() {
        if (this.button != null &&
            this.input != null) {
            // setup field CSS class for selector
            String generatedClassName = "copy-text-" + randomAlphanumeric(6);

            this.input.addStyleName(generatedClassName);

            com.vaadin.ui.Button vButton = button.unwrap(com.vaadin.ui.Button.class);

            disableExtension(this.button);

            CubaCopyButtonExtension extension = copyWith(vButton, "." + generatedClassName);
            extension.addCopyListener(event ->
                    publish(CopyEvent.class, new CopyEvent(this, event.isSuccess()))
            );
        }
    }
}
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

package com.haulmont.cuba.gui.app.security.constraintloc.edit;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.security.entity.LocalizedConstraintMessage;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Locale;
import java.util.Map;

public class ConstraintLocalizationEdit extends AbstractEditor<LocalizedConstraintMessage> {
    @Named("fieldGroup.operationType")
    protected LookupField operationTypeField;

    @Inject
    protected LookupField<Locale> localesSelect;

    @Inject
    protected TextField entityName;

    @Inject
    protected TextField caption;

    @Inject
    protected ResizableTextArea message;

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected Metadata metadata;

    protected LocalizationValueChangeListener captionValueChangeListener;
    protected LocalizationValueChangeListener messageValueChangeListener;

    @Override
    protected void postInit() {
        operationTypeField.setTextInputAllowed(false);

        initEntityNameField();
        initCaptionField();
        initMessageField();
        initLocalesField();
    }

    protected void initEntityNameField() {
        String entityName = getItem().getEntityName();
        MetaClass metaClass = metadata.getClass(entityName);

        if (metaClass != null) {
            MessageTools messageTools = messages.getTools();
            this.entityName.setValue(messageTools.getEntityCaption(metaClass) + " (" + metaClass.getName() + ")");
        } else {
            this.entityName.setValue(entityName);
        }
    }

    protected void initLocalesField() {
        Map<String, Locale> locales = globalConfig.getAvailableLocales();
        localesSelect.setOptionsMap(locales);

        localesSelect.addValueChangeListener(createLocaleSelectValueChangeListener());

        localesSelect.setValue(userSessionSource.getLocale());
    }

    protected HasValue.ValueChangeListener createLocaleSelectValueChangeListener() {
        return e -> {
            captionValueChangeListener.suspend();
            messageValueChangeListener.suspend();

            Locale selectedLocale = (Locale) e.getValue();
            String localeCode = messages.getTools().localeToString(selectedLocale);
            caption.setValue(getItem().getLocalizedCaption(localeCode));
            message.setValue(getItem().getLocalizedMessage(localeCode));

            captionValueChangeListener.resume();
            messageValueChangeListener.resume();
        };
    }

    protected void initCaptionField() {
        captionValueChangeListener = createCaptionValueChangeListener();
        caption.addValueChangeListener(captionValueChangeListener);
    }

    protected LocalizationValueChangeListener createCaptionValueChangeListener() {
        return new LocalizationValueChangeListener() {
            @Override
            protected void updateValues(LocalizedConstraintMessage item, String localeCode, String value) {
                item.putLocalizedCaption(localeCode, value);
            }
        };
    }

    protected void initMessageField() {
        messageValueChangeListener = createMessageValueChangeListener();
        message.addValueChangeListener(messageValueChangeListener);
    }

    protected LocalizationValueChangeListener createMessageValueChangeListener() {
        return new LocalizationValueChangeListener() {
            @Override
            protected void updateValues(LocalizedConstraintMessage item, String localeCode, String value) {
                item.putLocalizedMessage(localeCode, value);
            }
        };
    }

    public void showHelp() {
        showMessageDialog(getMessage("helpDialogCaption"), getMessage("helpDialogMessage"),
                MessageType.CONFIRMATION_HTML
                        .modal(false)
                        .width(480));
    }

    protected abstract class LocalizationValueChangeListener implements HasValue.ValueChangeListener {
        protected boolean active = true;

        @Override
        public void valueChanged(HasValue.ValueChangeEvent e) {
            if (active) {
                Locale selectedLocale = localesSelect.getValue();
                String localeCode = messages.getTools().localeToString(selectedLocale);
                updateValues(getItem(), localeCode, (String) e.getValue());
            }
        }

        public void suspend() {
            active = false;
        }

        public void resume() {
            active = true;
        }

        protected abstract void updateValues(LocalizedConstraintMessage item, String localeCode, String value);
    }
}
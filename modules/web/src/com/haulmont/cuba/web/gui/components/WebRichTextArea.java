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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.RichTextArea;
import com.haulmont.cuba.web.widgets.CubaRichTextArea;
import com.haulmont.cuba.web.widgets.client.richtextarea.CubaRichTextAreaState;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WebRichTextArea extends WebAbstractField<CubaRichTextArea> implements RichTextArea {

    public WebRichTextArea() {
        component = new CubaRichTextArea();
        attachListener(component);

        component.setNullRepresentation("");
        component.setInvalidAllowed(false);
        component.setInvalidCommitted(true);
        component.setLocaleMap(loadLabels());
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getValue() {
        return super.getValue();
    }

    protected Map<String, String> loadLabels() {
        Map<String, String> labels = new HashMap<>();

        Collection<String> locales = Arrays.asList(
                CubaRichTextAreaState.RICH_TEXT_AREA_FOREGROUND_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_BACKGROUND_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_BLACK_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_WHITE_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_RED_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_GREEN_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_YELLOW_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_BLUE_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_FONT_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_NORMAL_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_SIZE_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_BOLD_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_ITALIC_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_UNDERLINE_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_SUBSCRIPT_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_SUPERSCRIPT_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_JUSTIFYCENTER_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_JUSTIFYRIGHT_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_JUSTIFYLEFT_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_STRIKETHROUGH_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_INDENT_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_OUTDENT_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_HR_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_OL_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_UL_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_INSERTIMAGE_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_CREATELINK_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_REMOVELINK_LABEL,
                CubaRichTextAreaState.RICH_TEXT_AREA_REMOVEFORMAT_LABEL
        );

        Messages messages = AppBeans.get(Messages.NAME);
        for (String locale : locales) {
            labels.put(locale, messages.getMainMessage(locale));
        }

        return labels;
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
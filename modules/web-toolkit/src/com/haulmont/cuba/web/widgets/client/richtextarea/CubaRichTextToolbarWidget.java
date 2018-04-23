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
 */

package com.haulmont.cuba.web.widgets.client.richtextarea;

import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.UIObject;
import com.vaadin.client.ui.richtextarea.VRichTextToolbar;

import java.util.HashMap;
import java.util.Map;

public class CubaRichTextToolbarWidget extends VRichTextToolbar{
    public CubaRichTextToolbarWidget(RichTextArea richText) {
        super(richText);
    }

    public void setLocaleMap(Map<String,String> localeMap) {
        Map<String, UIObject> locales = new HashMap<>();
        locales.put(CubaRichTextAreaState.RICH_TEXT_AREA_BOLD_LABEL, bold);
        locales.put(CubaRichTextAreaState.RICH_TEXT_AREA_ITALIC_LABEL, italic);
        locales.put(CubaRichTextAreaState.RICH_TEXT_AREA_UNDERLINE_LABEL, underline);
        locales.put(CubaRichTextAreaState.RICH_TEXT_AREA_SUBSCRIPT_LABEL, subscript);
        locales.put(CubaRichTextAreaState.RICH_TEXT_AREA_SUPERSCRIPT_LABEL, superscript);
        locales.put(CubaRichTextAreaState.RICH_TEXT_AREA_JUSTIFYCENTER_LABEL, justifyCenter);
        locales.put(CubaRichTextAreaState.RICH_TEXT_AREA_JUSTIFYRIGHT_LABEL, justifyRight);
        locales.put(CubaRichTextAreaState.RICH_TEXT_AREA_JUSTIFYLEFT_LABEL, justifyLeft);
        locales.put(CubaRichTextAreaState.RICH_TEXT_AREA_STRIKETHROUGH_LABEL, strikethrough);
        locales.put(CubaRichTextAreaState.RICH_TEXT_AREA_INDENT_LABEL, indent);
        locales.put(CubaRichTextAreaState.RICH_TEXT_AREA_OUTDENT_LABEL, outdent);
        locales.put(CubaRichTextAreaState.RICH_TEXT_AREA_HR_LABEL, hr);
        locales.put(CubaRichTextAreaState.RICH_TEXT_AREA_OL_LABEL, ol);
        locales.put(CubaRichTextAreaState.RICH_TEXT_AREA_UL_LABEL, ul);
        locales.put(CubaRichTextAreaState.RICH_TEXT_AREA_INSERTIMAGE_LABEL, insertImage);
        locales.put(CubaRichTextAreaState.RICH_TEXT_AREA_CREATELINK_LABEL, createLink);
        locales.put(CubaRichTextAreaState.RICH_TEXT_AREA_REMOVELINK_LABEL, removeLink);
        locales.put(CubaRichTextAreaState.RICH_TEXT_AREA_REMOVEFORMAT_LABEL, removeFormat);

        for (Map.Entry<String, UIObject> entry : locales.entrySet()) {
            entry.getValue().setTitle(localeMap.get(entry.getKey()));
            entry.getValue().getElement().setAttribute("icon-id", entry.getKey());
        }

        setBackgroundColorLocaleMap(localeMap);
        setForegroundColorLocaleMap(localeMap);
        setSizeLocaleMap(localeMap);
        setFontLocaleMap(localeMap);
    }

    public void setSizeLocaleMap(Map<String, String> localeMap) {
        fontSizes.setItemText(0, localeMap.get(CubaRichTextAreaState.RICH_TEXT_AREA_SIZE_LABEL));
    }

    public void setFontLocaleMap(Map<String, String> localeMap) {
        fonts.setItemText(0, localeMap.get(CubaRichTextAreaState.RICH_TEXT_AREA_FONT_LABEL));
        fonts.setItemText(1, localeMap.get(CubaRichTextAreaState.RICH_TEXT_AREA_NORMAL_LABEL));
    }

    public void setBackgroundColorLocaleMap(Map<String, String> localeMap) {
        backColors.clear();

        backColors.addItem(localeMap.get(CubaRichTextAreaState.RICH_TEXT_AREA_BACKGROUND_LABEL));
        backColors.addItem(localeMap.get(CubaRichTextAreaState.RICH_TEXT_AREA_WHITE_LABEL), "white");
        backColors.addItem(localeMap.get(CubaRichTextAreaState.RICH_TEXT_AREA_BLACK_LABEL), "black");
        backColors.addItem(localeMap.get(CubaRichTextAreaState.RICH_TEXT_AREA_RED_LABEL), "red");
        backColors.addItem(localeMap.get(CubaRichTextAreaState.RICH_TEXT_AREA_GREEN_LABEL), "green");
        backColors.addItem(localeMap.get(CubaRichTextAreaState.RICH_TEXT_AREA_YELLOW_LABEL), "yellow");
        backColors.addItem(localeMap.get(CubaRichTextAreaState.RICH_TEXT_AREA_BLUE_LABEL), "blue");

        backColors.setTabIndex(-1);
    }

    public void setForegroundColorLocaleMap(Map<String, String> localeMap) {
        foreColors.clear();

        foreColors.addItem(localeMap.get(CubaRichTextAreaState.RICH_TEXT_AREA_FOREGROUND_LABEL));
        foreColors.addItem(localeMap.get(CubaRichTextAreaState.RICH_TEXT_AREA_WHITE_LABEL), "white");
        foreColors.addItem(localeMap.get(CubaRichTextAreaState.RICH_TEXT_AREA_BLACK_LABEL), "black");
        foreColors.addItem(localeMap.get(CubaRichTextAreaState.RICH_TEXT_AREA_RED_LABEL), "red");
        foreColors.addItem(localeMap.get(CubaRichTextAreaState.RICH_TEXT_AREA_GREEN_LABEL), "green");
        foreColors.addItem(localeMap.get(CubaRichTextAreaState.RICH_TEXT_AREA_YELLOW_LABEL), "yellow");
        foreColors.addItem(localeMap.get(CubaRichTextAreaState.RICH_TEXT_AREA_BLUE_LABEL), "blue");

        foreColors.setTabIndex(-1);
    }
}

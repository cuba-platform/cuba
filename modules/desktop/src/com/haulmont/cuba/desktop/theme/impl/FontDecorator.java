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

package com.haulmont.cuba.desktop.theme.impl;

import java.awt.*;

public class FontDecorator extends PropertyPathDecorator {

    private String family;
    private Integer style;
    private Integer size;

    public FontDecorator(String propertyPath, String family, Integer style, Integer size) {
        super(propertyPath);
        this.family = family;
        this.style = style;
        this.size = size;
    }

    @Override
    protected void apply(Object object, String property) {
        java.awt.Component component = (java.awt.Component) object;
        Font oldFont = component.getFont();

        String newFamily = oldFont.getFamily();
        int newStyle = oldFont.getStyle();
        int newSize = oldFont.getSize();
        if (family != null) {
            newFamily = family;
        }

        if (style != null) {
            newStyle = style;
        }

        if (size != null) {
            newSize = size;
        }

        Font newFont = new Font(newFamily, newStyle, newSize);
        setProperty(object, property, newFont);
    }
}
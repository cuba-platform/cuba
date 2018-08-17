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

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.QueryUtils;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.SuggestionField;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import groovy.text.GStringTemplateEngine;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.io.IOException;
import java.io.StringWriter;

public abstract class SuggestionFieldQueryLoader<T extends Field> extends AbstractFieldLoader<T> {

    protected void loadQuery(SuggestionField suggestionField, Element element) {
        Element queryElement = element.element("query");
        if (queryElement != null) {
            final boolean escapeValue;

            String stringQuery = queryElement.getStringValue();

            String searchFormat = queryElement.attributeValue("searchStringFormat");

            String view = queryElement.attributeValue("view");

            String escapeValueForLike = queryElement.attributeValue("escapeValueForLike");
            if (StringUtils.isNotEmpty(escapeValueForLike)) {
                escapeValue = Boolean.valueOf(escapeValueForLike);
            } else {
                escapeValue = false;
            }

            String entityClassName = queryElement.attributeValue("entityClass");
            if (StringUtils.isNotEmpty(entityClassName)) {
                suggestionField.setSearchExecutor((searchString, searchParams) -> {
                    LegacyFrame frame = (LegacyFrame) suggestionField.getFrame().getFrameOwner();
                    DataSupplier supplier = frame.getDsContext().getDataSupplier();
                    Class<Entity> entityClass = ReflectionHelper.getClass(entityClassName);
                    if (escapeValue) {
                        searchString = QueryUtils.escapeForLike(searchString);
                    }
                    searchString = applySearchFormat(searchString, searchFormat);

                    LoadContext loadContext = LoadContext.create(entityClass);
                    if (StringUtils.isNotEmpty(view)) {
                        loadContext.setView(view);
                    }
                    loadContext.setQuery(LoadContext.createQuery(stringQuery).setParameter("searchString", searchString));

                    //noinspection unchecked
                    return supplier.loadList(loadContext);
                });
            } else {
                throw new GuiDevelopmentException(String.format("Field 'entityClass' is empty in component %s.",
                        suggestionField.getId()), getContext().getFullFrameId());
            }
        }
    }

    protected String applySearchFormat(String searchString, String format) {
        if (StringUtils.isNotEmpty(format)) {
            GStringTemplateEngine engine = new GStringTemplateEngine();
            StringWriter writer = new StringWriter();
            try {
                engine.createTemplate(format).make(ParamsMap.of("searchString", searchString)).writeTo(writer);
                return writer.toString();
            } catch (ClassNotFoundException | IOException e) {
                throw new IllegalStateException(e);
            }
        }
        return searchString;
    }
}

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

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.export.ExportDataProvider;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * A component for embedding external objects, such as images or PDF documents.
 * <p>
 * Web implementation may require a browser plugin. Only images support is mandatory for all implementations.
 *
 */
public interface Embedded extends Component, Component.BelongToFrame {

    String NAME = "embedded";

    void setMIMEType(String mt);

    void setSource(@Nullable URL src);
    void setSource(@Nullable String src);
    void setSource(String fileName, @Nullable InputStream src);
    void setSource(String fileName, @Nullable ExportDataProvider dataProvider);

    void resetSource();

    void addParameter(String name, String value);
    void removeParameter(String name);
    Map<String, String> getParameters();

    void setType(Type t);
    Type getType();

    enum Type {
        OBJECT,
        IMAGE,
        BROWSER
    }
}
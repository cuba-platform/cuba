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

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.cuba.gui.components.data.*;
import com.haulmont.cuba.gui.components.data.options.CollectionDatasourceOptions;
import com.haulmont.cuba.gui.components.data.options.EnumOptions;
import com.haulmont.cuba.gui.components.data.options.ListOptions;
import com.haulmont.cuba.gui.components.data.options.MapOptions;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface OptionsField<V> extends Field<V> {
    default void setOptions(Stream<V> options) {
        setOptions(options.collect(Collectors.toList()));
    }
    default void setOptions(Collection<V> options) {
        setOptionsSource(new ListOptions<>(options));
    }

    void setOptionsSource(OptionsSource<V> optionsSource);
    OptionsSource<V> getOptionsSource();

    void setOptionCaptionProvider(Function<? super V, String> captionProvider);
    Function<? super V, String> getOptionCaptionProvider();

    /*
     * Deprecated API
     */

    @Deprecated
    CaptionMode getCaptionMode();
    @Deprecated
    void setCaptionMode(CaptionMode captionMode);

    @Deprecated
    String getCaptionProperty();
    @Deprecated
    void setCaptionProperty(String captionProperty);

    @Deprecated
    default CollectionDatasource getOptionsDatasource() {
        OptionsSource<V> optionsSource = getOptionsSource();
        if (optionsSource instanceof CollectionDatasourceOptions) {
            return ((CollectionDatasourceOptions) optionsSource).getDatasource();
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    @Deprecated
    default void setOptionsDatasource(CollectionDatasource datasource) {
        if (datasource == null) {
            setOptionsSource(null);
        } else {
            setOptionsSource(new CollectionDatasourceOptions<>(datasource));
        }
    }

    default List getOptionsList() {
        OptionsSource optionsSource = getOptionsSource();
        if (optionsSource instanceof ListOptions) {
            return (List) ((ListOptions) optionsSource).getItemsCollection();
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    default void setOptionsList(List optionsList) {
        setOptionsSource(new ListOptions<>(optionsList));
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    default Map<String, ?> getOptionsMap() {
        OptionsSource optionsSource = getOptionsSource();
        if (optionsSource instanceof MapOptions) {
            return ((MapOptions) optionsSource).getItemsCollection();
        }
        return null;
    }
    @Deprecated
    default void setOptionsMap(Map<String, V> map) {
        BiMap<String, V> biMap = ImmutableBiMap.copyOf(map);

        setOptionsSource(new MapOptions<>(map));
        setOptionCaptionProvider(v -> biMap.inverse().get(v));
    }

    @Deprecated
    default Class<? extends EnumClass> getOptionsEnum() {
        OptionsSource optionsSource = getOptionsSource();
        if (optionsSource instanceof EnumOptions) {
            return ((EnumOptions) optionsSource).getEnumClass();
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    @Deprecated
    default void setOptionsEnum(Class<V> optionsEnum) {
        setOptionsSource(new EnumOptions(optionsEnum));
    }
}
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
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.cuba.gui.components.data.Options;
import com.haulmont.cuba.gui.components.data.options.DatasourceOptions;
import com.haulmont.cuba.gui.components.data.options.EnumOptions;
import com.haulmont.cuba.gui.components.data.options.ListOptions;
import com.haulmont.cuba.gui.components.data.options.MapOptions;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * todo JavaDoc
 *
 * @param <V>
 * @param <I>
 */
public interface OptionsField<V, I> extends Field<V> {
    void setOptions(Options<I> options);
    Options<I> getOptions();

    void setOptionCaptionProvider(Function<? super I, String> captionProvider);
    Function<? super I, String> getOptionCaptionProvider();

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
        Options<I> options = getOptions();
        if (options instanceof DatasourceOptions) {
            return ((DatasourceOptions) options).getDatasource();
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    @Deprecated
    default void setOptionsDatasource(CollectionDatasource datasource) {
        if (datasource == null) {
            setOptions(null);
        } else {
            setOptions(new DatasourceOptions<>(datasource));
        }
    }

    default List getOptionsList() {
        Options options = getOptions();
        if (options instanceof ListOptions) {
            return (List) ((ListOptions) options).getItemsCollection();
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    default void setOptionsList(List optionsList) {
        setOptions(new ListOptions<>(optionsList));
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    default Map<String, ?> getOptionsMap() {
        Options options = getOptions();
        if (options instanceof MapOptions) {
            return ((MapOptions) options).getItemsCollection();
        }
        return null;
    }

    /**
     * JavaDoc
     */
    @Deprecated
    default void setOptionsMap(Map<String, I> map) {
        Preconditions.checkNotNullArgument(map);

        BiMap<String, I> biMap = ImmutableBiMap.copyOf(map);

        setOptions(new MapOptions<>(map));
        setOptionCaptionProvider(v -> biMap.inverse().get(v));
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    default Class<? extends EnumClass> getOptionsEnum() {
        Options options = getOptions();
        if (options instanceof EnumOptions) {
            return ((EnumOptions) options).getEnumClass();
        }
        return null;
    }

    /**
     * JavaDoc
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    default void setOptionsEnum(Class<V> optionsEnum) {
        Preconditions.checkNotNullArgument(optionsEnum);

        setOptions(new EnumOptions(optionsEnum));
    }
}
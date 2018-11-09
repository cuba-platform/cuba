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
import com.haulmont.cuba.gui.components.data.Options;
import com.haulmont.cuba.gui.components.data.options.DatasourceOptions;
import com.haulmont.cuba.gui.components.data.options.EnumOptions;
import com.haulmont.cuba.gui.components.data.options.ListOptions;
import com.haulmont.cuba.gui.components.data.options.MapOptions;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * UI component having options.
 *
 * @param <V> type of value
 * @param <I> type of option items
 */
public interface OptionsField<V, I> extends Field<V>, HasCaptionMode {

    /**
     * Sets options for UI component.
     *
     * @param options options
     * @see ListOptions
     */
    void setOptions(Options<I> options);
    /**
     * @return options object
     */
    Options<I> getOptions();

    /**
     * Sets function that provides caption for option items.
     *
     * @param optionCaptionProvider caption provider for options
     */
    void setOptionCaptionProvider(Function<? super I, String> optionCaptionProvider);
    /**
     * @return caption provider for options
     */
    Function<? super I, String> getOptionCaptionProvider();

    /**
     * Sets options from the passed list.
     *
     * @param optionsList options
     * @see ListOptions#of(Object, Object[])
     */
    default void setOptionsList(List<I> optionsList) {
        setOptions(new ListOptions<>(optionsList));
    }

    /**
     * Sets options from the passed map and automatically applies option caption provider based on map keys.
     *
     * @param map options
     * @see ListOptions#of(Object, Object[])
     */
    default void setOptionsMap(Map<String, I> map) {
        checkNotNullArgument(map);

        BiMap<String, I> biMap = ImmutableBiMap.copyOf(map);

        setOptions(new MapOptions<>(map));
        setOptionCaptionProvider(v -> biMap.inverse().get(v));
    }

    /**
     * Sets options from the passed enum class. Enum class must be Java enumeration and implement {@link EnumClass}.
     *
     * @param optionsEnum enum class
     */
    @SuppressWarnings("unchecked")
    default void setOptionsEnum(Class<I> optionsEnum) {
        checkNotNullArgument(optionsEnum);

        if (!optionsEnum.isEnum()
                || !EnumClass.class.isAssignableFrom(optionsEnum)) {
            throw new IllegalArgumentException("Options class must be enumeration and implement EnumClass " + optionsEnum);
        }

        setOptions(new EnumOptions(optionsEnum));
    }

    /*
     * Deprecated API
     */

    /**
     * @return options datasource
     * @deprecated Use {@link #getOptions()} instead.
     */
    @Deprecated
    default CollectionDatasource getOptionsDatasource() {
        Options<I> options = getOptions();
        if (options instanceof DatasourceOptions) {
            return ((DatasourceOptions) options).getDatasource();
        }
        return null;
    }

    /**
     * @param datasource datasource
     * @deprecated set options using {@link #setOptions(Options)} with {@link DatasourceOptions}.
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    default void setOptionsDatasource(CollectionDatasource datasource) {
        if (datasource == null) {
            setOptions(null);
        } else {
            setOptions(new DatasourceOptions<>(datasource));
        }
    }

    /**
     * @return options list
     * @deprecated Use {@link #getOptions()} instead.
     */
    @Deprecated
    default List getOptionsList() {
        Options options = getOptions();
        if (options instanceof ListOptions) {
            return (List) ((ListOptions) options).getItemsCollection();
        }
        return null;
    }

    /**
     * @return options map
     * @deprecated Use {@link #getOptions()} instead.
     */
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
     * @return enumclass
     * @deprecated Use {@link #getOptions()} instead.
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    default Class<? extends EnumClass> getOptionsEnum() {
        Options options = getOptions();
        if (options instanceof EnumOptions) {
            return ((EnumOptions) options).getEnumClass();
        }
        return null;
    }
}
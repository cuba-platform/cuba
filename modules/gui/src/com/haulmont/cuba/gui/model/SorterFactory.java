/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.gui.model;

import com.haulmont.cuba.gui.model.impl.CollectionContainerSorter;
import com.haulmont.cuba.gui.model.impl.CollectionPropertyContainerSorter;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

/**
 * Factory bean for sorters.
 */
@Component("cuba_SorterFactory")
public class SorterFactory {

    public static final String NAME = "cuba_SorterFactory";

    /**
     * Creates {@code Sorter}.
     */
    public Sorter createCollectionContainerSorter(CollectionContainer container, @Nullable BaseCollectionLoader loader) {
        return new CollectionContainerSorter(container, loader);
    }

    /**
     * Creates {@code Sorter}.
     */
    public Sorter createCollectionPropertyContainerSorter(CollectionPropertyContainer container) {
        return new CollectionPropertyContainerSorter(container);
    }
}

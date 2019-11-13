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

package com.haulmont.cuba.gui.components.data.options;

import com.haulmont.bali.events.Subscription;
import com.haulmont.bali.events.sys.VoidSubscription;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.data.Options;
import com.haulmont.cuba.gui.components.data.meta.EntityOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Options based on a map that contains entities.
 *
 * @param <E> entity type
 */
public class MapEntityOptions<E extends Entity> extends MapOptions<E> implements Options<E>, EntityOptions<E> {

    private static final Logger log = LoggerFactory.getLogger(MapEntityOptions.class);

    protected E selectedItem = null;

    public MapEntityOptions(Map<String, E> options) {
        super(options);
    }

    @Override
    public void setSelectedItem(E item) {
        this.selectedItem = item;
    }

    public E getSelectedItem() {
        return selectedItem;
    }

    @Override
    public boolean containsItem(E item) {
        return getItemsCollection().containsValue(item);
    }

    @Override
    public void updateItem(E item) {
        // do nothing
        log.debug("The 'updateItem' method is ignored, because underlying collection may be unmodifiable");
    }

    @Override
    public void refresh() {
        // do nothing
        log.debug("The 'refresh' method is ignored because the underlying collection contains static data");
    }

    @Override
    public Subscription addValueChangeListener(Consumer<ValueChangeEvent<E>> listener) {
        return VoidSubscription.INSTANCE;
    }

    @Override
    public MetaClass getEntityMetaClass() {
        MetaClass metaClass = null;
        if (selectedItem != null) {
            metaClass = selectedItem.getMetaClass();
        } else {
            List<E> itemsCollection = new ArrayList<>(getItemsCollection().values());
            if (!itemsCollection.isEmpty()) {
                metaClass = itemsCollection.get(0).getMetaClass();
            }
        }
        return metaClass;
    }
}

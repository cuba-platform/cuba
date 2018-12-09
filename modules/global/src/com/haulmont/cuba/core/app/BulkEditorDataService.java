/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.core.app;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface BulkEditorDataService {

    String NAME = "cuba_BulkEditorDataService";

    /**
     * Reloads selected items with the passed view.
     *
     * @param loadDescriptor load descriptor
     * @return reloaded instances
     */
    List<Entity> reload(LoadDescriptor loadDescriptor);

    class LoadDescriptor implements Serializable {
        private final Collection<? extends Entity> selectedItems;
        private final MetaClass metaClass;
        private final View view;
        private final boolean loadDynamicAttributes;

        public LoadDescriptor(Collection<? extends Entity> selectedItems, MetaClass metaClass,
                              View view, boolean loadDynamicAttributes) {
            this.selectedItems = selectedItems;
            this.metaClass = metaClass;
            this.view = view;
            this.loadDynamicAttributes = loadDynamicAttributes;
        }

        public Collection<? extends Entity> getSelectedItems() {
            return selectedItems;
        }

        public MetaClass getMetaClass() {
            return metaClass;
        }

        public View getView() {
            return view;
        }

        public boolean isLoadDynamicAttributes() {
            return loadDynamicAttributes;
        }
    }
}
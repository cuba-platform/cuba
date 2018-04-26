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

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.components.data.value.DatasourceValueSource;
import com.haulmont.cuba.gui.components.data.HasValueBinding;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.data.Datasource;

/**
 * A component that represents data from one property of a datasource.
 * vaadin8
 */
@Deprecated
public interface DatasourceComponent<V> extends Component, HasValue<V>, HasValueBinding<V> {

    /**
     * vaadin8
     *
     * @return datasource instance
     */
    @Deprecated
    default Datasource getDatasource() {
        ValueSource<V> valueSource = getValueSource();
        return valueSource == null ? null : ((DatasourceValueSource) valueSource).getDatasource();
    }

    /**
     * vaadin8
     *
     * @return datasource property
     * @deprecated Use {@link #getMetaPropertyPath()}
     */
    @Deprecated
    default MetaProperty getMetaProperty() {
        ValueSource<V> valueSource = getValueSource();
        return valueSource == null ? null : ((DatasourceValueSource) valueSource).getMetaPropertyPath().getMetaProperty();
    }

    /**
     * vaadin8
     *
     * @return datasource property path
     */
    @Deprecated
    default MetaPropertyPath getMetaPropertyPath() {
        ValueSource<V> valueSource = getValueSource();
        return valueSource == null ? null : ((DatasourceValueSource) valueSource).getMetaPropertyPath();
    }

    /**
     * Set datasource and its property.
     * <p>
     * vaadin8
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    default void setDatasource(Datasource datasource, String property) {
        if (datasource != null) {
            this.setValueSource(new DatasourceValueSource(datasource, property));
        } else {
            this.setValueSource(null);
        }
    }
}
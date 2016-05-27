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
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.DatasourceComponent;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public abstract class AbstractDatasourceComponentLoader<T extends DatasourceComponent> extends AbstractComponentLoader<T> {

    protected void loadDatasource(DatasourceComponent component, Element element) {
        final String datasource = element.attributeValue("datasource");
        if (!StringUtils.isEmpty(datasource)) {
            Datasource ds = context.getDsContext().get(datasource);
            if (ds == null) {
                throw new GuiDevelopmentException(String.format("Datasource '%s' is not defined", datasource),
                        getContext().getFullFrameId(), "Component ID", component.getId());
            }
            String property = element.attributeValue("property");
            if (StringUtils.isEmpty(property)) {
                throw new GuiDevelopmentException(
                        String.format("Can't set datasource '%s' for component '%s' because 'property' " +
                                "attribute is not defined", datasource, component.getId()),
                        context.getFullFrameId());
            }

            component.setDatasource(ds, property);
        }
    }
}
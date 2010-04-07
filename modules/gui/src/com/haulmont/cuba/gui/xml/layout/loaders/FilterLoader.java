/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 14.10.2009 14:22:13
 *
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.xml.layout.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.apache.commons.lang.BooleanUtils;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

public class FilterLoader extends ComponentLoader {

    public FilterLoader(Context context) {
        super(context);
    }

    public Component loadComponent(ComponentsFactory factory, Element element, Component parent)
            throws InstantiationException, IllegalAccessException
    {
        final Filter filter = factory.createComponent("filter");

        assignXmlDescriptor(filter, element);
        loadId(filter, element);
        loadVisible(filter, element);
        loadStyleName(filter, element);

        String datasource = element.attributeValue("datasource");
        if (!StringUtils.isBlank(datasource)) {
            CollectionDatasource ds = context.getDsContext().get(datasource);
            if (ds == null)
                throw new IllegalStateException("Cannot find data source by name: " + datasource);

            filter.setDatasource(ds);
        }

        assignFrame(filter);

        context.addLazyTask(
                new LazyTask() {
                    public void execute(Context context, IFrame frame) {
                        filter.loadFiltersAndApplyDefault();
                    }
                }
        );

        return filter;
    }
}

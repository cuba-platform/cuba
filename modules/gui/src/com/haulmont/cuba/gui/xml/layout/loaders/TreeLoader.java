/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author krivopustov
 * @version $Id$
 */
public class TreeLoader extends ActionsHolderLoader<Tree> {
    @Override
    public void createComponent() {
        resultComponent = (Tree) factory.createComponent(Tree.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadVisible(resultComponent, element);

        loadEnable(resultComponent, element);
        loadEditable(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);

        loadStyleName(resultComponent, element);

        loadActions(resultComponent, element);

        String multiselect = element.attributeValue("multiselect");
        if (StringUtils.isNotEmpty(multiselect)) {
            resultComponent.setMultiSelect(Boolean.parseBoolean(multiselect));
        }

        Element itemsElem = element.element("treechildren");
        String datasource = itemsElem.attributeValue("datasource");
        if (!StringUtils.isBlank(datasource)) {
            HierarchicalDatasource ds = (HierarchicalDatasource) context.getDsContext().get(datasource);
            resultComponent.setDatasource(ds);

            String captionProperty = itemsElem.attributeValue("captionProperty");
            if (!StringUtils.isEmpty(captionProperty)) {
                resultComponent.setCaptionProperty(captionProperty);
                resultComponent.setCaptionMode(CaptionMode.PROPERTY);
            }
        }
    }
}
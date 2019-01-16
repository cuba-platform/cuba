/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.test.ds.api.consistency;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ds.api.consistency.SearchFieldDsTest;
import com.haulmont.cuba.web.gui.components.WebSearchField;
import com.haulmont.cuba.web.test.stubs.TestUiComponents;
import mockit.Expectations;

public class WebSearchFieldDsDsTest extends SearchFieldDsTest {

    @Override
    protected void initExpectations() {
        super.initExpectations();

        new Expectations() {
            {
                AppContext.getProperty("cuba.mainMessagePack"); result = "com.haulmont.cuba.web"; minTimes = 0;
            }
        };

        this.uiComponents = new TestUiComponents(applicationContext);
    }

    @Override
    protected void autowireUiComponent(Component component) {
        super.autowireUiComponent(component);

        WebSearchField searchField = (WebSearchField) component;
        searchField.setBeanLocator(beanLocator);
        searchField.setMetadataTools(metadata.getTools());
    }
}
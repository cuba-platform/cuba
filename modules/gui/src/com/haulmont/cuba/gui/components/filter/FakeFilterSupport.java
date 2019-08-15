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

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.sys.xmlparsing.Dom4jTools;
import com.haulmont.cuba.gui.components.Component.HasXmlDescriptor;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.gui.data.impl.DsContextImpl;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.gui.sys.FrameContextImpl;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.FilterEntity;

public class FakeFilterSupport {
    protected Filter filter;
    protected FilterEntity filterEntity;
    protected MetaClass metaClass;
    protected ConditionsTree conditionsTree;
    protected FrameOwner frameOwner;
    protected FilterParser filterParser;

    public FakeFilterSupport(FrameOwner frameOwner, MetaClass metaClass) {
        this.metaClass = metaClass;
        this.frameOwner = frameOwner;
        filterParser = AppBeans.get(FilterParser.class);
    }

    public Filter createFakeFilter() {
        if (filter != null) {
            return filter;
        }

        Filter fakeFilter = AppBeans.get(ComponentsFactory.NAME, ComponentsFactory.class).createComponent(Filter.class);
        Dom4jTools dom4JTools = AppBeans.get(Dom4jTools.NAME);
        ((HasXmlDescriptor) fakeFilter).setXmlDescriptor(dom4JTools.readDocument("<filter/>").getRootElement());
        CollectionDatasourceImpl fakeDatasource = new CollectionDatasourceImpl();

        LegacyFrame legacyFrame = (LegacyFrame) this.frameOwner;

        DsContextImpl fakeDsContext = new DsContextImpl(legacyFrame.getDsContext().getDataSupplier());
        FrameContextImpl fakeFrameContext = new FrameContextImpl((Frame) legacyFrame);
        fakeDsContext.setFrameContext(fakeFrameContext);
        fakeDatasource.setDsContext(fakeDsContext);
        //Attention: this query should match the logic in com.haulmont.reports.wizard.ReportingWizardBean.createJpqlDataSet()
        fakeDatasource.setQuery("select queryEntity from " + metaClass.getName() + " queryEntity");
        fakeDatasource.setMetaClass(metaClass);
        fakeFilter.setDatasource(fakeDatasource);
        fakeFilter.setFrame(UiControllerUtils.getFrame(frameOwner));
        return fakeFilter;
    }

    public ConditionsTree createFakeConditionsTree(Filter filter, FilterEntity filterEntity) {
        boolean emptyFilter = filterEntity.getXml() == null || filterEntity.getXml().equals("<filter/>");
        return conditionsTree != null
                ? conditionsTree
                : emptyFilter ? new ConditionsTree() : filterParser.getConditions(filter, filterEntity.getXml());
    }

    public FilterEntity createFakeFilterEntity(String xml) {
        if (filterEntity != null) {
            return filterEntity;
        }

        Metadata metadata = AppBeans.get(Metadata.NAME);
        FilterEntity fakeFilterEntity = metadata.create(FilterEntity.class);
        fakeFilterEntity.setXml(xml);
        return fakeFilterEntity;
    }
}
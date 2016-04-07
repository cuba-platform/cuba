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
 */
package com.haulmont.cuba.gui.commonlookup;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.inject.Inject;
import java.util.Map;

public class CommonLookupController extends AbstractLookup {

    @Inject
    protected MessageTools messageTools;
    @Inject
    protected ComponentsFactory componentsFactory;
    @Inject
    protected Metadata metadata;
    @Inject
    protected MetadataTools metadataTools;
    @Inject
    protected ViewRepository viewRepository;
    @Inject
    protected ThemeConstantsManager themeConstantsManager;

    @Inject
    protected BoxLayout mainPane;

    @WindowParam(name = CLASS_PARAMETER)
    protected MetaClass metaClass;

    protected Filter filter;
    protected Table entitiesTable;
    protected CollectionDatasource entitiesDs;
    protected View view;

    public static final String CLASS_PARAMETER = "class";
    public static final String SCREEN_ID = "commonLookup";

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        getDialogOptions().setWidth(themeConstantsManager.getThemeValueInt("cuba.gui.commonLookup.width"))
                .setHeight(themeConstantsManager.getThemeValueInt("cuba.gui.commonLookup.height"))
                .setResizable(true);
        setCaption(messageTools.getEntityCaption(metaClass));
        initView();
        initDatasource();
        initTable();
        initFilter();

        mainPane.add(filter);
        mainPane.add(entitiesTable);
        mainPane.expand(entitiesTable);

        setLookupComponent(entitiesTable);
    }

    protected void initDatasource() {
        entitiesDs = new DsBuilder(getDsContext())
                .setId("entitiesDs")
                .setMetaClass(metaClass)
                .setView(view)
                .buildCollectionDatasource();
        entitiesDs.setQuery(String.format("select e from %s e", metaClass.getName()));
    }

    protected void initFilter() {
        filter = componentsFactory.createComponent(Filter.class);
        filter.setFrame(this);
        filter.setId("filter");
        filter.setApplyTo(entitiesTable);
        filter.setDatasource(entitiesDs);
        filter.setMaxResults(100);
        filter.setUseMaxResults(true);
        filter.setManualApplyRequired(true);
        filter.setEditable(true);
        ((FilterImplementation) filter).loadFiltersAndApplyDefault();

        filter.apply(true);
    }

    protected void initTable() {
        entitiesTable = componentsFactory.createComponent(Table.class);
        entitiesTable.setId("table");
        entitiesTable.setDatasource(entitiesDs);
        entitiesTable.setMultiSelect(true);
        entitiesTable.setWidth("100%");
        entitiesTable.setHeight("100%");

        RowsCount rowsCount = componentsFactory.createComponent(RowsCount.class);
        rowsCount.setDatasource(entitiesDs);
        rowsCount.setOwner(entitiesTable);
        entitiesTable.setRowsCount(rowsCount);

        entitiesTable.requestFocus();
    }

    @SuppressWarnings("unchecked")
    protected void initView() {
        View localView = viewRepository.getView(metaClass, View.LOCAL);
        View minimalView = viewRepository.getView(metaClass, View.MINIMAL);
        view = new View(localView, "entitiesView", false);
        minimalView.getProperties().stream().filter(property -> !view.containsProperty(property.getName())).forEach(property -> {
            view.addProperty(property.getName(), property.getView(), property.getFetchMode());
        });
    }
}
/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.app.core.lookup;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public class SimpleLookup extends AbstractLookup {

    public enum ComponentType {
        TABLE,
        LOOKUP_FIELD
    }

    private LookupConfig lookupConfig;

    private static Log log = LogFactory.getLog(SimpleLookup.class);

    @Inject
    protected Metadata metadata;
    @Inject
    protected MessageTools messageTools;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        lookupConfig = (LookupConfig)params.get("lookupConfig");

        checkValid();

        String entityName = lookupConfig.getEntity();
        String view = lookupConfig.getView();
        String query = lookupConfig.getQuery();
        ComponentType componentType = lookupConfig.getComponentType();
        List<String> columns = lookupConfig.getColumns();
        String lookupTitle = lookupConfig.getLookupTitle();
        Map<String, Object> queryParams = lookupConfig.getQueryParams();

        MetaClass metaClass = metadata.getSession().getClassNN(entityName);

        CollectionDatasource datasource = new DsBuilder(getDsContext())
                .setId(metaClass.getName())
                .setMetaClass(metaClass)
                .setViewName(view)
                .buildCollectionDatasource();

        if (query != null)
            datasource.setQuery(query);
        if(queryParams !=null)
            datasource.refresh(queryParams);
        else
            datasource.refresh();

        Container lookupPanel = getComponent("lookupPanel");

        switch (componentType) {
            case TABLE:
                if (columns == null)
                    throw new RuntimeException("'Columns' property isn't set in SimpleLookup.LookupConfig");
                Table table = createTable(columns, metaClass);
                table.setDatasource(datasource);

                lookupPanel.add(table);
                table.setFrame(((BelongToFrame)lookupPanel).getFrame());
                ((ExpandingLayout) lookupPanel).expand(table);
                setLookupComponent(table);
                break;
            //LOOKUP_FIELD is used by default
            default: 
                LookupField lookupField = AppConfig.getFactory().createComponent(LookupField.NAME);
                lookupField.setOptionsDatasource(datasource);

                lookupPanel.add(lookupField);
                setLookupComponent(lookupField);
        }

        String entityNameLoc = MessageProvider.getMessage(metaClass.getJavaClass(), metaClass.getJavaClass().getSimpleName());
        if (lookupTitle == null)
            setCaption(entityNameLoc);
        else
            setCaption(lookupTitle);

//        MessageProvider.formatMessage(getClass(), "lookup.select", entityNameLoc)
    }

    private Table createTable(List<String> columnNames, MetaClass metaClass) {
        Table table = AppConfig.getFactory().createComponent(Table.NAME);
        table.setId("lookupComponent");
        table.setHeight("100%");
        table.setWidth("100%");
        if (BooleanUtils.isTrue(lookupConfig.isMultiSelect()))
            table.setMultiSelect(true);

        for (String columnName : columnNames) {
            MetaProperty metaProperty = metaClass.getProperty(columnName);
            if (metaProperty == null) {
                log.error("MetaProperty with name " + columnName + " wasn't found");
                continue;
            }
            MetaPropertyPath metaPropertyPath = new MetaPropertyPath(metaClass, metaProperty);
            Table.Column column = new Table.Column(metaPropertyPath);
            column.setType(metaProperty.getJavaType());
            String caption = messageTools.getPropertyCaption(metaClass, metaProperty.getName());
            column.setCaption(caption);
            table.addColumn(column);
        }
        return table;
    }

    private void checkValid() {
        if (lookupConfig == null)
            throw new RuntimeException("SimpleLookup.LookupConfig param is null");
        if (lookupConfig.getEntity() == null)
            throw new RuntimeException("'entity' field isn't set in SimpleLookup.LokupConfig");
        if ((lookupConfig.getComponentType() == ComponentType.TABLE) && (lookupConfig.getColumns() == null))
            throw new RuntimeException("'columns' field isn't set in SimpleLookup.LookupConfig");
    }

    public static class LookupConfig {
        private String entity;
        private String query;
        private ComponentType componentType;
        private String view;
        private List<String> columns;
        private String lookupTitle;
        private Boolean multiSelect;
        private Map<String, Object> queryParams;

        public String getEntity() {
            return entity;
        }

        public void setEntity(String entity) {
            this.entity = entity;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public ComponentType getComponentType() {
            return componentType;
        }

        public void setComponentType(ComponentType componentType) {
            this.componentType = componentType;
        }

        public String getView() {
            return view;
        }

        public void setView(String view) {
            this.view = view;
        }

        public List<String> getColumns() {
            return columns;
        }

        public void setColumns(List<String> columns) {
            this.columns = columns;
        }

        public String getLookupTitle() {
            return lookupTitle;
        }

        public void setLookupTitle(String lookupTitle) {
            this.lookupTitle = lookupTitle;
        }

        public Boolean isMultiSelect() {
            return multiSelect;
        }

        public void setMultiSelect(Boolean multiSelect) {
            this.multiSelect = multiSelect;
        }

        public Map<String, Object> getQueryParams() {
            return queryParams;
        }

        public void setQueryParams(Map<String, Object> queryParams) {
            this.queryParams = queryParams;
        }
    }

}

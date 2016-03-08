/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.appproperties;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.app.importexport.EntityImportExportService;
import com.haulmont.cuba.core.config.AppPropertyEntity;
import com.haulmont.cuba.core.entity.Config;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller of the {@code appproperties-browse.xml} screen
 */
public class AppPropertiesBrowse extends AbstractWindow {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private AppPropertiesDatasource paramsDs;

    @Named("paramsTable.editValue")
    private Action editValueAction;

    @Inject
    private TreeTable<AppPropertyEntity> paramsTable;

    @Inject
    private TextField searchField;

    @Inject
    private Button exportBtn;

    @Override
    public void init(Map<String, Object> params) {
        paramsDs.addItemChangeListener(e -> {
            boolean enabled = e.getItem() != null && !e.getItem().getCategory();
            editValueAction.setEnabled(enabled);
            exportBtn.setEnabled(enabled);
        });
        paramsTable.setItemClickAction(editValueAction);

        paramsTable.sortBy(paramsDs.getMetaClass().getPropertyPath("name"), true);

        searchField.addValueChangeListener(e -> {
            paramsDs.refresh(ParamsMap.of("name", e.getValue()));
            if (StringUtils.isNotEmpty((String) e.getValue())) {
                paramsTable.expandAll();
            }
        });
    }

    public void editValue() {
        openWindow("appPropertyEditor", WindowManager.OpenType.DIALOG, ParamsMap.of("item", paramsDs.getItem()));
    }

    public void exportAsSql() {
        List<AppPropertyEntity> exported = paramsTable.getSelected().stream()
                .filter(appPropertyEntity -> !appPropertyEntity.getCategory())
                .collect(Collectors.toList());
        if (!exported.isEmpty()) {
            openWindow("appPropertiesExport", WindowManager.OpenType.DIALOG, ParamsMap.of("exported", exported));
        }
    }
}

/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.appproperties;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.config.AppPropertyEntity;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.RefreshAction;
import com.haulmont.cuba.gui.settings.Settings;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller of the {@code appproperties-browse.xml} screen
 */
public class AppPropertiesBrowse extends AbstractWindow {

    @Inject
    private AppPropertiesDatasource paramsDs;

    @Named("paramsTable.editValue")
    private Action editValueAction;

    @Named("paramsTable.refresh")
    private RefreshAction refreshAction;

    @Inject
    private TreeTable<AppPropertyEntity> paramsTable;

    @Inject
    private TextField searchField;

    @Inject
    private Button exportBtn;

    @Inject
    private HBoxLayout hintBox;

    private AppPropertyEntity lastSelected;

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

        refreshAction.setBeforeActionPerformedHandler(() -> {
            lastSelected = paramsTable.getSingleSelected();
        });
        refreshAction.setAfterActionPerformedHandler(() -> {
            if (lastSelected != null) {
                for (AppPropertyEntity entity : paramsDs.getItems()) {
                    if (entity.getName().equals(lastSelected.getName())) {
                        paramsTable.expand(entity.getId());
                        paramsTable.setSelected(entity);
                    }
                }
            }
        });
    }

    public void editValue() {
        AppPropertiesEdit editor = (AppPropertiesEdit) openWindow(
                "appPropertyEditor", WindowManager.OpenType.DIALOG, ParamsMap.of("item", paramsDs.getItem()));
        editor.addCloseWithCommitListener(() -> {
            refreshAction.actionPerform(null);
        });
    }

    public void exportAsSql() {
        List<AppPropertyEntity> exported = paramsTable.getSelected().stream()
                .filter(appPropertyEntity -> !appPropertyEntity.getCategory())
                .collect(Collectors.toList());
        if (!exported.isEmpty()) {
            openWindow("appPropertiesExport", WindowManager.OpenType.DIALOG, ParamsMap.of("exported", exported));
        }
    }

    public void closeHint() {
        hintBox.setVisible(false);
        getSettings().get(hintBox.getId()).addAttribute("visible", "false");
    }

    @Override
    public void applySettings(Settings settings) {
        super.applySettings(settings);
        String visible = settings.get(hintBox.getId()).attributeValue("visible");
        if (visible != null)
            hintBox.setVisible(Boolean.parseBoolean(visible));
    }
}

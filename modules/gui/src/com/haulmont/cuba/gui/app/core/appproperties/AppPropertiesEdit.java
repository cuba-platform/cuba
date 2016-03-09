/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.appproperties;

import com.haulmont.cuba.core.app.ConfigStorageService;
import com.haulmont.cuba.core.config.AppPropertyEntity;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.Datasource;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

/**
 * Controller of the {@code appproperties-edit.xml} screen
 */
public class AppPropertiesEdit extends AbstractWindow {

    @WindowParam
    private AppPropertyEntity item;

    @Inject
    private Datasource<AppPropertyEntity> appPropertyDs;

    @Inject
    private ConfigStorageService configStorageService;

    @Named("fieldGroup.currentValue")
    private TextField currentValueField;

    @Inject
    private Label cannotEditValueLabel;

    @Override
    public void init(Map<String, Object> params) {
        appPropertyDs.setItem(item);
        currentValueField.setEditable(!item.getOverridden());
        cannotEditValueLabel.setVisible(item.getOverridden());
    }

    public void ok() {
        AppPropertyEntity appPropertyEntity = appPropertyDs.getItem();
        configStorageService.setDbProperty(appPropertyEntity.getName(), appPropertyEntity.getCurrentValue());
        close(COMMIT_ACTION_ID);
    }

    public void cancel() {
        close(CLOSE_ACTION_ID);
    }
}

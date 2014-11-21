/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.showinfo;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.EntitySqlGenerationService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.theme.ThemeConstants;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class SystemInfoWindow extends AbstractWindow {

    public interface Companion {
        void initInfoTable(Table infoTable);
    }

    @Inject
    protected EntityParamsDatasource paramsDs;

    @Inject
    protected Table infoTable;

    @Inject
    protected ThemeConstants themeConstants;

    @Inject
    protected TextArea scriptArea;

    @Inject
    protected EntitySqlGenerationService sqlGenerationService;

    @Inject
    protected ClientConfig clientConfig;

    @WindowParam(name = "item")
    protected Entity item;

    @Inject
    protected Button insert;

    @Inject
    protected Button select;

    @Inject
    protected Button update;

    @Inject
    protected BoxLayout buttonsHbox;

    @Inject
    protected Metadata metadata;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        paramsDs.setInstance(item);
        paramsDs.setInstanceMetaClass((MetaClass) params.get("metaClass"));

        paramsDs.refresh();

        Companion companion = getCompanion();
        if (companion != null) {
            companion.initInfoTable(infoTable);
        }

        // remove all actions
        List<Action> tableActions = new ArrayList<>(infoTable.getActions());
        for (Action action : tableActions)
            infoTable.removeAction(action);

        if (!clientConfig.getSystemInfoScriptsEnabled()
                || item == null
                || !metadata.getTools().isPersistent(item.getMetaClass())) {
            buttonsHbox.setVisible(false);
        }
    }

    public void generateInsert() {
        scriptArea.setEditable(true);
        scriptArea.setValue(sqlGenerationService.generateInsertScript(item));
        scriptArea.setVisible(true);
        scriptArea.setEditable(false);
    }

    public void generateUpdate() {
        scriptArea.setEditable(true);
        scriptArea.setValue(sqlGenerationService.generateUpdateScript(item));
        scriptArea.setVisible(true);
        scriptArea.setEditable(false);
    }

    public void generateSelect() {
        scriptArea.setEditable(true);
        scriptArea.setValue(sqlGenerationService.generateSelectScript(item));
        scriptArea.setVisible(true);
        scriptArea.setEditable(false);
    }
}
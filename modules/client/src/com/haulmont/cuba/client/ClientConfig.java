/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.client;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultInt;
import com.haulmont.cuba.core.config.defaults.DefaultInteger;
import com.haulmont.cuba.core.config.defaults.DefaultString;
import com.haulmont.cuba.core.config.type.CommaSeparatedStringListTypeFactory;
import com.haulmont.cuba.core.config.type.Factory;

import java.util.List;

/**
 * Configuration parameters interface used by the WEB and DESKTOP layers.
 *
 * @author krivopustov
 * @version $Id$
 */
@Source(type = SourceType.APP)
public interface ClientConfig extends Config {

    /**
     * @return middleware connection URL list
     */
    @Property("cuba.connectionUrlList")
    @Factory(factory = CommaSeparatedStringListTypeFactory.class)
    List<String> getConnectionUrlList();

    /**
     * DEPRECATED! Use {@link #getConnectionUrlList()} to avoid mistakes when connecting to the cluster.
     * @return middleware connection URL as it is defined in app.properties, i.e. in case of several URLs it will
     * be a comma-separated list.
     */
    @Deprecated
    @Property("cuba.connectionUrl")
    String getConnectionUrl();

    /**
     * @return Context of the middleware file download controller.
     */
    @Property("cuba.fileDownloadContext")
    @DefaultString("/download")
    String getFileDownloadContext();

    /**
     * @return Maximum size of uploaded file in megabytes.
     */
    @Property("cuba.client.maxUploadSizeMb")
    @DefaultInt(20)
    int getMaxUploadSizeMb();
    void setMaxUploadSizeMb(int value);

    /**
     * @return Whether to enable sorting of datasource data on DB (using separate SELECT with ORDER BY clause).
     */
    @Property("cuba.collectionDatasourceDbSortEnabled")
    @DefaultBoolean(true)
    boolean getCollectionDatasourceDbSortEnabled();

    /**
     * List of screen aliases for which saving screen history is enabled.
     * <p>Obsolete. Recommended way to specify this information is entity annotations
     * in <code>*-metadata.xml</code></p>
     * @return  comma-separated list of screen aliases
     */
    @Property("cuba.screenIdsToSaveHistory")
    String getScreenIdsToSaveHistory();

    /**
     * @return Whether to enable password policy. If true, all new passwords will be checked for the compliance with
     * <code>cuba.passwordPolicyRegExp</code>
     */
    @Property("cuba.passwordPolicyEnabled")
    @DefaultBoolean(false)
    public boolean getPasswordPolicyEnabled();

    /**
     * @return The regular expression which is used by password policy (see also <code>cuba.passwordPolicyEnabled<code/>).
     */
    @Property("cuba.passwordPolicyRegExp")
    @DefaultString("((?=.*\\d)(?=.*\\p{javaLowerCase})(?=.*\\p{javaUpperCase}).{6,20})")
    public String getPasswordPolicyRegExp();

    /**
     * @return If true, all generic filters will require explicit Apply after screen opening. Empty connected table
     * is shown.<br/>
     * If false, the filter will be applied automatically, refreshing the table immediately after the screen opening.
     */
    @Property("cuba.gui.genericFilterManualApplyRequired")
    @DefaultBoolean(false)
    boolean getGenericFilterManualApplyRequired();

    /**
     * @return If true, then check filter conditions (empty or not) before applying filter. If all conditions are empty
     * (no parameters entered), the filter doesn't apply and special message to user is shown.<br/>
     * If false, no checks are performed and the filter applyes, refreshing connected table.
     */
    @Property("cuba.gui.genericFilterChecking")
    @DefaultBoolean(true)
    boolean getGenericFilterChecking();

    /**
     * @return  If true, enable adding conditions through tree-like structure of properties, groups, etc.<br/>
     * If false, switch to adding conditions through plain dropdown list (old style).
     */
    @Property("cuba.gui.genericFilterTreeConditionSelect")
    @DefaultBoolean(true)
    boolean getGenericFilterTreeConditionSelect();

    /**
     * @return If true, all generic text filters will trim value.<br/>
     * If false, the text filter will not be trim value.
     */
    @Property("cuba.gui.genericFilterTrimParamValues")
    @DefaultBoolean(true)
    boolean getGenericFilterTrimParamValues();

    @DefaultString("CTRL-INSERT")
    @Property("cuba.gui.tableInsertShortcut")
    String getTableInsertShortcut();

    @DefaultString("CTRL-ALT-INSERT")
    @Property("cuba.gui.tableAddShortcut")
    String getTableAddShortcut();

    @DefaultString("CTRL-DELETE")
    @Property("cuba.gui.tableRemoveShortcut")
    String getTableRemoveShortcut();

    @DefaultString("ENTER")
    @Property("cuba.gui.tableEditShortcut")
    String getTableEditShortcut();

    @DefaultString("CTRL-ENTER")
    @Property("cuba.gui.commitShortcut")
    String getCommitShortcut();

    @DefaultString("ESCAPE")
    @Property("cuba.gui.closeShortcut")
    String getCloseShortcut();

    @DefaultString("SHIFT-ENTER")
    @Property("cuba.gui.filterApplyShortcut")
    String getFilterApplyShortcut();

    @DefaultString("CTRL-SHIFT-PAGE_DOWN")
    @Property("cuba.gui.nextTabShortcut")
    String getNextTabShortcut();

    @DefaultString("CTRL-SHIFT-PAGE_UP")
    @Property("cuba.gui.previousTabShortcut")
    String getPreviousTabShortcut();

    /**
     * @return If true, client will try to find missing localized messages on the server.
     */
    @Property("cuba.remoteMessagesSearchEnabled")
    @DefaultBoolean(false)
    boolean getRemoteMessagesSearchEnabled();

    @Property("cuba.gui.pickerShortcut.modifiers")
    @DefaultString("CTRL-ALT")
    String getPickerShortcutModifiers();

    @Property("cuba.gui.pickerShortcut.lookup")
    @DefaultString("CTRL-ALT-L")
    String getPickerLookupShortcut();

    @Property("cuba.gui.pickerShortcut.open")
    @DefaultString("CTRL-ALT-O")
    String getPickerOpenShortcut();

    @Property("cuba.gui.pickerShortcut.clear")
    @DefaultString("CTRL-ALT-C")
    String getPickerClearShortcut();

    @Property("cuba.gui.useSaveConfirmation")
    @DefaultBoolean(true)
    boolean getUseSaveConfirmation();

    @Property("cuba.gui.loadObsoleteSettingsForTable")
    @DefaultBoolean(false)
    boolean getLoadObsoleteSettingsForTable();
}
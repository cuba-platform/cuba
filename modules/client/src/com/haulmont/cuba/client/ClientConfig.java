/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.client;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.Default;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultInt;
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
     * @return Context of the middleware file download controller.
     */
    @Property("cuba.fileDownloadContext")
    @DefaultString("/download")
    String getFileDownloadContext();

    /**
     * @return Maximum size of uploaded file in megabytes.
     */
    @Property("cuba.maxUploadSizeMb")
    @Source(type = SourceType.DATABASE)
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
     * @return If true, client will try to find missing localized messages on the server.
     */
    @Property("cuba.remoteMessagesSearchEnabled")
    @DefaultBoolean(false)
    boolean getRemoteMessagesSearchEnabled();

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
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(false)
    boolean getPasswordPolicyEnabled();

    /**
     * @return The regular expression which is used by password policy (see also <code>cuba.passwordPolicyEnabled<code/>).
     */
    @Property("cuba.passwordPolicyRegExp")
    @Source(type = SourceType.DATABASE)
    @DefaultString("((?=.*\\d)(?=.*\\p{javaLowerCase})(?=.*\\p{javaUpperCase}).{6,20})")
    String getPasswordPolicyRegExp();

    /**
     * @return If true, all generic filters will require explicit Apply after screen opening. Empty connected table
     * is shown.<br/>
     * If false, the filter will be applied automatically, refreshing the table immediately after the screen opening.
     */
    @Property("cuba.gui.genericFilterManualApplyRequired")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(false)
    boolean getGenericFilterManualApplyRequired();

    /**
     * @return If true, then check filter conditions (empty or not) before applying filter. If all conditions are empty
     * (no parameters entered), the filter doesn't apply and special message to user is shown.<br/>
     * If false, no checks are performed and the filter applyes, refreshing connected table.
     */
    @Property("cuba.gui.genericFilterChecking")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(false)
    boolean getGenericFilterChecking();

    /**
     * @return If true, all generic text filters will trim value.<br/>
     * If false, the text filter will not be trim value.
     */
    @Property("cuba.gui.genericFilterTrimParamValues")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(true)
    boolean getGenericFilterTrimParamValues();

    /**
     * @return Number of columns with conditions in generic filter UI component.
     */
    @Property("cuba.gui.genericFilterColumnsCount")
    @Source(type = SourceType.DATABASE)
    @DefaultInt(3)
    int getGenericFilterColumnsCount();

    /**
     * @return location of panel with conditions in generic filter component.
     * If {@code top} then conditions will be placed above filter control elements
     * or below them if {@code bottom}.
     */
    @Property("cuba.gui.genericFilterConditionsLocation")
    @Source(type = SourceType.DATABASE)
    @Default("top")
    String getGenericFilterConditionsLocation();

    /**
     * Returns a number of items to be displayed in popup list near the 'Search' button. If number of filter
     * entities exceeds this value then 'Show more..' action is added to the popup list. The action
     * will show new dialog window with all possible filter entities for selecting a desired one.
     */
    @Property("cuba.gui.genericFilterPopupListSize")
    @Source(type = SourceType.DATABASE)
    @DefaultInt(10)
    int getGenericFilterPopupListSize();

    /**
     * Returns a template for filter controls layout. Each component has the following format:
     * [<i>component_name</i> | <i>options-comma-separated</i>], e.g. [pin | no-caption, no-icon].
     * <p>Available component names:</p>
     * <ul>
     *     <li>{@code filters_popup} - popup button for selecting a filter entity combined with Search button.
     *     When using this component there is no need to add a separate Search button</li>
     *     <li>{@code filters_lookup} - lookup field for selecting a filter entity. Search button should be added as
     *     a separate component</li>
     *     <li>{@code search} - search button. Do not add if use {@code filters_popup}</li>
     *     <li>{@code add_condition} - button for adding a new condition</li>
     *     <li>{@code spacer} - space between component groups </li>
     *     <li>{@code settings} - settings button. Specify actions names that should be displayed in Settings popup
     *     as component options </li>
     *     <li>{@code max_results} - group of components for setting max number of records to be displayed</li>
     *     <li>{@code fts_switch} - checkbox for switching to FTS mode</li>
     * </ul>
     * The following components can be used as options for {@code settings} component. They also can be used as
     * independent components if for example you want to display a Pin button:
     * <ul>
     *     <li>{@code save}</li>
     *     <li>{@code save_as}</li>
     *     <li>{@code edit}</li>
     *     <li>{@code remove}</li>
     *     <li>{@code pin}</li>
     *     <li>{@code make_default}</li>
     *     <li>{@code save_search_folder}</li>
     *     <li>{@code save_app_folder}</li>
     * </ul>
     * Action components can have the following options:
     * <ul>
     *     <li>{@code no-icon} - if an icon shouldn't be displayed on action button. For example: [save | no-icon]</li>
     *     <li>{@code no-caption} - if a caption shouldn't be displayed on action button. For example: [pin | no-caption]</li>
     * </ul>
     * @return a template for filter controls layout
     */
    @Property("cuba.gui.genericFilterControlsLayout")
    @Source(type = SourceType.DATABASE)
    @Default("[filters_popup] [add_condition] [spacer] [settings | save, save_as, edit, remove, make_default, pin, save_search_folder, save_app_folder] [max_results] [fts_switch]")
    String getGenericFilterControlsLayout();

    /**
     * Returns a comma-separated list of values that are used as options for "Show rows" lookup field
     * of generic filter component. Add NULL option to the list if the lookup field should contain an empty value.
     */
    @Property("cuba.gui.genericFilterMaxResultsOptions")
    @Source(type = SourceType.DATABASE)
    @Default("NULL, 20, 50, 100, 500, 1000, 5000")
    String getGenericFilterMaxResultsOptions();

    /**
     * Support e-mail. Exception report emails are sent to this address.
     */
    @Property("cuba.supportEmail")
    @Source(type = SourceType.DATABASE)
    String getSupportEmail();

    /**
     * @return System ID. Use for identification (support emails).
     */
    @Property("cuba.systemId")
    @DefaultString("CUBA")
    @Source(type = SourceType.DATABASE)
    String getSystemID();

    @Property("cuba.gui.tableShortcut.insert")
    @Source(type = SourceType.DATABASE)
    @DefaultString("CTRL-BACKSLASH")
    String getTableInsertShortcut();

    @Property("cuba.gui.tableShortcut.add")
    @Source(type = SourceType.DATABASE)
    @DefaultString("CTRL-ALT-BACKSLASH")
    String getTableAddShortcut();

    @Property("cuba.gui.tableShortcut.remove")
    @Source(type = SourceType.DATABASE)
    @DefaultString("CTRL-DELETE")
    String getTableRemoveShortcut();

    @Property("cuba.gui.tableShortcut.edit")
    @Source(type = SourceType.DATABASE)
    @DefaultString("ENTER")
    String getTableEditShortcut();

    @Property("cuba.gui.commitShortcut")
    @Source(type = SourceType.DATABASE)
    @DefaultString("CTRL-ENTER")
    String getCommitShortcut();

    @Property("cuba.gui.closeShortcut")
    @Source(type = SourceType.DATABASE)
    @DefaultString("ESCAPE")
    String getCloseShortcut();

    @Property("cuba.gui.filterApplyShortcut")
    @Source(type = SourceType.DATABASE)
    @DefaultString("SHIFT-ENTER")
    String getFilterApplyShortcut();

    @Property("cuba.gui.filterSelectShortcut")
    @Source(type = SourceType.DATABASE)
    @DefaultString("SHIFT-BACKSPACE")
    String getFilterSelectShortcut();

    @Property("cuba.gui.nextTabShortcut")
    @Source(type = SourceType.DATABASE)
    @DefaultString("CTRL-SHIFT-PAGE_DOWN")
    String getNextTabShortcut();

    @Property("cuba.gui.previousTabShortcut")
    @Source(type = SourceType.DATABASE)
    @DefaultString("CTRL-SHIFT-PAGE_UP")
    String getPreviousTabShortcut();

    @Property("cuba.gui.pickerShortcut.modifiers")
    @Source(type = SourceType.DATABASE)
    @DefaultString("CTRL-ALT")
    String getPickerShortcutModifiers();

    @Property("cuba.gui.pickerShortcut.lookup")
    @Source(type = SourceType.DATABASE)
    @DefaultString("CTRL-ALT-L")
    String getPickerLookupShortcut();

    @Property("cuba.gui.pickerShortcut.open")
    @Source(type = SourceType.DATABASE)
    @DefaultString("CTRL-ALT-O")
    String getPickerOpenShortcut();

    @Property("cuba.gui.pickerShortcut.clear")
    @Source(type = SourceType.DATABASE)
    @DefaultString("CTRL-ALT-C")
    String getPickerClearShortcut();

    @Property("cuba.gui.useSaveConfirmation")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(true)
    boolean getUseSaveConfirmation();

    @Property("cuba.gui.loadObsoleteSettingsForTable")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(false)
    boolean getLoadObsoleteSettingsForTable();

    @Property("cuba.gui.layoutAnalyzerEnabled")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(true)
    boolean getLayoutAnalyzerEnabled();

    @Property("cuba.gui.systemInfoScriptsEnabled")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(true)
    boolean getSystemInfoScriptsEnabled();
    void setSystemInfoScriptsEnabled(boolean enabled);

    @Property("cuba.gui.manualScreenSettingsSaving")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(false)
    boolean getManualScreenSettingsSaving();

    @Property("cuba.gui.showIconsForPopupMenuActions")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(false)
    boolean getShowIconsForPopupMenuActions();
}
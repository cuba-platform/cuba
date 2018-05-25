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

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.client.sys.PersistenceManagerClient;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.entity.AppFolder;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.global.filter.DenyingClause;
import com.haulmont.cuba.core.global.filter.QueryFilter;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManagerProvider;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action.Status;
import com.haulmont.cuba.gui.components.Component.Alignment;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.gui.components.Frame.MessageType;
import com.haulmont.cuba.gui.components.KeyCombination.Key;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.components.filter.condition.*;
import com.haulmont.cuba.gui.components.filter.edit.FilterEditor;
import com.haulmont.cuba.gui.components.filter.filterselect.FilterSelectWindow;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.gui.settings.SettingsImpl;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.FilterEntity;
import com.haulmont.cuba.security.entity.SearchFolder;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@org.springframework.stereotype.Component(FilterDelegate.NAME)
@Scope("prototype")
public class FilterDelegateImpl implements FilterDelegate {

    protected static final String BORDER_HIDDEN_STYLENAME = "border-hidden";

    protected static final String GLOBAL_FILTER_PERMISSION = "cuba.gui.filter.global";
    protected static final String GLOBAL_APP_FOLDERS_PERMISSION = "cuba.gui.appFolder.global";
    protected static final String FILTER_EDIT_PERMISSION = "cuba.gui.filter.edit";
    protected static final String CONDITIONS_LOCATION_TOP = "top";

    protected static final Logger log = LoggerFactory.getLogger(FilterDelegateImpl.class);
    protected static final String MODIFIED_INDICATOR_SYMBOL = " *";

    @Inject
    protected ComponentsFactory componentsFactory;
    @Inject
    protected ThemeConstantsManager themeConstantsManager;
    @Inject
    protected Messages messages;
    @Inject
    protected WindowManagerProvider windowManagerProvider;
    @Inject
    protected Metadata metadata;
    @Inject
    protected WindowConfig windowConfig;
    @Inject
    protected UserSessionSource userSessionSource;
    @Inject
    protected Configuration configuration;
    @Inject
    protected Security security;
    @Inject
    protected FilterHelper filterHelper;
    @Inject
    protected FilterParser filterParser;
    @Inject
    protected MaxResultsFieldHelper maxResultsFieldHelper;

    protected FtsFilterHelper ftsFilterHelper;
    protected DataService dataService;
    protected PersistenceManagerClient persistenceManager;
    protected ClientConfig clientConfig;
    protected GlobalConfig globalConfig;
    protected AddConditionHelper addConditionHelper;
    protected ThemeConstants theme;
    protected WindowManager windowManager;

    protected Filter filter;
    protected FilterEntity adHocFilter;
    protected ConditionsTree conditions = new ConditionsTree();
    protected ConditionsTree prevConditions = new ConditionsTree();
    protected List<AbstractCondition> initialConditions = new ArrayList<>();
    protected FilterEntity filterEntity;
    protected FilterEntity initialFilterEntity;
    protected CollectionDatasource datasource;
    protected QueryFilter dsQueryFilter;
    protected List<FilterEntity> filterEntities = new ArrayList<>();
    protected AppliedFilter lastAppliedFilter;
    protected LinkedList<AppliedFilterHolder> appliedFilters = new LinkedList<>();
    protected List<Filter.FilterEntityChangeListener> filterEntityChangeListeners = new ArrayList<>();

    protected GroupBoxLayout groupBoxLayout;
    protected BoxLayout layout;
    protected PopupButton filtersPopupButton;
    protected Component.Container conditionsLayout;
    protected BoxLayout maxResultsLayout;
    protected Field maxResultsField;
    protected TextField maxResultsTextField;
    protected LookupField maxResultsLookupField;
    protected BoxLayout controlsLayout;
    protected Component.Container appliedFiltersLayout;
    protected PopupButton settingsBtn;
    protected Component applyTo;
    protected SaveAction saveAction;
    protected SaveAction saveWithValuesAction;
    protected TextField ftsSearchCriteriaField;
    protected CheckBox ftsSwitch;
    protected LinkButton addConditionBtn;
    protected Component.Container filtersPopupBox;
    protected Button searchBtn;
    protected Component controlsLayoutGap;
    protected Object paramEditComponentToFocus;

    protected String caption;
    protected int maxResults = -1;
    protected boolean useMaxResults;
    protected boolean textMaxResults;
    protected Boolean manualApplyRequired;
    protected boolean folderActionsEnabled = true;
    protected boolean filtersLookupListenerEnabled = true;
    protected boolean filtersPopupDisplayed = false;
    protected boolean filtersLookupDisplayed = false;
    protected boolean maxResultsAddedToLayout = false;
    protected boolean editable = true;
    protected FilterMode filterMode;
    protected boolean filterSavingPossible = true;
    protected Integer columnsCount;
    protected String initialWindowCaption;
    protected String conditionsLocation;
    protected boolean filterActionsCreated = false;
    protected boolean delayedFocus;
    protected boolean modeSwitchVisible = true;

    protected SaveAsAction saveAsAction;
    protected EditAction editAction;
    protected MakeDefaultAction makeDefaultAction;
    protected RemoveAction removeAction;
    protected ClearValuesAction clearValuesAction;
    protected PinAppliedAction pinAppliedAction;
    protected SaveAsFolderAction saveAsAppFolderAction;
    protected SaveAsFolderAction saveAsSearchFolderAction;
    protected LookupField filtersLookup;

    protected List<FDExpandedStateChangeListener> expandedStateChangeListeners;

    protected Filter.BeforeFilterAppliedHandler beforeFilterAppliedHandler;

    protected Filter.AfterFilterAppliedHandler afterFilterAppliedHandler;
    protected boolean borderVisible = true;

    protected Set<String> ftsLastDatasourceRefreshParamsNames = new HashSet<>();
    protected Consumer<String> captionChangedListener;

    protected enum ConditionsFocusType {
        NONE,
        FIRST,
        LAST
    }

    @PostConstruct
    public void init() {
        theme = themeConstantsManager.getConstants();
        windowManager = windowManagerProvider.get();
        dataService = AppBeans.get(DataService.class);
        persistenceManager = AppBeans.get(PersistenceManagerClient.class);
        globalConfig = configuration.getConfig(GlobalConfig.class);
        clientConfig = configuration.getConfig(ClientConfig.class);
        if (AppBeans.containsBean(FtsFilterHelper.NAME)) {
            ftsFilterHelper = AppBeans.get(FtsFilterHelper.class);
        }
        filterMode = FilterMode.GENERIC_MODE;

        conditionsLocation = clientConfig.getGenericFilterConditionsLocation();
        createLayout();
    }

    protected void createLayout() {
        if (layout == null) {
            groupBoxLayout = componentsFactory.createComponent(GroupBoxLayout.class);
            groupBoxLayout.addExpandedStateChangeListener(e -> fireExpandStateChange());
            groupBoxLayout.setOrientation(GroupBoxLayout.Orientation.VERTICAL);
            groupBoxLayout.setWidth("100%");
            layout = componentsFactory.createComponent(VBoxLayout.class);
            layout.setWidth("100%");
            groupBoxLayout.add(layout);
            if (caption == null)
                setCaption(getMainMessage("filter.groupBoxCaption"));
        } else {
            Collection<Component> components = layout.getComponents();
            for (Component component : components) {
                layout.remove(component);
            }
        }
        layout.setSpacing(true);

        appliedFiltersLayout = componentsFactory.createComponent(VBoxLayout.class);

        conditionsLayout = componentsFactory.createComponent(HBoxLayout.class);
        conditionsLayout.setVisible(false); // initially hidden
        conditionsLayout.setWidth("100%");
        conditionsLayout.setStyleName("filter-conditions");

        if (filterMode == FilterMode.GENERIC_MODE) {
            createControlsLayoutForGeneric();
        } else {
            createControlsLayoutForFts();
        }

        if (CONDITIONS_LOCATION_TOP.equals(conditionsLocation)) {
            layout.add(conditionsLayout);
            layout.add(controlsLayout);
        } else {
            layout.add(controlsLayout);
            layout.add(conditionsLayout);
        }
    }

    protected void createControlsLayoutForGeneric() {
        controlsLayout = componentsFactory.createComponent(HBoxLayout.class);
        controlsLayout.setSpacing(true);
        controlsLayout.setWidth("100%");
        filterHelper.setInternalDebugId(controlsLayout, "controlsLayout");

        filtersPopupBox = filterHelper.createSearchButtonGroupContainer();
        filtersPopupBox.addStyleName("filter-search-button-layout");
        filterHelper.setInternalDebugId(filtersPopupBox, "filtersPopupBox");

        searchBtn = componentsFactory.createComponent(Button.class);
        filtersPopupBox.add(searchBtn);
        searchBtn.setStyleName("filter-search-button");
        searchBtn.setCaption(getMainMessage("filter.search"));
        searchBtn.setIcon("icons/search.png");
        searchBtn.setAction(new AbstractAction("search") {
            @Override
            public void actionPerform(Component component) {
                apply(false);
            }
        });
        filterHelper.setInternalDebugId(searchBtn, "searchBtn");

        filtersPopupButton = componentsFactory.createComponent(PopupButton.class);
        filtersPopupButton.setStyleName("icon-only");
        filterHelper.setInternalDebugId(filtersPopupButton, "filtersPopupButton");
        filtersPopupBox.add(filtersPopupButton);

        filtersLookup = componentsFactory.createComponent(LookupField.class);
        filtersLookup.setWidth(theme.get("cuba.gui.filter.select.width"));
        filtersLookup.addValueChangeListener(new FiltersLookupChangeListener());
        filterHelper.setLookupNullSelectionAllowed(filtersLookup, false);
        filterHelper.setInternalDebugId(filtersLookup, "filtersLookup");

        addConditionBtn = componentsFactory.createComponent(LinkButton.class);
        addConditionBtn.setAlignment(Alignment.MIDDLE_LEFT);
        addConditionBtn.setCaption(getMainMessage("filter.addCondition"));
        addConditionBtn.setAction(new AbstractAction("openAddConditionDlg") {
            @Override
            public void actionPerform(Component component) {
                addConditionHelper.addCondition(conditions);
            }
        });
        filterHelper.setInternalDebugId(addConditionBtn, "addConditionBtn");

        controlsLayoutGap = componentsFactory.createComponent(Label.class);
        filterHelper.setInternalDebugId(controlsLayoutGap, "controlsLayoutGap");
        controlsLayout.add(controlsLayoutGap);
        controlsLayout.expand(controlsLayoutGap);

        settingsBtn = componentsFactory.createComponent(PopupButton.class);
        settingsBtn.setIcon("icons/gear.png");
        settingsBtn.setStyleName("filter-settings-button");
        filterHelper.setInternalDebugId(settingsBtn, "settingsBtn");
        createFilterActions();

        createMaxResultsLayout();
        if (isFtsModeEnabled()) {
            createFtsSwitch();
            ftsSwitch.setAlignment(Alignment.MIDDLE_RIGHT);
            filterHelper.setInternalDebugId(ftsSwitch, "ftsSwitch");
        }

        String layoutDescription = clientConfig.getGenericFilterControlsLayout();
        ControlsLayoutBuilder controlsLayoutBuilder = createControlsLayoutBuilder(layoutDescription);
        controlsLayoutBuilder.build();
        if (isMaxResultsLayoutVisible()) {
            initMaxResults();
        }

        maxResultsLayout.setVisible(isMaxResultsLayoutVisible());
        filterHelper.setInternalDebugId(maxResultsLayout, "maxResultsLayout");
    }

    protected void createControlsLayoutForFts() {
        controlsLayout = componentsFactory.createComponent(HBoxLayout.class);
        controlsLayout.setSpacing(true);
        controlsLayout.setWidthFull();

        ftsSearchCriteriaField = componentsFactory.createComponent(TextField.class);
        ftsSearchCriteriaField.setWidth(theme.get("cuba.gui.filter.ftsSearchCriteriaField.width"));
        ftsSearchCriteriaField.setInputPrompt(getMainMessage("filter.enterSearchPhrase"));
        filterHelper.setInternalDebugId(ftsSearchCriteriaField, "ftsSearchCriteriaField");
        filterHelper.addShortcutListener(ftsSearchCriteriaField, createFtsSearchShortcutListener());

        paramEditComponentToFocus = ftsSearchCriteriaField;
        controlsLayout.add(ftsSearchCriteriaField);

        searchBtn = componentsFactory.createComponent(Button.class);
        searchBtn.setCaption(getMainMessage("filter.search"));
        searchBtn.setIcon("icons/search.png");
        searchBtn.setAction(new AbstractAction("search") {
            @Override
            public void actionPerform(Component component) {
                applyFts();
            }
        });
        controlsLayout.add(searchBtn);

        controlsLayoutGap = componentsFactory.createComponent(Label.class);
        controlsLayout.add(controlsLayoutGap);
        controlsLayout.expand(controlsLayoutGap);

        createMaxResultsLayout();
        if (isMaxResultsLayoutVisible()) {
            controlsLayout.add(maxResultsLayout);
            initMaxResults();
        }

        createFtsSwitch();
        ftsSwitch.setAlignment(Alignment.MIDDLE_RIGHT);
        controlsLayout.add(ftsSwitch);
    }

    protected FilterHelper.ShortcutListener createFtsSearchShortcutListener() {
        return new FilterHelper.ShortcutListener("ftsSearch", new KeyCombination(Key.ENTER)) {
            @Override
            public void handleShortcutPressed() {
                applyFts();
            }
        };
    }

    protected void createFtsSwitch() {
        ftsSwitch = componentsFactory.createComponent(CheckBox.class);
        ftsSwitch.setCaption(getMainMessage("filter.ftsSwitch"));
        ftsSwitch.setValue(filterMode == FilterMode.FTS_MODE);

        ftsSwitch.addValueChangeListener(e -> {
            filterMode = Boolean.TRUE.equals(e.getValue()) ? FilterMode.FTS_MODE : FilterMode.GENERIC_MODE;
            switchFilterMode(filterMode);
        });

        ftsSwitch.setVisible(modeSwitchVisible);
    }

    @Override
    public void switchFilterMode(FilterMode filterMode) {
        if (filterMode == FilterMode.FTS_MODE && !isFtsModeEnabled() && !isEntityAvailableForFts()) {
            log.warn("Unable to switch to the FTS filter mode. FTS mode is not supported for the {} entity",
                    datasource.getMetaClass().getName());
            return;
        }
        this.filterMode = filterMode;
        if (filterMode == FilterMode.FTS_MODE) {
            prevConditions = conditions;
            ((CollectionDatasource.SupportsApplyToSelected) datasource).unpinAllQuery();
            appliedFilters.clear();
            lastAppliedFilter = null;
        }
        conditions = (filterMode == FilterMode.GENERIC_MODE) ? prevConditions : new ConditionsTree();
        createLayout();

        if (filterMode == FilterMode.GENERIC_MODE) {
            fillConditionsLayout(ConditionsFocusType.FIRST);
            addConditionBtn.setVisible(editable && userCanEditFilers());
            setFilterActionsEnabled();
            initFilterSelectComponents();
            if (ftsSwitch != null && !isEntityAvailableForFts()) {
                controlsLayout.remove(ftsSwitch);
            }
        }
        if (paramEditComponentToFocus != null)
            requestFocusToParamEditComponent();
        else if (filtersPopupDisplayed)
            filtersPopupButton.requestFocus();
        else if (filtersLookupDisplayed) {
            filtersLookup.requestFocus();
        }
        updateWindowCaption();
    }

    protected void createMaxResultsLayout() {
        maxResultsLayout = componentsFactory.createComponent(HBoxLayout.class);
        maxResultsLayout.setStyleName("c-maxresults");
        maxResultsLayout.setSpacing(true);
        Label maxResultsLabel = componentsFactory.createComponent(Label.class);
        maxResultsLabel.setStyleName("c-maxresults-label");
        maxResultsLabel.setValue(messages.getMainMessage("filter.maxResults.label1"));
        maxResultsLabel.setAlignment(Alignment.MIDDLE_RIGHT);
        maxResultsLayout.add(maxResultsLabel);

        maxResultsTextField = componentsFactory.createComponent(TextField.class);
        maxResultsTextField.setStyleName("c-maxresults-input");
        maxResultsTextField.setMaxLength(4);
        maxResultsTextField.setWidth(theme.get("cuba.gui.Filter.maxResults.width"));
        maxResultsTextField.setDatatype(Datatypes.get("int"));

        maxResultsLookupField = maxResultsFieldHelper.createMaxResultsLookupField();
        maxResultsLookupField.setStyleName("c-maxresults-select");

        maxResultsField = textMaxResults ? maxResultsTextField : maxResultsLookupField;
        maxResultsLayout.add(maxResultsField);
    }

    @Override
    public void setBorderVisible(boolean visible) {
        borderVisible = visible;

        if (visible) {
            groupBoxLayout.removeStyleName(BORDER_HIDDEN_STYLENAME);
        } else {
            groupBoxLayout.addStyleName(BORDER_HIDDEN_STYLENAME);
        }
    }

    @Override
    public boolean isBorderVisible() {
        return borderVisible;
    }

    /**
     * Loads filter entities, finds default filter and applies it if found
     */
    @Override
    public void loadFiltersAndApplyDefault() {
        initShortcutActions();
        initAdHocFilter();
        loadFilterEntities();
        FilterEntity defaultFilter = getDefaultFilter(filterEntities);
        initFilterSelectComponents();

        if (defaultFilter == null) {
            defaultFilter = adHocFilter;
        }

        try {
            setFilterEntity(defaultFilter);
        } catch (Exception e) {
            log.error("Exception on loading default filter '" + defaultFilter.getName() + "'", e);
            windowManager.showNotification(messages.formatMainMessage("filter.errorLoadingDefaultFilter", defaultFilter.getName()), Frame.NotificationType.ERROR);
            defaultFilter = adHocFilter;
            setFilterEntity(adHocFilter);
        }

        if (defaultFilter != adHocFilter && (filterMode == FilterMode.GENERIC_MODE)) {
            Window window = ComponentsHelper.getWindow(filter);
            if (!WindowParams.DISABLE_AUTO_REFRESH.getBool(window.getContext())) {
                if (getResultingManualApplyRequired()) {
                    if (BooleanUtils.isTrue(defaultFilter.getApplyDefault())) {
                        apply(true);
                    }
                } else
                    apply(true);
                if (filterEntity != null) {
                    window.setDescription(getFilterCaption(filterEntity));
                } else
                    window.setDescription(null);
            }
        }
    }

    protected boolean suitableCondition(AbstractCondition condition) {
        if (condition instanceof PropertyCondition) {
            return datasource.getMetaClass()
                    .getPropertyPath(condition.getName()) != null;
        }

        if (condition instanceof DynamicAttributesCondition) {
            return DynamicAttributesUtils.getMetaPropertyPath(
                    datasource.getMetaClass(),
                    ((DynamicAttributesCondition) condition).getCategoryAttributeId()
            ) != null;
        }

        return true;
    }

    /**
     * Sets filter entity, creates condition editor components and applies filter if necessary
     */
    @Override
    public void setFilterEntity(FilterEntity filterEntity) {
        this.filterEntity = filterEntity;
        conditions = filterParser.getConditions(filter, filterEntity.getXml());
        prevConditions = conditions;
        initialConditions = conditions.toConditionsList();

        for (AbstractCondition condition : initialConditions) {
            if (!suitableCondition(condition)) {
                String message = String.format(getMainMessage("filter.inappropriate.filter"),
                        filterEntity.getName(), datasource.getMetaClass().getName());

                windowManager.showNotification(message, Frame.NotificationType.HUMANIZED);
                setFilterEntity(adHocFilter);
                break;
            }

            condition.addListener(new AbstractCondition.Listener() {
                @Override
                public void captionChanged() {
                }

                @Override
                public void paramChanged(Param oldParam, Param newParam) {
                    updateFilterModifiedIndicator();
                }
            });
        }

        // If there are window parameters named as filter parameters, assign values to the corresponding
        // filter params. Together with passing a filter code in 'filter' window parameter it allows to open an
        // arbitrary filter with parameters regardless of a user defined default filter.
        Window window = ComponentsHelper.getWindow(filter);
        for (AbstractCondition condition : conditions.toConditionsList()) {
            if (condition.getParam() != null) {
                for (Map.Entry<String, Object> entry : window.getContext().getParams().entrySet()) {
                    if (entry.getKey().equals(condition.getParam().getName()))
                        condition.getParam().parseValue((String) entry.getValue());
                }
            }
        }

        saveInitialFilterState();

        if (filtersLookupDisplayed) {
            filtersLookupListenerEnabled = false;
            filtersLookup.setValue(filterEntity);
            filtersLookupListenerEnabled = true;
        }

        setFilterActionsEnabled();
        setFilterActionsVisible();
        fillConditionsLayout(ConditionsFocusType.FIRST);
        if (delayedFocus) {
            delayedFocus = false;
            requestFocus();
        } else {
            requestFocusToParamEditComponent();
        }

        setConditionsLayoutVisible(true);

        if (BooleanUtils.isTrue(filterEntity.getIsSet())
                || (filterEntity.getFolder() != null && BooleanUtils.isNotFalse(filterEntity.getApplyDefault()))) {
            apply(true);
        }

        for (Filter.FilterEntityChangeListener listener : filterEntityChangeListeners) {
            listener.filterEntityChanged(filterEntity);
        }

        updateWindowCaption();
    }

    /**
     * Saves initial filter state. It is used for indicating of filter modifications
     */
    protected void saveInitialFilterState() {
        initialFilterEntity = metadata.create(FilterEntity.class);
        initialFilterEntity.setName(filterEntity.getName());
        initialFilterEntity.setCode(filterEntity.getCode());
        initialFilterEntity.setUser(filterEntity.getUser());
        initialFilterEntity.setXml(filterEntity.getXml());
    }

    /**
     * Sets conditionsLayout visibility and shows/hides top border of controlsLayout
     */
    protected void setConditionsLayoutVisible(boolean visible) {
        conditionsLayout.setVisible(visible && !conditionsLayout.getComponents().isEmpty());

        controlsLayout.setStyleName(getControlsLayoutStyleName());
    }

    protected void setFilterActionsEnabled() {
        boolean isGlobal = filterEntity.getUser() == null;
        boolean userCanEditGlobalFilter = uerCanEditGlobalFilter();
        boolean userCanEditFilters = userCanEditFilers();
        boolean filterEditable = isEditable();
        boolean userCanEditGlobalAppFolder = userSessionSource.getUserSession().isSpecificPermitted(GLOBAL_APP_FOLDERS_PERMISSION);
        boolean createdByCurrentUser = userSessionSource.getUserSession().getCurrentOrSubstitutedUser().equals(filterEntity.getUser());
        boolean hasCode = !Strings.isNullOrEmpty(filterEntity.getCode());
        boolean isFolder = filterEntity.getFolder() != null;
        boolean isSearchFolder = isFolder && (filterEntity.getFolder() instanceof SearchFolder);
        boolean isAppFolder = isFolder && (filterEntity.getFolder() instanceof AppFolder);
        boolean isSet = BooleanUtils.isTrue(filterEntity.getIsSet());
        boolean isDefault = BooleanUtils.isTrue(filterEntity.getIsDefault());
        boolean isAdHocFilter = filterEntity == adHocFilter;

        boolean editActionEnabled = !isSet && filterEditable && userCanEditFilters && (!isGlobal || userCanEditGlobalFilter);
        filterSavingPossible = editActionEnabled &&
                ((isGlobal && userCanEditGlobalFilter) || (!isGlobal && createdByCurrentUser)) &&
                ((!isFolder && !hasCode) || isSearchFolder || (isAppFolder && userCanEditGlobalAppFolder));
        boolean saveActionEnabled = filterSavingPossible && (isFolder || isFilterModified());
        boolean saveAsActionEnabled = !isSet && filterEditable && userCanEditFilters;
        boolean removeActionEnabled = !isSet &&
                (!hasCode && !isFolder) &&
                ((isGlobal && userCanEditGlobalFilter) || (!isGlobal && createdByCurrentUser)) &&
                !isAdHocFilter && filterEditable && userCanEditFilters;
        boolean makeDefaultActionEnabled = !isDefault && !isFolder && !isSet && !isAdHocFilter && (!isGlobal || userCanEditGlobalFilter);
        boolean pinAppliedActionEnabled = lastAppliedFilter != null
                && !(lastAppliedFilter.getFilterEntity() == adHocFilter && lastAppliedFilter.getConditions().getRoots().size() == 0)
                && (datasource == null || Stores.isMain(metadata.getTools().getStoreName(datasource.getMetaClass())));
        boolean saveAsSearchFolderActionEnabled = folderActionsEnabled && !isFolder && !hasCode;
        boolean saveAsAppFolderActionEnabled = folderActionsEnabled && !isFolder && !hasCode && userCanEditGlobalAppFolder;

        saveAction.setEnabled(saveActionEnabled);
        saveWithValuesAction.setEnabled(filterSavingPossible && !conditions.toConditionsList().isEmpty());
        saveAsAction.setEnabled(saveAsActionEnabled);
        editAction.setEnabled(editActionEnabled);
        removeAction.setEnabled(removeActionEnabled);
        makeDefaultAction.setEnabled(makeDefaultActionEnabled);
        pinAppliedAction.setEnabled(pinAppliedActionEnabled);
        saveAsSearchFolderAction.setEnabled(saveAsSearchFolderActionEnabled);
        saveAsAppFolderAction.setEnabled(saveAsAppFolderActionEnabled);

        if (filterHelper.isTableActionsEnabled()) {
            fillTableActions();
        }
    }

    protected void setFilterActionsVisible() {
        saveAsSearchFolderAction.setVisible(folderActionsEnabled);
        saveAsAppFolderAction.setVisible(folderActionsEnabled);
        saveAction.setVisible(editable);
        saveWithValuesAction.setVisible(editable);
        saveAsAction.setVisible(editable);
        editAction.setVisible(editable);
        removeAction.setVisible(editable);
    }

    protected void createFilterActions() {
        saveAction = new SaveAction("save", false, getMainMessage("filter.save"));
        saveWithValuesAction = new SaveAction("save_with_values", true, getMainMessage("filter.saveWithValues"));
        saveAsAction = new SaveAsAction();
        editAction = new EditAction();
        makeDefaultAction = new MakeDefaultAction();
        removeAction = new RemoveAction();
        pinAppliedAction = new PinAppliedAction();
        saveAsAppFolderAction = new SaveAsFolderAction(true);
        saveAsSearchFolderAction = new SaveAsFolderAction(false);
        clearValuesAction = new ClearValuesAction();
        filterActionsCreated = true;
    }

    protected boolean uerCanEditGlobalFilter() {
        return userSessionSource.getUserSession().isSpecificPermitted(GLOBAL_FILTER_PERMISSION);
    }

    protected boolean userCanEditFilers() {
        return security.isSpecificPermitted(FILTER_EDIT_PERMISSION);
    }

    protected void saveFilterEntity() {
        Boolean isDefault = filterEntity.getIsDefault();
        Boolean applyDefault = filterEntity.getApplyDefault();
        if (filterEntity.getFolder() == null) {
            CommitContext ctx = new CommitContext(Collections.singletonList(filterEntity));
            Set<Entity> result = dataService.commit(ctx);
            FilterEntity savedFilterEntity = (FilterEntity) result.iterator().next();
            filterEntities.remove(filterEntity);
            filterEntity = savedFilterEntity;
            filterEntities.add(filterEntity);

            filterEntity.setApplyDefault(applyDefault);
            filterEntity.setIsDefault(isDefault);
        } else {
            filterEntity.getFolder().setName(filterEntity.getName());
            filterEntity.getFolder().setFilterXml(filterEntity.getXml());
            AbstractSearchFolder folder = saveFolder(filterEntity.getFolder());
            filterEntity.setFolder(folder);
        }

        saveInitialFilterState();
        setFilterActionsEnabled();
        updateFilterModifiedIndicator();
    }

    @Nullable
    protected AbstractSearchFolder saveFolder(AbstractSearchFolder folder) {
        return filterHelper.saveFolder(folder);
    }

    protected void saveAsFolder(boolean isAppFolder) {
        final AbstractSearchFolder folder;
        if (isAppFolder)
            folder = (metadata.create(AppFolder.class));
        else
            folder = (metadata.create(SearchFolder.class));

        if (filterEntity.getCode() == null) {
            String folderName = filterEntity != adHocFilter ? filterEntity.getName() : "";
            folder.setName(folderName);
            folder.setTabName(folderName);
        } else {
            String name = messages.getMainMessage(filterEntity.getCode());
            folder.setName(name);
            folder.setTabName(name);
        }

        String newXml = filterParser.getXml(conditions, Param.ValueProperty.VALUE);

        folder.setFilterComponentId(filterEntity.getComponentId());
        folder.setFilterXml(newXml);
        if (!isAppFolder) {
            if (uerCanEditGlobalFilter())
                ((SearchFolder) folder).setUser(filterEntity.getUser());
            else
                ((SearchFolder) folder).setUser(userSessionSource.getUserSession().getCurrentOrSubstitutedUser());
        }
        Presentations presentations;
        if (applyTo != null && applyTo instanceof Component.HasPresentations) {
            final Component.HasPresentations presentationsOwner = (Component.HasPresentations) applyTo;
            presentations = presentationsOwner.isUsePresentations()
                    ? presentationsOwner.getPresentations() : null;
        } else {
            presentations = null;
        }

        Runnable commitHandler;
        if (isAppFolder) {
            commitHandler = () -> {
                AbstractSearchFolder savedFolder = saveFolder(folder);
                filterEntity.setFolder(savedFolder);
            };
        } else {
            commitHandler = () -> {
                AbstractSearchFolder savedFolder = saveFolder(folder);
                filterEntity.setFolder(savedFolder);
            };
        }

        filterHelper.openFolderEditWindow(isAppFolder, folder, presentations, commitHandler);

    }

    /**
     * Removes all components from conditionsLayout and fills it with components for editing filter conditions
     *
     * @param conditionsFocusType where to set focus (first condition, last condition, no focus)
     */
    protected void fillConditionsLayout(ConditionsFocusType conditionsFocusType) {
        for (Component component : conditionsLayout.getComponents()) {
            conditionsLayout.remove(component);
        }

        paramEditComponentToFocus = null;

        recursivelyCreateConditionsLayout(conditionsFocusType, false, conditions.getRootNodes(), conditionsLayout, 0);

        conditionsLayout.setVisible(!conditionsLayout.getComponents().isEmpty());
    }

    protected void recursivelyCreateConditionsLayout(ConditionsFocusType conditionsFocusType,
                                                     boolean initialFocusSet,
                                                     List<Node<AbstractCondition>> nodes,
                                                     Component.Container parentContainer,
                                                     int level) {
        List<Node<AbstractCondition>> visibleConditionNodes = fetchVisibleNodes(nodes);

        if (visibleConditionNodes.isEmpty()) {
            if (level == 0)
                controlsLayout.setStyleName("filter-control-no-border");
            return;
        }

        //note that this is not grid columns count, but number of conditions (label cell + value cell) in one row
        int conditionsCount = getColumnsCount();
        int row = 0;
        int nextColumnStart = 0;
        GridLayout grid = componentsFactory.createComponent(GridLayout.class);
        grid.setStyleName("conditions-grid");
        grid.setColumns(conditionsCount * 2);
        //set expand ratio only for cells with param edit components
        for (int i = 0; i < conditionsCount; i++) {
            grid.setColumnExpandRatio(i * 2 + 1, 1);
        }
        grid.setRows(1);
        grid.setSpacing(true);
        grid.setWidth("100%");

        ParamEditor firstParamEditor = null;
        ParamEditor lastParamEditor = null;

        boolean currentFocusSet = initialFocusSet;

        for (int i = 0; i < visibleConditionNodes.size(); i++) {
            Node<AbstractCondition> node = visibleConditionNodes.get(i);
            final AbstractCondition condition = node.getData();
            BoxLayout labelAndOperationCellContent = null;
            Component paramEditComponentCellContent = null;
            Component groupCellContent = null;
            if (condition.isGroup()) {
                groupCellContent = createGroupConditionBox(condition, node, conditionsFocusType, currentFocusSet, level);
                level++;
            } else {
                if (condition.getParam().getJavaClass() != null) {
                    ParamEditor paramEditor = createParamEditor(condition);

                    if (firstParamEditor == null) firstParamEditor = paramEditor;
                    lastParamEditor = paramEditor;
                    currentFocusSet = true;

                    labelAndOperationCellContent = paramEditor.getLabelAndOperationLayout();
                    paramEditComponentCellContent = paramEditor.getParamEditComponentLayout();
                } else {
                    BoxLayout paramLayout = componentsFactory.createComponent(HBoxLayout.class);
                    paramLayout.setSpacing(true);
                    paramLayout.setMargin(false);

                    labelAndOperationCellContent = paramLayout;
                }
            }

            //groupBox for group conditions must occupy the whole line in conditions grid
            Integer conditionWidth = condition.isGroup() ? (Integer) conditionsCount : condition.getWidth();
            int nextColumnEnd = nextColumnStart + conditionWidth - 1;
            if (nextColumnEnd >= conditionsCount) {
                //complete current row in grid with gaps if next cell will be on next row
                completeGridRowWithGaps(grid, row, nextColumnStart, false);
                //place cell to next row in grid
                nextColumnStart = 0;
                nextColumnEnd = conditionWidth - 1;
                row++;
                grid.setRows(row + 1);
            }

            if (groupCellContent != null) {
                grid.add(groupCellContent, nextColumnStart * 2, row, nextColumnEnd * 2 + 1, row);
            }
            if (labelAndOperationCellContent != null) {
                labelAndOperationCellContent.addStyleName("param-label-layout");
                grid.add(labelAndOperationCellContent, nextColumnStart * 2, row, nextColumnStart * 2, row);
                if (nextColumnStart != 0)
                    labelAndOperationCellContent.setMargin(false, false, false, true);
            }
            if (paramEditComponentCellContent != null) {
                paramEditComponentCellContent.addStyleName("param-field-layout");
                grid.add(paramEditComponentCellContent, nextColumnStart * 2 + 1, row, nextColumnEnd * 2 + 1, row);
            }

            nextColumnStart = nextColumnEnd + 1;

            //add next row if necessary
            if (i < visibleConditionNodes.size() - 1) {
                if (nextColumnStart >= conditionsCount) {
                    nextColumnStart = 0;
                    row++;
                    grid.setRows(row + 1);
                }
            }
        }

        if (!initialFocusSet) {
            switch (conditionsFocusType) {
                case FIRST:
                    if (firstParamEditor != null)
                        paramEditComponentToFocus = firstParamEditor;
                    break;
                case LAST:
                    if (lastParamEditor != null)
                        paramEditComponentToFocus = lastParamEditor;
            }
        }

        //complete last row in grid with gaps
        completeGridRowWithGaps(grid, row, nextColumnStart, true);

        if (parentContainer != null) {
            parentContainer.add(grid);
        }

        if (level == 0)
            controlsLayout.setStyleName(getControlsLayoutStyleName());
    }

    protected List<Node<AbstractCondition>> fetchVisibleNodes(List<Node<AbstractCondition>> nodes) {
        List<Node<AbstractCondition>> visibleConditionNodes = new ArrayList<>();
        for (Node<AbstractCondition> node : nodes) {
            AbstractCondition condition = node.getData();
            if (!condition.getHidden())
                visibleConditionNodes.add(node);
        }
        return visibleConditionNodes;
    }

    protected Component createGroupConditionBox(AbstractCondition condition, Node<AbstractCondition> node, ConditionsFocusType conditionsFocusType, boolean focusSet, int level) {
        Component groupCellContent;
        GroupBoxLayout groupBox = componentsFactory.createComponent(GroupBoxLayout.class);
        groupBox.setStyleName("conditions-group");
        groupBox.setWidth("100%");
        groupBox.setCaption(condition.getLocCaption());

        if (!node.getChildren().isEmpty()) {
            recursivelyCreateConditionsLayout(conditionsFocusType, focusSet, node.getChildren(), groupBox, level);
        }
        groupCellContent = groupBox;
        return groupCellContent;
    }

    protected ParamEditor createParamEditor(final AbstractCondition condition) {
        boolean conditionRemoveEnabled = !initialConditions.contains(condition);
        ParamEditor paramEditor = new ParamEditor(condition, conditionRemoveEnabled, isParamEditorOperationEditable());
        AbstractAction removeConditionAction = new AbstractAction("") {
            @Override
            public void actionPerform(Component component) {
                conditions.removeCondition(condition);
                fillConditionsLayout(ConditionsFocusType.NONE);
                updateFilterModifiedIndicator();
            }
        };
        removeConditionAction.setVisible(conditionRemoveEnabled);
        paramEditor.setRemoveButtonAction(removeConditionAction);
        return paramEditor;
    }

    protected boolean isParamEditorOperationEditable() {
        return isEditable() && userCanEditFilers();
    }

    /**
     * Adds empty containers to grid row. If not to complete the row with gaps then in case of grid with one element (element width = 1)
     * this element will occupy 100% of grid width, but expected behaviour is to occupy 1/3 of grid width
     */
    protected void completeGridRowWithGaps(GridLayout grid, int row, int startColumn, boolean lastRow) {
        for (int i = startColumn * 2; i < grid.getColumns(); i++) {
            Component gap = componentsFactory.createComponent(Label.class);
            gap.setWidth("100%");
            grid.add(gap, i, row);
        }
    }

    protected String getControlsLayoutStyleName() {
        String styleName = "filter-control-no-border";
        if (conditionsLayout.isVisible() && !conditionsLayout.getComponents().isEmpty()) {
            styleName = CONDITIONS_LOCATION_TOP.equals(conditionsLocation) ? "filter-control-with-top-border"
                    : "filter-control-with-bottom-border";
        }
        return styleName;
    }

    protected boolean isFilterModified() {
        boolean filterPropertiesModified =
                !Objects.equals(initialFilterEntity.getName(), filterEntity.getName()) ||
                        !Objects.equals(initialFilterEntity.getCode(), filterEntity.getCode()) ||
                        !Objects.equals(initialFilterEntity.getUser(), filterEntity.getUser());
        if (filterPropertiesModified) return true;
        String filterXml = filterEntity.getFolder() == null ? filterParser.getXml(conditions, Param.ValueProperty.DEFAULT_VALUE)
                : filterParser.getXml(conditions, Param.ValueProperty.VALUE);
        return !Objects.equals(filterXml, initialFilterEntity.getXml());
    }

    protected void updateFilterModifiedIndicator() {
        boolean filterModified = isFilterModified();
        saveAction.setEnabled(filterSavingPossible && filterModified);
        saveWithValuesAction.setEnabled(filterSavingPossible);

        String currentCaption = filter.isBorderVisible() ? groupBoxLayout.getCaption() : filter.getCaption();

        if (StringUtils.isEmpty(currentCaption))
            return;

        if (filterModified && !currentCaption.endsWith(MODIFIED_INDICATOR_SYMBOL)) {
            captionChangedListener.accept(currentCaption + MODIFIED_INDICATOR_SYMBOL);
        }
        if (!filterModified && currentCaption.endsWith(MODIFIED_INDICATOR_SYMBOL)) {
            captionChangedListener.accept(currentCaption.substring(0, currentCaption.length() - 1));
        }
    }

    /**
     * Load filter entities from database and saves them in {@code filterEntities} collection.
     */
    protected void loadFilterEntities() {
        LoadContext<FilterEntity> ctx = LoadContext.create(FilterEntity.class);
        ctx.setView("app");
        ctx.setQueryString("select f from sec$Filter f left join f.user u " +
                "where f.componentId = :component and (u.id = :userId or u is null) order by f.name")
                .setParameter("component", ComponentsHelper.getFilterComponentPath(filter))
                .setParameter("userId", userSessionSource.getUserSession().getCurrentOrSubstitutedUser().getId());

        filterEntities = new ArrayList<>(dataService.loadList(ctx));
    }

    protected FilterEntity getDefaultFilter(List<FilterEntity> filters) {
        Window window = ComponentsHelper.getWindow(filter);

        // First check if there is parameter with name equal to this filter component id, containing a filter code to apply
        Map<String, Object> params = filter.getFrame().getContext().getParams();
        String code = (String) params.get(filter.getId());
        if (!StringUtils.isBlank(code)) {
            for (FilterEntity filter : filters) {
                if (code.equals(filter.getCode()))
                    return filter;
            }
        }

        // No 'filter' parameter found, load default filter
        SettingsImpl settings = new SettingsImpl(window.getId());

        String componentPath = ComponentsHelper.getFilterComponentPath(filter);
        String[] strings = ValuePathHelper.parse(componentPath);
        String name = ValuePathHelper.format((String[]) ArrayUtils.subarray(strings, 1, strings.length));

        Element e = settings.get(name).element("defaultFilter");
        if (e != null) {
            String defIdStr = e.attributeValue("id");
            Boolean applyDefault = Boolean.valueOf(e.attributeValue("applyDefault"));
            if (!StringUtils.isBlank(defIdStr)) {
                UUID defaultId = null;
                try {
                    defaultId = UUID.fromString(defIdStr);
                } catch (IllegalArgumentException ex) {
                    //
                }
                if (defaultId != null) {
                    for (FilterEntity filter : filters) {
                        if (defaultId.equals(filter.getId())) {
                            filter.setIsDefault(true);
                            filter.setApplyDefault(applyDefault);
                            return filter;
                        }
                    }
                }
            }
        }

        FilterEntity globalDefaultFilter = filters.stream()
                .filter(filterEntity -> Boolean.TRUE.equals(filterEntity.getGlobalDefault()))
                .findAny()
                .orElse(null);
        return globalDefaultFilter;
    }

    protected void initFiltersPopupButton() {
        filtersPopupButton.removeAllActions();
        addFiltersPopupActions();
    }

    protected void addFiltersPopupActions() {
        addResetFilterAction(filtersPopupButton);

        filterEntities.sort(Comparator.comparing(this::getFilterCaption));

        Iterator<FilterEntity> it = filterEntities.iterator();
        int addedEntitiesCount = 0;
        while (it.hasNext() && addedEntitiesCount < clientConfig.getGenericFilterPopupListSize()) {
            final FilterEntity fe = it.next();
            addSetFilterEntityAction(filtersPopupButton, fe);
            addedEntitiesCount++;
        }

        if (filterEntities.size() > clientConfig.getGenericFilterPopupListSize()) {
            addShowMoreFilterEntitiesAction(filtersPopupButton);
        }
    }

    protected void addSetFilterEntityAction(PopupButton popupButton, final FilterEntity fe) {
        popupButton.addAction(new AbstractAction("setEntity" + fe.getId()) {
            @Override
            public void actionPerform(Component component) {
                if (fe != filterEntity) {
                    setFilterEntity(fe);
                }
            }

            @Override
            public String getCaption() {
                return getFilterCaption(fe);
            }
        });
    }

    protected void addShowMoreFilterEntitiesAction(PopupButton popupButton) {
        popupButton.addAction(new AbstractAction("showMoreFilterEntities") {
            @Override
            public void actionPerform(Component component) {
                WindowInfo windowInfo = windowConfig.getWindowInfo("filterSelect");
                FilterSelectWindow window = (FilterSelectWindow) windowManager.openWindow(windowInfo,
                        WindowManager.OpenType.DIALOG,
                        ParamsMap.of("filterEntities", filterEntities));

                window.addCloseListener(actionId -> {
                    if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                        FilterEntity selectedEntity = window.getFilterEntity();
                        setFilterEntity(selectedEntity);
                    }
                });
            }

            @Override
            public String getCaption() {
                return formatMainMessage("filter.showMore", filterEntities.size());
            }
        });
    }

    protected void addResetFilterAction(PopupButton popupButton) {
        popupButton.addAction(new AbstractAction("resetFilter") {
            @Override
            public void actionPerform(Component component) {
                conditions = new ConditionsTree();
                setFilterEntity(adHocFilter);
            }

            @Override
            public String getCaption() {
                return getMainMessage("filter.resetFilter");
            }
        });
    }

    protected void initFiltersLookup() {
        Map<Object, String> captionsMap = new LinkedHashMap<>();
        for (FilterEntity entity : filterEntities) {
            String caption = getFilterCaption(entity);
            if (entity.getIsDefault()) {
                caption += " " + getMainMessage("filter.default");
            }
            captionsMap.put(entity, caption);
        }
        captionsMap.put(adHocFilter, getFilterCaption(adHocFilter));
        filtersLookupListenerEnabled = false;
        //set null to remove previous value from lookup options list
        filtersLookup.setValue(null);
        List<Object> optionsList = new ArrayList<>();
        optionsList.add(adHocFilter);
        optionsList.addAll(filterEntities);
        filtersLookup.setOptionsList(optionsList);
        filterHelper.setLookupCaptions(filtersLookup, captionsMap);
        filtersLookup.setValue(filterEntity);
        filtersLookupListenerEnabled = true;
    }

    protected void initFilterSelectComponents() {
        if (filtersPopupDisplayed) {
            initFiltersPopupButton();
        }
        if (filtersLookupDisplayed) {
            initFiltersLookup();
        }
    }

    protected void initAdHocFilter() {
        adHocFilter = metadata.create(FilterEntity.class);
        String emptyXml = filterParser.getXml(new ConditionsTree(), Param.ValueProperty.VALUE);
        adHocFilter.setXml(emptyXml);
        adHocFilter.setComponentId(ComponentsHelper.getFilterComponentPath(filter));
        adHocFilter.setUser(userSessionSource.getUserSession().getCurrentOrSubstitutedUser());
        adHocFilter.setName(getMainMessage("filter.adHocFilter"));
    }

    protected void addAppliedFilter() {
        if (lastAppliedFilter == null)
            return;

        if (!appliedFilters.isEmpty() && appliedFilters.getLast().filter.equals(lastAppliedFilter))
            return;

        this.layout.add(appliedFiltersLayout, CONDITIONS_LOCATION_TOP.equals(conditionsLocation) ? 0 : 1);

        BoxLayout layout = componentsFactory.createComponent(HBoxLayout.class);
        layout.setSpacing(true);

        if (!appliedFilters.isEmpty()) {
            AppliedFilterHolder holder = appliedFilters.getLast();
            holder.layout.remove(holder.button);
        }

        Label label = componentsFactory.createComponent(Label.class);
        label.setValue(lastAppliedFilter.getText());
        layout.add(label);
        label.setAlignment(Alignment.MIDDLE_LEFT);

        LinkButton button = componentsFactory.createComponent(LinkButton.class);
        button.setIcon("icons/item-remove.png");
        button.setAction(new AbstractAction("") {
            @Override
            public void actionPerform(Component component) {
                removeAppliedFilter();
            }
        });
        layout.add(button);

        addAppliedFilterLayoutHook(layout);
        appliedFiltersLayout.add(layout);

        appliedFilters.add(new AppliedFilterHolder(lastAppliedFilter, layout, button));
    }

    protected void addAppliedFilterLayoutHook(Component.Container layout) {
        //nothing
    }

    protected void removeAppliedFilter() {
        if (!appliedFilters.isEmpty()) {
            if (appliedFilters.size() == 1) {
                AppliedFilterHolder holder = appliedFilters.removeLast();
                appliedFiltersLayout.remove(holder.layout);
                ((CollectionDatasource.SupportsApplyToSelected) datasource).unpinAllQuery();
                this.layout.remove(appliedFiltersLayout);
            } else {
                windowManager.showOptionDialog(
                        messages.getMainMessage("removeApplied.title"),
                        messages.getMainMessage("removeApplied.message"),
                        MessageType.WARNING,
                        new Action[]{
                                new DialogAction(Type.YES).withHandler(event -> {
                                    for (AppliedFilterHolder holder : appliedFilters) {
                                        appliedFiltersLayout.remove(holder.layout);
                                        FilterDelegateImpl.this.layout.remove(appliedFiltersLayout);
                                    }
                                    appliedFilters.clear();
                                    ((CollectionDatasource.SupportsApplyToSelected) datasource).unpinAllQuery();
                                }),
                                new DialogAction(Type.NO, Status.PRIMARY)
                        });
            }
        }
    }

    protected String getFilterCaption(FilterEntity filterEntity) {
        String name;
        if (filterEntity != null) {
            if (filterEntity.getCode() == null)
                name = InstanceUtils.getInstanceName(filterEntity);
            else {
                name = messages.getMainMessage(filterEntity.getCode());
            }
            AbstractSearchFolder folder = filterEntity.getFolder();
            if (folder != null) {
                if (!StringUtils.isBlank(folder.getTabName()))
                    name = messages.getMainMessage(folder.getTabName());
                else if (!StringUtils.isBlank(folder.getName())) {
                    name = messages.getMainMessage(folder.getName());
                }
                if (BooleanUtils.isTrue(filterEntity.getIsSet()))
                    name = getMainMessage("filter.setPrefix") + " " + name;
                else
                    name = getMainMessage("filter.folderPrefix") + " " + name;
            }
        } else
            name = "";
        return name;
    }

    protected String formatMainMessage(String key, Object... params) {
        return messages.formatMainMessage(key, params);
    }

    protected String getMainMessage(String key) {
        return messages.getMainMessage(key);
    }

    @Override
    public Component.Container getLayout() {
        return groupBoxLayout;
    }

    @Override
    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;
        this.dsQueryFilter = datasource.getQueryFilter();

        if (getResultingManualApplyRequired()) {
            // set initial denying condition to get empty datasource before explicit filter applying
            QueryFilter queryFilter = new QueryFilter(new DenyingClause());
            if (dsQueryFilter != null) {
                queryFilter = QueryFilter.merge(dsQueryFilter, queryFilter);
            }
            datasource.setQueryFilter(queryFilter);
        }

        if (datasource instanceof CollectionDatasource.Lazy || datasource instanceof HierarchicalDatasource) {
            setUseMaxResults(false);
        } else if (useMaxResults) {
            initMaxResults();
        }

        if (ftsSwitch != null && !isEntityAvailableForFts()) {
            controlsLayout.remove(ftsSwitch);
        }
    }

    @Override
    public CollectionDatasource getDatasource() {
        return datasource;
    }

    protected void initMaxResults() {
        int maxResults;
        if (this.maxResults != -1) {
            maxResults = this.maxResults;
        } else {
            maxResults = datasource.getMaxResults();
        }

        if (maxResults == 0 || maxResults == persistenceManager.getMaxFetchUI(datasource.getMetaClass().getName())) {
            maxResults = persistenceManager.getFetchUI(datasource.getMetaClass().getName());
        }

        if (maxResultsAddedToLayout) {
            if (!textMaxResults) {
                List<Integer> optionsList = ((LookupField) maxResultsField).getOptionsList();
                if (!optionsList.contains(maxResults)) {
                    maxResults = findClosestValue(maxResults, optionsList);

                    Collections.sort(optionsList);
                    ((LookupField) maxResultsField).setOptionsList(optionsList);
                }
            }
            maxResultsField.setValue(maxResults);
        }

        datasource.setMaxResults(maxResults);
    }

    protected int findClosestValue(int maxResults, List<Integer> optionsList) {
        int minimumValue = Integer.MAX_VALUE;
        int closest = maxResults;

        for (int option : optionsList) {
            int diff = Math.abs(option - maxResults);
            if (diff < minimumValue) {
                minimumValue = diff;
                closest = option;
            }
        }

        return closest;
    }

    protected boolean isFtsModeEnabled() {
        return FtsConfigHelper.getEnabled();
    }

    protected boolean isEntityAvailableForFts() {
        return datasource != null
                && ftsFilterHelper != null
                && ftsFilterHelper.isEntityIndexed(datasource.getMetaClass().getName())
                && Stores.isMain(metadata.getTools().getStoreName(datasource.getMetaClass()));
    }

    @Override
    public int getMaxResults() {
        return maxResults;
    }

    @Override
    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        initMaxResults();
    }

    @Override
    public void setUseMaxResults(boolean useMaxResults) {
        this.useMaxResults = useMaxResults;
        if (maxResultsLayout != null)
            maxResultsLayout.setVisible(isMaxResultsLayoutVisible());
    }

    protected boolean isMaxResultsLayoutVisible() {
        return useMaxResults && security.isSpecificPermitted("cuba.gui.filter.maxResults") && maxResultsAddedToLayout;
    }

    @Override
    public boolean getUseMaxResults() {
        return useMaxResults;
    }

    @Override
    public void setTextMaxResults(boolean textMaxResults) {
        boolean valueChanged = this.textMaxResults != textMaxResults;
        this.textMaxResults = textMaxResults;
        if (maxResultsAddedToLayout && valueChanged) {
            maxResultsLayout.remove(maxResultsField);
            maxResultsField = textMaxResults ? maxResultsTextField : maxResultsLookupField;
            maxResultsLayout.add(maxResultsField);
        }
    }

    @Override
    public boolean getTextMaxResults() {
        return textMaxResults;
    }

    @Override
    public boolean apply(boolean notifyInvalidConditions) {
        if (beforeFilterAppliedHandler != null) {
            if (!beforeFilterAppliedHandler.beforeFilterApplied()) return false;
        }
        if (clientConfig.getGenericFilterChecking()) {
            if (filterEntity != null && conditions.getRoots().size() > 0) {
                boolean haveCorrectCondition = hasCorrectCondition();
                if (!haveCorrectCondition) {
                    if (!notifyInvalidConditions) {
                        windowManager.showNotification(messages.getMainMessage("filter.emptyConditions"),
                                Frame.NotificationType.HUMANIZED);
                    }
                    return false;
                }
            }
        }

        if (filterEntity != null) {
            lastAppliedFilter = new AppliedFilter(filterEntity, conditions);
        } else {
            lastAppliedFilter = null;
        }

        if (filterEntity != null) {
            boolean haveRequiredConditions = haveFilledRequiredConditions();
            if (!haveRequiredConditions) {
                if (!notifyInvalidConditions) {
                    windowManager.showNotification(messages.getMainMessage("filter.emptyRequiredConditions"),
                            Frame.NotificationType.HUMANIZED);
                }
                return false;
            }
            setFilterActionsEnabled();
        }

        applyDatasourceFilter();
        initDatasourceMaxResults();

        Map<String, Object> parameters = prepareDatasourceCustomParams();
        refreshDatasource(parameters);

        if ((applyTo != null) && (Table.class.isAssignableFrom(applyTo.getClass()))) {
            filterHelper.removeTableFtsTooltips((Table) applyTo);
        }

        if (afterFilterAppliedHandler != null) {
            afterFilterAppliedHandler.afterFilterApplied();
        }
        return true;
    }

    protected Map<String, Object> prepareDatasourceCustomParams() {
        Map<String, Object> lastRefreshParameters = new HashMap<>(datasource.getLastRefreshParameters());
        for (String paramName : ftsLastDatasourceRefreshParamsNames) {
            lastRefreshParameters.remove(paramName);
        }
        List<FtsCondition> ftsConditions = conditions.toConditionsList().stream()
                .filter(abstractCondition -> abstractCondition instanceof FtsCondition)
                .map(abstractCondition -> (FtsCondition) abstractCondition)
                .collect(Collectors.toList());

        for (FtsCondition ftsCondition : ftsConditions) {
            String searchTerm = (String) ftsCondition.getParam().getValue();
            if (!Strings.isNullOrEmpty(searchTerm)) {
                FtsFilterHelper.FtsSearchResult ftsSearchResult = ftsFilterHelper.search(searchTerm, datasource.getMetaClass().getName());
                int queryKey = ftsSearchResult.getQueryKey();

                lastRefreshParameters.put(ftsCondition.getSessionIdParamName(), userSessionSource.getUserSession().getId());
                lastRefreshParameters.put(ftsCondition.getQueryKeyParamName(), queryKey);

                ftsLastDatasourceRefreshParamsNames.add(ftsCondition.getSessionIdParamName());
                ftsLastDatasourceRefreshParamsNames.add(ftsCondition.getQueryKeyParamName());
            }
        }
        return lastRefreshParameters;
    }

    protected void applyFts() {
        if (ftsFilterHelper == null)
            return;

        if (beforeFilterAppliedHandler != null) {
            if (!beforeFilterAppliedHandler.beforeFilterApplied()) return;
        }

        String searchTerm = ftsSearchCriteriaField.getValue();
        if (Strings.isNullOrEmpty(searchTerm) && clientConfig.getGenericFilterChecking()) {
            windowManager.showNotification(getMainMessage("filter.fillSearchCondition"), Frame.NotificationType.TRAY);
            return;
        }

        Map<String, Object> params = new HashMap<>();

        if (!Strings.isNullOrEmpty(searchTerm)) {
            FtsFilterHelper.FtsSearchResult ftsSearchResult = ftsFilterHelper.search(searchTerm, datasource.getMetaClass().getName());
            int queryKey = ftsSearchResult.getQueryKey();
            params.put(FtsFilterHelper.SESSION_ID_PARAM_NAME, userSessionSource.getUserSession().getId());
            params.put(FtsFilterHelper.QUERY_KEY_PARAM_NAME, queryKey);

            CustomCondition ftsCondition = ftsFilterHelper.createFtsCondition(datasource.getMetaClass().getName());
            conditions = new ConditionsTree();
            conditions.getRootNodes().add(new Node<>(ftsCondition));

            if ((applyTo != null) && (Table.class.isAssignableFrom(applyTo.getClass()))) {
                filterHelper.initTableFtsTooltips((Table) applyTo, ftsSearchResult.getHitInfos());
            }
        } else if ((applyTo != null) && (Table.class.isAssignableFrom(applyTo.getClass()))) {
            filterHelper.initTableFtsTooltips((Table) applyTo, Collections.emptyMap());
        }

        applyDatasourceFilter();
        initDatasourceMaxResults();
        datasource.refresh(params);

        if (afterFilterAppliedHandler != null) {
            afterFilterAppliedHandler.afterFilterApplied();
        }
    }

    /**
     * The method is invoked before search. It sets datasource {@code maxResults} value based on the maxResults
     * field (if visible) or on maxFetchUI value.
     * Method also resets the datasource {@code firstResult} value
     */
    protected void initDatasourceMaxResults() {
        if (datasource == null) {
            throw new DevelopmentException("Filter datasource is not set");
        }
        if (this.maxResults != -1) {
            datasource.setMaxResults(maxResults);
        } else if (maxResultsAddedToLayout && useMaxResults) {
            Integer maxResults = maxResultsField.getValue();
            if (maxResults != null && maxResults > 0) {
                datasource.setMaxResults(maxResults);
            } else {
                datasource.setMaxResults(persistenceManager.getFetchUI(datasource.getMetaClass().getName()));
            }
        }
        if (datasource instanceof CollectionDatasource.SupportsPaging) {
            ((CollectionDatasource.SupportsPaging) datasource).setFirstResult(0);
        }
    }

    protected void applyDatasourceFilter() {
        if (datasource != null) {

            String currentFilterXml = filterParser.getXml(conditions, Param.ValueProperty.VALUE);

            if (!Strings.isNullOrEmpty(currentFilterXml)) {
                Element element = Dom4j.readDocument(currentFilterXml).getRootElement();
                QueryFilter queryFilter = new QueryFilter(element);

                if (dsQueryFilter != null) {
                    queryFilter = QueryFilter.merge(dsQueryFilter, queryFilter);
                }

                datasource.setQueryFilter(queryFilter);
            } else {
                datasource.setQueryFilter(dsQueryFilter);
            }
        } else {
            log.warn("Unable to apply datasource filter with null datasource");
        }
    }

    protected boolean haveFilledRequiredConditions() {
        for (AbstractCondition condition : conditions.toConditionsList()) {
            if ((condition.getRequired())
                    && (condition.getParam() != null)
                    && (condition.getParam().getValue() == null)) {
                return false;
            }
        }
        return true;
    }

    protected boolean hasCorrectCondition() {
        boolean haveCorrectCondition = false;

        for (AbstractCondition condition : conditions.toConditionsList()) {
            if ((condition.getParam() != null) && (condition.getParam().getValue() != null)
                    || condition instanceof CustomCondition && condition.getHidden()) {
                haveCorrectCondition = true;
                break;
            }
        }
        return haveCorrectCondition;
    }

    /**
     * extenders should be able to modify the datasource
     * before it will be refreshed
     */
    protected void refreshDatasource(Map<String, Object> parameters) {
        if (datasource instanceof CollectionDatasource.Suspendable)
            ((CollectionDatasource.Suspendable) datasource).refreshIfNotSuspended(parameters);
        else
            datasource.refresh(parameters);
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String getIcon() {
        return groupBoxLayout.getIcon();
    }

    @Override
    public void setIcon(String icon) {
        groupBoxLayout.setIcon(icon);
    }

    @Override
    public void setManualApplyRequired(Boolean manualApplyRequired) {
        this.manualApplyRequired = manualApplyRequired;
    }

    @Override
    public Boolean getManualApplyRequired() {
        return manualApplyRequired;
    }

    protected boolean getResultingManualApplyRequired() {
        return manualApplyRequired != null ? manualApplyRequired : clientConfig.getGenericFilterManualApplyRequired();
    }

    @Override
    public Component getOwnComponent(String id) {
        for (AbstractCondition condition : conditions.toConditionsList()) {
            if (condition.getParam() != null) {
                String paramName = condition.getParam().getName();

                String componentName = paramName.substring(paramName.lastIndexOf('.') + 1);
                if (id.equals(componentName)) {
                    ParamWrapper wrapper = new ParamWrapper(condition, condition.getParam());
                    return wrapper;
                }
            }
        }
        return null;
    }

    @Override
    @Nullable
    public Component getComponent(String id) {
        String[] elements = ValuePathHelper.parse(id);
        if (elements.length == 1) {
            return getOwnComponent(id);
        } else {
            throw new UnsupportedOperationException("Filter contains only one level of sub components");
        }
    }

    @Override
    public void applySettings(Element element) {
        Element groupBoxExpandedEl = element.element("groupBoxExpanded");
        if (groupBoxExpandedEl != null) {
            Boolean expanded = Boolean.valueOf(groupBoxExpandedEl.getText());
            groupBoxLayout.setExpanded(expanded);
        }

        Element maxResultsEl = element.element("maxResults");
        if (maxResultsEl != null && isMaxResultsLayoutVisible()) {
            try {
                Integer maxResultsFromSettings = Integer.valueOf(maxResultsEl.getText());
                datasource.setMaxResults(maxResultsFromSettings);
                initMaxResults();
            } catch (NumberFormatException ex) {
                log.error("Error on parsing maxResults setting value", ex);
            }
        }
    }

    @Override
    public boolean saveSettings(Element element) {
        Boolean changed = false;
        Element e = element.element("defaultFilter");
        if (e == null)
            e = element.addElement("defaultFilter");

        UUID defaultId = null;
        Boolean applyDefault = false;

        for (FilterEntity filter : filterEntities) {
            if (BooleanUtils.isTrue(filter.getIsDefault())) {
                defaultId = filter.getId();
                applyDefault = filter.getApplyDefault();
                break;
            }
        }

        String newDef = defaultId != null ? defaultId.toString() : null;
        Attribute attr = e.attribute("id");
        String oldDef = attr != null ? attr.getValue() : null;
        if (!Objects.equals(oldDef, newDef)) {
            if (newDef == null && attr != null) {
                e.remove(attr);
            } else {
                if (attr == null)
                    e.addAttribute("id", newDef);
                else
                    attr.setValue(newDef);
            }
            changed = true;
        }
        Boolean newApplyDef = BooleanUtils.isTrue(applyDefault);
        Attribute applyDefaultAttr = e.attribute("applyDefault");
        Boolean oldApplyDef = applyDefaultAttr != null ? Boolean.valueOf(applyDefaultAttr.getValue()) : false;
        if (!Objects.equals(oldApplyDef, newApplyDef)) {
            if (applyDefaultAttr != null) {
                applyDefaultAttr.setValue(newApplyDef.toString());
            } else {
                e.addAttribute("applyDefault", newApplyDef.toString());
            }
            changed = true;
        }

        Element groupBoxExpandedEl = element.element("groupBoxExpanded");
        if (groupBoxExpandedEl == null)
            groupBoxExpandedEl = element.addElement("groupBoxExpanded");

        Boolean oldGroupBoxExpandedValue = Boolean.valueOf(groupBoxExpandedEl.getText());
        Boolean newGroupBoxExpandedValue = groupBoxLayout.isExpanded();
        if (!Objects.equals(oldGroupBoxExpandedValue, newGroupBoxExpandedValue)) {
            groupBoxExpandedEl.setText(newGroupBoxExpandedValue.toString());
            changed = true;
        }

        if (isMaxResultsLayoutVisible()) {
            Element maxResultsEl = element.element("maxResults");
            if (maxResultsEl == null)
                maxResultsEl = element.addElement("maxResults");
            try {
                Integer oldMaxResultsValue = !Strings.isNullOrEmpty(maxResultsEl.getText()) ?
                        Integer.valueOf(maxResultsEl.getText()) : null;
                Integer newMaxResultsValue = maxResultsField.getValue();
                if (newMaxResultsValue != null && !Objects.equals(oldMaxResultsValue, newMaxResultsValue)) {
                    maxResultsEl.setText(newMaxResultsValue.toString());
                    changed = true;
                }
            } catch (NumberFormatException ex) {
                log.error("Error on parsing maxResults setting value", ex);
            }
        }

        return changed;
    }

    @Override
    public Component getApplyTo() {
        return applyTo;
    }

    @Override
    public void setApplyTo(Component applyTo) {
        this.applyTo = applyTo;
    }

    @Override
    public void setFolderActionsEnabled(boolean folderActionsEnabled) {
        this.folderActionsEnabled = folderActionsEnabled;
    }

    @Override
    public boolean isFolderActionsEnabled() {
        return folderActionsEnabled;
    }

    /**
     * Adds actions of 'Entities Set' functionality to Table component
     */
    protected void fillTableActions() {
        Table table;
        if ((applyTo != null) && (Table.class.isAssignableFrom(applyTo.getClass()))) {
            table = (Table) applyTo;
        } else {
            return;
        }
        ButtonsPanel buttons = table.getButtonsPanel();
        if (buttons == null) {
            return; // in lookup windows, there is no button panel
        }
        com.haulmont.cuba.gui.components.Button addToSetBtn = (Button) buttons.getComponent("addToSetBtn");
        com.haulmont.cuba.gui.components.Button addToCurSetBtn = (Button) buttons.getComponent("addToCurSetBtn");
        com.haulmont.cuba.gui.components.Button removeFromCurSetBtn = (Button) buttons.getComponent("removeFromCurSetBtn");

        Action addToSet = table.getAction("filter.addToSet");

        Action addToCurrSet = table.getAction("filter.addToCurSet");
        Action removeFromCurrSet = table.getAction("filter.removeFromCurSet");

        if (addToSet != null)
            table.removeAction(addToSet);
        if (addToSetBtn != null)
            addToSetBtn.setVisible(false);
        if (addToCurrSet != null) {
            table.removeAction(addToCurrSet);
        }
        if (addToCurSetBtn != null) {
            addToCurSetBtn.setVisible(false);
        }
        if (removeFromCurrSet != null) {
            table.removeAction(removeFromCurrSet);
        }
        if (removeFromCurSetBtn != null) {
            removeFromCurSetBtn.setVisible(false);
        }
        if ((filterEntity != null) && (BooleanUtils.isTrue(filterEntity.getIsSet()))) {
            addToCurrSet = new AddToCurrSetAction();

            if (addToCurSetBtn == null) {
                addToCurSetBtn = componentsFactory.createComponent(Button.class);
                addToCurSetBtn.setId("addToCurSetBtn");
                addToCurSetBtn.setCaption(getMainMessage("filter.addToCurSet"));
                buttons.add(addToCurSetBtn);
            } else {
                addToCurSetBtn.setVisible(true);
            }
            if (StringUtils.isEmpty(addToCurSetBtn.getIcon())) {
                addToCurSetBtn.setIcon("icons/join-to-set.png");
            }
            addToCurSetBtn.setAction(addToCurrSet);
            table.addAction(addToCurrSet);

            removeFromCurrSet = new RemoveFromSetAction(table);
            if (removeFromCurSetBtn == null) {
                removeFromCurSetBtn = componentsFactory.createComponent(Button.class);
                removeFromCurSetBtn.setId("removeFromCurSetBtn");
                removeFromCurSetBtn.setCaption(getMainMessage("filter.removeFromCurSet"));
                buttons.add(removeFromCurSetBtn);
            } else {
                removeFromCurSetBtn.setVisible(true);
            }
            if (StringUtils.isEmpty(removeFromCurSetBtn.getIcon())) {
                removeFromCurSetBtn.setIcon("icons/delete-from-set.png");
            }
            removeFromCurSetBtn.setAction(removeFromCurrSet);

            table.addAction(removeFromCurrSet);
        } else {
            addToSet = new AddToSetAction(table);
            if (addToSetBtn == null) {
                addToSetBtn = componentsFactory.createComponent(Button.class);
                addToSetBtn.setId("addToSetBtn");
                addToSetBtn.setCaption(getMainMessage("filter.addToSet"));
                buttons.add(addToSetBtn);
            } else {
                addToSetBtn.setVisible(true);
            }
            if (StringUtils.isEmpty(addToSetBtn.getIcon())) {
                addToSetBtn.setIcon("icons/insert-to-set.png");
            }
            addToSetBtn.setAction(addToSet);
            table.addAction(addToSet);
        }
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
        addConditionBtn.setVisible(editable && userCanEditFilers());
        //do not process actions if method is invoked from filter loader
        if (filterActionsCreated && filterEntity != null) {
            setFilterActionsEnabled();
            setFilterActionsVisible();
        }
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public Object getParamValue(String paramName) {
        Component component = getOwnComponent(paramName);
        if (component instanceof Component.HasValue) {
            return ((Component.HasValue) component).getValue();
        }
        return null;
    }

    @Override
    public void setParamValue(String paramName, Object value) {
        Component component = getOwnComponent(paramName);
        if (component instanceof Component.HasValue) {
            ((Component.HasValue) component).setValue(value);
        }
    }

    @Override
    public void addFilterEntityChangeListener(Filter.FilterEntityChangeListener listener) {
        filterEntityChangeListeners.add(listener);
    }

    @Override
    public List<Filter.FilterEntityChangeListener> getFilterEntityChangeListeners() {
        return filterEntityChangeListeners;
    }

    @Override
    public Integer getColumnsCount() {
        return columnsCount != null ? columnsCount : clientConfig.getGenericFilterColumnsCount();
    }

    @Override
    public void setColumnsCount(int columnsCount) {
        this.columnsCount = columnsCount;
    }

    @Override
    public boolean isExpanded() {
        return groupBoxLayout.isExpanded();
    }

    @Override
    public void setExpanded(boolean expanded) {
        groupBoxLayout.setExpanded(expanded);
    }

    @Override
    public boolean isCollapsable() {
        return groupBoxLayout.isCollapsable();
    }

    @Override
    public void setCollapsable(boolean collapsable) {
        groupBoxLayout.setCollapsable(collapsable);
    }

    @Override
    public void setModeSwitchVisible(boolean modeSwitchVisible) {
        this.modeSwitchVisible = modeSwitchVisible;
        if (ftsSwitch != null) {
            ftsSwitch.setVisible(modeSwitchVisible && isFtsModeEnabled() && isEntityAvailableForFts());
        }
    }

    @Override
    public void requestFocus() {
        if (filterEntity == null) {
            delayedFocus = true;
            return;
        }
        if (paramEditComponentToFocus != null) {
            requestFocusToParamEditComponent();
        } else if (filtersLookupDisplayed) {
            filtersLookup.requestFocus();
        } else if (filtersPopupDisplayed) {
            filtersPopupButton.requestFocus();
        }
    }

    protected void requestFocusToParamEditComponent() {
        if (paramEditComponentToFocus instanceof ParamEditor) {
            ((ParamEditor) paramEditComponentToFocus).requestFocus();
        } else if (paramEditComponentToFocus instanceof TextField) {
            ((TextField) paramEditComponentToFocus).requestFocus();
        }
    }

    @Override
    public void addExpandedStateChangeListener(FDExpandedStateChangeListener listener) {
        if (expandedStateChangeListeners == null) {
            expandedStateChangeListeners = new ArrayList<>();
        }
        if (!expandedStateChangeListeners.contains(listener)) {
            expandedStateChangeListeners.add(listener);
        }
    }

    @Override
    public void removeExpandedStateChangeListener(FDExpandedStateChangeListener listener) {
        if (expandedStateChangeListeners != null) {
            expandedStateChangeListeners.remove(listener);
        }
    }

    protected void fireExpandStateChange() {
        if (expandedStateChangeListeners != null) {
            FDExpandedStateChangeEvent event = new FDExpandedStateChangeEvent(this, isExpanded());

            for (FDExpandedStateChangeListener listener : expandedStateChangeListeners) {
                listener.expandedStateChanged(event);
            }
        }
    }

    @Override
    public void setFilter(Filter filter) {
        this.filter = filter;
        addConditionHelper = new AddConditionHelper(filter, new AddConditionHelper.Handler() {
            @Override
            public void handle(AbstractCondition condition) {
                try {
                    addCondition(condition);
                } catch (Exception e) {
                    conditions.removeCondition(condition);
                    throw e;
                }
            }
        });
    }

    protected void addCondition(AbstractCondition condition) {
        conditions.getRootNodes().add(new Node<>(condition));
        fillConditionsLayout(ConditionsFocusType.LAST);
        requestFocusToParamEditComponent();
        updateFilterModifiedIndicator();
        condition.addListener(new AbstractCondition.Listener() {
            @Override
            public void captionChanged() {
            }

            @Override
            public void paramChanged(Param oldParam, Param newParam) {
                updateFilterModifiedIndicator();
            }
        });
    }

    protected void initShortcutActions() {
        if (filter.getFrame().getAction(Filter.APPLY_ACTION_ID) == null) {
            filter.getFrame().addAction(new AbstractAction(Filter.APPLY_ACTION_ID, clientConfig.getFilterApplyShortcut()) {
                @Override
                public void actionPerform(Component component) {
                    if (isVisible() && datasource != null) {
                        if (filterMode == FilterMode.GENERIC_MODE) {
                            apply(false);
                        } else {
                            applyFts();
                        }
                    }
                }
            });
        }

        if (filter.getFrame().getAction(Filter.SELECT_ACTION_ID) == null) {
            filter.getFrame().addAction(new AbstractAction(Filter.SELECT_ACTION_ID, clientConfig.getFilterSelectShortcut()) {
                @Override
                public void actionPerform(Component component) {
                    if (isVisible() && datasource != null && filtersPopupButton.isEnabled()) {
                        filtersPopupButton.setPopupVisible(true);
                    }
                }
            });
        }
    }

    protected void updateWindowCaption() {
        Window window = ComponentsHelper.getWindow(filter);
        String filterTitle;
        if (filterMode == FilterMode.GENERIC_MODE && filterEntity != null && filterEntity != adHocFilter) {
            filterTitle = getFilterCaption(filterEntity);
        } else {
            filterTitle = null;
        }
        window.setDescription(filterTitle);

        if (initialWindowCaption == null) {
            initialWindowCaption = window.getCaption();
        }

        windowManager.setWindowCaption(window, initialWindowCaption, filterTitle);

        String newCaption = Strings.isNullOrEmpty(filterTitle) ? caption : caption + ": " + filterTitle;
        captionChangedListener.accept(newCaption);
    }

    protected ControlsLayoutBuilder createControlsLayoutBuilder(String layoutDescription) {
        return new ControlsLayoutBuilder(layoutDescription);
    }

    /**
     * Method sets default = false to all filters except the current one
     */
    protected void resetDefaultFilters() {
        for (FilterEntity filter : filterEntities) {
            if (!Objects.equals(filter, filterEntity)) {
                if (BooleanUtils.isTrue(filter.getIsDefault())) {
                    filter.setIsDefault(false);
                }
            }
        }
    }

    @Override
    public Filter.BeforeFilterAppliedHandler getBeforeFilterAppliedHandler() {
        return beforeFilterAppliedHandler;
    }

    @Override
    public void setBeforeFilterAppliedHandler(Filter.BeforeFilterAppliedHandler beforeFilterAppliedHandler) {
        this.beforeFilterAppliedHandler = beforeFilterAppliedHandler;
    }

    @Override
    public Filter.AfterFilterAppliedHandler getAfterFilterAppliedHandler() {
        return afterFilterAppliedHandler;
    }

    @Override
    public void setAfterFilterAppliedHandler(Filter.AfterFilterAppliedHandler afterFilterAppliedHandler) {
        this.afterFilterAppliedHandler = afterFilterAppliedHandler;
    }

    @Override
    public void setCaptionChangedListener(Consumer<String> captionChangedListener) {
        this.captionChangedListener = captionChangedListener;
    }

    protected class FiltersLookupChangeListener implements Component.ValueChangeListener {
        public FiltersLookupChangeListener() {
        }

        @Override
        public void valueChanged(Component.ValueChangeEvent e) {
            if (!filtersLookupListenerEnabled) return;
            if (e.getValue() instanceof FilterEntity) {
                setFilterEntity((FilterEntity) e.getValue());
            }
        }
    }

    protected class SaveAction extends AbstractAction {

        private final boolean saveWithValues;

        public SaveAction(String id, boolean saveWithValues, String caption) {
            super(id);
            this.saveWithValues = saveWithValues;
            this.caption = caption;
        }

        @Override
        public void actionPerform(Component component) {
            if (PersistenceHelper.isNew(filterEntity) && filterEntity.getFolder() == null) {
                WindowInfo windowInfo = windowConfig.getWindowInfo("saveFilter");
                Map<String, Object> params = new HashMap<>();
                if (!getMainMessage("filter.adHocFilter").equals(filterEntity.getName())) {
                    params.put("filterName", filterEntity.getName());
                }
                final SaveFilterWindow window = (SaveFilterWindow) windowManager.openWindow(windowInfo, WindowManager.OpenType.DIALOG, params);
                window.addCloseListener(actionId -> {
                    if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                        String filterName = window.getFilterName();
                        filterEntity.setName(filterName);

                        if (saveWithValues) {
                            conditions.toConditionsList().forEach(condition -> {
                                condition.getParam().setDefaultValue(condition.getParam().getValue());
                            });
                        }

                        filterEntity.setXml(filterParser.getXml(conditions, Param.ValueProperty.DEFAULT_VALUE));
                        saveFilterEntity();
                        initAdHocFilter();
                        initFilterSelectComponents();
                        updateWindowCaption();
                        //recreate layout to remove delete conditions buttons
                        initialConditions = conditions.toConditionsList();
                        fillConditionsLayout(ConditionsFocusType.NONE);
                    }
                    settingsBtn.requestFocus();
                });
            } else {
                if (saveWithValues) {
                    conditions.toConditionsList().forEach(condition -> {
                        condition.getParam().setDefaultValue(condition.getParam().getValue());
                    });
                }

                String xml = filterEntity.getFolder() == null ? filterParser.getXml(conditions, Param.ValueProperty.DEFAULT_VALUE)
                        : filterParser.getXml(conditions, Param.ValueProperty.VALUE);
                filterEntity.setXml(xml);
                saveFilterEntity();
                initFilterSelectComponents();
            }
        }

        @Override
        public String getIcon() {
            return "icons/save.png";
        }
    }

    protected class SaveAsAction extends AbstractAction {

        protected SaveAsAction() {
            super("saveAs");
        }

        @Override
        public void actionPerform(Component component) {
            WindowInfo windowInfo = windowConfig.getWindowInfo("saveFilter");
            final SaveFilterWindow window = (SaveFilterWindow) windowManager.openWindow(windowInfo, WindowManager.OpenType.DIALOG);
            window.addCloseListener(actionId -> {
                if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                    String filterName = window.getFilterName();
                    FilterEntity newFilterEntity = metadata.create(FilterEntity.class);
                    metadata.getTools().copy(filterEntity, newFilterEntity);
                    newFilterEntity.setCode(null);
                    newFilterEntity.setId(UuidProvider.createUuid());
                    newFilterEntity.setGlobalDefault(false);
                    //if filter was global but current user cannot create global filter then new filter
                    //will be connected with current user
                    if (newFilterEntity.getUser() == null && !uerCanEditGlobalFilter()) {
                        newFilterEntity.setUser(userSessionSource.getUserSession().getCurrentOrSubstitutedUser());
                    }
                    String xml = filterEntity.getFolder() == null ? filterParser.getXml(conditions, Param.ValueProperty.DEFAULT_VALUE)
                            : filterParser.getXml(conditions, Param.ValueProperty.VALUE);
                    filterEntity = newFilterEntity;
                    filterEntity.setName(filterName);
                    filterEntity.setXml(xml);
                    saveFilterEntity();
                    initFilterSelectComponents();
                    updateWindowCaption();

                    //recreate layout to remove delete conditions buttons
                    initialConditions = conditions.toConditionsList();
                    fillConditionsLayout(ConditionsFocusType.NONE);
                }
                settingsBtn.requestFocus();
            });
        }

        @Override
        public String getCaption() {
            return getMainMessage("filter.saveAs");
        }
    }

    protected class EditAction extends AbstractAction {

        protected EditAction() {
            super("edit");
        }

        @Override
        public void actionPerform(Component component) {
            WindowInfo windowInfo = windowConfig.getWindowInfo("filterEditor");
            Map<String, Object> params = new HashMap<>();
            params.put("filterEntity", filterEntity);
            params.put("filter", filter);
            params.put("conditions", conditions);

            FilterEditor window = (FilterEditor) windowManager.openWindow(windowInfo, WindowManager.OpenType.DIALOG, params);
            window.addCloseListener(actionId -> {
                if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                    conditions = window.getConditions();
                    filterEntity.setXml(filterParser.getXml(conditions, Param.ValueProperty.DEFAULT_VALUE));
                    if (filterEntity.getIsDefault()) {
                        resetDefaultFilters();
                    }
                    saveFilterEntity();
                    initAdHocFilter();
                    Set<FilterEntity> modifiedGlobalDefaultFilters = window.getModifiedGlobalDefaultFilters();
                    for (FilterEntity modifiedGlobalDefaultFilter : modifiedGlobalDefaultFilters) {
                        if (filterEntities.contains(modifiedGlobalDefaultFilter)) {
                            filterEntities.set(filterEntities.indexOf(modifiedGlobalDefaultFilter), modifiedGlobalDefaultFilter);
                        }
                    }
                    initFilterSelectComponents();
                    updateWindowCaption();
                    fillConditionsLayout(ConditionsFocusType.FIRST);
                    requestFocusToParamEditComponent();
                    updateFilterModifiedIndicator();
                } else {
                    requestFocusToParamEditComponent();
                }
                settingsBtn.requestFocus();
            });
        }

        @Override
        public String getCaption() {
            return getMainMessage("filter.edit");
        }

        @Override
        public String getIcon() {
            return "icons/edit.png";
        }
    }

    protected class MakeDefaultAction extends AbstractAction {

        public MakeDefaultAction() {
            super("filter.makeDefault");
        }

        @Override
        public void actionPerform(Component component) {
            setDefaultFilter();
        }

        protected void setDefaultFilter() {
            if (filterEntity != null) {
                filterEntity.setIsDefault(true);
            }
            resetDefaultFilters();
            initFilterSelectComponents();
            setFilterActionsEnabled();
        }

    }

    protected class RemoveAction extends AbstractAction {
        public RemoveAction() {
            super("remove");
        }

        @Override
        public void actionPerform(Component component) {
            if (filterEntity == adHocFilter) return;
            windowManager.showOptionDialog(
                    getMainMessage("filter.removeDialogTitle"),
                    getMainMessage("filter.removeDialogMessage"),
                    MessageType.CONFIRMATION,
                    new Action[]{
                            new DialogAction(Type.YES).withHandler(event -> {
                                removeFilterEntity();
                                settingsBtn.requestFocus();
                            }),
                            new DialogAction(Type.NO, Status.PRIMARY).withHandler(event -> {
                                settingsBtn.requestFocus();
                            })
                    });
        }

        @Override
        public String getCaption() {
            return getMainMessage("filter.remove");
        }

        @Override
        public String getIcon() {
            return "icons/remove.png";
        }
    }

    /**
     * Action clears values of all visible filter conditions
     */
    protected class ClearValuesAction extends AbstractAction {

        protected ClearValuesAction() {
            super("filter.clearValues");
        }

        @Override
        public void actionPerform(Component component) {
            for (AbstractCondition condition : conditions.toConditionsList()) {
                if (!Boolean.TRUE.equals(condition.getHidden()) && condition.getParam() != null) {
                    condition.getParam().setValue(null);
                }
            }
        }

        @Override
        public String getCaption() {
            return getMainMessage("filter.clearValues");
        }

        @Override
        public String getIcon() {
            return "icons/erase.png";
        }
    }

    protected void removeFilterEntity() {
        CommitContext ctx = new CommitContext(Collections.emptyList(), Collections.singletonList(filterEntity));
        dataService.commit(ctx);
        filterEntities.remove(filterEntity);
        setFilterEntity(adHocFilter);
        initFilterSelectComponents();
        updateWindowCaption();
    }

    protected class PinAppliedAction extends AbstractAction {

        public PinAppliedAction() {
            super("pinApplied");
        }

        @Override
        public void actionPerform(Component component) {
            if (datasource instanceof CollectionDatasource.SupportsApplyToSelected) {
                ((CollectionDatasource.SupportsApplyToSelected) datasource).pinQuery();
                addAppliedFilter();

            }
        }

        @Override
        public String getCaption() {
            return getMainMessage("filter.pinApplied");
        }

        @Override
        public String getIcon() {
            return "icons/pin.png";
        }
    }

    protected class SaveAsFolderAction extends AbstractAction {

        public static final String SAVE_AS_APP_FOLDER = "saveAsAppFolder";
        public static final String SAVE_AS_SEARCH_FOLDER = "saveAsSearchFolder";

        protected boolean isAppFolder;

        protected SaveAsFolderAction(boolean isAppFolder) {
            super(isAppFolder ? SAVE_AS_APP_FOLDER : SAVE_AS_SEARCH_FOLDER);
            this.isAppFolder = isAppFolder;
        }

        @Override
        public String getCaption() {
            return getMainMessage("filter." + getId());
        }

        @Override
        public void actionPerform(Component component) {
            saveAsFolder(isAppFolder);
        }
    }

    protected class AddToSetAction extends ItemTrackingAction {
        protected AddToSetAction(Table table) {
            super(table, "filter.addToSet");
        }

        @Override
        public String getCaption() {
            return getMainMessage(getId());
        }

        @Override
        public void actionPerform(Component component) {
            Set<Entity> ownerSelection = target.getSelected();

            if (!ownerSelection.isEmpty()) {
                String entityType = target.getDatasource().getMetaClass().getName();
                Map<String, Object> params = new HashMap<>();
                params.put("entityType", entityType);
                params.put("items", ownerSelection);
                params.put("componentPath", ComponentsHelper.getFilterComponentPath(filter));
                String[] strings = ValuePathHelper.parse(ComponentsHelper.getFilterComponentPath(filter));
                String componentId = ValuePathHelper.format(Arrays.copyOfRange(strings, 1, strings.length));
                params.put("componentId", componentId);
                params.put("foldersPane", filterHelper.getFoldersPane());
                params.put("entityClass", datasource.getMetaClass().getJavaClass().getName());
                params.put("query", datasource.getQuery());
                filter.getFrame().openWindow("saveSetInFolder",
                        WindowManager.OpenType.DIALOG,
                        params);
            }
        }
    }

    protected class RemoveFromSetAction extends ItemTrackingAction {
        protected RemoveFromSetAction(Table table) {
            super(table, "filter.removeFromCurSet");
        }

        @Override
        public String getCaption() {
            return getMainMessage(getId());
        }

        @Override
        public void actionPerform(Component component) {
            if (filterEntity == null) {
                // todo add notification 'Filter not selected'
                return;
            }
            Set selected = target.getSelected();
            if (selected.isEmpty()) {
                return;
            }

            if (target.getDatasource().getItemIds().size() == 1) {
                filterHelper.removeFolderFromFoldersPane(filterEntity.getFolder());
                removeFilterEntity();

                Window window = ComponentsHelper.getWindow(filter);
                windowManager.close(window);
            } else {
                String filterXml = filterEntity.getXml();
                filterEntity.setXml(UserSetHelper.removeEntities(filterXml, selected));
                filterEntity.getFolder().setFilterXml(filterEntity.getXml());
                filterEntity.setFolder(saveFolder((filterEntity.getFolder())));
                setFilterEntity(filterEntity);
            }
        }
    }

    protected class AddToCurrSetAction extends BaseAction {

        protected AddToCurrSetAction() {
            super("filter.addToCurSet");
        }

        @Override
        public String getCaption() {
            return getMainMessage(getId());
        }

        @Override
        public void actionPerform(Component component) {
            if (filterEntity == null) {
                // todo add notification 'Filter not selected'
                return;
            }

            Frame frame = filter.getFrame();
            String[] strings = ValuePathHelper.parse(ComponentsHelper.getFilterComponentPath(filter));
            String windowAlias = strings[0];
            StringBuilder lookupAlias = new StringBuilder(windowAlias);
            if (windowAlias.endsWith(Window.BROWSE_WINDOW_SUFFIX)) {
                int index = lookupAlias.lastIndexOf(Window.BROWSE_WINDOW_SUFFIX);
                lookupAlias.delete(index, lookupAlias.length());
                lookupAlias.append(Window.LOOKUP_WINDOW_SUFFIX);
            }
            frame.openLookup(lookupAlias.toString(), new Window.Lookup.Handler() {

                @Override
                public void handleLookup(Collection items) {
                    String filterXml = filterEntity.getXml();
                    filterEntity.setXml(UserSetHelper.addEntities(filterXml, items));
                    filterEntity.getFolder().setFilterXml(filterEntity.getXml());
                    filterEntity.setFolder(saveFolder(filterEntity.getFolder()));
                    setFilterEntity(filterEntity);
                }
            }, WindowManager.OpenType.THIS_TAB);
        }
    }

    protected static class AppliedFilterHolder {
        public final AppliedFilter filter;
        public final Component.Container layout;
        public final Button button;

        protected AppliedFilterHolder(AppliedFilter filter, Component.Container layout, Button button) {
            this.filter = filter;
            this.layout = layout;
            this.button = button;
        }
    }

    /**
     * Class creates filter controls layout based on template.
     * See template format in documentation for {@link ClientConfig#getGenericFilterControlsLayout()}
     */
    protected class ControlsLayoutBuilder {

        protected Map<String, List<String>> components = new LinkedHashMap<>();
        protected Map<String, AbstractAction> filterActions = new HashMap<>();

        public ControlsLayoutBuilder(String layoutDescription) {
            initFilterActions();
            parseLayoutDescription(layoutDescription);
        }

        protected void initFilterActions() {
            filterActions.put("save", saveAction);
            filterActions.put("save_with_values", saveWithValuesAction);
            filterActions.put("save_as", saveAsAction);
            filterActions.put("edit", editAction);
            filterActions.put("remove", removeAction);
            filterActions.put("pin", pinAppliedAction);
            filterActions.put("save_search_folder", saveAsSearchFolderAction);
            filterActions.put("save_app_folder", saveAsAppFolderAction);
            filterActions.put("make_default", makeDefaultAction);
            filterActions.put("clear_values", clearValuesAction);
        }

        protected void parseLayoutDescription(String layoutDescription) {
            Pattern panelComponentPattern = Pattern.compile("\\[(.*?)\\]");
            Matcher matcher = panelComponentPattern.matcher(layoutDescription);
            Splitter componentDescriptionSplitter = Splitter.on("|").trimResults();
            Splitter optionsSplitter = Splitter.on(",").trimResults();
            while (matcher.find()) {
                String componentDescription = matcher.group(1);
                Iterable<String> parts = componentDescriptionSplitter.split(componentDescription);
                Iterator<String> iterator = parts.iterator();
                String componentName = iterator.next();
                String componentOptions = iterator.hasNext() ? iterator.next() : "";
                Iterable<String> options = optionsSplitter.split(componentOptions);
                components.put(componentName, Lists.newArrayList(options));
            }
        }

        public void build() {
            for (Map.Entry<String, List<String>> entry : components.entrySet()) {
                String componentName = entry.getKey();
                Component component = getControlsLayoutComponent(componentName, entry.getValue());
                if (component == null) {
                    //in case of disabled FTS add-on, the missing fts_switch component is not an error
                    if (!isFtsModeEnabled() && "fts_switch".equals(componentName)) {
                        continue;
                    }
                    log.warn("Filter controls layout component {} not supported", componentName);
                    continue;
                }
                controlsLayout.add(component);
                if (component == controlsLayoutGap) {
                    controlsLayout.expand(component);
                }
            }
        }

        @Nullable
        protected Component getControlsLayoutComponent(String name, List<String> options) {
            switch (name) {
                case "filters_popup":
                    filtersPopupDisplayed = true;
                    return filtersPopupBox;
                case "filters_lookup":
                    filtersLookupDisplayed = true;
                    return filtersLookup;
                case "search":
                    searchBtn.setParent(null);
                    return searchBtn;
                case "add_condition":
                    return addConditionBtn;
                case "settings":
                    fillSettingsBtn(options);
                    return settingsBtn;
                case "max_results":
                    maxResultsAddedToLayout = true;
                    return maxResultsLayout;
                case "fts_switch":
                    return ftsSwitch;
                case "spacer":
                    return controlsLayoutGap;
                case "pin":
                case "save":
                case "save_with_values":
                case "save_as":
                case "edit":
                case "remove":
                case "make_default":
                case "save_search_folder":
                case "save_app_folder":
                case "clear_values":
                    return createActionBtn(name, options);
            }
            return null;
        }

        protected Button createActionBtn(String actionName, List<String> options) {
            if (!isActionAllowed(actionName)) {
                return null;
            }
            Button button = componentsFactory.createComponent(Button.class);
            button.setAction(filterActions.get(actionName));
            if (options.contains("no-caption")) {
                button.setCaption(null);
                button.setDescription(filterActions.get(actionName).getCaption());
            }
            if (options.contains("no-icon")) {
                button.setIcon(null);
            }
            return button;
        }

        protected void fillSettingsBtn(List<String> actionNames) {
            for (String actionName : actionNames) {
                AbstractAction action = filterActions.get(actionName);
                if (action == null) {
                    log.warn("Action {} cannot be added to settingsBtn", actionName);
                    continue;
                }
                if (isActionAllowed(actionName)) {
                    settingsBtn.addAction(action);
                }
            }
        }

        protected boolean isActionAllowed(String actionName) {
            switch (actionName) {
                case "pin":
                    return globalConfig.getAllowQueryFromSelected();
                case "save_search_folder":
                case "save_app_folder":
                    return folderActionsEnabled && filterHelper.isFolderActionsEnabled();
                default:
                    return true;
            }
        }
    }
}
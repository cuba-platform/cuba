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
import com.haulmont.bali.datastruct.Pair;
import com.haulmont.bali.events.Subscription;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.client.sys.PersistenceManagerClient;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.entity.AppFolder;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.global.filter.*;
import com.haulmont.cuba.core.global.queryconditions.JpqlCondition;
import com.haulmont.cuba.core.sys.xmlparsing.Dom4jTools;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action.Status;
import com.haulmont.cuba.gui.components.Component.Alignment;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.gui.components.KeyCombination.Key;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit;
import com.haulmont.cuba.gui.components.data.meta.DatasourceDataUnit;
import com.haulmont.cuba.gui.components.data.meta.EntityDataUnit;
import com.haulmont.cuba.gui.components.filter.condition.*;
import com.haulmont.cuba.gui.components.filter.edit.FilterEditor;
import com.haulmont.cuba.gui.components.filter.filterselect.FilterSelectWindow;
import com.haulmont.cuba.gui.components.sys.ValuePathHelper;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.model.BaseCollectionLoader;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.LoaderSupportsApplyToSelected;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.gui.settings.SettingsImpl;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.security.entity.FilterEntity;
import com.haulmont.cuba.security.entity.SearchFolder;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
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
    protected UiComponents uiComponents;
    @Inject
    protected ThemeConstantsManager themeConstantsManager;
    @Inject
    protected Messages messages;
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
    protected ScreenBuilders screenBuilders;
    @Inject
    protected Dom4jTools dom4JTools;

    @Inject
    protected DataService dataService;
    @Inject
    protected PersistenceManagerClient persistenceManager;
    @Inject
    protected ClientConfig clientConfig;
    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected BeanLocator beanLocator;

    protected FtsFilterHelper ftsFilterHelper;
    protected AddConditionHelper addConditionHelper;
    protected ThemeConstants theme;

    protected Filter filter;
    protected FilterEntity adHocFilter;
    protected ConditionsTree conditions = new ConditionsTree();
    protected ConditionsTree prevConditions = new ConditionsTree();
    protected List<AbstractCondition> initialConditions = new ArrayList<>();
    protected FilterEntity filterEntity;
    protected FilterEntity initialFilterEntity;
    protected CollectionDatasource datasource;
    protected BaseCollectionLoader dataLoader;
    protected Adapter adapter;
    protected QueryFilter dsQueryFilter;
    protected List<FilterEntity> filterEntities = new ArrayList<>();
    protected AppliedFilter lastAppliedFilter;
    protected LinkedList<AppliedFilterHolder> appliedFilters = new LinkedList<>();
    protected List<Filter.FilterEntityChangeListener> filterEntityChangeListeners = new ArrayList<>();

    protected GroupBoxLayout groupBoxLayout;
    protected GroupBoxLayout layout;  // layout for all nested panels
    protected PopupButton filtersPopupButton;
    protected ComponentContainer conditionsLayout;
    protected BoxLayout maxResultsLayout;
    protected Field<Integer> maxResultsField;
    protected TextField<Integer> maxResultsTextField;
    protected LookupField maxResultsLookupField;
    protected BoxLayout controlsLayout;
    protected ComponentContainer appliedFiltersLayout;
    protected PopupButton settingsBtn;
    protected Component applyTo;
    protected SaveAction saveAction;
    protected SaveAction saveWithValuesAction;
    protected TextField<String> ftsSearchCriteriaField;
    protected CheckBox ftsSwitch;
    protected LinkButton addConditionBtn;
    protected ComponentContainer filtersPopupBox;
    protected Button searchBtn;
    protected Component controlsLayoutGap;
    protected Object paramEditComponentToFocus;

    protected String caption;
    protected int maxResults = -1;
    protected boolean useMaxResults;
    protected boolean textMaxResults;
    protected boolean maxResultValueChanged = false;
    protected boolean groupBoxExpandedChanged = false;
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
    protected LookupField<FilterEntity> filtersLookup;

    protected Consumer<FDExpandedStateChangeEvent> expandedStateChangeListener;

    protected Filter.BeforeFilterAppliedHandler beforeFilterAppliedHandler;

    protected Filter.AfterFilterAppliedHandler afterFilterAppliedHandler;
    protected boolean borderVisible = true;

    protected Set<String> ftsLastDatasourceRefreshParamsNames = new HashSet<>();
    protected Consumer<String> captionChangedListener;
    protected boolean windowCaptionUpdateEnabled = true;

    protected List<Subscription> paramValueChangeSubscriptions;
    protected Map<AbstractCondition, AbstractCondition.Listener> conditionListeners;
    protected Map<AbstractCondition, ParamEditor> paramEditors;
    protected Boolean applyImmediately;
    protected String controlsLayoutTemplate;

    protected enum ConditionsFocusType {
        NONE,
        FIRST,
        LAST
    }

    @Inject
    public void setScreenBuilders(ScreenBuilders screenBuilders) {
        this.screenBuilders = screenBuilders;
    }

    @PostConstruct
    public void init() {
        theme = themeConstantsManager.getConstants();
        if (beanLocator.containsBean(FtsFilterHelper.NAME)) {
            ftsFilterHelper = beanLocator.get(FtsFilterHelper.class);
        }
        filterMode = FilterMode.GENERIC_MODE;

        conditionsLocation = clientConfig.getGenericFilterConditionsLocation();
        applyImmediately = clientConfig.getGenericFilterApplyImmediately();

        createLayout();
    }

    @Override
    public void createLayout() {
        if (layout == null) {
            groupBoxLayout = uiComponents.create(GroupBoxLayout.class);
            groupBoxLayout.addExpandedStateChangeListener(e -> fireExpandStateChange(e.isUserOriginated()));
            groupBoxLayout.setOrientation(GroupBoxLayout.Orientation.VERTICAL);
            groupBoxLayout.setWidthFull();

            layout = groupBoxLayout;
            layout.setSpacing(true);

            if (caption == null) {
                setCaption(getMainMessage("filter.groupBoxCaption"));
            }
        } else {
            Collection<Component> components = layout.getComponents();
            for (Component component : components) {
                layout.remove(component);
            }
        }

        appliedFiltersLayout = uiComponents.create(VBoxLayout.class);

        if (AppConfig.getClientType() == ClientType.DESKTOP) {
            conditionsLayout = uiComponents.create(HBoxLayout.class);
        } else {
            conditionsLayout = uiComponents.create(CssLayout.class);
        }

        conditionsLayout.setVisible(false); // initially hidden
        conditionsLayout.setWidthFull();
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
        controlsLayout = uiComponents.create(HBoxLayout.class);
        controlsLayout.setSpacing(true);
        controlsLayout.setWidthFull();
        filterHelper.setInternalDebugId(controlsLayout, "controlsLayout");

        filtersPopupBox = filterHelper.createSearchButtonGroupContainer();
        filtersPopupBox.addStyleName("filter-search-button-layout");
        filterHelper.setInternalDebugId(filtersPopupBox, "filtersPopupBox");

        searchBtn = uiComponents.create(Button.class);
        filtersPopupBox.add(searchBtn);
        searchBtn.setStyleName("filter-search-button");
        searchBtn.setCaption(getSearchBtnCaption());
        searchBtn.setIcon("icons/search.png");
        searchBtn.setDescription(getMainMessage("filter.searchBtn.description"));
        searchBtn.addClickListener(e ->
                apply(false)
        );

        filterHelper.setInternalDebugId(searchBtn, "searchBtn");

        filtersPopupButton = uiComponents.create(PopupButton.class);
        filtersPopupButton.setStyleName("icon-only");
        filterHelper.setInternalDebugId(filtersPopupButton, "filtersPopupButton");
        filtersPopupBox.add(filtersPopupButton);

        filtersLookup = uiComponents.create(LookupField.class);
        filtersLookup.setWidth(theme.get("cuba.gui.filter.select.width"));
        filtersLookup.addValueChangeListener(new FiltersLookupChangeListener());
        filterHelper.setLookupNullSelectionAllowed(filtersLookup, false);
        filterHelper.setInternalDebugId(filtersLookup, "filtersLookup");

        addConditionBtn = uiComponents.create(LinkButton.class);
        addConditionBtn.setAlignment(Alignment.MIDDLE_LEFT);
        addConditionBtn.setCaption(getMainMessage("filter.addCondition"));
        addConditionBtn.addClickListener(e ->
                addConditionHelper.addCondition(conditions)
        );
        filterHelper.setInternalDebugId(addConditionBtn, "addConditionBtn");

        controlsLayoutGap = uiComponents.create(Label.class);
        filterHelper.setInternalDebugId(controlsLayoutGap, "controlsLayoutGap");
        controlsLayout.add(controlsLayoutGap);
        controlsLayout.expand(controlsLayoutGap);

        settingsBtn = uiComponents.create(PopupButton.class);
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

        String layoutDescription = !Strings.isNullOrEmpty(controlsLayoutTemplate) ?
                controlsLayoutTemplate :
                clientConfig.getGenericFilterControlsLayout();
        ControlsLayoutBuilder controlsLayoutBuilder = createControlsLayoutBuilder(layoutDescription);
        controlsLayoutBuilder.build();
        if (isMaxResultsLayoutVisible()) {
            initMaxResults();
        }

        maxResultsLayout.setVisible(isMaxResultsLayoutVisible());
        filterHelper.setInternalDebugId(maxResultsLayout, "maxResultsLayout");
    }

    protected void createControlsLayoutForFts() {
        controlsLayout = uiComponents.create(HBoxLayout.class);
        controlsLayout.setSpacing(true);
        controlsLayout.setWidthFull();

        ftsSearchCriteriaField = uiComponents.create(TextField.NAME);
        ftsSearchCriteriaField.setWidth(theme.get("cuba.gui.filter.ftsSearchCriteriaField.width"));
        ftsSearchCriteriaField.setInputPrompt(getMainMessage("filter.enterSearchPhrase"));
        filterHelper.addShortcutListener(ftsSearchCriteriaField, createFtsSearchShortcutListener());
        ftsSearchCriteriaField.addValueChangeListener(valueChangeEvent -> {
            if (isApplyImmediately()) {
                applyFts();
            }
        });

        filterHelper.setInternalDebugId(ftsSearchCriteriaField, "ftsSearchCriteriaField");

        paramEditComponentToFocus = ftsSearchCriteriaField;
        controlsLayout.add(ftsSearchCriteriaField);

        searchBtn = uiComponents.create(Button.class);
        searchBtn.setCaption(getSearchBtnCaption());
        searchBtn.setIcon("icons/search.png");
        searchBtn.setAction(new AbstractAction("search") {
            @Override
            public void actionPerform(Component component) {
                applyFts();
            }
        });
        controlsLayout.add(searchBtn);

        controlsLayoutGap = uiComponents.create(Label.class);
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
                // disable search in immediate mode, because it will be handled in value change listener
                if (!isApplyImmediately()) {
                    applyFts();
                }
            }
        };
    }

    protected void createFtsSwitch() {
        ftsSwitch = uiComponents.create(CheckBox.class);
        ftsSwitch.setCaption(getMainMessage("filter.ftsSwitch"));
        ftsSwitch.setValue(filterMode == FilterMode.FTS_MODE);

        ftsSwitch.addValueChangeListener(e -> {
            filterMode = Boolean.TRUE.equals(e.getValue()) ? FilterMode.FTS_MODE : FilterMode.GENERIC_MODE;
            switchFilterMode(filterMode);
            // try to apply in order to actualize showed data
            applyWithImmediateMode();
        });

        ftsSwitch.setVisible(modeSwitchVisible);
    }

    @Override
    public void switchFilterMode(FilterMode filterMode) {
        if (filterMode == FilterMode.FTS_MODE && !isFtsModeEnabled() && !isEntityAvailableForFts()) {
            log.warn("Unable to switch to the FTS filter mode. FTS mode is not supported for the {} entity",
                    adapter.getMetaClass().getName());
            return;
        }
        this.filterMode = filterMode;
        if (filterMode == FilterMode.FTS_MODE) {
            prevConditions = conditions;
            adapter.unpinAllQuery();
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
            filtersPopupButton.focus();
        else if (filtersLookupDisplayed) {
            filtersLookup.focus();
        }
        updateWindowCaption();
    }

    protected void createMaxResultsLayout() {
        maxResultsLayout = uiComponents.create(HBoxLayout.class);
        maxResultsLayout.setStyleName("c-maxresults");
        maxResultsLayout.setSpacing(true);
        Label<String> maxResultsLabel = uiComponents.create(Label.NAME);
        maxResultsLabel.setStyleName("c-maxresults-label");
        maxResultsLabel.setValue(messages.getMainMessage("filter.maxResults.label1"));
        maxResultsLabel.setAlignment(Alignment.MIDDLE_RIGHT);
        maxResultsLayout.add(maxResultsLabel);

        maxResultsTextField = uiComponents.create(TextField.TYPE_INTEGER);
        maxResultsTextField.setStyleName("c-maxresults-input");
        maxResultsTextField.setMaxLength(4);
        maxResultsTextField.setWidth(theme.get("cuba.gui.Filter.maxResults.width"));

        maxResultsLookupField = maxResultsFieldHelper.createMaxResultsLookupField();
        maxResultsLookupField.setStyleName("c-maxresults-select");

        maxResultsField = textMaxResults ? maxResultsTextField : maxResultsLookupField;
        maxResultsField.addValueChangeListener(valueChangeEvent -> {
            maxResultValueChanged = true;
            if (valueChangeEvent.isUserOriginated() && isApplyImmediately()) {
                if (filterMode == FilterMode.FTS_MODE) {
                    applyFts();
                }
                if (filterMode == FilterMode.GENERIC_MODE) {
                    applyWithImmediateMode();
                }
            }
        });
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
            log.error("Exception on loading default filter '{}'", defaultFilter.getName(), e);
            getNotifications().create(Notifications.NotificationType.ERROR)
                    .withCaption(messages.formatMainMessage("filter.errorLoadingDefaultFilter"))
                    .withDescription(defaultFilter.getName())
                    .show();
            defaultFilter = adHocFilter;
            setFilterEntity(adHocFilter);
        }

        if (defaultFilter != adHocFilter && (filterMode == FilterMode.GENERIC_MODE)) {
            Window window = getWindow();
            if (!WindowParams.DISABLE_AUTO_REFRESH.getBool(window.getContext())) {
                if (getResultingManualApplyRequired()) {
                    if (BooleanUtils.isTrue(defaultFilter.getApplyDefault())) {
                        adapter.preventNextDataLoading();
                        apply(true);
                    }
                } else {
                    adapter.preventNextDataLoading();
                    apply(true);
                }
                if (filterEntity != null && windowCaptionUpdateEnabled) {
                    window.setDescription(getFilterCaption(filterEntity));
                } else
                    window.setDescription(null);
            }
        }
    }

    protected boolean suitableCondition(AbstractCondition condition) {
        if (condition instanceof PropertyCondition) {
            return adapter.getMetaClass()
                    .getPropertyPath(condition.getName()) != null;
        }

        if (condition instanceof DynamicAttributesCondition) {
            return DynamicAttributesUtils.getMetaPropertyPath(
                    adapter.getMetaClass(),
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
                        filterEntity.getName(), adapter.getMetaClass().getName());
                getNotifications().create(Notifications.NotificationType.HUMANIZED)
                        .withCaption(message)
                        .show();
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
        Window window = getWindow();
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

        updateWindowCaption();

        for (Filter.FilterEntityChangeListener listener : filterEntityChangeListeners) {
            listener.filterEntityChanged(filterEntity);
        }
    }

    @Override
    public FilterEntity getFilterEntity() {
        return filterEntity;
    }

    protected Window getWindow() {
        Window window = ComponentsHelper.getWindowImplementation(filter);
        if (window == null)
            throw new IllegalStateException(String.format("Cannot get window for filter %s", filter.getId()));
        return window;
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
                && (adapter == null || Stores.isMain(metadata.getTools().getStoreName(adapter.getMetaClass())));
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

        if (filterHelper.isTableActionsEnabled()
                && filterHelper.mainScreenHasFoldersPane(filter.getFrame())) {
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
        if (applyTo != null && applyTo instanceof HasPresentations) {
            final HasPresentations presentationsOwner = (HasPresentations) applyTo;
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

        clearParamValueChangeSubscriptions();

        recursivelyCreateConditionsLayout(conditionsFocusType, false, conditions.getRootNodes(), conditionsLayout, 0);

        if (isApplyImmediately()) {
            List<Node<AbstractCondition>> nodes = conditions.getRootNodes();
            subscribeToParamValueChangeEventRecursively(nodes);
        }

        conditionsLayout.setVisible(!conditionsLayout.getComponents().isEmpty());
    }

    protected void recursivelyCreateConditionsLayout(ConditionsFocusType conditionsFocusType,
                                                     boolean initialFocusSet,
                                                     List<Node<AbstractCondition>> nodes,
                                                     ComponentContainer parentContainer,
                                                     int level) {
        FilterDataContext filterDataContext = new FilterDataContext(filter.getFrame());
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
        GridLayout grid = uiComponents.create(GridLayout.class);
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

        RuntimeException entityParamInformationException = null;

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

                    Pair<ParamEditor, RuntimeException> pair = createParamEditorWithChecks(condition, filterDataContext);
                    ParamEditor paramEditor = pair.getFirst();
                    addParamEditor(condition, paramEditor);

                    if (pair.getSecond() != null) {
                        entityParamInformationException = pair.getSecond();
                    }

                    if (firstParamEditor == null) firstParamEditor = paramEditor;
                    lastParamEditor = paramEditor;
                    currentFocusSet = true;

                    labelAndOperationCellContent = paramEditor.getLabelAndOperationLayout();
                    paramEditComponentCellContent = paramEditor.getParamEditComponentLayout();
                } else {
                    BoxLayout paramLayout = uiComponents.create(HBoxLayout.class);
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
                if (condition.getParam().getType() == Param.Type.UNARY) {
                    paramEditComponentCellContent.addStyleName("unary-param-type");
                }
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

        filterDataContext.loadAll();

        if (!initialFocusSet) {
            switch (conditionsFocusType) {
                case FIRST:
                    if (firstParamEditor != null) {
                        paramEditComponentToFocus = firstParamEditor;
                    }
                    break;
                case LAST:
                    if (lastParamEditor != null) {
                        paramEditComponentToFocus = lastParamEditor;
                    }
                    break;
                default:
                    // no action
                    break;
            }
        }

        //complete last row in grid with gaps
        completeGridRowWithGaps(grid, row, nextColumnStart, true);

        if (parentContainer != null) {
            parentContainer.add(grid);
        }

        if (level == 0)
            controlsLayout.setStyleName(getControlsLayoutStyleName());

        if (entityParamInformationException != null) {
            getDialogs()
                    .createExceptionDialog()
                    .withThrowable(entityParamInformationException)
                    .show();
        }
    }

    protected void addParamEditor(AbstractCondition condition, ParamEditor paramEditor) {
        if (paramEditors == null) {
            paramEditors = new HashMap<>();
        }

        paramEditors.put(condition, paramEditor);
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
        GroupBoxLayout groupBox = uiComponents.create(GroupBoxLayout.class);
        groupBox.setStyleName("conditions-group");
        groupBox.setWidth("100%");
        groupBox.setCaption(condition.getLocCaption());

        if (!node.getChildren().isEmpty()) {
            recursivelyCreateConditionsLayout(conditionsFocusType, focusSet, node.getChildren(), groupBox, level);
        }
        groupCellContent = groupBox;
        return groupCellContent;
    }

    protected Pair<ParamEditor, RuntimeException> createParamEditorWithChecks(final AbstractCondition condition,
                                                                              FilterDataContext filterDataContext) {
        ParamEditor paramEditor;
        RuntimeException informationException = null;

        //check that entity param view exists
        try {
            if (!Strings.isNullOrEmpty(condition.getEntityParamView())) {
                metadata.getViewRepository()
                        .getView(condition.getEntityMetaClass(), condition.getEntityParamView());
            }
        } catch (RuntimeException e) {
            condition.getParam().entityView = null;
            informationException = e;
        }

        try {
            paramEditor = createParamEditor(condition, filterDataContext);
        } catch (RuntimeException e) {
            condition.getParam().entityWhere = null;
            paramEditor = createParamEditor(condition, filterDataContext);
            informationException = e;
        }

        return new Pair<>(paramEditor, informationException);
    }

    protected ParamEditor createParamEditor(final AbstractCondition condition, FilterDataContext filterDataContext) {
        boolean conditionRemoveEnabled = !initialConditions.contains(condition);
        ParamEditor paramEditor = new ParamEditor(condition, filterDataContext, conditionRemoveEnabled, isParamEditorOperationEditable());
        AbstractAction removeConditionAction = new AbstractAction("") {
            @Override
            public void actionPerform(Component component) {
                conditions.removeCondition(condition);
                fillConditionsLayout(ConditionsFocusType.NONE);
                updateFilterModifiedIndicator();

                applyWithImmediateMode();
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
     * Adds empty containers to grid row. If not to complete the row with gaps then in case of grid with one element (element width = 1) this element
     * will occupy 100% of grid width, but expected behaviour is to occupy 1/3 of grid width
     */
    protected void completeGridRowWithGaps(GridLayout grid, int row, int startColumn, boolean lastRow) {
        for (int i = startColumn * 2; i < grid.getColumns(); i++) {
            Component gap = uiComponents.create(Label.class);
            gap.setWidthFull();
            grid.add(gap, i, row);
        }
    }

    protected String getControlsLayoutStyleName() {
        String styleName = "filter-control-no-border";
        if (conditionsLayout.isVisibleRecursive() && !conditionsLayout.getComponents().isEmpty()) {
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
        Window window = ComponentsHelper.getWindowImplementation(filter);
        if (window == null) {
            throw new IllegalStateException("There is no window set for filter");
        }

        // First check if there is parameter with name equal to this filter component id, containing a filter code to apply
        Map<String, Object> params = filter.getFrame().getContext().getParams();
        String code = (String) params.get(filter.getId());
        if (!StringUtils.isBlank(code)) {
            for (FilterEntity filter : filters) {
                if (code.equals(filter.getCode())) {
                    return filter;
                }
            }
        }

        // No 'filter' parameter found, load default filter
        SettingsImpl settings = new SettingsImpl(window.getId());

        String componentPath = ComponentsHelper.getFilterComponentPath(filter);
        String[] strings = ValuePathHelper.parse(componentPath);
        String name = ValuePathHelper.pathSuffix(strings);

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
                    applyWithImmediateMode();
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
                FilterSelectWindow window = (FilterSelectWindow) getWindowManager().openWindow(windowInfo,
                        OpenType.DIALOG,
                        ParamsMap.of("filterEntities", filterEntities));

                window.addCloseListener(actionId -> {
                    if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                        FilterEntity selectedEntity = window.getFilterEntity();
                        setFilterEntity(selectedEntity);
                        applyWithImmediateMode();
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
                applyWithImmediateMode();
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
        List<FilterEntity> optionsList = new ArrayList<>();
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

        BoxLayout layout = uiComponents.create(HBoxLayout.class);
        layout.setSpacing(true);

        if (!appliedFilters.isEmpty()) {
            AppliedFilterHolder holder = appliedFilters.getLast();
            holder.layout.remove(holder.button);
        }

        Label<String> label = uiComponents.create(Label.NAME);
        label.setValue(lastAppliedFilter.getText());
        layout.add(label);
        label.setAlignment(Alignment.MIDDLE_LEFT);

        LinkButton button = uiComponents.create(LinkButton.class);
        button.setIcon("icons/item-remove.png");
        button.addClickListener(e -> removeAppliedFilter());
        layout.add(button);

        addAppliedFilterLayoutHook(layout);
        appliedFiltersLayout.add(layout);

        appliedFilters.add(new AppliedFilterHolder(lastAppliedFilter, layout, button));
    }

    protected void addAppliedFilterLayoutHook(ComponentContainer layout) {
        //nothing
    }

    protected void removeAppliedFilter() {
        if (!appliedFilters.isEmpty()) {
            if (appliedFilters.size() == 1) {
                AppliedFilterHolder holder = appliedFilters.removeLast();
                appliedFiltersLayout.remove(holder.layout);
                adapter.unpinAllQuery();
                this.layout.remove(appliedFiltersLayout);
            } else {
                getDialogs().createOptionDialog(Dialogs.MessageType.WARNING)
                        .withCaption(messages.getMainMessage("removeApplied.title"))
                        .withMessage(messages.getMainMessage("removeApplied.message"))
                        .withActions(new DialogAction(Type.YES).withHandler(event -> {
                                    for (AppliedFilterHolder holder : appliedFilters) {
                                        appliedFiltersLayout.remove(holder.layout);
                                        FilterDelegateImpl.this.layout.remove(appliedFiltersLayout);
                                    }
                                    appliedFilters.clear();
                                    adapter.unpinAllQuery();
                                }),
                                new DialogAction(Type.NO, Status.PRIMARY))
                        .show();
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
    public ComponentContainer getLayout() {
        return groupBoxLayout;
    }

    @Override
    public MetaClass getEntityMetaClass() {
        checkState();
        return adapter.getMetaClass();
    }

    @Override
    public String getEntityAlias() {
        checkState();
        String query = adapter.getQuery();
        String metaClassName = adapter.getMetaClass().getName();
        if (query == null) {
            query = String.format("select e from %s e", metaClassName);
            adapter.setQuery(query);
        }
        QueryParser parser = QueryTransformerFactory.createParser(query);
        return parser.getEntityAlias(metaClassName);
    }

    @Override
    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;
        this.dsQueryFilter = datasource.getQueryFilter();
        this.adapter = new DatasourceAdapter(datasource);

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

            // set to false because it's initial value
            maxResultValueChanged = false;
        }

        if (ftsSwitch != null && !isEntityAvailableForFts()) {
            controlsLayout.remove(ftsSwitch);
        }
    }

    @Override
    public BaseCollectionLoader getDataLoader() {
        return dataLoader;
    }

    @Override
    public void setDataLoader(BaseCollectionLoader dataLoader) {
        this.dataLoader = dataLoader;
        this.adapter = new LoaderAdapter(dataLoader, filter);
        this.adapter.setDataLoaderCondition(dataLoader.getCondition());

        if (getResultingManualApplyRequired()) {
            // set initial denying condition to get empty datasource before explicit filter applying
            JpqlCondition denyingCondition = new JpqlCondition("0<>0");
            this.dataLoader.setCondition(denyingCondition);
        }

        if (useMaxResults) {
            initMaxResults();

            // set to false because it's initial value
            maxResultValueChanged = false;
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
        checkState();

        int maxResults;
        if (this.maxResults != -1) {
            maxResults = this.maxResults;
        } else {
            maxResults = adapter.getMaxResults();
        }

        if (maxResults == 0 || maxResults == persistenceManager.getMaxFetchUI(adapter.getMetaClass().getName())) {
            maxResults = persistenceManager.getFetchUI(adapter.getMetaClass().getName());
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

        adapter.setMaxResults(maxResults);
    }

    protected void checkState() {
        if (dataLoader == null && datasource == null) {
            throw new IllegalStateException("Set DataLoader or Datasource first");
        }
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
        return adapter != null
                && ftsFilterHelper != null
                && ftsFilterHelper.isEntityIndexed(adapter.getMetaClass().getName())
                && Stores.isMain(metadata.getTools().getStoreName(adapter.getMetaClass()));
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
    public boolean apply(Filter.FilterOptions options) {
        if (beforeFilterAppliedHandler != null) {
            if (!beforeFilterAppliedHandler.beforeFilterApplied()) return false;
        }
        if (clientConfig.getGenericFilterChecking()) {
            if (filterEntity != null && conditions.getRoots().size() > 0) {
                boolean haveCorrectCondition = hasCorrectCondition();
                if (!haveCorrectCondition) {
                    if (!options.isNotifyInvalidConditions()) {
                        getNotifications().create(Notifications.NotificationType.HUMANIZED)
                                .withCaption(messages.getMainMessage("filter.emptyConditions"))
                                .show();
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
                if (!options.isNotifyInvalidConditions()) {
                    getNotifications().create(Notifications.NotificationType.HUMANIZED)
                            .withCaption(messages.getMainMessage("filter.emptyRequiredConditions"))
                            .show();
                }
                return false;
            }
            setFilterActionsEnabled();
        }

        if (!options.isLoadData()) {
            adapter.preventNextDataLoading();
        }

        applyDatasourceFilter();
        initDatasourceMaxResults();

        Map<String, Object> parameters = prepareDatasourceCustomParams();
        refreshDatasource(parameters);

        if ((applyTo != null) && (ListComponent.class.isAssignableFrom(applyTo.getClass()))) {
            if (clientConfig.getGenericFilterFtsTableTooltipsEnabled()) {
                filterHelper.removeTableFtsTooltips((ListComponent) applyTo);
            }
            if (clientConfig.getGenericFilterFtsDetailsActionEnabled()) {
                ((ListComponent) applyTo).removeAction(FtsFilterHelper.FTS_DETAILS_ACTION_ID);
            }
        }

        if (afterFilterAppliedHandler != null) {
            afterFilterAppliedHandler.afterFilterApplied();
        }

        return true;
    }

    protected void applyWithImmediateMode() {
        if (isApplyImmediately()) {
            apply(false);
        }
    }

    @Override
    public boolean apply(boolean notifyInvalidConditions) {
        return apply(Filter.FilterOptions.create()
                .setNotifyInvalidConditions(notifyInvalidConditions));
    }

    protected Map<String, Object> prepareDatasourceCustomParams() {
        Map<String, Object> lastRefreshParameters = new HashMap<>(adapter.getLastRefreshParameters());
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
                FtsFilterHelper.FtsSearchResult ftsSearchResult = ftsFilterHelper.search(searchTerm, adapter.getMetaClass().getName());
                int queryKey = ftsSearchResult.getQueryKey();

                lastRefreshParameters.put(ftsCondition.getSessionIdParamName(), userSessionSource.getUserSession().getId());
                lastRefreshParameters.put(ftsCondition.getQueryKeyParamName(), queryKey);

                ftsLastDatasourceRefreshParamsNames.add(ftsCondition.getSessionIdParamName());
                ftsLastDatasourceRefreshParamsNames.add(ftsCondition.getQueryKeyParamName());
                adapter.addFtsComponentParameter(ftsCondition.getParamName());
                adapter.addFtsCustomParameter(ftsCondition.getSessionIdParamName());
                adapter.addFtsCustomParameter(ftsCondition.getQueryKeyParamName());
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
            getNotifications().create(Notifications.NotificationType.TRAY)
                    .withCaption(getMainMessage("filter.fillSearchCondition"))
                    .show();
            return;
        }

        Map<String, Object> params = new HashMap<>();

        conditions = new ConditionsTree();
        if (!Strings.isNullOrEmpty(searchTerm)) {
            FtsFilterHelper.FtsSearchResult ftsSearchResult = ftsFilterHelper.search(searchTerm, adapter.getMetaClass().getName());
            int queryKey = ftsSearchResult.getQueryKey();
            params.put(FtsFilterHelper.SESSION_ID_PARAM_NAME, userSessionSource.getUserSession().getId());
            params.put(FtsFilterHelper.QUERY_KEY_PARAM_NAME, queryKey);

            CustomCondition ftsCondition = ftsFilterHelper.createFtsCondition(adapter.getMetaClass().getName());
            conditions.getRootNodes().add(new Node<>(ftsCondition));

            if ((applyTo != null) && ListComponent.class.isAssignableFrom(applyTo.getClass())) {
                if (clientConfig.getGenericFilterFtsTableTooltipsEnabled()) {
                    filterHelper.initTableFtsTooltips((ListComponent) applyTo, adapter.getMetaClass(), searchTerm);
                }
                if (clientConfig.getGenericFilterFtsDetailsActionEnabled()) {
                    initFtsDetailsAction((ListComponent) applyTo, searchTerm);
                }
            }
        } else if ((applyTo != null) && ListComponent.class.isAssignableFrom(applyTo.getClass())) {
            if (clientConfig.getGenericFilterFtsTableTooltipsEnabled()) {
                filterHelper.removeTableFtsTooltips((ListComponent) applyTo);
            }
            if (clientConfig.getGenericFilterFtsDetailsActionEnabled()) {
                ((ListComponent) applyTo).removeAction(FtsFilterHelper.FTS_DETAILS_ACTION_ID);
            }
        }

        applyDatasourceFilter();
        initDatasourceMaxResults();
        adapter.refreshIfNotSuspended(params);

        if (afterFilterAppliedHandler != null) {
            afterFilterAppliedHandler.afterFilterApplied();
        }
    }

    protected void initFtsDetailsAction(ListComponent listComponent, String searchTerm) {
        listComponent.addAction(ftsFilterHelper.createFtsDetailsAction(searchTerm));
    }

    /**
     * The method is invoked before search. It sets datasource {@code maxResults} value based on the maxResults field (if visible) or on maxFetchUI
     * value. Method also resets the datasource {@code firstResult} value
     */
    protected void initDatasourceMaxResults() {
        checkState();

        if (this.maxResults != -1 && !useMaxResults) {
            adapter.setMaxResults(maxResults);
        } else if (maxResultsAddedToLayout && useMaxResults) {
            Integer maxResults = maxResultsField.getValue();
            if (maxResults != null && maxResults > 0) {
                adapter.setMaxResults(maxResults);
            } else {
                adapter.setMaxResults(persistenceManager.getFetchUI(adapter.getMetaClass().getName()));
            }
        }
        adapter.setFirstResult(0);
    }

    protected void applyDatasourceFilter() {
        checkState();

        String currentFilterXml = filterParser.getXml(conditions, Param.ValueProperty.VALUE);

        if (!Strings.isNullOrEmpty(currentFilterXml)) {
            Element element = dom4JTools.readDocument(currentFilterXml).getRootElement();
            QueryFilter queryFilter = new QueryFilter(element);

            if (dsQueryFilter != null) {
                queryFilter = QueryFilter.merge(dsQueryFilter, queryFilter);
            }

            adapter.setQueryFilter(queryFilter);
        } else {
            adapter.setQueryFilter(dsQueryFilter);
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
     * extenders should be able to modify the datasource before it will be refreshed
     */
    protected void refreshDatasource(Map<String, Object> parameters) {
        adapter.refreshIfNotSuspended(parameters);
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
        if (!adapter.applyMaxResultsSettingsBeforeLoad()) {
            applyMaxResultsSettings(element);
        }
    }

    @Override
    public void applyDataLoadingSettings(Element element) {
        if (adapter.applyMaxResultsSettingsBeforeLoad()) {
            applyMaxResultsSettings(element);
        }
    }

    protected void applyMaxResultsSettings(Element element) {
        Element maxResultsEl = element.element("maxResults");
        if (maxResultsEl != null && !maxResultsEl.getText().equals("") && isMaxResultsLayoutVisible()) {
            try {
                int maxResultsFromSettings = Integer.parseInt(maxResultsEl.getText());
                adapter.setMaxResults(maxResultsFromSettings);
                initMaxResults();

                // set to false cause it's initial value from settings
                maxResultValueChanged = false;
            } catch (NumberFormatException ex) {
                log.error("Error on parsing maxResults setting value", ex);
            }
        }
    }

    @Override
    public boolean saveSettings(Element element) {
        boolean changed = false;
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

        if (groupBoxExpandedChanged) {
            Element groupBoxExpandedEl = element.element("groupBoxExpanded");
            if (groupBoxExpandedEl == null)
                groupBoxExpandedEl = element.addElement("groupBoxExpanded");

            Boolean oldGroupBoxExpandedValue =
                    groupBoxExpandedEl.getText().isEmpty() ? Boolean.TRUE : Boolean.valueOf(groupBoxExpandedEl.getText());

            Boolean newGroupBoxExpandedValue = groupBoxLayout.isExpanded();
            if (!Objects.equals(oldGroupBoxExpandedValue, newGroupBoxExpandedValue)) {
                groupBoxExpandedEl.setText(newGroupBoxExpandedValue.toString());
                changed = true;
            }
        }

        if (isMaxResultsLayoutVisible()) {
            if (maxResultValueChanged) {
                Element maxResultsEl = element.element("maxResults");
                if (maxResultsEl == null) {
                    maxResultsEl = element.addElement("maxResults");
                }

                Integer newMaxResultsValue = maxResultsField.getValue();
                if (newMaxResultsValue != null) {
                    maxResultsEl.setText(newMaxResultsValue.toString());
                    changed = true;
                }
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
            addToSet = table.getAction("addToSet");
            if (addToSet != null) {
                addToSet.setVisible(false);
            }

            addToCurrSet = new AddToCurrSetAction();

            if (addToCurSetBtn == null) {
                addToCurSetBtn = uiComponents.create(Button.class);
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
                removeFromCurSetBtn = uiComponents.create(Button.class);
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
        } else if (filter.getFrame().getFrameOwner() instanceof LegacyFrame) {
            addToSet = new AddToSetAction(table);
            if (addToSetBtn == null) {
                addToSetBtn = uiComponents.create(Button.class);
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
        if (component instanceof HasValue) {
            return ((HasValue) component).getValue();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setParamValue(String paramName, Object value) {
        Component component = getOwnComponent(paramName);
        if (component instanceof HasValue) {
            ((HasValue) component).setValue(value);
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
        //if the filter is already initialized and there are conditions, then recreate conditions layout
        if (!conditions.getRoots().isEmpty()) {
            fillConditionsLayout(ConditionsFocusType.FIRST);
        }
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
            filtersLookup.focus();
        } else if (filtersPopupDisplayed) {
            filtersPopupButton.focus();
        }
    }

    protected void requestFocusToParamEditComponent() {
        if (paramEditComponentToFocus instanceof ParamEditor) {
            ((ParamEditor) paramEditComponentToFocus).requestFocus();
        } else if (paramEditComponentToFocus instanceof TextField) {
            ((TextField) paramEditComponentToFocus).focus();
        }
    }

    @Override
    public void setExpandedStateChangeListener(Consumer<FDExpandedStateChangeEvent> listener) {
        expandedStateChangeListener = listener;
    }

    protected void fireExpandStateChange(boolean userOriginated) {
        if (expandedStateChangeListener != null) {
            FDExpandedStateChangeEvent event = new FDExpandedStateChangeEvent(this, isExpanded(), userOriginated);
            expandedStateChangeListener.accept(event);

            if (userOriginated) {
                groupBoxExpandedChanged = true;
            }
        }
    }

    @Override
    public void setFilter(Filter filter) {
        this.filter = filter;

        addConditionHelper = new AddConditionHelper(filter, condition -> {
            try {
                addCondition(condition);
            } catch (Exception e) {
                conditions.removeCondition(condition);
                throw e;
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
                    if (isVisible() && adapter != null) {
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
                    if (isVisible() && adapter != null && filtersPopupButton.isEnabled()) {
                        filtersPopupButton.setPopupVisible(true);
                    }
                }
            });
        }
    }

    protected void updateWindowCaption() {
        Window window = getWindow();
        String filterTitle;
        if (filterMode == FilterMode.GENERIC_MODE && filterEntity != null && filterEntity != adHocFilter) {
            filterTitle = getFilterCaption(filterEntity);
        } else {
            filterTitle = null;
        }

        if (windowCaptionUpdateEnabled) {
            if (initialWindowCaption == null) {
                initialWindowCaption = window.getCaption();
            }
            getWindowManager().setWindowCaption(window, initialWindowCaption, filterTitle);
        }

        String newCaption = Strings.isNullOrEmpty(filterTitle) ? caption : caption + ": " + filterTitle;
        captionChangedListener.accept(newCaption);
    }

    @Override
    public boolean isWindowCaptionUpdateEnabled() {
        return windowCaptionUpdateEnabled;
    }

    @Override
    public void setWindowCaptionUpdateEnabled(boolean windowCaptionUpdateEnabled) {
        this.windowCaptionUpdateEnabled = windowCaptionUpdateEnabled;
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

    @Override
    public void frameAssigned(Frame frame) {
        updateSettingsBtn(frame);
        updateControlsLayout(frame);
    }

    protected void updateSettingsBtn(Frame frame) {
        if (settingsBtn != null
                && !filterHelper.isFolderActionsAllowed(frame)) {
            List<Action> folderActions = settingsBtn.getActions()
                    .stream()
                    .filter(action -> action instanceof SaveAsFolderAction)
                    .collect(Collectors.toList());

            folderActions.forEach(settingsBtn::removeAction);
        }
    }

    protected void updateControlsLayout(Frame frame) {
        if (controlsLayout != null
                && !filterHelper.isFolderActionsAllowed(frame)) {
            List<Component> folderActionButtons = controlsLayout.getComponents()
                    .stream()
                    .filter(c -> c instanceof Button
                            && ((Button) c).getAction() instanceof SaveAsFolderAction)
                    .collect(Collectors.toList());

            folderActionButtons.forEach(controlsLayout::remove);
        }
    }

    @Override
    public ConditionsTree getConditionsTree() {
        return conditions;
    }

    @Override
    public void setApplyImmediately(boolean immediately) {
        this.applyImmediately = immediately;

        String caption = getSearchBtnCaption();
        if (searchBtn != null && !caption.equals(searchBtn.getCaption())) {
            searchBtn.setCaption(caption);
        }
    }

    @Override
    public boolean isApplyImmediately() {
        return applyImmediately;
    }

    @Override
    public String getControlsLayoutTemplate() {
        return controlsLayoutTemplate;
    }

    @Override
    public void setControlsLayoutTemplate(String controlsLayoutTemplate) {
        this.controlsLayoutTemplate = controlsLayoutTemplate;
    }

    protected void clearParamValueChangeSubscriptions() {
        if (paramValueChangeSubscriptions != null) {
            paramValueChangeSubscriptions.forEach(Subscription::remove);
            paramValueChangeSubscriptions.clear();
        }

        if (conditionListeners != null) {
            for (Map.Entry<AbstractCondition, AbstractCondition.Listener> item : conditionListeners.entrySet()) {
                item.getKey().removeListener(item.getValue());
            }
            conditionListeners.clear();
        }

        if (paramEditors != null) {
            for (Map.Entry<AbstractCondition, ParamEditor> item : paramEditors.entrySet()) {
                item.getKey().removeListener(item.getValue());
            }
            paramEditors.clear();
        }
    }

    protected void subscribeToParamValueChangeEventRecursively(List<Node<AbstractCondition>> conditions) {
        if (paramValueChangeSubscriptions == null) {
            paramValueChangeSubscriptions = new ArrayList<>();
        }

        for (Node<AbstractCondition> node : conditions) {
            AbstractCondition condition = node.getData();
            if (condition.isGroup()) {
                subscribeToParamValueChangeEventRecursively(node.getChildren());
            } else {
                Subscription subscription = condition.getParam()
                        .addParamValueChangeListener(event -> applyWithImmediateMode());
                paramValueChangeSubscriptions.add(subscription);

                addConditionListener(condition, subscription);
            }
        }
    }

    protected void addConditionListener(AbstractCondition condition, Subscription current) {
        if (conditionListeners == null) {
            conditionListeners = new HashMap<>();
        }

        AbstractCondition.Listener listener = new AbstractCondition.Listener() {
            protected Subscription previous = current;

            @Override
            public void captionChanged() {
                // do nothing
            }

            @Override
            public void paramChanged(Param oldParam, Param newParam) {
                previous.remove();
                paramValueChangeSubscriptions.remove(previous);

                Subscription newSubscription = newParam.addParamValueChangeListener(event -> applyWithImmediateMode());
                paramValueChangeSubscriptions.add(newSubscription);

                previous = newSubscription;

                applyWithImmediateMode();
            }
        };

        condition.addListener(listener);
        conditionListeners.put(condition, listener);
    }

    protected String getSearchBtnCaption() {
        return isApplyImmediately()
                ? getMainMessage("filter.searchBtn.applyImmediately.caption")
                : getMainMessage("filter.search");
    }

    protected class FiltersLookupChangeListener implements Consumer<HasValue.ValueChangeEvent<FilterEntity>> {
        public FiltersLookupChangeListener() {
        }

        @Override
        public void accept(HasValue.ValueChangeEvent<FilterEntity> e) {
            if (!filtersLookupListenerEnabled) {
                return;
            }
            if (e.getValue() != null) {
                setFilterEntity(e.getValue());
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
                final SaveFilterWindow window = (SaveFilterWindow) getWindowManager().openWindow(windowInfo, OpenType.DIALOG, params);
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
                    settingsBtn.focus();
                });
            } else {
                if (saveWithValues) {
                    for (AbstractCondition condition : conditions.toConditionsList()) {
                        if (condition.getParam() != null) {
                            condition.getParam().setDefaultValue(condition.getParam().getValue());
                        }
                    }
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
            Map<String, Object> params = ParamsMap.of(
                    "existingNames",
                    filterEntities.stream()
                            .map(FilterEntity::getName)
                            .collect(Collectors.toList())
            );

            final SaveFilterWindow window = (SaveFilterWindow) getWindowManager().openWindow(windowInfo, OpenType.DIALOG, params);
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
                settingsBtn.focus();
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
            params.put("conditionsTree", conditions);

            // remove subscriptions because if param default value is editing it will invoke value change event
            clearParamValueChangeSubscriptions();

            FilterEditor window = (FilterEditor) getWindowManager().openWindow(windowInfo, OpenType.DIALOG, params);
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
                    applyWithImmediateMode();
                } else {
                    requestFocusToParamEditComponent();
                    // subscribe if editor was closed without changes
                    if (isApplyImmediately()) {
                        subscribeToParamValueChangeEventRecursively(conditions.getRootNodes());
                    }
                }
                settingsBtn.focus();
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
            getDialogs().createOptionDialog(Dialogs.MessageType.CONFIRMATION)
                    .withCaption(getMainMessage("filter.removeDialogTitle"))
                    .withMessage(getMainMessage("filter.removeDialogMessage"))
                    .withActions(new DialogAction(Type.YES).withHandler(event -> {
                                removeFilterEntity();
                                settingsBtn.focus();
                            }),
                            new DialogAction(Type.NO, Status.PRIMARY).withHandler(event -> {
                                settingsBtn.focus();
                            }))
                    .show();
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
            clearParamValueChangeSubscriptions();

            for (AbstractCondition condition : conditions.toConditionsList()) {
                if (!Boolean.TRUE.equals(condition.getHidden()) && condition.getParam() != null) {
                    condition.getParam().setValue(null);
                }
            }

            if (isApplyImmediately()) {
                subscribeToParamValueChangeEventRecursively(conditions.getRootNodes());
                apply(false);
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

    @Nullable
    protected WindowManager getWindowManager() {
        Window window = ComponentsHelper.getWindow(filter);
        if (window != null) {
            return window.getWindowManager();
        }
        return null;
    }

    protected Notifications getNotifications() {
        return ComponentsHelper.getScreenContext(filter).getNotifications();
    }

    protected Dialogs getDialogs() {
        return ComponentsHelper.getScreenContext(filter).getDialogs();
    }

    protected class PinAppliedAction extends AbstractAction {

        public PinAppliedAction() {
            super("pinApplied");
        }

        @Override
        public void actionPerform(Component component) {
            if (adapter.supportsApplyToSelected()) {
                adapter.pinQuery();
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
                String entityType;
                if (target.getItems() instanceof EntityDataUnit) {
                    MetaClass metaClass = ((EntityDataUnit) target.getItems()).getEntityMetaClass();
                    entityType = metaClass.getName();
                } else {
                    throw new UnsupportedOperationException("Unsupported data unit " + target.getItems());
                }

                String[] strings = ValuePathHelper.parse(ComponentsHelper.getFilterComponentPath(filter));
                String componentId = ValuePathHelper.pathSuffix(strings);

                Map<String, Object> params = new HashMap<>();
                params.put("entityType", entityType);
                params.put("items", ownerSelection);
                params.put("componentPath", ComponentsHelper.getFilterComponentPath(filter));
                params.put("componentId", componentId);
                params.put("foldersPane", filterHelper.getFoldersPane());
                params.put("entityClass", adapter.getMetaClass().getJavaClass().getName());
                params.put("query", adapter.getQuery());

                WindowManager wm = (WindowManager) ComponentsHelper.getScreenContext(filter).getScreens();
                WindowInfo windowInfo = windowConfig.getWindowInfo("saveSetInFolder");
                wm.openWindow(windowInfo, OpenType.DIALOG, params);
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

            int size;

            if (target.getItems() instanceof ContainerDataUnit) {
                CollectionContainer container = ((ContainerDataUnit) target.getItems()).getContainer();
                size = container.getItems().size();
            } else if (target.getItems() instanceof DatasourceDataUnit) {
                CollectionDatasource datasource = ((DatasourceDataUnit) target.getItems()).getDatasource();
                size = datasource.getItemIds().size();
            } else {
                throw new UnsupportedOperationException("Unsupported data unit " + target.getItems());
            }

            if (size == selected.size()) {
                filterHelper.removeFolderFromFoldersPane(filterEntity.getFolder());
                removeFilterEntity();

                Window window = ComponentsHelper.getWindow(filter);
                window.getWindowManager().close(window);
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

            MetaClass metaClass = dataLoader == null ?
                    datasource.getMetaClass() : dataLoader.getContainer().getEntityMetaClass();

            screenBuilders.lookup(metaClass.getJavaClass(), filter.getFrame().getFrameOwner())
                    .withSelectHandler(entities -> {
                        String filterXml = filterEntity.getXml();
                        filterEntity.setXml(UserSetHelper.addEntities(filterXml, entities));
                        filterEntity.getFolder().setFilterXml(filterEntity.getXml());
                        filterEntity.setFolder(saveFolder(filterEntity.getFolder()));
                        setFilterEntity(filterEntity);
                    })
                    .show();
        }
    }

    protected static class AppliedFilterHolder {
        public final AppliedFilter filter;
        public final ComponentContainer layout;
        public final Button button;

        protected AppliedFilterHolder(AppliedFilter filter, ComponentContainer layout, Button button) {
            this.filter = filter;
            this.layout = layout;
            this.button = button;
        }
    }

    /**
     * Class creates filter controls layout based on template. See template format in documentation for {@link
     * ClientConfig#getGenericFilterControlsLayout()}
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
            Button button = uiComponents.create(Button.class);
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

    protected interface Adapter {

        MetaClass getMetaClass();

        int getMaxResults();

        void setMaxResults(int maxResults);

        int getFirstResult();

        void setFirstResult(int firstResult);

        void setQueryFilter(QueryFilter filter);

        void setDataLoaderCondition(com.haulmont.cuba.core.global.queryconditions.Condition dataLoaderCondition);

        Map<String, Object> getLastRefreshParameters();

        void addFtsComponentParameter(String parameterName);

        void addFtsCustomParameter(String parameterName);

        void refresh(Map<String, Object> parameters);

        void refreshIfNotSuspended(Map<String, Object> parameters);

        boolean supportsApplyToSelected();

        boolean applyMaxResultsSettingsBeforeLoad();

        void pinQuery();

        void unpinAllQuery();

        String getQuery();

        void setQuery(String query);

        void preventNextDataLoading();
    }

    protected static class LoaderAdapter implements Adapter {

        protected BaseCollectionLoader loader;
        protected Filter filter;
        protected QueryFilter queryFilter;
        protected boolean preventDataLoading;
        protected List<String> lastQueryFilterParameters = new ArrayList<>();
        protected Set<String> ftsComponentParameters = new HashSet<>();
        protected Set<String> ftsCustomParameters = new HashSet<>();

        protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.class);
        protected PersistenceManagerClient persistenceManager = AppBeans.get(PersistenceManagerClient.class);

        /**
         * Condition which was set on DataLoader before applying the filter
         */
        protected com.haulmont.cuba.core.global.queryconditions.Condition dataLoaderCondition;

        protected static final Pattern COMPONENT_PARAM_PATTERN = Pattern.compile("(:)component\\$([\\w.]+)");
        protected static final Pattern CUSTOM_PARAM_PATTERN = Pattern.compile("(:)custom\\$([\\w.]+)");
        protected static final Pattern SESSION_PARAM_PATTERN = Pattern.compile("(:)session\\$([\\w.]+)");

        public LoaderAdapter(BaseCollectionLoader loader, Filter filter) {
            this.filter = filter;
            if (loader.getContainer() == null) {
                throw new IllegalStateException("DataLoader must be connected to a Container");
            }
            this.loader = loader;
        }

        @Override
        public MetaClass getMetaClass() {
            return loader.getContainer().getEntityMetaClass();
        }

        @Override
        public int getMaxResults() {
            return loader.getMaxResults() == Integer.MAX_VALUE && getMetaClass() != null ?
                    persistenceManager.getFetchUI(getMetaClass().getName()) :
                    loader.getMaxResults();
        }

        @Override
        public void setMaxResults(int maxResults) {
            loader.setMaxResults(maxResults);
        }

        @Override
        public int getFirstResult() {
            return loader.getFirstResult();
        }

        @Override
        public void setFirstResult(int firstResult) {
            loader.setFirstResult(firstResult);
        }

        @Override
        public void setQueryFilter(QueryFilter filter) {
            queryFilter = filter;
        }

        @Override
        public void setDataLoaderCondition(com.haulmont.cuba.core.global.queryconditions.Condition dataLoaderCondition) {
            this.dataLoaderCondition = dataLoaderCondition;
        }

        @Override
        public Map<String, Object> getLastRefreshParameters() {
            return Collections.emptyMap();
        }

        @Override
        public void addFtsComponentParameter(String parameterName) {
            ftsComponentParameters.add(parameterName);
        }

        @Override
        public void addFtsCustomParameter(String parameterName) {
            ftsCustomParameters.add(parameterName);
        }

        @Override
        public void refresh(Map<String, Object> parameters) {
            loader.setParameters(parameters);
            loader.load();
        }

        @Override
        public void refreshIfNotSuspended(Map<String, Object> parameters) {
            for (String paramName : lastQueryFilterParameters) {
                loader.removeParameter(paramName);
            }
            lastQueryFilterParameters.clear();


            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                setLoaderParameter(entry.getKey(), entry.getValue());
            }

            if (queryFilter != null) {
                replaceParamNames(queryFilter.getRoot());
                Map<String, Object> loaderParameters = new HashMap<>();
                for (ParameterInfo parameterInfo : queryFilter.getCompiledParameters()) {
                    if (parameterInfo.getType() == ParameterInfo.Type.COMPONENT) {
                        String fullName = parameterInfo.getPath();
                        if (ftsComponentParameters.contains(parameterInfo.getName())) {
                            continue;
                        }
                        int i = fullName.lastIndexOf('.');
                        String name = i == -1 ? fullName : fullName.substring(i + 1);

                        parameterInfo.setType(ParameterInfo.Type.NONE);
                        parameterInfo.setPath(parameterInfo.getPath().replace(".", "_"));

                        Object inputValue = filter.getParamValue(name);
                        if (inputValue != null) {
                            loaderParameters.put(parameterInfo.getFlatName(), filter.getParamValue(name));
                        }
                        lastQueryFilterParameters.add(parameterInfo.getFlatName());
                    } else if (parameterInfo.getType() == ParameterInfo.Type.CUSTOM) {
                        if (ftsCustomParameters.contains(parameterInfo.getPath())) {
                            parameterInfo.setType(ParameterInfo.Type.NONE);
                            lastQueryFilterParameters.add(parameterInfo.getFlatName());
                        }
                        //when the Full-text search filter is used, query parameter names are "__queryKey" and "__sessionId"
                        //we should add them to "lastQueryFilterParameters" list in order these parameters to be removed from
                        //data loader params
                        if (FtsFilterHelper.SESSION_ID_PARAM_NAME.equals(parameterInfo.getPath()) ||
                                FtsFilterHelper.QUERY_KEY_PARAM_NAME.equals(parameterInfo.getPath())) {
                            lastQueryFilterParameters.add(parameterInfo.getPath());
                        }
                    } else if (parameterInfo.getType() == ParameterInfo.Type.SESSION) {
                        UserSession userSession = userSessionSource.getUserSession();
                        Object value = userSession.getAttribute(parameterInfo.getPath());
                        if (value instanceof String && parameterInfo.isCaseInsensitive()) {
                            value = makeCaseInsensitive((String) value);
                        }
                        parameterInfo.setType(ParameterInfo.Type.NONE);
                        loaderParameters.put(parameterInfo.getFlatName(), value);
                        lastQueryFilterParameters.add(parameterInfo.getFlatName());
                    }
                }
                ftsComponentParameters.clear();
                ftsCustomParameters.clear();


                com.haulmont.cuba.core.global.queryconditions.Condition condition = queryFilter.toQueryCondition(loaderParameters.keySet());

                if (dataLoaderCondition != null) {
                    com.haulmont.cuba.core.global.queryconditions.LogicalCondition combined =
                            new com.haulmont.cuba.core.global.queryconditions.LogicalCondition(
                                    com.haulmont.cuba.core.global.queryconditions.LogicalCondition.Type.AND);
                    combined.add(dataLoaderCondition);
                    if (condition != null) {
                        combined.add(condition);
                    }
                    condition = combined;
                }

                loader.setCondition(condition);

                Collection<String> actualizedParameters = queryFilter.getActualizedQueryParameterNames(loaderParameters.keySet());
                for (Map.Entry<String, Object> entry : loaderParameters.entrySet()) {
                    if (actualizedParameters.contains(entry.getKey())) {
                        setLoaderParameter(entry.getKey(), entry.getValue());
                    }
                }
            } else {
                loader.setCondition(dataLoaderCondition);
            }

            if (preventDataLoading) {
                preventDataLoading = false;
            } else {
                loader.load();
            }
        }

        protected void setLoaderParameter(String key, Object value) {
            if (value == null || (value instanceof String && value.equals(""))) {
                loader.removeParameter(key);
            } else {
                loader.setParameter(key, value);
            }
        }

        /**
         * Recursively replaces parameter names in condition's text, e.g. "u.name like :component$usersFilter.name26607" -&gt; "u.name like
         * :usersFilter_name26607" "u.name like :custom$usersFilter.name26607" -&gt; "u.name like :usersFilter_name26607" "u.name like
         * :session$userLogin" -&gt; "u.name like :session_userLogin"
         */
        protected void replaceParamNames(Condition condition) {
            if (condition instanceof Clause) {
                for (ParameterInfo parameterInfo : condition.getCompiledParameters()) {
                    if (parameterInfo.getType() == ParameterInfo.Type.COMPONENT) {
                        replaceParamName(condition, COMPONENT_PARAM_PATTERN);
                    } else if (parameterInfo.getType() == ParameterInfo.Type.CUSTOM) {
                        replaceParamName(condition, CUSTOM_PARAM_PATTERN);
                    } else if (parameterInfo.getType() == ParameterInfo.Type.SESSION) {
                        replaceParamName(condition, SESSION_PARAM_PATTERN);
                    }
                }
            } else if (condition instanceof LogicalCondition) {
                for (Condition nestedCond : condition.getConditions()) {
                    replaceParamNames(nestedCond);
                }
            }
        }

        protected void replaceParamName(Condition condition, Pattern pattern) {
            Matcher m = pattern.matcher(((Clause) condition).getContent());
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                m.appendReplacement(sb, m.group(1) + m.group(2).replace('.', '_'));
            }
            m.appendTail(sb);
            ((Clause) condition).setContent(sb.toString());
        }

        protected String makeCaseInsensitive(String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(ParametersHelper.CASE_INSENSITIVE_MARKER);
            if (!value.startsWith("%")) {
                sb.append("%");
            }
            sb.append(value);
            if (!value.endsWith("%")) {
                sb.append("%");
            }
            return sb.toString();
        }

        @Override
        public boolean supportsApplyToSelected() {
            return loader instanceof LoaderSupportsApplyToSelected;
        }

        @Override
        public boolean applyMaxResultsSettingsBeforeLoad() {
            return true;
        }

        @Override
        public void pinQuery() {
            UserSession userSession = userSessionSource.getUserSession();
            LoaderSupportsApplyToSelected supportsApplyToSelected = (LoaderSupportsApplyToSelected) loader;
            List<LoadContext.Query> prevQueries = supportsApplyToSelected.getPrevQueries();

            if (prevQueries == null) {
                supportsApplyToSelected.setPrevQueries(new LinkedList<>());

                Integer queryKey = userSession.getAttribute("_queryKey");
                queryKey = queryKey != null ? queryKey + 1 : 1;

                userSession.setAttribute("_queryKey", queryKey);
                supportsApplyToSelected.setQueryKey(queryKey);
            }

            if (supportsApplyToSelected.getLastQuery() != null) {
                supportsApplyToSelected.getPrevQueries().add(supportsApplyToSelected.getLastQuery());
            }
        }

        @Override
        public void unpinAllQuery() {
            LoaderSupportsApplyToSelected supportsApplyToSelected = (LoaderSupportsApplyToSelected) loader;
            supportsApplyToSelected.setPrevQueries(null);
            supportsApplyToSelected.setQueryKey(null);
        }

        @Override
        public void setQuery(String query) {
            loader.setQuery(query);
        }

        @Override
        public String getQuery() {
            return loader.getQuery();
        }

        @Override
        public void preventNextDataLoading() {
            preventDataLoading = true;
        }
    }

    protected static class DatasourceAdapter implements Adapter {

        protected CollectionDatasource datasource;

        public DatasourceAdapter(CollectionDatasource datasource) {
            this.datasource = datasource;
        }

        @Override
        public MetaClass getMetaClass() {
            return datasource.getMetaClass();
        }

        @Override
        public int getMaxResults() {
            return datasource.getMaxResults();
        }

        @Override
        public void setMaxResults(int maxResults) {
            datasource.setMaxResults(maxResults);
        }

        @Override
        public int getFirstResult() {
            if (datasource instanceof CollectionDatasource.SupportsPaging) {
                return ((CollectionDatasource.SupportsPaging) datasource).getFirstResult();
            }
            return 0;
        }

        @Override
        public void setFirstResult(int firstResult) {
            if (datasource instanceof CollectionDatasource.SupportsPaging) {
                ((CollectionDatasource.SupportsPaging) datasource).setFirstResult(firstResult);
            }
        }

        @Override
        public void setQueryFilter(QueryFilter filter) {
            datasource.setQueryFilter(filter);
        }

        @Override
        public void setDataLoaderCondition(com.haulmont.cuba.core.global.queryconditions.Condition dataLoaderCondition) {
        }

        @Override
        public Map<String, Object> getLastRefreshParameters() {
            return datasource.getLastRefreshParameters();
        }

        @Override
        public void addFtsComponentParameter(String parameterName) {
        }

        @Override
        public void addFtsCustomParameter(String parameterName) {
        }

        @Override
        public void refresh(Map<String, Object> parameters) {
            datasource.refresh(parameters);
        }

        @Override
        public void refreshIfNotSuspended(Map<String, Object> parameters) {
            if (datasource instanceof CollectionDatasource.Suspendable)
                ((CollectionDatasource.Suspendable) datasource).refreshIfNotSuspended(parameters);
            else
                datasource.refresh(parameters);
        }

        @Override
        public boolean supportsApplyToSelected() {
            return true;
        }

        @Override
        public boolean applyMaxResultsSettingsBeforeLoad() {
            return false;
        }

        @Override
        public void pinQuery() {
            ((CollectionDatasource.SupportsApplyToSelected) datasource).pinQuery();
        }

        @Override
        public void unpinAllQuery() {
            ((CollectionDatasource.SupportsApplyToSelected) datasource).unpinAllQuery();
        }

        @Override
        public String getQuery() {
            return datasource.getQuery();
        }

        @Override
        public void setQuery(String query) {
            datasource.setQuery(query);
        }

        @Override
        public void preventNextDataLoading() {
            // do nothing
        }
    }
}
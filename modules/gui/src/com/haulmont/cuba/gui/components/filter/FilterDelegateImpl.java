/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.entity.AppFolder;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManagerProvider;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action.Status;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.condition.CustomCondition;
import com.haulmont.cuba.gui.components.filter.edit.FilterEditor;
import com.haulmont.cuba.gui.components.filter.filterselect.FilterSelectWindow;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.filter.DenyingClause;
import com.haulmont.cuba.gui.filter.QueryFilter;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.gui.settings.SettingsImpl;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.FilterEntity;
import com.haulmont.cuba.security.entity.SearchFolder;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gorbunkov
 * @version $Id$
 */
@ManagedBean(FilterDelegate.NAME)
@Scope("prototype")
public class FilterDelegateImpl implements FilterDelegate {

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

    protected FtsFilterHelper ftsFilterHelper;
    protected DataService dataService;
    protected PersistenceManagerService persistenceManager;
    protected ClientConfig clientConfig;
    protected GlobalConfig globalConfig;
    protected FtsConfig ftsConfig;
    protected AddConditionHelper addConditionHelper;
    protected ThemeConstants theme;
    protected WindowManager windowManager;

    protected Filter filter;
    protected FilterEntity adHocFilter;
    protected ConditionsTree conditions;
    protected ConditionsTree prevConditions;
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
    protected TextField ftsSearchCriteriaField;
    protected CheckBox ftsSwitch;
    protected LinkButton addConditionBtn;
    protected HBoxLayout filtersPopupBox;
    protected Button searchBtn;
    protected Component controlsLayoutGap;

    protected String caption;
    protected int maxResults = -1;
    protected boolean useMaxResults;
    protected boolean textMaxResults;
    protected Boolean manualApplyRequired;
    protected boolean folderActionsEnabled = true;
    protected boolean filtersLookupListenerEnabled = true;
    protected boolean filtersPopupDisplayed = false;
    protected boolean filtersLookupDisplayed = false;
    protected boolean maxResultsDisplayed = false;
    protected boolean editable = true;
    protected FilterMode filterMode;
    protected boolean filterSavingPossible = true;
    protected Integer columnsCount;
    protected String initialWindowCaption;
    protected String conditionsLocation;
    protected boolean filterActionsCreated = false;

    protected SaveAsAction saveAsAction;
    protected EditAction editAction;
    protected MakeDefaultAction makeDefaultAction;
    protected RemoveAction removeAction;
    protected PinAppliedAction pinAppliedAction;
    protected SaveAsFolderAction saveAsAppFolderAction;
    protected SaveAsFolderAction saveAsSearchFolderAction;
    protected LookupField filtersLookup;

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
        persistenceManager = AppBeans.get(PersistenceManagerService.class);
        globalConfig = configuration.getConfig(GlobalConfig.class);
        clientConfig = configuration.getConfig(ClientConfig.class);
        ftsConfig = configuration.getConfig(FtsConfig.class);
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
            groupBoxLayout.setOrientation(GroupBoxLayout.Orientation.VERTICAL);
            groupBoxLayout.setStyleName("cuba-generic-filter");
            groupBoxLayout.setWidth("100%");
            layout = componentsFactory.createComponent(VBoxLayout.class);
            layout.setWidth("100%");
            groupBoxLayout.add(layout);
            if (caption == null)
                setCaption(getMessage("Filter.groupBoxCaption"));
        } else {
            Collection<Component> components = layout.getComponents();
            for (Component component : components) {
                layout.remove(component);
            }
        }
        layout.setSpacing(false);

        appliedFiltersLayout = componentsFactory.createComponent(VBoxLayout.class);

        conditionsLayout = componentsFactory.createComponent(HBoxLayout.class);
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

        filtersPopupBox = componentsFactory.createComponent(HBoxLayout.class);
        filtersPopupBox.setStyleName("filter-search-button-layout");

        searchBtn = componentsFactory.createComponent(Button.class);
        filtersPopupBox.add(searchBtn);
        searchBtn.setStyleName("filter-search-button");
        searchBtn.setCaption(getMessage("Filter.search"));
        searchBtn.setIcon("icons/search.png");
        searchBtn.setAction(new AbstractAction("search") {
            @Override
            public void actionPerform(Component component) {
                apply(false);
            }
        });

        filtersPopupButton = componentsFactory.createComponent(PopupButton.class);
        filtersPopupBox.add(filtersPopupButton);

        filtersLookup = componentsFactory.createComponent(LookupField.class);
        filtersLookup.setWidth(theme.get("cuba.gui.filter.select.width"));
        filtersLookup.addListener(new FiltersLookupChangeListener());
        filterHelper.setLookupNullSelectionAllowed(filtersLookup, false);

        addConditionBtn = componentsFactory.createComponent(LinkButton.class);
        addConditionBtn.setAlignment(Component.Alignment.MIDDLE_LEFT);
        addConditionBtn.setCaption(getMessage("Filter.addCondition"));
        addConditionBtn.setAction(new AbstractAction("openAddConditionDlg") {
            @Override
            public void actionPerform(Component component) {
                addConditionHelper.addCondition(conditions);
            }
        });

        controlsLayoutGap = componentsFactory.createComponent(Label.class);
        controlsLayout.add(controlsLayoutGap);
        controlsLayout.expand(controlsLayoutGap);

        settingsBtn = componentsFactory.createComponent(PopupButton.class);
        settingsBtn.setIcon("icons/gear.png");
        createFilterActions();

        createMaxResultsLayout();
        if (maxResultsDisplayed) {
            controlsLayout.add(maxResultsLayout);
        }

        createFtsSwitch();
        ftsSwitch.setAlignment(Component.Alignment.MIDDLE_RIGHT);

        String layoutDescription = clientConfig.getGenericFilterControlsLayout();
        ControlsLayoutBuilder controlsLayoutBuilder = createControlsLayoutBuilder(layoutDescription);
        controlsLayoutBuilder.build();
    }

    protected void createControlsLayoutForFts() {
        controlsLayout = componentsFactory.createComponent(HBoxLayout.class);
        controlsLayout.setSpacing(true);
        controlsLayout.setWidth("100%");

        ftsSearchCriteriaField = componentsFactory.createComponent(TextField.class);
        ftsSearchCriteriaField.setAlignment(Component.Alignment.MIDDLE_LEFT);
        ftsSearchCriteriaField.setWidth(theme.get("cuba.gui.filter.ftsSearchCriteriaField.width"));
        ftsSearchCriteriaField.setInputPrompt(getMessage("Filter.enterSearchPhrase"));
        ftsSearchCriteriaField.requestFocus();
        filterHelper.addShortcutListener(ftsSearchCriteriaField, new FilterHelper.ShortcutListener("ftsSearch", new KeyCombination(KeyCombination.Key.ENTER, null)) {
            @Override
            public void handleShortcutPressed() {
                applyFts();
            }
        });
        controlsLayout.add(ftsSearchCriteriaField);

        searchBtn = componentsFactory.createComponent(Button.class);
        searchBtn.setCaption(getMessage("Filter.search"));
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
        if (maxResultsDisplayed) {
            controlsLayout.add(maxResultsLayout);
        }

        createFtsSwitch();
        ftsSwitch.setAlignment(Component.Alignment.MIDDLE_RIGHT);
        controlsLayout.add(ftsSwitch);
    }

    protected void createFtsSwitch() {
        ftsSwitch = componentsFactory.createComponent(CheckBox.class);
        ftsSwitch.setCaption(getMessage("Filter.ftsSwitch"));
        ftsSwitch.setValue(filterMode == FilterMode.FTS_MODE);
        ftsSwitch.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                filterMode = Boolean.TRUE.equals(value) ? FilterMode.FTS_MODE : FilterMode.GENERIC_MODE;
                if (filterMode == FilterMode.FTS_MODE) {
                    prevConditions = conditions;
                    ((CollectionDatasource.SupportsApplyToSelected) datasource).unpinAllQuery();
                    appliedFilters.clear();
                    lastAppliedFilter = null;
                }
                conditions = (filterMode == FilterMode.GENERIC_MODE) ? prevConditions : new ConditionsTree();
                createLayout();
                initMaxResults();
                if (filterMode == FilterMode.GENERIC_MODE) {
                    fillConditionsLayout(ConditionsFocusType.FIRST);
                    setFilterActionsEnabled();
                    initFilterSelectComponents();
                }
                updateWindowCaption();
            }
        });
    }

    protected void createMaxResultsLayout() {
        maxResultsLayout = componentsFactory.createComponent(HBoxLayout.class);
        maxResultsLayout.setSpacing(true);
        maxResultsLayout.setAlignment(Component.Alignment.MIDDLE_RIGHT);
        Label maxResultsLabel1 = componentsFactory.createComponent(Label.class);
        maxResultsLabel1.setValue(messages.getMainMessage("filter.maxResults.label1"));
        maxResultsLabel1.setAlignment(Component.Alignment.MIDDLE_RIGHT);
        maxResultsLayout.add(maxResultsLabel1);

        maxResultsTextField = componentsFactory.createComponent(TextField.class);
        maxResultsTextField.setAlignment(Component.Alignment.MIDDLE_RIGHT);
        maxResultsTextField.setMaxLength(4);
        maxResultsTextField.setWidth(theme.get("cuba.gui.Filter.maxResults.width"));
        maxResultsTextField.setDatatype(Datatypes.get("int"));

        maxResultsLookupField = componentsFactory.createComponent(LookupField.class);
        maxResultsLookupField.setAlignment(Component.Alignment.MIDDLE_RIGHT);
        maxResultsLookupField.setWidth(theme.get("cuba.gui.Filter.maxResults.lookup.width"));
        filterHelper.setLookupTextInputAllowed(maxResultsLookupField, false);
        List<Integer> options = Arrays.asList(20, 50, 100, 500, 1000, 5000);
        maxResultsLookupField.setOptionsList(options);

        maxResultsField = textMaxResults ? maxResultsTextField : maxResultsLookupField;
        maxResultsLayout.add(maxResultsField);
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
            setFilterEntity(defaultFilter);

        if (defaultFilter != adHocFilter) {
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

    /**
     * Sets filter entity, creates condition editor components and applies filter if necessary
     */
    @Override
    public void setFilterEntity(FilterEntity filterEntity) {
        this.filterEntity = filterEntity;
        conditions = FilterParser.getConditions(filter, filterEntity.getXml());
        initialConditions = conditions.toConditionsList();
        for (AbstractCondition condition : conditions.toConditionsList()) {
            condition.addListener(new AbstractCondition.Listener() {
                @Override
                public void captionChanged() {}

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
        fillConditionsLayout(ConditionsFocusType.FIRST);
        setConditionsLayoutVisible(true);

        if (!filterEntity.equals(adHocFilter) && (BooleanUtils.isTrue(filterEntity.getApplyDefault()) ||
                BooleanUtils.isTrue(filterEntity.getIsSet()) ||
                !getResultingManualApplyRequired()))
            apply(true);

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
        conditionsLayout.setVisible(visible);
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

        boolean editActionEnabled = !isSet && filterEditable && userCanEditFilters;
        filterSavingPossible = editActionEnabled &&
                ((isGlobal && userCanEditGlobalFilter) || (!isGlobal && createdByCurrentUser)) &&
                ((!isFolder && !hasCode) || isSearchFolder || (isAppFolder && userCanEditGlobalAppFolder));
        boolean saveActionEnabled = filterSavingPossible && (isFolder || isFilterModified());
        boolean saveAsActionEnabled = !isSet && filterEditable && userCanEditFilters;
        boolean removeActionEnabled = !isSet &&
                (!hasCode && !isFolder) &&
                ((isGlobal && userCanEditGlobalFilter) || (!isGlobal && createdByCurrentUser)) &&
                !isAdHocFilter && filterEditable && userCanEditFilters;
        boolean makeDefaultActionEnabled = !isDefault && !isFolder && !isSet && !isAdHocFilter;
        boolean pinAppliedActionEnabled = lastAppliedFilter != null
                && !(lastAppliedFilter.getFilterEntity() == adHocFilter && lastAppliedFilter.getConditions().getRoots().size() == 0);
        boolean saveAsSearchFolderActionEnabled = !isFolder && !hasCode;
        boolean saveAsAppFolderActionEnabled = !isFolder && !hasCode && userCanEditGlobalAppFolder;

        saveAction.setEnabled(saveActionEnabled);
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

    protected void createFilterActions() {
        saveAction = new SaveAction();
        saveAsAction = new SaveAsAction();
        editAction = new EditAction();
        makeDefaultAction = new MakeDefaultAction();
        removeAction = new RemoveAction();
        pinAppliedAction = new PinAppliedAction();
        saveAsAppFolderAction = new SaveAsFolderAction(true);
        saveAsSearchFolderAction = new SaveAsFolderAction(false);
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

        String newXml = FilterParser.getXml(conditions, Param.ValueProperty.VALUE);

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
            commitHandler = new Runnable() {
                @Override
                public void run() {
                    AbstractSearchFolder savedFolder = saveFolder(folder);
                    filterEntity.setFolder(savedFolder);
                }
            };
        } else {
            commitHandler = new Runnable() {
                @Override
                public void run() {
                    AbstractSearchFolder savedFolder = saveFolder(folder);
                    filterEntity.setFolder(savedFolder);
                }
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
        layout.setSpacing(false);
        for (Component component : conditionsLayout.getComponents()) {
            conditionsLayout.remove(component);
        }

        boolean hasGroups = false;
        for (AbstractCondition condition : conditions.getRoots()) {
            if (condition.isGroup() && !condition.getHidden()) {
                hasGroups = true;
                break;
            }
        }

        if (hasGroups && conditions.getRootNodes().size() > 1) {
            GroupBoxLayout groupBox = componentsFactory.createComponent(GroupBoxLayout.class);
            groupBox.setWidth("100%");
            groupBox.setCaption(getMessage("GroupType.AND"));
            conditionsLayout.add(groupBox);
            recursivelyCreateConditionsLayout(conditionsFocusType, false, conditions.getRootNodes(), groupBox, 0);
        } else {
            recursivelyCreateConditionsLayout(conditionsFocusType, false, conditions.getRootNodes(), conditionsLayout, 0);
        }

        if (!conditionsLayout.getComponents().isEmpty()) layout.setSpacing(true);
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
            Integer conditionWidth = condition.isGroup() ? conditionsCount : condition.getWidth();
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
                grid.add(labelAndOperationCellContent, nextColumnStart * 2, row, nextColumnStart * 2, row);
                if (nextColumnStart != 0)
                    labelAndOperationCellContent.setMargin(false, false, false, true);
            }
            if (paramEditComponentCellContent != null) {
                paramEditComponentCellContent.setAlignment(Component.Alignment.MIDDLE_LEFT);
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
                        firstParamEditor.requestFocus();
                    break;
                case LAST:
                    if (lastParamEditor != null)
                        lastParamEditor.requestFocus();
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
        ParamEditor paramEditor = new ParamEditor(condition, conditionRemoveEnabled, isEditable() && userCanEditFilers());
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
        String filterXml = filterEntity.getFolder() == null ? FilterParser.getXml(conditions, Param.ValueProperty.DEFAULT_VALUE)
                : FilterParser.getXml(conditions, Param.ValueProperty.VALUE);
        return !StringUtils.equals(filterXml, initialFilterEntity.getXml());
    }

    protected void updateFilterModifiedIndicator() {
        boolean filterModified = isFilterModified();
        saveAction.setEnabled(filterSavingPossible && filterModified);

        String currentCaption = groupBoxLayout.getCaption();
        if (filterModified && !currentCaption.endsWith(MODIFIED_INDICATOR_SYMBOL)) {
            groupBoxLayout.setCaption(currentCaption + MODIFIED_INDICATOR_SYMBOL);
        }
        if (!filterModified && currentCaption.endsWith(MODIFIED_INDICATOR_SYMBOL)) {
            groupBoxLayout.setCaption(currentCaption.substring(0, currentCaption.length() - 1));
        }
    }

    /**
     * Load filter entities from database and saves them in {@code filterEntities} collection.
     */
    protected void loadFilterEntities() {
        LoadContext ctx = new LoadContext(metadata.getExtendedEntities().getEffectiveMetaClass(FilterEntity.class));
        ctx.setView("app");

        User user = userSessionSource.getUserSession().getCurrentOrSubstitutedUser();
        MetaClass effectiveMetaClass = metadata.getExtendedEntities().getEffectiveMetaClass(FilterEntity.class);

        ctx.setQueryString("select f from " + effectiveMetaClass.getName() + " f " +
                "where f.componentId = :component and (f.user is null or f.user.id = :userId) order by f.name")
                .setParameter("component", ComponentsHelper.getFilterComponentPath(filter))
                .setParameter("userId", user.getId());

        filterEntities = new ArrayList<>(dataService.<FilterEntity>loadList(ctx));
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
        return null;
    }

    protected void initFiltersPopupButton() {
        filtersPopupButton.removeAllActions();
        addFiltersPopupActions();
    }

    protected void addFiltersPopupActions() {
        addResetFilterAction(filtersPopupButton);

        Collections.sort(
                filterEntities,
                new Comparator<FilterEntity>() {
                    @Override
                    public int compare(FilterEntity f1, FilterEntity f2) {
                        return getFilterCaption(f1).compareTo(getFilterCaption(f2));
                    }
                }
        );

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
                final FilterSelectWindow window = (FilterSelectWindow) windowManager.openWindow(windowInfo,
                        WindowManager.OpenType.DIALOG,
                        Collections.<String, Object>singletonMap("filterEntities", filterEntities));
                window.addListener(new Window.CloseListener() {
                    @Override
                    public void windowClosed(String actionId) {
                        if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                            FilterEntity selectedEntity = window.getFilterEntity();
                            setFilterEntity(selectedEntity);
                        }
                    }
                });
            }

            @Override
            public String getCaption() {
                return formatMessage("Filter.showMore", filterEntities.size());
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
                return getMessage("Filter.resetFilter");
            }
        });
    }

    protected void initFiltersLookup() {
        Map<Object, String> captionsMap = new LinkedHashMap<>();
        for (FilterEntity entity : filterEntities) {
            String caption = getFilterCaption(entity);
            if (entity.getIsDefault()) {
                caption += " " + getMessage("Filter.default");
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
        String emptyXml = FilterParser.getXml(new ConditionsTree(), Param.ValueProperty.VALUE);
        adHocFilter.setXml(emptyXml);
        adHocFilter.setComponentId(ComponentsHelper.getFilterComponentPath(filter));
        adHocFilter.setUser(userSessionSource.getUserSession().getCurrentOrSubstitutedUser());
        adHocFilter.setName(getMessage("Filter.adHocFilter"));
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
        label.setAlignment(Component.Alignment.MIDDLE_LEFT);

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

                windowManager.showOptionDialog(messages.getMainMessage("removeApplied.title"),
                        messages.getMainMessage("removeApplied.message"), Frame.MessageType.WARNING,
                        new Action[]{
                                new DialogAction(Type.YES) {
                                    @Override
                                    public void actionPerform(Component component) {
                                        for (AppliedFilterHolder holder : appliedFilters) {
                                            appliedFiltersLayout.remove(holder.layout);
                                            FilterDelegateImpl.this.layout.remove(appliedFiltersLayout);
                                        }
                                        appliedFilters.clear();
                                        ((CollectionDatasource.SupportsApplyToSelected) datasource).unpinAllQuery();
                                    }
                                },
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
                    name = getMessage("Filter.setPrefix") + " " + name;
                else
                    name = getMessage("Filter.folderPrefix") + " " + name;
            }
        } else
            name = "";
        return name;
    }

    protected String getMessage(String key) {
        return messages.getMessage(FilterDelegateImpl.class, key);
    }

    protected String formatMessage(String key, Object... params) {
        return messages.formatMessage(FilterDelegateImpl.class, key, params);
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
            QueryFilter queryFilter = new QueryFilter(new DenyingClause(), datasource.getMetaClass().getName());
            if (dsQueryFilter != null) {
                queryFilter = new QueryFilter(dsQueryFilter, queryFilter);
            }
            datasource.setQueryFilter(queryFilter);
        }

        if (datasource instanceof CollectionDatasource.Lazy || datasource instanceof HierarchicalDatasource) {
            setUseMaxResults(false);
        } else if (useMaxResults) {
            initMaxResults();
        }

        if (!isFtsModeEnabled()) {
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

        if (maxResultsDisplayed) {
            if (!textMaxResults) {
                List<Integer> optionsList = ((LookupField) maxResultsField).getOptionsList();
                if (!optionsList.contains(maxResults)) {
                    ArrayList<Integer> newOptions = new ArrayList<>(optionsList);
                    newOptions.add(maxResults);
                    Collections.sort(newOptions);
                    ((LookupField) maxResultsField).setOptionsList(newOptions);
                }
            }
            maxResultsField.setValue(maxResults);
        }

        datasource.setMaxResults(maxResults);
    }

    protected boolean isFtsModeEnabled() {
        return ftsConfig.getEnabled()
                && ftsFilterHelper != null
                && datasource != null
                && ftsFilterHelper.isEntityIndexed(datasource.getMetaClass().getName());
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

        if (maxResultsDisplayed) {
            Security security = AppBeans.get(Security.NAME);
            maxResultsLayout.setVisible(useMaxResults && security.isSpecificPermitted("cuba.gui.filter.maxResults"));
        }
    }

    @Override
    public boolean getUseMaxResults() {
        return useMaxResults;
    }

    @Override
    public void setTextMaxResults(boolean textMaxResults) {
        boolean valueChanged = this.textMaxResults != textMaxResults;
        this.textMaxResults = textMaxResults;
        if (maxResultsDisplayed && valueChanged) {
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
    public boolean apply(boolean isNewWindow) {
        if (clientConfig.getGenericFilterChecking()) {
            if (filterEntity != null && conditions.getRoots().size() > 0) {
                boolean haveCorrectCondition = hasCorrectCondition();
                if (!haveCorrectCondition) {
                    if (!isNewWindow) {
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
                if (!isNewWindow) {
                    windowManager.showNotification(messages.getMainMessage("filter.emptyRequiredConditions"),
                            Frame.NotificationType.HUMANIZED);
                }
                return false;
            }
            setFilterActionsEnabled();
        }

        applyDatasourceFilter();
        initDatasourceMaxResults();
        refreshDatasource();

        if ((applyTo != null) && (Table.class.isAssignableFrom(applyTo.getClass()))) {
            filterHelper.removeTableFtsTooltips((Table) applyTo);
        }

        return true;
    }

    protected void applyFts() {
        if (ftsFilterHelper == null)
            return;

        String searchTerm = ftsSearchCriteriaField.getValue();
        if (Strings.isNullOrEmpty(searchTerm) && clientConfig.getGenericFilterChecking()) {
            windowManager.showNotification(getMessage("Filter.fillSearchCondition"), Frame.NotificationType.TRAY);
            return;
        }

        Map<String, Object> params = new HashMap<>();

        if (!Strings.isNullOrEmpty(searchTerm)) {
            FtsFilterHelper.FtsSearchResult ftsSearchResult = ftsFilterHelper.search(searchTerm, datasource.getMetaClass().getName());
            int queryKey = ftsSearchResult.getQueryKey();
            params.put("sessionId", userSessionSource.getUserSession().getId());
            params.put("queryKey", queryKey);

            CustomCondition ftsCondition = ftsFilterHelper.createFtsCondition(queryKey);
            conditions.getRootNodes().add(new Node<AbstractCondition>(ftsCondition));

            if ((applyTo != null) && (Table.class.isAssignableFrom(applyTo.getClass()))) {
                filterHelper.initTableFtsTooltips((Table) applyTo, ftsSearchResult.getHitInfos());
            }
        } else if ((applyTo != null) && (Table.class.isAssignableFrom(applyTo.getClass()))) {
            filterHelper.initTableFtsTooltips((Table) applyTo, Collections.<UUID, String>emptyMap());
        }

        applyDatasourceFilter();
        initDatasourceMaxResults();
        datasource.refresh(params);
    }

    protected void initDatasourceMaxResults() {
        if (this.maxResults != -1) {
            datasource.setMaxResults(maxResults);
        } else if (maxResultsDisplayed && useMaxResults) {
            Integer maxResults = maxResultsField.getValue();
            if (maxResults != null && maxResults > 0) {
                datasource.setMaxResults(maxResults);
            } else {
                datasource.setMaxResults(persistenceManager.getMaxFetchUI(datasource.getMetaClass().getName()));
            }
        }
        if (datasource instanceof CollectionDatasource.SupportsPaging) {
            ((CollectionDatasource.SupportsPaging) datasource).setFirstResult(0);
        }
    }

    protected void applyDatasourceFilter() {
        if (datasource != null) {

            String currentFilterXml = FilterParser.getXml(conditions, Param.ValueProperty.VALUE);

            if (!Strings.isNullOrEmpty(currentFilterXml)) {
                Element element = Dom4j.readDocument(currentFilterXml).getRootElement();
                QueryFilter queryFilter = new QueryFilter(element, datasource.getMetaClass().getName());

                if (dsQueryFilter != null) {
                    queryFilter = new QueryFilter(dsQueryFilter, queryFilter);
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
    protected void refreshDatasource() {
        if (datasource instanceof CollectionDatasource.Suspendable)
            ((CollectionDatasource.Suspendable) datasource).refreshIfNotSuspended();
        else
            datasource.refresh();
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
        groupBoxLayout.setCaption(caption);
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
            throw new UnsupportedOperationException("Filter contains only one level of subcomponents");
        }
    }

    @Override
    public void applySettings(Element element) {
        Element groupBoxExpandedEl = element.element("groupBoxExpanded");
        if (groupBoxExpandedEl != null) {
            Boolean expanded = Boolean.valueOf(groupBoxExpandedEl.getText());
            groupBoxLayout.setExpanded(expanded);
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
        if (!ObjectUtils.equals(oldDef, newDef)) {
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
        if (!ObjectUtils.equals(oldApplyDef, newApplyDef)) {
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
        if (!ObjectUtils.equals(oldGroupBoxExpandedValue, newGroupBoxExpandedValue)) {
            groupBoxExpandedEl.setText(newGroupBoxExpandedValue.toString());
            changed = true;
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

        Action addToSet = table.getAction("addToSet");

        Action addToCurrSet = table.getAction("addToCurSet");
        Action removeFromCurrSet = table.getAction("removeFromCurSet");

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
                addToCurSetBtn.setCaption(getMessage("addToCurSet"));
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
                removeFromCurSetBtn.setCaption(getMessage("removeFromCurSet"));
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
                addToSetBtn.setCaption(getMessage("addToSet"));
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
        addConditionBtn.setEnabled(editable && userCanEditFilers());
        //do not process actions if method is invoked from filter loader
        if (filterActionsCreated && filterEntity != null) {
            setFilterActionsEnabled();
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
    public void addListener(Component.Collapsable.ExpandListener listener) {
        groupBoxLayout.addListener(listener);
    }

    @Override
    public void removeListener(Component.Collapsable.ExpandListener listener) {
        groupBoxLayout.removeListener(listener);
    }

    @Override
    public void addListener(Component.Collapsable.CollapseListener listener) {
        groupBoxLayout.addListener(listener);
    }

    @Override
    public void removeListener(Component.Collapsable.CollapseListener listener) {
        groupBoxLayout.removeListener(listener);
    }

    @Override
    public void setFilter(Filter filter) {
        this.filter = filter;
        addConditionHelper = new AddConditionHelper(filter, new AddConditionHelper.Handler() {
            @Override
            public void handle(AbstractCondition condition) {
                addCondition(condition);
            }
        });
    }

    protected void addCondition(AbstractCondition condition) {
        conditions.getRootNodes().add(new Node<>(condition));
        fillConditionsLayout(ConditionsFocusType.LAST);
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

        groupBoxLayout.setCaption(Strings.isNullOrEmpty(filterTitle) ? caption : caption + ": " + filterTitle);
    }

    protected ControlsLayoutBuilder createControlsLayoutBuilder(String layoutDescription) {
        return new ControlsLayoutBuilder(layoutDescription);
    }

    protected class FiltersLookupChangeListener implements ValueListener {

        public FiltersLookupChangeListener() {
        }

        @Override
        public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
            if (!filtersLookupListenerEnabled) return;
            if (value instanceof FilterEntity) {
                setFilterEntity((FilterEntity) value);
            }
        }
    }

    protected class SaveAction extends AbstractAction {

        protected SaveAction() {
            super("save");
        }

        @Override
        public void actionPerform(Component component) {
            if (PersistenceHelper.isNew(filterEntity) && filterEntity.getFolder() == null) {
                WindowInfo windowInfo = windowConfig.getWindowInfo("saveFilter");
                Map<String, Object> params = new HashMap<>();
                if (!getMessage("Filter.adHocFilter").equals(filterEntity.getName())) {
                    params.put("filterName", filterEntity.getName());
                }
                final SaveFilterWindow window = (SaveFilterWindow) windowManager.openWindow(windowInfo, WindowManager.OpenType.DIALOG, params);
                window.addListener(new Window.CloseListener() {
                    @Override
                    public void windowClosed(String actionId) {
                        if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                            String filterName = window.getFilterName();
                            filterEntity.setName(filterName);
                            filterEntity.setXml(FilterParser.getXml(conditions, Param.ValueProperty.DEFAULT_VALUE));
                            saveFilterEntity();
                            initAdHocFilter();
                            initFilterSelectComponents();
                            updateWindowCaption();

                            //recreate layout to remove delete conditions buttons
                            initialConditions = conditions.toConditionsList();
                            fillConditionsLayout(ConditionsFocusType.NONE);
                        }
                    }
                });
            } else {
                String xml = filterEntity.getFolder() == null ?  FilterParser.getXml(conditions, Param.ValueProperty.DEFAULT_VALUE)
                        : FilterParser.getXml(conditions, Param.ValueProperty.VALUE);
                filterEntity.setXml(xml);
                saveFilterEntity();
            }
        }

        @Override
        public String getCaption() {
            return getMessage("Filter.save");
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
            window.addListener(new Window.CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                        String filterName = window.getFilterName();
                        FilterEntity newFilterEntity = metadata.create(FilterEntity.class);
                        InstanceUtils.copy(filterEntity, newFilterEntity);
                        newFilterEntity.setCode(null);
                        newFilterEntity.setId(UuidProvider.createUuid());
                        //if filter was global but current user cannot create global filter then new filter
                        //will be connected with current user
                        if (newFilterEntity.getUser() == null && !uerCanEditGlobalFilter()) {
                            newFilterEntity.setUser(userSessionSource.getUserSession().getCurrentOrSubstitutedUser());
                        }
                        String xml = filterEntity.getFolder() == null ?  FilterParser.getXml(conditions, Param.ValueProperty.DEFAULT_VALUE)
                                : FilterParser.getXml(conditions, Param.ValueProperty.VALUE);
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
                }
            });
        }

        @Override
        public String getCaption() {
            return getMessage("Filter.saveAs");
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
            final FilterEditor window = (FilterEditor) windowManager.openWindow(windowInfo, WindowManager.OpenType.DIALOG, params);
            window.addListener(new Window.CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                        conditions = window.getConditions();
                        initFilterSelectComponents();
                        updateWindowCaption();
                        fillConditionsLayout(ConditionsFocusType.FIRST);
                        updateFilterModifiedIndicator();
                    }
                }
            });
        }

        @Override
        public String getCaption() {
            return getMessage("Filter.edit");
        }

        @Override
        public String getIcon() {
            return "icons/edit.png";
        }
    }

    protected class MakeDefaultAction extends AbstractAction {

        public MakeDefaultAction() {
            super("Filter.makeDefault");
        }

        @Override
        public void actionPerform(Component component) {
            setDefaultFilter();
        }

        protected void setDefaultFilter() {
            if (filterEntity != null) {
                filterEntity.setIsDefault(true);
            }
            for (FilterEntity filter : filterEntities) {
                if (!ObjectUtils.equals(filter, filterEntity)) {
                    if (BooleanUtils.isTrue(filter.getIsDefault())) {
                        filter.setIsDefault(false);
                    }
                }
            }
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
                    getMessage("Filter.removeDialogTitle"),
                    getMessage("Filter.removeDialogMessage"),
                    Frame.MessageType.CONFIRMATION,
                    new Action[]{
                            new DialogAction(Type.YES) {
                                @Override
                                public void actionPerform(Component component) {
                                    removeFilterEntity();
                                }
                            },
                            new DialogAction(Type.NO)
                    });
        }

        @Override
        public String getCaption() {
            return getMessage("Filter.remove");
        }

        @Override
        public String getIcon() {
            return "icons/remove.png";
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
            return getMessage("Filter.pinApplied");
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
            return getMessage("Filter." + getId());
        }

        @Override
        public void actionPerform(Component component) {
            saveAsFolder(isAppFolder);
        }
    }

    protected class AddToSetAction extends ItemTrackingAction {
        protected AddToSetAction(Table table) {
            super(table, "addToSet");
        }

        @Override
        public String getCaption() {
            return getMessage(getId());
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
            super(table, "removeFromCurSet");
        }

        @Override
        public String getCaption() {
            return getMessage(getId());
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
            super("addToCurSet");
        }

        @Override
        public String getCaption() {
            return getMessage(getId());
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

    protected class AppliedFilterHolder {
        public final AppliedFilter filter;
        public final Component.Container layout;
        public final Button button;

        protected AppliedFilterHolder(AppliedFilter filter, Component.Container layout, Button button) {
            this.filter = filter;
            this.layout = layout;
            this.button = button;
        }
    }

    protected enum FilterMode {
        GENERIC_MODE,
        FTS_MODE
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
            filterActions.put("save_as", saveAsAction);
            filterActions.put("edit", editAction);
            filterActions.put("remove", removeAction);
            filterActions.put("pin", pinAppliedAction);
            filterActions.put("save_search_folder", saveAsSearchFolderAction);
            filterActions.put("save_app_folder", saveAsAppFolderAction);
            filterActions.put("make_default", makeDefaultAction);
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
                Component component = getControlsLayoutComponent(entry.getKey(), entry.getValue());
                if (component == null) {
                    log.warn("Filter controls layout component " + entry.getKey() + " not supported");
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
                    maxResultsDisplayed = true;
                    return maxResultsLayout;
                case "fts_switch":
                    return ftsSwitch;
                case "spacer":
                    return controlsLayoutGap;
                case "pin":
                case "save":
                case "save_as":
                case "edit":
                case "remove":
                case "make_default":
                case "save_search_folder":
                case "save_app_folder":
                    return createActionBtn(name, options);
            }
            return null;
        }

        protected Button createActionBtn(String actionName, List<String> options) {
            if (!isActionAllowed(actionName)) return null;
            Button button = componentsFactory.createComponent(Button.class);
            button.setAction(filterActions.get(actionName));
            if (options.contains("no-caption")) {
                button.setCaption(null);
                button.setDescription(filterActions.get(actionName).getCaption());
            }
            if (options.contains("no-icon"))
                button.setIcon(null);
            return button;
        }

        protected void fillSettingsBtn(List<String> actionNames) {
            for (String actionName : actionNames) {
                AbstractAction action = filterActions.get(actionName);
                if (action == null) {
                    log.warn("Action " + actionName + " cannot be added to settingsBtn");
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

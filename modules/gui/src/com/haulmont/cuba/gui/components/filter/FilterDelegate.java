/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.google.common.base.Strings;
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
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.condition.CustomCondition;
import com.haulmont.cuba.gui.components.filter.condition.RuntimePropCondition;
import com.haulmont.cuba.gui.components.filter.edit.FilterEditor;
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
import org.apache.commons.lang.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.*;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Class encapsulates common  generic filter behaviour. Client specific behaviour is placed into implementations of
 * {@link com.haulmont.cuba.gui.components.filter.FilterHelper}. All filter components delegates its method invocations
 * to this class.
 *
 * @author gorbunkov
 * @version $Id$
 */
public class FilterDelegate {

    protected static final String GLOBAL_FILTER_PERMISSION = "cuba.gui.filter.global";
    protected static final String GLOBAL_APP_FOLDERS_PERMISSION = "cuba.gui.appFolder.global";
    protected static final String FILTER_EDIT_PERMISSION = "cuba.gui.filter.edit";
    protected static final String FILTER_REMOVE_DISABLED_ICON = "icons/filter-remove-disabled.png";
    protected static final String FILTER_REMOVE_ENABLED_ICON = "icons/filter-remove-enabled.png";

    protected static final Log log = LogFactory.getLog(FilterDelegate.class);

    protected ComponentsFactory componentsFactory;
    protected ThemeConstants theme;
    protected Messages messages;
    protected WindowManager windowManager;
    protected Metadata metadata;
    protected WindowConfig windowConfig;
    protected UserSessionSource userSessionSource;
    protected PersistenceManagerService persistenceManager;
    protected Configuration configuration;
    protected ClientConfig clientConfig;
    protected GlobalConfig globalConfig;
    protected DataService dataService;
    protected Security security;
    protected AddConditionHelper addConditionHelper;
    protected FilterHelper filterHelper;

    protected Filter filter;
    protected FilterEntity adHocFilter;
    protected ConditionsTree conditions;
    protected FilterEntity filterEntity;
    protected FilterEntity initialFilterEntity;
    protected CollectionDatasource datasource;
    protected QueryFilter dsQueryFilter;
    protected List<FilterEntity> filterEntities = new ArrayList<>();
    protected AppliedFilter lastAppliedFilter;
    protected LinkedList<AppliedFilterHolder> appliedFilters = new LinkedList<>();

    protected GroupBoxLayout layout;
    protected LookupField filtersLookup;
    protected Component.Container conditionsLayout;
    protected BoxLayout maxResultsLayout;
    protected TextField maxResultsField;
    protected CheckBox maxResultsCb;
    protected Component.Container controlsLayout;
    protected Component.Container appliedFiltersLayout;
    protected LinkButton allowRemoveButton;
    protected PopupButton settingsBtn;
    protected Component applyTo;
    protected LinkButton filterModifiedIndicator;
    protected SaveAction saveAction;

    protected String caption;
    protected boolean useMaxResults;
    protected Boolean manualApplyRequired;
    protected boolean folderActionsEnabled = true;
    protected boolean filtersLookupListenerEnabled = true;
    protected boolean conditionsRemoveEnabled = false;
    protected boolean editable;

    public FilterDelegate(Filter filter) {
        this.filter = filter;

        filterHelper = AppBeans.get(FilterHelper.class);
        configuration = AppBeans.get(Configuration.class);
        globalConfig = configuration.getConfig(GlobalConfig.class);
        clientConfig = configuration.getConfig(ClientConfig.class);
        componentsFactory = AppBeans.get(ComponentsFactory.class);
        messages = AppBeans.get(Messages.class);
        ThemeConstantsManager themeConstantsManager = AppBeans.get(ThemeConstantsManager.class);
        theme = themeConstantsManager.getConstants();
        windowManager = AppBeans.get(WindowManagerProvider.class).get();
        windowConfig = AppBeans.get(WindowConfig.class);
        metadata = AppBeans.get(Metadata.class);
        userSessionSource = AppBeans.get(UserSessionSource.NAME);
        persistenceManager = AppBeans.get(PersistenceManagerService.class);
        clientConfig = configuration.getConfig(ClientConfig.class);
        dataService = AppBeans.get(DataService.class);
        security = AppBeans.get(Security.class);

        createLayout();

        addConditionHelper = new AddConditionHelper(filter, new AddConditionHelper.Handler() {
            @Override
            public void handle(AbstractCondition condition) {
                conditions.getRootNodes().add(new Node<>(condition));
                fillConditionsLayout(false);
                updateFilterModifiedIndicator();
                condition.addListener(new AbstractCondition.Listener() {
                    @Override
                    public void captionChanged() {
                    }

                    @Override
                    public void paramChanged() {
                        updateFilterModifiedIndicator();
                    }
                });
            }
        });
    }

    /**
     * Loads filter entities, finds default filter and applies it if found
     */
    public void loadFiltersAndApplyDefault() {
        initShortcutActions();

        initAdHocFilter();
        loadFilterEntities();
        FilterEntity defaultFilter = getDefaultFilter(filterEntities);
        if (defaultFilter == null) {
            defaultFilter = adHocFilter;
        }
        fillFiltersLookup();
        filtersLookup.setValue(defaultFilter);

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

    protected void initShortcutActions() {
        if (filter.getFrame().getAction("applyFilter") == null) {
            filter.getFrame().addAction(new AbstractAction("applyFilter", clientConfig.getFilterApplyShortcut()) {
                @Override
                public void actionPerform(Component component) {
                    if (isVisible() && datasource != null) {
                        apply(false);
                    }
                }
            });
        }
    }

    public void createLayout() {
        layout = componentsFactory.createComponent(GroupBoxLayout.NAME);
        layout.setOrientation(GroupBoxLayout.Orientation.VERTICAL);
        layout.setStyleName("cuba-generic-filter");
        layout.setWidth("100%");

        appliedFiltersLayout = componentsFactory.createComponent(BoxLayout.VBOX);
        layout.add(appliedFiltersLayout);

        conditionsLayout = componentsFactory.createComponent(BoxLayout.HBOX);
        conditionsLayout.setWidth("100%");
        conditionsLayout.setStyleName("filter-conditions");
        layout.add(conditionsLayout);

        controlsLayout = createControlsLayout();
        layout.add(controlsLayout);
    }

    protected Component.Container createControlsLayout() {
        final BoxLayout controlsLayout = componentsFactory.createComponent(BoxLayout.HBOX);
        controlsLayout.setSpacing(true);
        controlsLayout.setWidth("100%");

        filtersLookup = componentsFactory.createComponent(LookupField.NAME);
        controlsLayout.add(filtersLookup);
        filtersLookup.setWidth(theme.get("cuba.gui.filter.select.width"));
        filtersLookup.addListener(new FiltersLookupChangeListener());
        filterHelper.setLookupNullSelectionAllowed(filtersLookup, false);

        Button searchBtn = componentsFactory.createComponent(Button.NAME);
        controlsLayout.add(searchBtn);
        searchBtn.setCaption(getMessage("Filter.search"));
        searchBtn.setIcon("icons/search.png");
        searchBtn.setAction(new AbstractAction("search") {
            @Override
            public void actionPerform(Component component) {
                apply(false);
            }
        });

        Component gap = componentsFactory.createComponent(BoxLayout.HBOX);
        controlsLayout.add(gap);
        controlsLayout.expand(gap);

        LinkButton addConditionBtn = componentsFactory.createComponent(LinkButton.NAME);
        controlsLayout.add(addConditionBtn);
        addConditionBtn.setCaption(getMessage("Filter.addCondition"));
        addConditionBtn.setAlignment(Component.Alignment.MIDDLE_LEFT);
        addConditionBtn.setAction(new AbstractAction("openAddConditionDlg") {
            @Override
            public void actionPerform(Component component) {
                addConditionHelper.addCondition();
            }
        });

        allowRemoveButton = componentsFactory.createComponent(LinkButton.NAME);
        controlsLayout.add(allowRemoveButton);
        allowRemoveButton.setAlignment(Component.Alignment.MIDDLE_LEFT);
        allowRemoveButton.setAction(new AbstractAction("") {
            @Override
            public void actionPerform(Component component) {
                conditionsRemoveEnabled = !conditionsRemoveEnabled;
                allowRemoveButton.setIcon(conditionsRemoveEnabled ? FILTER_REMOVE_ENABLED_ICON : FILTER_REMOVE_DISABLED_ICON);
                fillConditionsLayout(false);
                updateFilterModifiedIndicator();
            }
        });
        allowRemoveButton.setDescription(getMessage("Filter.allowRemoveConditions"));

        filterModifiedIndicator = componentsFactory.createComponent(LinkButton.NAME);
        filterModifiedIndicator.setIcon("icons/save.png");
        filterModifiedIndicator.setDescription(getMessage("Filter.modified"));
        filterModifiedIndicator.setEnabled(false);
        filterModifiedIndicator.setAlignment(Component.Alignment.MIDDLE_RIGHT);
        controlsLayout.add(filterModifiedIndicator);

        settingsBtn = componentsFactory.createComponent(PopupButton.NAME);
        settingsBtn.setIcon("icons/gear.png");
        searchBtn.setAlignment(Component.Alignment.MIDDLE_LEFT);
        controlsLayout.add(settingsBtn);
        createMaxResultsLayout();
        controlsLayout.add(maxResultsLayout);

        searchBtn.setAlignment(Component.Alignment.MIDDLE_RIGHT);

        return controlsLayout;
    }

    protected void createMaxResultsLayout() {
        maxResultsLayout = componentsFactory.createComponent(BoxLayout.HBOX);
        maxResultsLayout.setSpacing(true);
        maxResultsLayout.setAlignment(Component.Alignment.MIDDLE_RIGHT);
        maxResultsCb = componentsFactory.createComponent(CheckBox.NAME);
        maxResultsCb.setCaption(messages.getMainMessage("filter.maxResults.label1"));
        maxResultsCb.setAlignment(Component.Alignment.MIDDLE_RIGHT);
        maxResultsCb.setValue(true);
        maxResultsCb.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                maxResultsField.setEnabled(BooleanUtils.isTrue((Boolean) maxResultsCb.getValue()));
            }
        });
        maxResultsLayout.add(maxResultsCb);

        maxResultsField = componentsFactory.createComponent(TextField.NAME);
        maxResultsField.setAlignment(Component.Alignment.MIDDLE_RIGHT);
        maxResultsField.setMaxLength(4);
        maxResultsField.setWidth(theme.get("cuba.gui.Filter.maxResults.width"));
        maxResultsField.setDatatype(Datatypes.get("int"));
        maxResultsLayout.add(maxResultsField);

        Label maxResultsLabel2 = componentsFactory.createComponent(Label.NAME);
        maxResultsLabel2.setAlignment(Component.Alignment.MIDDLE_RIGHT);
        maxResultsLabel2.setValue(messages.getMainMessage("filter.maxResults.label2"));
        maxResultsLayout.add(maxResultsLabel2);

    }

    protected void initMaxResults() {
        int maxResults = datasource.getMaxResults();
        if (maxResults == 0 || maxResults == persistenceManager.getMaxFetchUI(datasource.getMetaClass().getName()))
            maxResults = persistenceManager.getFetchUI(datasource.getMetaClass().getName());
        maxResultsField.setValue(maxResults);

        datasource.setMaxResults(maxResults);
    }

    /**
     * Sets conditionsLayout visibility and shows/hides top border of controlsLayout
     */
    protected void setConditionsLayoutVisible(boolean visible) {
        conditionsLayout.setVisible(visible);
        if (!visible) {
            controlsLayout.setStyleName("filter-control-no-border");
        } else if (!conditionsLayout.getComponents().isEmpty()) {
            controlsLayout.setStyleName("filter-control-with-border");
        }
    }

    /**
     * Removes all actions from 'Settings' PopupButton and creates all actions anew
     */
    protected void fillActions() {
        settingsBtn.removeAllActions();

        if (filterEntity == null)
            return;

        saveAction = new SaveAction();
        SaveAsAction saveAsAction = new SaveAsAction();
        EditAction editAction = new EditAction();
        MakeDefaultAction makeDefaultAction = new MakeDefaultAction();
        RemoveAction removeAction = new RemoveAction();
        PinAppliedAction pinAppliedAction = new PinAppliedAction();
        HideConditionsAction hideConditionsAction = new HideConditionsAction();
        SaveAsFolderAction saveAsAppFolderAction = new SaveAsFolderAction(true);
        SaveAsFolderAction saveAsSearchFolderAction = new SaveAsFolderAction(false);

        boolean isGlobal = filterEntity.getUser() == null;
        boolean userCanEditGlobalFilter = userSessionSource.getUserSession().isSpecificPermitted(GLOBAL_FILTER_PERMISSION);
        boolean userCanEditGlobalAppFolder = userSessionSource.getUserSession().isSpecificPermitted(GLOBAL_APP_FOLDERS_PERMISSION);
        boolean createdByCurrentUser = userSessionSource.getUserSession().getCurrentOrSubstitutedUser().equals(filterEntity.getUser());
        boolean hasCode = !Strings.isNullOrEmpty(filterEntity.getCode());
        boolean isFolder = filterEntity.getFolder() != null;
        boolean isSearchFolder = isFolder && (filterEntity.getFolder() instanceof SearchFolder);
        boolean isAppFolder = isFolder && (filterEntity.getFolder() instanceof AppFolder);
        boolean isSet = BooleanUtils.isTrue(filterEntity.getIsSet());
        boolean isDefault = BooleanUtils.isTrue(filterEntity.getIsDefault());
        boolean isAdHocFilter = filterEntity == adHocFilter;

        boolean editActionEnabled = !isSet &&
                ((isGlobal && userCanEditGlobalFilter) || (!isGlobal && createdByCurrentUser)) &&
                ((!isFolder && !hasCode) || isSearchFolder || (isAppFolder && userCanEditGlobalAppFolder));

        boolean saveActionEnabled = editActionEnabled && isFilterModified();

        boolean saveAsActionEnabled = !isSet;

        boolean removeActionEnabled = !isSet &&
                (!hasCode && !isFolder) &&
                ((isGlobal && userCanEditGlobalFilter) || (!isGlobal && createdByCurrentUser)) &&
                !isAdHocFilter;

        boolean makeDefaultActionEnabled = !isDefault && !isFolder && !isSet && !isAdHocFilter;

        boolean pinAppliedActionEnabled = lastAppliedFilter != null;

        boolean saveAsSearchFolderActionEnabled = !isFolder && !hasCode;

        boolean saveAsAppFolderActionEnabled = !isFolder && !hasCode;

        boolean hideConditionsActionEnabled = !isSet && !isFolder;

        saveAction.setEnabled(saveActionEnabled);
        saveAsAction.setEnabled(saveAsActionEnabled);
        editAction.setEnabled(editActionEnabled);
        removeAction.setEnabled(removeActionEnabled);
        makeDefaultAction.setEnabled(makeDefaultActionEnabled);
        pinAppliedAction.setEnabled(pinAppliedActionEnabled);
        saveAsSearchFolderAction.setEnabled(saveAsSearchFolderActionEnabled);
        saveAsAppFolderAction.setEnabled(saveAsAppFolderActionEnabled);
        hideConditionsAction.setEnabled(hideConditionsActionEnabled);

        settingsBtn.addAction(saveAction);
        settingsBtn.addAction(saveAsAction);
        settingsBtn.addAction(editAction);
        settingsBtn.addAction(makeDefaultAction);
        settingsBtn.addAction(removeAction);
        if (globalConfig.getAllowQueryFromSelected()) {
            settingsBtn.addAction(pinAppliedAction);
        }
        settingsBtn.addAction(hideConditionsAction);
        if (folderActionsEnabled && filterHelper.isFolderActionsEnabled()) {
            settingsBtn.addAction(saveAsSearchFolderAction);
            settingsBtn.addAction(saveAsAppFolderAction);
        }

        if (filterHelper.isTableActionsEnabled())
            fillTableActions();

        filterModifiedIndicator.setAction(saveAction);
        filterModifiedIndicator.setCaption("");
    }

    protected void saveFilterEntity() {
        Boolean isDefault = filterEntity.getIsDefault();
        Boolean applyDefault = filterEntity.getApplyDefault();
        if (filterEntity.getFolder() == null) {
            CommitContext ctx = new CommitContext(Collections.singletonList(filterEntity));
            Set<Entity> result = dataService.commit(ctx);
            for (Entity entity : result) {
                if (entity.equals(filterEntity)) {
                    filterEntities.remove(filterEntity);
                    filterEntity = (FilterEntity) entity;
                    filterEntities.add(filterEntity);
                    break;
                }
            }
            filterEntity.setApplyDefault(applyDefault);
            filterEntity.setIsDefault(isDefault);
        } else {
            filterEntity.getFolder().setName(filterEntity.getName());
            filterEntity.getFolder().setFilterXml(filterEntity.getXml());
            AbstractSearchFolder folder = saveFolder(filterEntity.getFolder());
            filterEntity.setFolder(folder);
        }

        saveInitialFilterState();
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
            folder.setName(filterEntity.getName());
            folder.setTabName(filterEntity.getName());
        } else {
            String name = messages.getMainMessage(filterEntity.getCode());
            folder.setName(name);
            folder.setTabName(name);
        }

        String newXml = FilterParser.getXml(conditions, Param.ValueProperty.VALUE);

        folder.setFilterComponentId(filterEntity.getComponentId());
        folder.setFilterXml(newXml);
        if (!isAppFolder) {
            if (userSessionSource.getUserSession().isSpecificPermitted(GLOBAL_FILTER_PERMISSION))
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
                    fillFiltersLookup();
                }
            };
        } else {
            commitHandler = new Runnable() {
                @Override
                public void run() {
                    AbstractSearchFolder savedFolder = saveFolder(folder);
                    filterEntity.setFolder(savedFolder);
                    fillFiltersLookup();
                }
            };
        }

        filterHelper.openFolderEditWindow(isAppFolder, folder, presentations, commitHandler);

    }

    /**
     * Removes all components from conditionsLayout and fills it with components for editing filter conditions
     *
     * @param focusOnConditions whether to set focus on first condition parameter edit component
     */
    protected void fillConditionsLayout(boolean focusOnConditions) {
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
            GroupBoxLayout groupBox = componentsFactory.createComponent(GroupBoxLayout.NAME);
            groupBox.setWidth("100%");
            groupBox.setCaption(getMessage("GroupType.AND"));
            conditionsLayout.add(groupBox);
            recursivelyCreateConditionsLayout(focusOnConditions, conditions.getRootNodes(), groupBox, 0);
        } else {
            recursivelyCreateConditionsLayout(focusOnConditions, conditions.getRootNodes(), conditionsLayout, 0);
        }

        if (!conditionsLayout.getComponents().isEmpty()) layout.setSpacing(true);
    }

    protected void recursivelyCreateConditionsLayout(boolean focusOnConditions,
                                                     List<Node<AbstractCondition>> nodes,
                                                     Component.Container parentContainer,
                                                     int level) {

        List<Node<AbstractCondition>> visibleConditionNodes = new ArrayList<>();
        for (Node<AbstractCondition> node : nodes) {
            AbstractCondition condition = node.getData();
            if (!condition.getHidden())
                visibleConditionNodes.add(node);
        }

        if (visibleConditionNodes.isEmpty()) {
            if (level == 0)
                controlsLayout.setStyleName("filter-control-no-border");
            return;
        }

        int columnsQty = clientConfig.getGenericFilterColumnsQty();
        int row = 0;
        int nextColumnStart = 0;
        GridLayout grid = componentsFactory.createComponent(GridLayout.NAME);
        grid.setColumns(columnsQty);
        for (int i = 0; i < columnsQty; i++) {
            grid.setColumnExpandRatio(i, 1);
        }
        grid.setRows(1);
        grid.setSpacing(true);
        grid.setWidth("100%");

        if (level == 0)
            controlsLayout.setStyleName("filter-control-with-border");

        boolean focusSet = false;

        for (int i = 0; i < visibleConditionNodes.size(); i++) {
            Node<AbstractCondition> node = visibleConditionNodes.get(i);
            final AbstractCondition condition = node.getData();
            Component cellContent;
            if (condition.isGroup()) {
                GroupBoxLayout groupBox = componentsFactory.createComponent(GroupBoxLayout.NAME);
                groupBox.setWidth("100%");
                groupBox.setCaption(condition.getLocCaption());

                if (!node.getChildren().isEmpty()) {
                    recursivelyCreateConditionsLayout(
                            focusOnConditions && !focusSet, node.getChildren(), groupBox, level++);
                }
                cellContent = groupBox;
            } else {
                if (condition.getParam().getJavaClass() != null) {
                    ParamEditor paramEditor = new ParamEditor(condition, conditionsRemoveEnabled);
                    AbstractAction removeConditionAction = new AbstractAction("") {
                        @Override
                        public void actionPerform(Component component) {
                            conditions.removeCondition(condition);
                            fillConditionsLayout(false);
                        }
                    };
                    removeConditionAction.setVisible(conditionsRemoveEnabled);
                    paramEditor.getRemoveButton().setAction(removeConditionAction);

                    if (focusOnConditions && !focusSet) {
                        paramEditor.requestFocus();
                        focusSet = true;
                    }

                    cellContent = paramEditor.getComponent();
                } else {
                    BoxLayout paramLayout = componentsFactory.createComponent(BoxLayout.HBOX);
                    paramLayout.setSpacing(true);
                    paramLayout.setMargin(false);

                    cellContent = paramLayout;
                }
            }

            //groupBox for group conditions must occupy the while line in conditions grid
            Integer conditionWidth = condition.isGroup() ? columnsQty : condition.getWidth();
            int nextColumnEnd = nextColumnStart + conditionWidth - 1;
            if (nextColumnEnd >= columnsQty) {
                //complete current row in grid with gaps if next cell will be on next row
                completeGridRowWithGaps(grid, row, nextColumnStart);
                //place cell to next row in grid
                nextColumnStart = 0;
                nextColumnEnd = conditionWidth - 1;
                row++;
                grid.setRows(row + 1);
            }

            grid.add(cellContent, nextColumnStart, row, nextColumnEnd, row);

            nextColumnStart = nextColumnEnd + 1;

            //add next row if necessary
            if (i < visibleConditionNodes.size() - 1) {
                if (nextColumnStart >= columnsQty) {
                    nextColumnStart = 0;
                    row++;
                    grid.setRows(row + 1);
                }
            }
        }

        //complete last row in grid with gaps
        completeGridRowWithGaps(grid, row, nextColumnStart);

        if (parentContainer != null) {
            parentContainer.add(grid);
        }
    }

    /**
     * Adds empty containers to grid row. If not to complete the row with gaps then in case of grid with one element (element width = 1)
     * this element will occupy 100% of grid width, but expected behaviour is to occupy 1/3 of grid width
     */
    protected void completeGridRowWithGaps(GridLayout grid, int row, int startColumn) {
        for (int i = startColumn; i < grid.getColumns(); i++) {
            Component gap = componentsFactory.createComponent(BoxLayout.HBOX);
            gap.setWidth("100%");
            grid.add(gap, i, row);
        }
    }

    /**
     * Sets filter entity, creates condition editor components and applies filter if necessary
     */
    public void setFilterEntity(FilterEntity filterEntity) {
        this.filterEntity = filterEntity;
        conditions = FilterParser.getConditions(filter, filterEntity.getXml());
        for (AbstractCondition condition : conditions.toConditionsList()) {
            condition.addListener(new AbstractCondition.Listener() {
                @Override
                public void captionChanged() {}

                @Override
                public void paramChanged() {
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

        if (!filterEntities.contains(filterEntity)) {
            filterEntities.add(filterEntity);
            fillFiltersLookup();
        }

        filtersLookupListenerEnabled = false;
        filtersLookup.setValue(filterEntity);
        filtersLookupListenerEnabled = true;

        conditionsRemoveEnabled = false;
        allowRemoveButton.setIcon(FILTER_REMOVE_DISABLED_ICON);

        fillActions();

        fillConditionsLayout(true);

        setConditionsLayoutVisible(true);

        if (BooleanUtils.isTrue(filterEntity.getApplyDefault()) ||
                BooleanUtils.isTrue(filterEntity.getIsSet()) ||
                !getResultingManualApplyRequired())
            apply(true);

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

    protected boolean isFilterModified() {
        boolean filterPropertiesModified =
                !Objects.equals(initialFilterEntity.getName(), filterEntity.getName()) ||
                !Objects.equals(initialFilterEntity.getCode(), filterEntity.getCode()) ||
                !Objects.equals(initialFilterEntity.getUser(), filterEntity.getUser());
        if (filterPropertiesModified) return true;
        String filterXml = FilterParser.getXml(conditions, Param.ValueProperty.DEFAULT_VALUE);
        return !StringUtils.equals(filterXml, initialFilterEntity.getXml());
    }

    protected void updateFilterModifiedIndicator() {
        saveAction.setEnabled(isFilterModified());
    }

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

        Collections.sort(
                filterEntities,
                new Comparator<FilterEntity>() {
                    @Override
                    public int compare(FilterEntity f1, FilterEntity f2) {
                        return getFilterCaption(f1).compareTo(getFilterCaption(f2));
                    }
                }
        );

        filterEntities.add(0, adHocFilter);
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

    protected void fillFiltersLookup() {
        Map<String, Object> optionsMap = new LinkedHashMap<>();
        for (FilterEntity entity : filterEntities) {
            String caption = getFilterCaption(entity);
            if (entity.getIsDefault()) {
                caption += " " + getMessage("Filter.default");
            }
            optionsMap.put(caption, entity);
        }

        filtersLookup.setOptionsMap(optionsMap);
    }

    protected void initAdHocFilter() {
        adHocFilter = metadata.create(FilterEntity.class);
        String emptyXml = FilterParser.getXml(new ConditionsTree(), Param.ValueProperty.VALUE);
        adHocFilter.setXml(emptyXml);
        adHocFilter.setComponentId(ComponentsHelper.getFilterComponentPath(filter));
        adHocFilter.setUser(userSessionSource.getUserSession().getCurrentOrSubstitutedUser());
        adHocFilter.setName(getMessage("Filter.adHocFilter"));
    }

    protected void addApplied() {
        if (lastAppliedFilter == null)
            return;

        if (!appliedFilters.isEmpty() && appliedFilters.getLast().filter.equals(lastAppliedFilter))
            return;

        BoxLayout layout = componentsFactory.createComponent(BoxLayout.HBOX);
        layout.setSpacing(true);

        if (!appliedFilters.isEmpty()) {
            AppliedFilterHolder holder = appliedFilters.getLast();
            holder.layout.remove(holder.button);
        }

        Label label = componentsFactory.createComponent(Label.NAME);
        label.setValue(lastAppliedFilter.getText());
        layout.add(label);
        label.setAlignment(Component.Alignment.MIDDLE_LEFT);

        LinkButton button = componentsFactory.createComponent(LinkButton.NAME);
        button.setStyleName("remove-applied-filter");
        button.setIcon("icons/item-remove.png");
        button.setAction(new AbstractAction("") {
            @Override
            public void actionPerform(Component component) {
                removeApplied();
            }
        });
        layout.add(button);

        appliedFiltersLayout.add(layout);

        appliedFilters.add(new AppliedFilterHolder(lastAppliedFilter, layout, button));
    }

    protected void removeApplied() {
        if (!appliedFilters.isEmpty()) {
            if (appliedFilters.size() == 1) {
                AppliedFilterHolder holder = appliedFilters.removeLast();
                appliedFiltersLayout.remove(holder.layout);
                ((CollectionDatasource.SupportsApplyToSelected) datasource).unpinAllQuery();
            } else {

                windowManager.showOptionDialog(messages.getMainMessage("removeApplied.title"),
                        messages.getMainMessage("removeApplied.message"), IFrame.MessageType.WARNING,
                        new Action[]{
                                new DialogAction(DialogAction.Type.YES) {
                                    @Override
                                    public void actionPerform(Component component) {
                                        for (AppliedFilterHolder holder : appliedFilters) {
                                            appliedFiltersLayout.remove(holder.layout);
                                        }
                                        appliedFilters.clear();
                                        ((CollectionDatasource.SupportsApplyToSelected) datasource).unpinAllQuery();
                                    }
                                },
                                new DialogAction(DialogAction.Type.NO)
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
        return messages.getMessage(FilterDelegate.class, key);
    }

    public Component.Container getLayout() {
        return layout;
    }

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
    }

    public CollectionDatasource getDatasource() {
        return datasource;
    }

    public boolean apply(boolean isNewWindow) {
        if (clientConfig.getGenericFilterChecking()) {
            if (filterEntity != null && conditions.getRoots().size() > 0) {

                boolean haveRequiredConditions = haveFilledRequiredConditions();
                if (!haveRequiredConditions) {
                    if (!isNewWindow) {
                        windowManager.showNotification(messages.getMainMessage("filter.emptyRequiredConditions"),
                                IFrame.NotificationType.HUMANIZED);
                    }
                    return false;
                }

                boolean haveCorrectCondition = hasCorrectCondition();
                if (!haveCorrectCondition) {
                    if (!isNewWindow) {
                        windowManager.showNotification(messages.getMainMessage("filter.emptyConditions"),
                                IFrame.NotificationType.HUMANIZED);
                    }
                    return false;
                }
            }
        }

        applyDatasourceFilter();

        if (useMaxResults) {
            int maxResults;
            if (BooleanUtils.isTrue((Boolean) maxResultsCb.getValue())) {
                Integer maxResultsFieldValue = maxResultsField.getValue();
                if (maxResultsFieldValue != null) {
                    maxResults = maxResultsFieldValue;
                } else
                    maxResults = persistenceManager.getMaxFetchUI(datasource.getMetaClass().getName());
            } else {
                maxResults = persistenceManager.getMaxFetchUI(datasource.getMetaClass().getName());
            }
            datasource.setMaxResults(maxResults);
        }
        if (datasource instanceof CollectionDatasource.SupportsPaging) {
            ((CollectionDatasource.SupportsPaging) datasource).setFirstResult(0);
        }

        refreshDatasource();


        if (filterEntity != null) {
            lastAppliedFilter = new AppliedFilter(filterEntity, conditions);
        } else {
            lastAppliedFilter = null;
        }

        fillActions();

        return true;
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

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
        layout.setCaption(caption);
    }

    public void setUseMaxResults(boolean useMaxResults) {
        this.useMaxResults = useMaxResults;

        Security security = AppBeans.get(Security.NAME);
        maxResultsLayout.setVisible(useMaxResults && security.isSpecificPermitted("cuba.gui.filter.maxResults"));

        if (datasource != null)
            initMaxResults();
    }

    public boolean getUseMaxResults() {
        return useMaxResults;
    }

    public void setManualApplyRequired(Boolean manualApplyRequired) {
        this.manualApplyRequired = manualApplyRequired;
    }

    public Boolean getManualApplyRequired() {
        return manualApplyRequired;
    }

    protected boolean getResultingManualApplyRequired() {
        return manualApplyRequired != null ? manualApplyRequired : clientConfig.getGenericFilterManualApplyRequired();
    }

    public <T extends Component> T getOwnComponent(String id) {
        for (AbstractCondition condition : conditions.toConditionsList()) {
            if (condition.getParam() != null) {
                String paramName = condition.getParam().getName();

                if (condition instanceof RuntimePropCondition) {
                    String paramName2 = ((RuntimePropCondition) condition).getCategoryAttributeParam().getName();
                    String componentName2 = paramName2.substring(paramName.lastIndexOf('.') + 1);
                    if (id.equals(componentName2)) {
                        ParamWrapper w = new ParamWrapper(condition, ((RuntimePropCondition) condition).getCategoryAttributeParam());
                        return (T) w;
                    }
                }
                String componentName = paramName.substring(paramName.lastIndexOf('.') + 1);
                if (id.equals(componentName)) {
                    ParamWrapper wrapper = new ParamWrapper(condition, condition.getParam());
                    return (T) wrapper;
                }
            }
        }
        return null;
    }

    @Nullable
    public <T extends Component> T getComponent(String id) {
        String[] elements = ValuePathHelper.parse(id);
        if (elements.length == 1) {
            return getOwnComponent(id);
        } else {
            throw new UnsupportedOperationException("Filter contains only one level of subcomponents");
        }
    }

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

        return changed;
    }

    public Component getApplyTo() {
        return applyTo;
    }

    public void setApplyTo(Component applyTo) {
        this.applyTo = applyTo;
    }

    public void setFolderActionsEnabled(boolean folderActionsEnabled) {
        this.folderActionsEnabled = folderActionsEnabled;
    }

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
        com.haulmont.cuba.gui.components.Button addToSetBtn = buttons.getComponent("addToSetBtn");
        com.haulmont.cuba.gui.components.Button addToCurSetBtn = buttons.getComponent("addToCurSetBtn");
        com.haulmont.cuba.gui.components.Button removeFromCurSetBtn = buttons.getComponent("removeFromCurSetBtn");

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
                addToCurSetBtn = componentsFactory.createComponent(Button.NAME);
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
                removeFromCurSetBtn = componentsFactory.createComponent(Button.NAME);
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
                addToSetBtn = componentsFactory.createComponent(Button.NAME);
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

    public void setEditable(boolean editable) {
        this.editable = editable;
        settingsBtn.setVisible(editable && security.isSpecificPermitted(FILTER_EDIT_PERMISSION));
    }

    public boolean isEditable() {
        return editable;
    }

    protected class FiltersLookupChangeListener implements ValueListener {

        protected String initialWindowCaption;

        @Override
        public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
            if (!filtersLookupListenerEnabled) return;
            setFilterEntity((FilterEntity) value);
            updateWindowCaption();
        }

        protected void updateWindowCaption() {
            Window window = ComponentsHelper.getWindow(filter);
            String filterTitle;
            if (filterEntity != null && filterEntity != adHocFilter) {
                if (filterEntity.getCode() != null) {
                    filterTitle = messages.getMainMessage(filterEntity.getCode());
                } else {
                    filterTitle = filterEntity.getName();
                }
            } else {
                filterTitle = null;
            }
            window.setDescription(filterTitle);

            if (initialWindowCaption == null) {
                initialWindowCaption = window.getCaption();
            }

            windowManager.setWindowCaption(window, initialWindowCaption, filterTitle);
        }
    }

    protected class SaveAction extends AbstractAction {

        protected SaveAction() {
            super("save");
        }

        @Override
        public void actionPerform(Component component) {
            if (PersistenceHelper.isNew(filterEntity)) {
                WindowInfo windowInfo = windowConfig.getWindowInfo("saveFilter");
                final SaveFilterWindow window = windowManager.openWindow(windowInfo, WindowManager.OpenType.DIALOG);
                window.addListener(new Window.CloseListener() {
                    @Override
                    public void windowClosed(String actionId) {
                        if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                            String filterName = window.getFilterName();
                            filterEntity.setName(filterName);
                            filterEntity.setXml(FilterParser.getXml(conditions, Param.ValueProperty.DEFAULT_VALUE));
                            saveFilterEntity();
                            initAdHocFilter();
                            filtersLookupListenerEnabled = false;
                            filterEntities.add(0, adHocFilter);
                            fillFiltersLookup();
                            //set null value to force updating the current item caption in filtersSelect
                            filtersLookup.setValue(null);
                            filtersLookup.setValue(filterEntity);
                            filtersLookupListenerEnabled = true;
                        }
                    }
                });
            } else {
                filterEntity.setXml(FilterParser.getXml(conditions, Param.ValueProperty.DEFAULT_VALUE));
                saveFilterEntity();
            }
        }

        @Override
        public String getCaption() {
            return getMessage("Filter.save");
        }
    }

    protected class SaveAsAction extends AbstractAction {

        protected SaveAsAction() {
            super("saveAs");
        }

        @Override
        public void actionPerform(Component component) {
            WindowInfo windowInfo = windowConfig.getWindowInfo("saveFilter");
            final SaveFilterWindow window = windowManager.openWindow(windowInfo, WindowManager.OpenType.DIALOG);
            window.addListener(new Window.CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    String filterName = window.getFilterName();
                    FilterEntity newFilterEntity = metadata.create(FilterEntity.class);
                    InstanceUtils.copy(filterEntity, newFilterEntity);
                    newFilterEntity.setId(UuidProvider.createUuid());
                    filterEntity = newFilterEntity;
                    filterEntity.setName(filterName);
                    filterEntity.setXml(FilterParser.getXml(conditions, Param.ValueProperty.DEFAULT_VALUE));
                    saveFilterEntity();
                    fillFiltersLookup();
                    filtersLookup.setValue(filterEntity);
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
            final FilterEditor window = windowManager.openWindow(windowInfo, WindowManager.OpenType.DIALOG, params);
            window.addListener(new Window.CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                        conditions = window.getConditions();
                        fillFiltersLookup();
                        fillConditionsLayout(true);
                        updateFilterModifiedIndicator();
                    }
                }
            });
        }

        @Override
        public String getCaption() {
            return getMessage("Filter.edit");
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
            fillFiltersLookup();
            fillActions();
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
                    IFrame.MessageType.CONFIRMATION,
                    new Action[]{
                            new DialogAction(DialogAction.Type.YES) {
                                @Override
                                public void actionPerform(Component component) {
                                    removeFilterEntity();
                                }
                            },
                            new DialogAction(DialogAction.Type.NO)
                    });
        }

        @Override
        public String getCaption() {
            return getMessage("Filter.remove");
        }
    }

    protected void removeFilterEntity() {
        CommitContext ctx = new CommitContext(Collections.emptyList(), Collections.singletonList(filterEntity));
        dataService.commit(ctx);
        filterEntities.remove(filterEntity);
        filtersLookup.setValue(filterEntities.get(0));
        fillFiltersLookup();
    }

    protected class PinAppliedAction extends AbstractAction {

        public PinAppliedAction() {
            super("pinApplied");
        }

        @Override
        public void actionPerform(Component component) {
            if (datasource instanceof CollectionDatasource.SupportsApplyToSelected) {
                ((CollectionDatasource.SupportsApplyToSelected) datasource).pinQuery();
                addApplied();

            }
        }

        @Override
        public String getCaption() {
            return getMessage("Filter.pinApplied");
        }

    }

    public class HideConditionsAction extends AbstractAction {
        protected HideConditionsAction() {
            super("Filter.hideConditions");
        }

        @Override
        public void actionPerform(Component component) {
            setConditionsLayoutVisible(!conditionsLayout.isVisible());
            setCaption(conditionsLayout.isVisible() ? getMessage("Filter.hideConditions") : getMessage("Filter.showConditions"));
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
        protected Table table;

        protected AddToSetAction(Table table) {
            super("addToSet");
            this.table = table;

            if (table.getSelected().isEmpty()) {
                updateApplicableTo(false);
            }
        }

        @Override
        public String getCaption() {
            return getMessage(getId());
        }

        @Override
        public void actionPerform(Component component) {
            if (!table.getSelected().isEmpty()) {
                String entityType = table.getDatasource().getMetaClass().getName();
                Map<String, Object> params = new HashMap<>();
                params.put("entityType", entityType);
                params.put("items", table.getSelected());
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
        protected Table table;

        protected RemoveFromSetAction(Table table) {
            super("removeFromCurSet");
            this.table = table;

            if (table.getSelected().isEmpty()) {
                updateApplicableTo(false);
            }
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
            Set selected = table.getSelected();
            if (selected.isEmpty()) {
                return;
            }

            if (table.getDatasource().getItemIds().size() == 1) {
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

    protected class AddToCurrSetAction extends AbstractAction {

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

            IFrame frame = filter.getFrame();
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
}

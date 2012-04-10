/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.entity.AppFolder;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.filter.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.data.ValueChangingListener;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.filter.DenyingClause;
import com.haulmont.cuba.gui.filter.QueryFilter;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.gui.settings.SettingsImpl;
import com.haulmont.cuba.gui.xml.ParametersHelper;
import com.haulmont.cuba.security.entity.FilterEntity;
import com.haulmont.cuba.security.entity.SearchFolder;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebWindowManager;
import com.haulmont.cuba.web.app.folders.AppFolderEditWindow;
import com.haulmont.cuba.web.app.folders.FolderEditWindow;
import com.haulmont.cuba.web.app.folders.FoldersPane;
import com.haulmont.cuba.web.gui.components.filter.*;
import com.haulmont.cuba.web.toolkit.ui.FilterSelect;
import com.haulmont.cuba.web.toolkit.ui.VerticalActionsLayout;
import com.vaadin.data.Property;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import org.apache.commons.lang.*;
import org.dom4j.*;
import org.vaadin.hene.popupbutton.PopupButton;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang.BooleanUtils.isTrue;

/**
 * Generic filter implementation for the web-client.
 * <p/>
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class WebFilter
        extends WebAbstractComponent<VerticalActionsLayout> implements Filter {
    private static final String MESSAGES_PACK = "com.haulmont.cuba.gui.components.filter";

    protected PersistenceManagerService persistenceManager;

    private CollectionDatasource datasource;
    private QueryFilter dsQueryFilter;
    private FilterEntity filterEntity;
    private ConditionsTree conditions = new ConditionsTree();

    private com.vaadin.ui.Component paramsLayout;
    private AbstractOrderedLayout editLayout;
    private FilterSelect select;
    private WebPopupButton actions;

    private Button applyBtn;

    private boolean defaultFilterEmpty = true;
    private boolean changingFilter;
    private boolean applyingDefault;
    private boolean editing;
    private FilterEditor editor;
    private FoldersPane foldersPane;

    private boolean useMaxResults;
    private CheckBox maxResultsCb;
    protected TextField maxResultsField;
    private AbstractOrderedLayout maxResultsLayout;
    private Boolean manualApplyRequired;

    private boolean editable = true;
    private boolean required = false;

    private Component applyTo;

    private static final String GLOBAL_FILTER_PERMISSION = "cuba.gui.filter.global";
    private static final String GLOBAL_APP_FOLDERS_PERMISSION = "cuba.gui.appFolder.global";

    private String mainMessagesPack = AppConfig.getMessagesPack();

    private FilterEntity noFilter;

    private GlobalConfig globalConfig = ConfigProvider.getConfig(GlobalConfig.class);
    private ClientConfig clientConfig = ConfigProvider.getConfig(ClientConfig.class);
    private String defaultFilterCaption;

    protected HorizontalLayout topLayout = null;

    public WebFilter() {
        persistenceManager = ServiceLocator.lookup(PersistenceManagerService.NAME);
        component = new VerticalActionsLayout();
        defaultFilterCaption = MessageProvider.getMessage(MESSAGES_PACK, "defaultFilter");
        component.addActionHandler(new com.vaadin.event.Action.Handler() {
            private com.vaadin.event.ShortcutAction shortcutAction =
                    new com.vaadin.event.ShortcutAction("applyFilterAction",
                            com.vaadin.event.ShortcutAction.KeyCode.ENTER,
                            new int[]{ShortcutAction.ModifierKey.SHIFT});

            @Override
            public com.vaadin.event.Action[] getActions(Object target, Object sender) {
                return new com.vaadin.event.Action[]{shortcutAction};
            }

            @Override
            public void handleAction(com.vaadin.event.Action action, Object sender, Object target) {

                if (ObjectUtils.equals(action, shortcutAction)) {
                    apply(false);
                }
            }
        });

        // don't add margin because filter is usually placed inside a groupbox that adds margins to its content
        component.setMargin(false);
        component.setStyleName("generic-filter");

        foldersPane = App.getInstance().getAppWindow().getFoldersPane();

        topLayout = new HorizontalLayout();
        topLayout.setSpacing(true);

        noFilter = new FilterEntity() {
            @Override
            public String toString() {
                return getName();
            }
        };
        noFilter.setName(MessageProvider.getMessage(mainMessagesPack, "filter.noFilter"));

        select = new FilterSelect();
        select.setWidth(300, Sizeable.UNITS_PIXELS);
        select.setStyleName("generic-filter-select");
        select.setNullSelectionAllowed(true);
        select.setNullSelectionItemId(noFilter);
        select.setImmediate(true);
        select.setPageLength(20);
        select.addListener(new SelectListener());
        App.getInstance().getWindowManager().setDebugId(select, "genericFilterSelect");
        topLayout.addComponent(select);

        applyBtn = WebComponentsHelper.createButton("icons/search.png");
        applyBtn.setCaption(MessageProvider.getMessage(mainMessagesPack, "actions.Apply"));
        applyBtn.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                apply(false);
            }
        });
        App.getInstance().getWindowManager().setDebugId(applyBtn, "genericFilterApplyBtn");
        topLayout.addComponent(applyBtn);

        actions = new WebPopupButton();
        actions.setCaption(MessageProvider.getMessage(MESSAGES_PACK, "actionsCaption"));
        topLayout.addComponent((com.vaadin.ui.Component) actions.getComponent());

        initMaxResultsLayout();
        topLayout.addComponent(maxResultsLayout);
        topLayout.setComponentAlignment(maxResultsLayout, com.vaadin.ui.Alignment.MIDDLE_LEFT);

        component.addComponent(topLayout);

        createParamsLayout(false);
        component.addComponent(paramsLayout);
        updateControls();
    }

    @Override
    public void requestFocus() {
        select.focus();
    }

    private void initMaxResultsLayout() {
        maxResultsLayout = new HorizontalLayout();
        maxResultsLayout.setSpacing(true);
        maxResultsCb = new CheckBox(MessageProvider.getMessage(mainMessagesPack, "filter.maxResults.label1"));
        maxResultsCb.setImmediate(true);
        maxResultsCb.setValue(true);
        maxResultsCb.addListener(
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        maxResultsField.setEnabled(BooleanUtils.isTrue((Boolean) maxResultsCb.getValue()));
                    }
                }
        );
        maxResultsCb.setStyleName("filter-maxresults");
        maxResultsLayout.addComponent(maxResultsCb);

        maxResultsField = new TextField();
        maxResultsField.setImmediate(true);
        maxResultsField.setMaxLength(4);
        maxResultsField.setWidth(40, UNITS_PIXELS);
        maxResultsField.setInvalidAllowed(false);
        maxResultsField.addValidator(
                new IntegerValidator(MessageProvider.getMessage(mainMessagesPack, "validation.invalidNumber")) {
                    @Override
                    public void validate(Object value) throws InvalidValueException {
                        try {
                            super.validate(value);
                        } catch (InvalidValueException e) {
                            maxResultsField.requestRepaint();
                            throw e;
                        }
                    }
                }
        );
        maxResultsLayout.addComponent(maxResultsField);

        Label maxResultsLabel2 = new Label(MessageProvider.getMessage(mainMessagesPack, "filter.maxResults.label2"));
        maxResultsLayout.addComponent(maxResultsLabel2);
        maxResultsLayout.setComponentAlignment(maxResultsLabel2, com.vaadin.ui.Alignment.MIDDLE_LEFT);

        maxResultsLayout.setStyleName("filter-maxresults");
    }

    private void fillActions() {
        for (Action action : new ArrayList<Action>(actions.getActions())) {
            actions.removeAction(action);
        }

        if (editing)
            return;

        actions.addAction(new CreateAction());

        if (filterEntity == null) {
            if (!defaultFilterEmpty) {
                actions.addAction(new MakeDefaultAction());
            }
            return;
        }

        if ((BooleanUtils.isNotTrue(filterEntity.getIsSet())))
            actions.addAction(new CopyAction());

        if (checkGlobalFilterPermission()) {
            if ((BooleanUtils.isNotTrue(filterEntity.getIsSet())) &&
                    ((filterEntity.getFolder() == null && (filterEntity.getCode() == null)) ||
                            (filterEntity.getFolder() instanceof SearchFolder) ||
                            ((filterEntity.getFolder() instanceof AppFolder) && checkGlobalAppFolderPermission())))
                actions.addAction(new EditAction());

            if (filterEntity.getCode() == null && filterEntity.getFolder() == null)
                actions.addAction(new DeleteAction());
        } else {
            if (filterEntity.getFolder() instanceof SearchFolder) {
                if ((UserSessionProvider.getUserSession().getUser().equals(((SearchFolder) filterEntity.getFolder()).getUser())) &&
                        (BooleanUtils.isNotTrue(filterEntity.getIsSet())))
                    actions.addAction(new EditAction());
            }
            if (filterEntity.getCode() == null && filterEntity.getFolder() == null &&
                    UserSessionProvider.getUserSession().getUser().equals(filterEntity.getUser()))
                actions.addAction(new DeleteAction());
        }
        if (filterEntity != null && BooleanUtils.isNotTrue(filterEntity.getIsDefault())
                && filterEntity.getFolder() == null
                && filterEntity.getIsSet() == null) {
            actions.addAction(new MakeDefaultAction());
        }

        if (filterEntity.getCode() == null && foldersPane != null && filterEntity.getFolder() == null)
            actions.addAction(new SaveAsFolderAction(false));
        if (checkGlobalAppFolderPermission()) {
            if (filterEntity.getCode() == null && foldersPane != null && filterEntity.getFolder() == null)
                actions.addAction(new SaveAsFolderAction(true));
        }
    }

    @Override
    public boolean apply(boolean isNewWindow) {
        if (clientConfig.getGenericFilterChecking()) {
            if (filterEntity != null) {
                WebWindowManager wm = App.getInstance().getWindowManager();

                boolean haveRequiredConditions = haveFilledRequiredConditions();
                if (!haveRequiredConditions) {
                    if (!isNewWindow) {
                        wm.showNotification(MessageProvider.getMessage(mainMessagesPack, "filter.emptyRequiredConditions"),
                                IFrame.NotificationType.HUMANIZED);
                    }
                    return false;
                }

                boolean haveCorrectCondition = hasCorrectCondition();
                if (!haveCorrectCondition) {
                    if (!isNewWindow) {
                        wm.showNotification(MessageProvider.getMessage(mainMessagesPack, "filter.emptyConditions"),
                                IFrame.NotificationType.HUMANIZED);
                    }
                    return false;
                }
            }
        }

        applyDatasourceFilter();

        if (useMaxResults) {
            int maxResults;
            if (BooleanUtils.isTrue((Boolean) maxResultsCb.getValue()))
                maxResults = Integer.valueOf((String) maxResultsField.getValue());  //persistenceManager.getFetchUI(datasource.getMetaClass().getName());
            else
                maxResults = persistenceManager.getMaxFetchUI(datasource.getMetaClass().getName());
            datasource.setMaxResults(maxResults);
        }
        if (datasource instanceof CollectionDatasource.SupportsPaging)
            ((CollectionDatasource.SupportsPaging) datasource).setFirstResult(0);

        refreshDatasource();
        return true;
    }

    protected boolean haveFilledRequiredConditions() {
        for (AbstractCondition condition : conditions.toConditionsList()) {
            if ((condition.isRequired())
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
            if ((condition.getParam() != null) && (condition.getParam().getValue() != null)) {
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
        datasource.refresh();
    }

    private void applyDatasourceFilter() {
        if (filterEntity != null && filterEntity.getXml() != null) {
            Element element = Dom4j.readDocument(filterEntity.getXml()).getRootElement();
            QueryFilter queryFilter = new QueryFilter(element, datasource.getMetaClass().getName());

            if (dsQueryFilter != null) {
                queryFilter = new QueryFilter(dsQueryFilter, queryFilter);
            }

            datasource.setQueryFilter(queryFilter);

        } else {
            datasource.setQueryFilter(dsQueryFilter);
        }
    }

    private void createFilterEntity() {
        filterEntity = new FilterEntity();

        filterEntity.setComponentId(getComponentPath());
        filterEntity.setName(MessageProvider.getMessage(MESSAGES_PACK, "newFilterName"));
        filterEntity.setUser(UserSessionProvider.getUserSession().getCurrentOrSubstitutedUser());
//        filterEntity.setXml(
//                "<filter><and>\n" +
//                        "<c type=\"PROPERTY\" name=\"login\">u.login like :component$usersFilter.login\n" +
//                            "<param name=\"component$users-filter.login\" class=\"java.lang.String\">aaa</param>\n" +
//                        "</c>\n" +
//                        "<c type=\"CUSTOM\" name=\"group\" join=\"left join u.group.hierarchyList h\">\n" +
//                            "(h.parent.id = :component$usersFilter.group or h.group.id = :component$usersFilter.group)" +
//                            " or (u.group.id = :component$usersFilter.group and h.parent is null)\n" +
//                            "<param name=\"component$users-filter.group\" class=\"com.haulmont.cuba.security.entity.Group\"></param>\n" +
//                        "</c>\n" +
//                "</and></filter>"
//        );
    }

    private void copyFilterEntity() {

        FilterEntity newFilterEntity = new FilterEntity();
        newFilterEntity.setComponentId(filterEntity.getComponentId());
        newFilterEntity.setName(MessageProvider.getMessage(MESSAGES_PACK, "newFilterName"));
        newFilterEntity.setUser(UserSessionProvider.getUserSession().getCurrentOrSubstitutedUser());
        //newFilterEntity.setCode(filterEntity.getCode());
        newFilterEntity.setXml(filterEntity.getXml());
        filterEntity = newFilterEntity;
    }

    private String getComponentPath() {
        StringBuilder sb = new StringBuilder(getId());
        IFrame frame = getFrame();
        while (frame != null) {
            sb.insert(0, ".");
            String s = frame.getId();
            if (s.contains("."))
                s = "[" + s + "]";
            sb.insert(0, s);
            if (frame instanceof Window)
                break;
            frame = frame.getFrame();
        }
        return sb.toString();
    }

    private void createParamsLayout(boolean focusOnConditions) {
        boolean hasGroups = false;
        for (AbstractCondition condition : conditions.getRoots()) {
            if (condition.isGroup() && !condition.isHidden()) {
                hasGroups = true;
                break;
            }
        }
        if (hasGroups && conditions.getRootNodes().size() > 1) {
            WebGroupBox groupBox = new WebGroupBox();
            groupBox.setWidth("-1");
            groupBox.setCaption(MessageProvider.getMessage(AbstractCondition.MESSAGES_PACK, "GroupType.AND"));
            paramsLayout = groupBox;
            recursivelyCreateParamsLayout(focusOnConditions, conditions.getRootNodes(), groupBox, 0);
        } else {
            paramsLayout = recursivelyCreateParamsLayout(focusOnConditions, conditions.getRootNodes(), null, 0);
        }
    }

    private ComponentContainer recursivelyCreateParamsLayout(boolean focusOnConditions,
                                                             List<Node<AbstractCondition>> nodes,
                                                             ComponentContainer parentContainer,
                                                             int level) {

        List<Node<AbstractCondition>> visibleConditionNodes = new ArrayList<Node<AbstractCondition>>();
        for (Node<AbstractCondition> node : nodes) {
            AbstractCondition condition = node.getData();
            if (!condition.isHidden())
                visibleConditionNodes.add(node);
        }

        if (visibleConditionNodes.isEmpty()) {
            HorizontalLayout horizontalLayout = new HorizontalLayout();

            if (parentContainer != null)
                parentContainer.addComponent(horizontalLayout);

            return horizontalLayout;
        }

        int columns = level == 0 ? 3 : 2;
        int rows = visibleConditionNodes.size() / columns;
        if (visibleConditionNodes.size() % columns != 0)
            rows++;
        com.vaadin.ui.GridLayout grid = new com.vaadin.ui.GridLayout(columns, rows);
        grid.setMargin(parentContainer == null, false, false, false);
        grid.setSpacing(true);

        boolean focusSet = false;

        for (int i = 0; i < visibleConditionNodes.size(); i++) {
            Node<AbstractCondition> node = visibleConditionNodes.get(i);
            AbstractCondition condition = node.getData();
            com.vaadin.ui.Component cellContent;
            if (condition.isGroup()) {
                WebGroupBox groupBox = new WebGroupBox();
                groupBox.setWidth("-1");
                groupBox.setCaption(condition.getLocCaption());

                if (!node.getChildren().isEmpty()) {
                    recursivelyCreateParamsLayout(
                            focusOnConditions && !focusSet, node.getChildren(), groupBox, level++);
                }
                cellContent = groupBox;
            } else {
                HorizontalLayout paramLayout = new HorizontalLayout();
                paramLayout.setSpacing(true);
                paramLayout.setMargin(false);
                if (condition.getParam().getJavaClass() != null) {
                    Label label = new Label(condition.getLocCaption());
                    paramLayout.addComponent(label);

                    ParamEditor paramEditor = new ParamEditor(condition, true);
                    if (focusOnConditions && !focusSet) {
                        paramEditor.setFocused();
                        focusSet = true;
                    }

                    paramLayout.addComponent(paramEditor);
                }
                cellContent = paramLayout;
            }
            grid.addComponent(cellContent, i % columns, i / columns);
            grid.setComponentAlignment(cellContent, com.vaadin.ui.Alignment.MIDDLE_RIGHT);
        }

        if (parentContainer != null)
            parentContainer.addComponent(grid);

        return grid;
    }

    private void setActions(Table table) {
        if (foldersPane == null) {
            return;
        }
        ButtonsPanel buttons = table.getButtonsPanel();
        if (buttons == null) {
            return; // in lookup windows, there is no button panel
        }
        com.haulmont.cuba.gui.components.Button addToSetBtn = buttons.getButton("addToSetBtn");
        com.haulmont.cuba.gui.components.Button addToCurSetBtn = buttons.getButton("addToCurSetBtn");
        com.haulmont.cuba.gui.components.Button removeFromCurSetBtn = buttons.getButton("removeFromCurSetBtn");

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
                addToCurSetBtn = new WebButton();
                addToCurSetBtn.setId("addToCurSetBtn");
                addToCurSetBtn.setCaption(MessageProvider.getMessage(MESSAGES_PACK, "addToCurSet"));
                buttons.addButton(addToCurSetBtn);
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
                removeFromCurSetBtn = new WebButton();
                removeFromCurSetBtn.setId("removeFromCurSetBtn");
                removeFromCurSetBtn.setCaption(MessageProvider.getMessage(MESSAGES_PACK, "removeFromCurSet"));
                buttons.addButton(removeFromCurSetBtn);
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
                addToSetBtn = new WebButton();
                addToSetBtn.setId("addToSetBtn");
                addToSetBtn.setCaption(MessageProvider.getMessage(MESSAGES_PACK, "addToSet"));
                buttons.addButton(addToSetBtn);
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
    public void setFilterEntity(FilterEntity filterEntity) {
        changingFilter = true;
        try {
            this.filterEntity = filterEntity;

            if ((filterEntity != null) && (applyTo != null) && (Table.class.isAssignableFrom(applyTo.getClass()))) {
                Table table = (Table) applyTo;
                setActions(table);
            }

            parseFilterXml();
            internalSetFilterEntity();
            updateControls();
            if (paramsLayout != null)
                component.removeComponent(paramsLayout);
            createParamsLayout(false);
            component.addComponent(paramsLayout);
        } finally {
            changingFilter = false;
        }
        if (BooleanUtils.isTrue(filterEntity.getApplyDefault()) ||
                BooleanUtils.isTrue(filterEntity.getIsSet()) ||
                !getResultingManualApplyRequired())
            apply(true);
    }

    @Override
    public void loadFiltersAndApplyDefault() {
        loadFilterEntities();

        Window window = ComponentsHelper.getWindow(this);

        Collection<FilterEntity> filters = (Collection<FilterEntity>) select.getItemIds();
        FilterEntity defaultFilter = getDefaultFilter(filters, window);
        if (defaultFilter != null) {
            defaultFilterEmpty = false;
            Map<String, Object> params = window.getContext().getParams();
            if (!BooleanUtils.isTrue((Boolean) params.get("disableAutoRefresh"))) {
                applyingDefault = true;
                try {
                    select.setValue(defaultFilter);
                    select.setItemCaption(defaultFilter, getFilterCaption(defaultFilter)
                            + " "
                            + defaultFilterCaption);
                    updateControls();
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
                } finally {
                    applyingDefault = false;
                }
            }
        } else {
            noFilter.setIsDefault(true);
            defaultFilterEmpty = true;
            updateControls();
        }

        updateComponentRequired(required);
    }

    @Override
    public void setUseMaxResults(boolean useMaxResults) {
        this.useMaxResults = useMaxResults;
        maxResultsLayout.setVisible(useMaxResults
                && UserSessionProvider.getUserSession().isSpecificPermitted("cuba.gui.filter.maxResults"));
    }

    @Override
    public boolean getUseMaxResults() {
        return useMaxResults;
    }

    public void editorCommitted() {
        changingFilter = true;
        try {
            saveFilterEntity();
            parseFilterXml();

            internalSetFilterEntity();
            if (BooleanUtils.isTrue(filterEntity.getIsDefault())) {
                setDefaultFilter();
            }
            switchToUse();
        } finally {
            changingFilter = false;
        }
    }

    private void internalSetFilterEntity() {
        List<FilterEntity> list = new ArrayList(select.getItemIds());
        list.remove(filterEntity);

        select.getContainerDataSource().removeAllItems();

        list.add(filterEntity);

        final Map<FilterEntity, String> captions = new HashMap<FilterEntity, String>();
        for (FilterEntity filter : list) {
            String filterCaption;
            if (filter == filterEntity) {
                filterCaption = getCurrentFilterCaption();
            } else {
                filterCaption = getFilterCaption(filter);
                if (BooleanUtils.isTrue(filter.getIsDefault()) && !noFilter.equals(filter)) {
                    filterCaption += " " + defaultFilterCaption;
                }
            }
            captions.put(filter, filterCaption);
        }

        Collections.sort(
                list,
                new Comparator<FilterEntity>() {
                    @Override
                    public int compare(FilterEntity f1, FilterEntity f2) {
                        return captions.get(f1).compareTo(captions.get(f2));
                    }
                }
        );

        select.addItem(noFilter);
        for (FilterEntity filter : list) {
            select.addItem(filter);
            select.setItemCaption(filter, captions.get(filter));
        }

        select.setValue(filterEntity);
    }

    public String getCurrentFilterCaption() {
        String name;
        if (filterEntity != null) {
            if (filterEntity.getCode() == null)
                name = InstanceUtils.getInstanceName(filterEntity);
            else {
                name = MessageProvider.getMessage(mainMessagesPack, filterEntity.getCode());
            }
            AbstractSearchFolder folder = filterEntity.getFolder();
            if (folder != null) {
                if (!StringUtils.isBlank(folder.getTabName()))
                    name = MessageProvider.getMessage(mainMessagesPack, folder.getTabName());
                else if (!StringUtils.isBlank(folder.getName())) {
                    name = MessageProvider.getMessage(mainMessagesPack, folder.getName());
                }
                if (BooleanUtils.isTrue(filterEntity.getIsSet()))
                    name = MessageProvider.getMessage(MESSAGES_PACK, "setPrefix") + " " + name;
                else
                    name = MessageProvider.getMessage(MESSAGES_PACK, "folderPrefix") + " " + name;
            }
        } else
            name = "";
        return name;
    }


    public List<AbstractCondition> getConditions() {
        return Collections.unmodifiableList(conditions.toConditionsList());
    }

    public void editorCancelled() {
        if (filterEntity.getXml() == null)
            filterEntity = null;

        switchToUse();
    }

    private String getFilterCaption(FilterEntity filter) {
        if (filter.getCode() == null)
            return filter.getName();
        else {
            return MessageProvider.getMessage(mainMessagesPack, filter.getCode());
        }
    }

    private void loadFilterEntities() {
        DataService ds = ServiceLocator.getDataService();
        LoadContext ctx = new LoadContext(FilterEntity.class);
        ctx.setView("app");

        User user = UserSessionProvider.getUserSession().getSubstitutedUser();
        if (user == null)
            user = UserSessionProvider.getUserSession().getUser();

        ctx.setQueryString("select f from sec$Filter f " +
                "where f.componentId = :component and (f.user is null or f.user.id = :userId) order by f.name")
                .addParameter("component", getComponentPath())
                .addParameter("userId", user.getId());

        List<FilterEntity> filters = new ArrayList(ds.loadList(ctx));
        final Map<FilterEntity, String> captions = new HashMap<FilterEntity, String>();
        for (FilterEntity filter : filters) {
            String filterCaption = getFilterCaption(filter);
            captions.put(filter, filterCaption);
        }

        Collections.sort(
                filters,
                new Comparator<FilterEntity>() {
                    @Override
                    public int compare(FilterEntity f1, FilterEntity f2) {
                        return captions.get(f1).compareTo(captions.get(f2));
                    }
                }
        );

        select.addItem(noFilter);
        for (FilterEntity filter : filters) {
            select.addItem(filter);
            select.setItemCaption(filter, captions.get(filter));
        }
    }

    private FilterEntity getDefaultFilter(Collection<FilterEntity> filters, Window window) {
        // First check if there is parameter with name equal to this filter component id, containing a filter code to apply
        Map<String, Object> params = window.getContext().getParams();
        String code = (String) params.get(getId());
        if (!StringUtils.isBlank(code)) {
            for (FilterEntity filter : filters) {
                if (code.equals(filter.getCode()))
                    return filter;
            }
        }

        // No 'filter' parameter found, load default filter
        SettingsImpl settings = new SettingsImpl(window.getId());

        String componentPath = getComponentPath();
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

    private void saveFilterEntity() {
        Boolean isDefault = filterEntity.getIsDefault();
        Boolean applyDefault = filterEntity.getApplyDefault();
        if (filterEntity.getFolder() == null) {
            DataService ds = ServiceLocator.getDataService();
            CommitContext ctx = new CommitContext(Collections.singletonList(filterEntity));
            Set<Entity> result = ds.commit(ctx);
            for (Entity entity : result) {
                if (entity.equals(filterEntity)) {
                    filterEntity = (FilterEntity) entity;
                    break;
                }
            }
            filterEntity.setApplyDefault(applyDefault);
            filterEntity.setIsDefault(isDefault);


        } else if (filterEntity.getFolder() instanceof SearchFolder) {
            filterEntity.getFolder().setName(filterEntity.getName());
            filterEntity.getFolder().setFilterXml(filterEntity.getXml());
            SearchFolder folder = saveFolder((SearchFolder) filterEntity.getFolder());
            filterEntity.setFolder(folder);
        } else if (filterEntity.getFolder() instanceof AppFolder) {
            filterEntity.getFolder().setName(filterEntity.getName());
            filterEntity.getFolder().setFilterXml(filterEntity.getXml());
            AppFolder folder = saveAppFolder((AppFolder) filterEntity.getFolder());
            filterEntity.setFolder(folder);
        }
    }

    private void deleteFilterEntity() {
        DataService ds = ServiceLocator.getDataService();
        CommitContext ctx = new CommitContext();
        ctx.setRemoveInstances(Collections.singletonList(filterEntity));
        ds.commit(ctx);
    }

    private void createEditLayout() {
        editLayout = new VerticalLayout();
        editLayout.setSpacing(true);

        List<String> names = new ArrayList<String>();
        Map<String, Locale> locales = globalConfig.getAvailableLocales();
        for (Object id : select.getItemIds()) {
            if (id != filterEntity) {
                FilterEntity fe = (FilterEntity) id;
                if (fe.getCode() == null)
                    names.add(fe.getName());
                else {
                    for (Map.Entry<String, Locale> locale : locales.entrySet()) {
                        names.add(MessageProvider.getMessage(mainMessagesPack, fe.getCode(), locale.getValue()));
                    }
                }
            }
        }

        editor = new FilterEditor(this, filterEntity, getXmlDescriptor(), names);
        editor.init();
        editor.getSaveButton().addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (BooleanUtils.isTrue(filterEntity.getIsDefault())) {
                    Collection<FilterEntity> filters = (Collection<FilterEntity>) select.getItemIds();
                    for (FilterEntity filter : filters) {
                        if (!filter.equals(filterEntity))
                            filter.setIsDefault(false);
                    }
                }
            }
        });
        editLayout.addComponent(editor.getLayout());
    }

    @Override
    public CollectionDatasource getDatasource() {
        return datasource;
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

        } else {
            int maxResults = persistenceManager.getFetchUI(datasource.getMetaClass().getName());
            maxResultsField.setValue(String.valueOf(maxResults));

            datasource.setMaxResults(maxResults);
        }
    }

    private void switchToUse() {
        editing = false;
        editor = null;
        updateControls();
        component.removeComponent(editLayout);
        createParamsLayout(true);
        component.addComponent(paramsLayout);
    }

    private void switchToEdit() {
        editing = true;
        updateControls();
        component.removeComponent(paramsLayout);
        createEditLayout();
        component.addComponent(editLayout);
    }

    private void updateControls() {
        fillActions();
        actions.setVisible(!editing);
        ((PopupButton) actions.getComponent()).setPopupVisible(false);
        ((PopupButton) actions.getComponent()).setVisible(editable);

        select.setEnabled(!editing);
        applyBtn.setVisible(!editing);
    }

    private boolean checkGlobalAppFolderPermission() {
        return UserSessionProvider.getUserSession().isSpecificPermitted(GLOBAL_APP_FOLDERS_PERMISSION);
    }

    private boolean checkGlobalFilterPermission() {
        if (filterEntity == null || filterEntity.getUser() != null)
            return true;
        else
            return UserSessionProvider.getUserSession().isSpecificPermitted(GLOBAL_FILTER_PERMISSION);
    }

    @Override
    public void add(Component component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(Component component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Component> T getOwnComponent(String id) {
        List<AbstractCondition> list = editor == null ? conditions.toConditionsList() : editor.getConditions();

        for (AbstractCondition condition : list) {
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

    @Override
    public <T extends Component> T getComponent(String id) {
        String[] elements = ValuePathHelper.parse(id);
        if (elements.length == 1)
            return (T) getOwnComponent(id);
        else
            throw new UnsupportedOperationException("Filter contains only one level of subcomponents");
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public Collection<Component> getComponents() {
        return getOwnComponents();
    }

    private void parseFilterXml() {
        if (filterEntity == null) {
            conditions = new ConditionsTree();
        } else {
            FilterParser parser =
                    new FilterParser(filterEntity.getXml(), getFrame().getMessagesPack(), getId(), datasource);
            conditions = parser.fromXml().getConditions();

            // If there are window parameters named as filter parameters, assign values to the corresponding
            // filter params. Together with passing a filter code in 'filter' window parameter it allows to open an
            // arbitrary filter with parameters regardless of a user defined default filter.
            Window window = ComponentsHelper.getWindow(this);
            for (AbstractCondition condition : conditions.toConditionsList()) {
                if (condition.getParam() != null) {
                    for (Map.Entry<String, Object> entry : window.getContext().getParams().entrySet()) {
                        if (entry.getKey().equals(condition.getParam().getName()))
                            condition.getParam().parseValue((String) entry.getValue());
                    }
                }
            }
        }
    }

    @Override
    public void applySettings(Element element) {
        // logic moved to loadFiltersAndApplyDefault()
    }

    @Override
    public boolean saveSettings(Element element) {
        Boolean changed = false;
        Element e = element.element("defaultFilter");
        if (e == null)
            e = element.addElement("defaultFilter");

        UUID defaultId = null;
        Boolean applyDefault = false;
        Collection<FilterEntity> filters = (Collection<FilterEntity>) select.getItemIds();
        for (FilterEntity filter : filters) {
            if (isTrue(filter.getIsDefault())) {
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

    @Override
    public Component getApplyTo() {
        return applyTo;
    }

    @Override
    public void setApplyTo(Component component) {
        applyTo = component;
        if ((applyTo != null) && (Table.class.isAssignableFrom(applyTo.getClass()))) {
            Table table = (Table) applyTo;
            setActions(table);
        }
    }

    private void saveAsFolder(boolean isAppFolder) {
        final AbstractSearchFolder folder;
        if (isAppFolder)
            folder = (MetadataProvider.create(AppFolder.class));
        else
            folder = (new SearchFolder());

        if (filterEntity.getCode() == null) {
            folder.setName(filterEntity.getName());
            folder.setTabName(filterEntity.getName());
        } else {
            String name = MessageProvider.getMessage(mainMessagesPack, filterEntity.getCode());
            folder.setName(name);
            folder.setTabName(name);
        }

        String newXml = submintParameters();

        folder.setFilterComponentId(filterEntity.getComponentId());
        folder.setFilterXml(newXml);
        if (!isAppFolder) {
            if (UserSessionProvider.getUserSession().isSpecificPermitted(GLOBAL_FILTER_PERMISSION))
                ((SearchFolder) folder).setUser(filterEntity.getUser());
            else
                ((SearchFolder) folder).setUser(UserSessionProvider.getUserSession().getCurrentOrSubstitutedUser());
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
            commitHandler = new Runnable() {
                @Override
                public void run() {
                    AppFolder savedFolder = saveAppFolder((AppFolder) folder);
                    filterEntity.setFolder(savedFolder);
                    //if (UserSessionProvider.getUserSession().isSpecificPermitted(GLOBAL_FILTER_PERMISSION))
                    //    deleteFilterEntity();
                    select.setItemCaption(filterEntity, getCurrentFilterCaption());
                }
            };
        } else {
            commitHandler = new Runnable() {
                @Override
                public void run() {
                    SearchFolder savedFolder = saveFolder((SearchFolder) folder);
                    filterEntity.setFolder(savedFolder);
                    //if (UserSessionProvider.getUserSession().isSpecificPermitted(GLOBAL_FILTER_PERMISSION))
                    //    deleteFilterEntity();
                    select.setItemCaption(filterEntity, getCurrentFilterCaption());

                    // search for existing folders with the same name
                    //                        boolean found = false;
                    //                        Collection<SearchFolder> folders = foldersPane.getSearchFolders();
                    //                        for (final SearchFolder existingFolder : folders) {
                    //                            if (ObjectUtils.equals(existingFolder.getName(), folder.getName())) {
                    //                                found = true;
                    //                                App.getInstance().getWindowManager().showOptionDialog(
                    //                                        MessageProvider.getMessage(AppConfig.getMessagesPack(), "dialogs.Confirmation"),
                    //                                        MessageProvider.getMessage(MESSAGES_PACK, "saveAsFolderConfirmUpdate"),
                    //                                        IFrame.MessageType.CONFIRMATION,
                    //                                        new Action[] {
                    //                                                new DialogAction(DialogAction.Type.YES) {
                    //                                                    @Override
                    //                                                    public void actionPerform(Component component) {
                    //                                                        // update existing folder
                    //                                                        existingFolder.setFilterComponentId(folder.getFilterComponentId());
                    //                                                        existingFolder.setFilterXml(folder.getFilterXml());
                    //                                                        saveFolder(existingFolder);
                    //                                                    }
                    //                                                },
                    //                                                new DialogAction(DialogAction.Type.NO) {
                    //                                                    @Override
                    //                                                    public void actionPerform(Component component) {
                    //                                                        // create new folder
                    //                                                        saveFolder(folder);
                    //                                                    }
                    //                                                }
                    //                                        }
                    //                                );
                    //                            }
                    //                        }
                    //                        if (!found) {
                    // create new folder
                    //                            saveFolder(folder);
                    //                        }
                }
            };
        }

        final FolderEditWindow window = AppFolderEditWindow.create(isAppFolder, false, folder, presentations, commitHandler);
        window.addListener(new com.vaadin.ui.Window.CloseListener() {
            @Override
            public void windowClose(com.vaadin.ui.Window.CloseEvent e) {
                App.getInstance().getAppWindow().removeWindow(window);
            }
        });
        App.getInstance().getAppWindow().addWindow(window);
    }

    private String submintParameters() {
        FilterParser parser = new FilterParser(filterEntity.getXml(), MESSAGES_PACK, filterEntity.getComponentId(), datasource);
        parser.fromXml();
        List<AbstractCondition> defaultConditions = parser.getConditions().toConditionsList();
        Iterator<AbstractCondition> it = conditions.toConditionsList().iterator();
        Iterator<AbstractCondition> defaultIt = defaultConditions.iterator();
        while (it.hasNext()) {
            AbstractCondition current = it.next();
            AbstractCondition defCondition = defaultIt.next();
            if (current.getParam().getValue() != null) {
                defCondition.setParam(current.getParam());
            }
        }
        return parser.toXml().getXml();
    }

    private SearchFolder saveFolder(SearchFolder folder) {
        SearchFolder savedFolder = (SearchFolder) foldersPane.saveFolder(folder);
        foldersPane.refreshFolders();
        return savedFolder;
    }

    private AppFolder saveAppFolder(AppFolder folder) {
        AppFolder savedFolder = (AppFolder) foldersPane.saveFolder(folder);
        foldersPane.refreshFolders();
        return savedFolder;
    }

    private void delete() {
        getFrame().showOptionDialog(
                MessageProvider.getMessage(MESSAGES_PACK, "deleteDlg.title"),
                MessageProvider.getMessage(MESSAGES_PACK, "deleteDlg.msg"),
                IFrame.MessageType.CONFIRMATION,
                new Action[]{
                        new DialogAction(DialogAction.Type.YES) {
                            @Override
                            public void actionPerform(Component component) {
                                deleteFilterEntity();
                                filterEntity = null;
                                select.removeItem(select.getValue());
                                if (!select.getItemIds().isEmpty()) {
                                    select.select(select.getItemIds().iterator().next());
                                } else {
                                    select.select(null);
                                }
                            }
                        },
                        new DialogAction(DialogAction.Type.NO)
                }
        );
    }

    private void setDefaultFilter() {
        if (filterEntity != null) {
            filterEntity.setIsDefault(true);
            defaultFilterEmpty = false;
        } else defaultFilterEmpty = true;
        Collection<FilterEntity> filters = (Collection<FilterEntity>) select.getItemIds();
        for (FilterEntity filter : filters) {
            if (!ObjectUtils.equals(filter, filterEntity)) {
                if (BooleanUtils.isTrue(filter.getIsDefault())) {
                    select.setItemCaption(filter, getFilterCaption(filter));
                    filter.setIsDefault(false);
                }
            }
        }
        if (filterEntity != null)
            select.setItemCaption(filterEntity, getFilterCaption(filterEntity) + " " + defaultFilterCaption);
    }

    @Override
    public void setManualApplyRequired(Boolean manualApplyRequired) {
        this.manualApplyRequired = manualApplyRequired;
    }

    @Override
    public Boolean getManualApplyRequired() {
        return manualApplyRequired;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
        ((PopupButton) actions.getComponent()).setVisible(editable);
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setRequired(boolean required) {
        updateComponentRequired(required);

        if (this.required != required)
            select.setNullSelectionAllowed(!required);
        this.required = required;
    }

    private void updateComponentRequired(boolean required) {
        if (required && (select.getValue() == null)) {
            // select first item
            Collection<?> itemIds = select.getItemIds();
            if ((itemIds != null) && (!itemIds.isEmpty())) {
                Object nullSelectionItemId = select.getNullSelectionItemId();
                Object defaultItemId = null;

                Iterator<?> iterator = itemIds.iterator();
                while (iterator.hasNext() && (defaultItemId == null)) {
                    Object itemId = iterator.next();
                    if (itemId != nullSelectionItemId)
                        defaultItemId = itemId;
                }

                if (defaultItemId != null)
                    select.select(defaultItemId);
            }
        }
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    private boolean getResultingManualApplyRequired() {
        return manualApplyRequired != null ? manualApplyRequired : clientConfig.getGenericFilterManualApplyRequired();
    }

    private class SelectListener implements Property.ValueChangeListener {
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            if (changingFilter)
                return;

            filterEntity = (FilterEntity) select.getValue();
            if ((filterEntity != null) && (applyTo != null) && (Table.class.isAssignableFrom(applyTo.getClass()))) {
                Table table = (Table) applyTo;
                setActions(table);
            }

            parseFilterXml();
            updateControls();
            component.removeComponent(paramsLayout);
            createParamsLayout(true);
            component.addComponent(paramsLayout);

            if (!applyingDefault) {
                Window window = ComponentsHelper.getWindow(WebFilter.this);
                String descr;
                if (filterEntity != null)
                    if (filterEntity.getCode() != null) {
                        descr = MessageProvider.getMessage(mainMessagesPack, filterEntity.getCode());
                    } else
                        descr = filterEntity.getName();
                else
                    descr = null;
                window.setDescription(descr);
                App.getInstance().getWindowManager().setCurrentWindowCaption(window, window.getCaption(), descr);
            }

            if (useMaxResults)
                maxResultsCb.setValue(true);
        }
    }

    private class CreateAction extends AbstractAction {

        protected CreateAction() {
            super("createAction");
        }

        @Override
        public String getCaption() {
            return MessageProvider.getMessage(MESSAGES_PACK, getId());
        }

        @Override
        public void actionPerform(Component component) {
            createFilterEntity();
            parseFilterXml();
            switchToEdit();
        }
    }

    private class CopyAction extends AbstractAction {
        protected CopyAction() {
            super("copyAction");
        }

        @Override
        public String getCaption() {
            return MessageProvider.getMessage(MESSAGES_PACK, getId());
        }

        @Override
        public void actionPerform(Component component) {
            copyFilterEntity();
            parseFilterXml();
            switchToEdit();
        }

    }

    private class EditAction extends AbstractAction {

        protected EditAction() {
            super("editAction");
        }

        @Override
        public String getCaption() {
            return MessageProvider.getMessage(MESSAGES_PACK, getId());
        }

        @Override
        public void actionPerform(Component component) {
            switchToEdit();
        }
    }

    private class DeleteAction extends AbstractAction {

        protected DeleteAction() {
            super("deleteAction");
        }

        @Override
        public String getCaption() {
            return MessageProvider.getMessage(MESSAGES_PACK, getId());
        }

        @Override
        public void actionPerform(Component component) {
            delete();
        }
    }

    private class MakeDefaultAction extends AbstractAction {
        public MakeDefaultAction() {
            super("makeDefault");
        }

        @Override
        public String getCaption() {
            return MessageProvider.getMessage(MESSAGES_PACK, getId());
        }

        @Override
        public void actionPerform(Component component) {
            setDefaultFilter();
            actions.removeAction(MakeDefaultAction.this);
        }
    }

    private class SaveAsFolderAction extends AbstractAction {

        private boolean isAppFolder;

        protected SaveAsFolderAction(boolean isAppFolder) {
            super(isAppFolder ? ("saveAsAppFolderAction") : ("saveAsFolderAction"));
            this.isAppFolder = isAppFolder;
        }

        @Override
        public String getCaption() {
            return MessageProvider.getMessage(MESSAGES_PACK, getId());
        }

        @Override
        public void actionPerform(Component component) {
            saveAsFolder(isAppFolder);
        }
    }

    public static final Pattern LIKE_PATTERN = Pattern.compile("\\slike\\s+" + ParametersHelper.QUERY_PARAMETERS_RE);

    private static class ParamWrapper implements HasValue {

        private final AbstractCondition condition;
        private final AbstractParam param;

        private ParamWrapper(AbstractCondition condition, AbstractParam param) {
            this.condition = condition;
            this.param = param;
        }

        @Override
        public <T> T getValue() {
            Object value = param.getValue();
            if (value instanceof String
                    && !StringUtils.isEmpty((String) value)
                    && !((String) value).contains("%")
                    && !((String) value).startsWith(ParametersHelper.CASE_INSENSITIVE_MARKER)) {
                // try to wrap value for case-insensitive "like" search
                if (condition instanceof PropertyCondition) {
                    Op op = ((PropertyCondition) condition).getOperator();
                    if (Op.CONTAINS.equals(op) || op.equals(Op.DOES_NOT_CONTAIN)) {
                        value = wrapValueForLike(value);
                    } else if (Op.STARTS_WITH.equals(op)) {
                        value = wrapValueForLike(value, false, true);
                    } else if (Op.ENDS_WITH.equals(op)) {
                        value = wrapValueForLike(value, true, false);
                    }
                } else if (condition instanceof RuntimePropCondition) {
                    Op op = ((RuntimePropCondition) condition).getOperator();
                    if (Op.CONTAINS.equals(op) || op.equals(Op.DOES_NOT_CONTAIN)) {
                        value = wrapValueForLike(value);
                    } else if (Op.STARTS_WITH.equals(op)) {
                        value = wrapValueForLike(value, false, true);
                    } else if (Op.ENDS_WITH.equals(op)) {
                        value = wrapValueForLike(value, true, false);
                    }
                } else if (condition instanceof CustomCondition) {
                    String where = ((CustomCondition) condition).getWhere();
                    Op op = ((CustomCondition) condition).getOperator();
                    Matcher matcher = LIKE_PATTERN.matcher(where);
                    if (matcher.find()) {
                        if (Op.STARTS_WITH.equals(op)) {
                            value = wrapValueForLike(value, false, true);
                        } else if (Op.ENDS_WITH.equals(op)) {
                            value = wrapValueForLike(value, true, false);
                        } else {
                            value = wrapValueForLike(value);
                        }
                    }
                }
            } else if (value instanceof EnumClass) {
                value = ((EnumClass) value).getId();
            }
            return (T) value;
        }

        private String wrapValueForLike(Object value) {
            return ParametersHelper.CASE_INSENSITIVE_MARKER + "%" + value + "%";
        }

        private String wrapValueForLike(Object value, boolean before, boolean after) {
            return ParametersHelper.CASE_INSENSITIVE_MARKER + (before ? "%" : "") + value + (after ? "%" : "");
        }

        @Override
        public void setValue(Object value) {
        }

        @Override
        public void addListener(ValueListener listener) {
            param.addListener(listener);
        }

        @Override
        public void removeListener(ValueListener listener) {
            param.removeListener(listener);
        }

        @Override
        public void setValueChangingListener(ValueChangingListener listener) {
        }

        @Override
        public void removeValueChangingListener() {
        }

        @Override
        public boolean isEditable() {
            return false;
        }

        @Override
        public void setEditable(boolean editable) {
        }

        @Override
        public String getId() {
            return param.getName();
        }

        @Override
        public void setId(String id) {
        }

        @Override
        public String getDebugId() {
            return null;
        }

        @Override
        public void setDebugId(String id) {
        }

        @Override
        public boolean isEnabled() {
            return false;
        }

        @Override
        public void setEnabled(boolean enabled) {
        }

        @Override
        public boolean isVisible() {
            return false;
        }

        @Override
        public void setVisible(boolean visible) {
        }

        @Override
        public void requestFocus() {
        }

        @Override
        public float getHeight() {
            return 0;
        }

        @Override
        public int getHeightUnits() {
            return 0;
        }

        @Override
        public void setHeight(String height) {
        }

        @Override
        public float getWidth() {
            return 0;
        }

        @Override
        public int getWidthUnits() {
            return 0;
        }

        @Override
        public void setWidth(String width) {
        }

        @Override
        public Alignment getAlignment() {
            return Alignment.TOP_LEFT;
        }

        @Override
        public void setAlignment(Alignment alignment) {
        }

        @Override
        public String getStyleName() {
            return null;
        }

        @Override
        public void setStyleName(String name) {
        }

        @Override
        public <A extends IFrame> A getFrame() {
            return null;
        }

        @Override
        public void setFrame(IFrame frame) {
        }
    }

    private class AddToSetAction extends AbstractAction {
        private Table table;

        private AddToSetAction(Table table) {
            super("addToSet");
            this.table = table;
        }

        @Override
        public String getCaption() {
            return MessageProvider.getMessage(MESSAGES_PACK, getId());
        }

        @Override
        public void actionPerform(Component component) {
            if (!table.getSelected().isEmpty()) {
                String entityType = table.getDatasource().getMetaClass().getName();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("entityType", entityType);
                params.put("items", table.getSelected());
                params.put("componentPath", getComponentPath());
                String[] strings = ValuePathHelper.parse(getComponentPath());
                String componentId = ValuePathHelper.format(Arrays.copyOfRange(strings, 1, strings.length));
                params.put("componentId", componentId);
                params.put("foldersPane", foldersPane);
                params.put("entityClass", datasource.getMetaClass().getJavaClass().getName());
                params.put("query", datasource.getQuery());
                WebFilter.this.getFrame().openWindow("sec$SaveSetWindow",
                        WindowManager.OpenType.DIALOG,
                        params);
            }
        }
    }

    private class RemoveFromSetAction extends AbstractAction {
        private Table table;

        protected RemoveFromSetAction(Table table) {
            super("removeFromCurSet");
            this.table = table;
        }

        @Override
        public String getCaption() {
            return MessageProvider.getMessage(MESSAGES_PACK, getId());
        }

        @Override
        public void actionPerform(Component component) {
            Set selected = table.getSelected();
            if (selected.isEmpty())
                return;
            if (table.getDatasource().getItemIds().size() == 1) {
                deleteFilterEntity();
                foldersPane.removeFolder(filterEntity.getFolder());
                foldersPane.refreshFolders();
                Collection<Window> windows = App.getInstance().getWindowManager().getOpenWindows();
                for (Window window : windows) {
                    if (window.equals(WebFilter.this.getFrame())) {
                        App.getInstance().getWindowManager().close(window);
                        break;
                    }
                }
                return;
            }
            String filterXml = filterEntity.getXml();
            filterEntity.setXml(WebFilter.UserSetHelper.removeEntities(filterXml, selected));
            filterEntity.getFolder().setFilterXml(filterEntity.getXml());
            filterEntity.setFolder(saveFolder((SearchFolder) filterEntity.getFolder()));
            parseFilterXml();
            apply(false);
        }
    }

    private class AddToCurrSetAction extends AbstractAction {

        protected AddToCurrSetAction() {
            super("addToCurSet");
        }

        @Override
        public String getCaption() {
            return MessageProvider.getMessage(MESSAGES_PACK, getId());
        }

        @Override
        public void actionPerform(Component component) {
            IFrame frame = WebFilter.this.getFrame();
            String[] strings = ValuePathHelper.parse(getComponentPath());
            String windowAlias = strings[0];
            frame.openLookup(windowAlias, new Window.Lookup.Handler() {
                @Override
                public void handleLookup(Collection items) {
                    String filterXml = filterEntity.getXml();
                    filterEntity.setXml(WebFilter.UserSetHelper.addEntities(filterXml, items));
                    filterEntity.getFolder().setFilterXml(filterEntity.getXml());
                    filterEntity.setFolder(saveFolder((SearchFolder) filterEntity.getFolder()));
                    parseFilterXml();
                    apply(false);
                }
            }, WindowManager.OpenType.THIS_TAB);
        }
    }

    public static class UserSetHelper {
        public static String generateSetFilter(Set ids, String entityClass, String componentId, String entityAlias) {
            Document document = DocumentHelper.createDocument();
            Element root = DocumentHelper.createElement("filter");
            Element or = root.addElement("and");
            Element condition = or.addElement("c");
            condition.addAttribute("name", "set");
            condition.addAttribute("inExpr", "true");
            condition.addAttribute("hidden", "true");
            condition.addAttribute("locCaption", "Set filter");
            condition.addAttribute("entityAlias", entityAlias);
            condition.addAttribute("class", entityClass);
            condition.addAttribute("type", ConditionType.CUSTOM.name());
            String listOfId = createIdsString(ids);
            String randomName = RandomStringUtils.randomAlphabetic(10);
            condition.addText(entityAlias + ".id in (:component$" + componentId + "." + randomName + ")");
            Element param = condition.addElement("param");
            param.addAttribute("name", "component$" + componentId + "." + randomName);
            param.addText(listOfId);
            document.add(root);
            return Dom4j.writeDocument(document, true);
        }

        public static Set parseSet(String text) {
            Set<String> set = new HashSet<String>();
            if ("NULL".equals(StringUtils.trimToEmpty(text)))
                return set;
            String[] ids = text.split(",");
            for (String id : ids) {
                String s = StringUtils.trimToNull(id);
                if (s != null)
                    set.add(s);
            }
            return set;
        }

        public static String createIdsString(Set entities) {
            return createIdsString(new HashSet<String>(), entities);
        }

        public static String createIdsString(Set<String> current, Collection entities) {
            Set<String> convertedSet = new HashSet<String>();
            for (Object entity : entities) {
                convertedSet.add(((BaseUuidEntity) entity).getId().toString());
            }
            current.addAll(convertedSet);
            if (current.isEmpty()) {
                return "NULL";
            }
            StringBuilder listOfId = new StringBuilder();
            Iterator it = current.iterator();
            while (it.hasNext()) {
                listOfId.append(it.next());
                if (it.hasNext()) {
                    listOfId.append(',');
                }
            }
            return listOfId.toString();
        }

        public static String removeIds(Set<String> current, Collection entities) {
            Set<String> convertedSet = new HashSet<String>();
            for (Object entity : entities) {
                convertedSet.add(((BaseUuidEntity) entity).getId().toString());
            }
            current.removeAll(convertedSet);
            if (current.isEmpty()) {
                return "NULL";
            }
            StringBuilder listOfId = new StringBuilder();
            Iterator it = current.iterator();
            while (it.hasNext()) {
                listOfId.append(it.next());
                if (it.hasNext()) {
                    listOfId.append(',');
                }
            }
            return listOfId.toString();
        }

        public static String removeEntities(String filterXml, Collection ids) {
            Document document;
            try {
                document = DocumentHelper.parseText(filterXml);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
            Element param = document.getRootElement().element("and").element("c").element("param");
            String currentIds = param.getTextTrim();
            Set set = parseSet(currentIds);
            String listOfIds = removeIds(set, ids);
            param.setText(listOfIds);
            return document.asXML();
        }

        public static String addEntities(String filterXml, Collection ids) {
            Document document;
            try {
                document = DocumentHelper.parseText(filterXml);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
            Element param = document.getRootElement().element("and").element("c").element("param");
            String currentIds = param.getTextTrim();
            Set set = parseSet(currentIds);
            String listOfIds = createIdsString(set, ids);
            param.setText(listOfIds);
            return document.asXML();
        }
    }
}

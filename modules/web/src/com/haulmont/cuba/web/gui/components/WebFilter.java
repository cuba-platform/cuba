/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 14.10.2009 14:03:58
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.UserSessionProvider;
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
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.AbstractParam;
import com.haulmont.cuba.gui.components.filter.ConditionType;
import com.haulmont.cuba.gui.components.filter.Op;
import com.haulmont.cuba.gui.data.CollectionDatasource;
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
import com.haulmont.cuba.web.app.folders.AppFolderEditWindow;
import com.haulmont.cuba.web.app.folders.FolderEditWindow;
import com.haulmont.cuba.web.app.folders.FoldersPane;
import com.haulmont.cuba.web.gui.components.filter.*;
import com.haulmont.cuba.web.toolkit.ui.FilterSelect;
import com.vaadin.data.Property;
import com.vaadin.data.validator.IntegerValidator;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang.BooleanUtils.isTrue;

public class WebFilter
        extends WebAbstractComponent<VerticalLayout> implements Filter {
    private static final String MESSAGES_PACK = "com.haulmont.cuba.gui.components.filter";

    private PersistenceManagerService persistenceManager;

    private CollectionDatasource datasource;
    private QueryFilter dsQueryFilter;
    private FilterEntity filterEntity;
    private List<AbstractCondition> conditions = Collections.EMPTY_LIST;

    private AbstractLayout paramsLayout;
    private AbstractOrderedLayout editLayout;
    private FilterSelect select;
    private WebPopupButton actions;

    private Button applyBtn;

    private boolean changingFilter;
    private boolean applyingDefault;
    private boolean editing;
    private FilterEditor editor;
    private FoldersPane foldersPane;

    private boolean useMaxResults;
    private CheckBox maxResultsCb;
    private TextField maxResultsField;
    private AbstractOrderedLayout maxResultsLayout;

    private Component applyTo;

    private static final String GLOBAL_FILTER_PERMISSION = "cuba.gui.filter.global";
    private static final String GLOBAL_APP_FOLDERS_PERMISSION = "cuba.gui.appFolder.global";

    private String mainMessagesPack = AppConfig.getMessagesPack();

    private FilterEntity noFilter;

    private GlobalConfig globalConfig = ConfigProvider.getConfig(GlobalConfig.class);
    private ClientConfig clientConfig = ConfigProvider.getConfig(ClientConfig.class);

    public WebFilter() {
        persistenceManager = ServiceLocator.lookup(PersistenceManagerService.NAME);
        component = new VerticalLayout();
        component.setMargin(true);
        component.setStyleName("generic-filter");

        foldersPane = App.getInstance().getAppWindow().getFoldersPane();

        HorizontalLayout topLayout = new HorizontalLayout();
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

        component.addComponent(topLayout);

        createParamsLayout(false);
        component.addComponent(paramsLayout);
        updateControls();
    }

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
                    public void buttonClick(Button.ClickEvent event) {
                        maxResultsField.setEnabled(BooleanUtils.isTrue((Boolean) maxResultsCb.getValue()));
                    }
                }
        );
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
    }

    private void fillActions() {
        for (Action action : new ArrayList<Action>(actions.getActions())) {
            actions.removeAction(action);
        }

        if (editing)
            return;

        actions.addAction(new CreateAction());

        if (filterEntity == null) {
            actions.addAction(new MakeDefaultAction());
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

        actions.addAction(new MakeDefaultAction());

        if (filterEntity.getCode() == null && foldersPane != null && filterEntity.getFolder() == null)
            actions.addAction(new SaveAsFolderAction(false));
        if (checkGlobalAppFolderPermission()) {
            if (filterEntity.getCode() == null && foldersPane != null && filterEntity.getFolder() == null)
                actions.addAction(new SaveAsFolderAction(true));
        }
    }

    public boolean apply(boolean isNewWindow) {
        if (clientConfig.getGenericFilterChecking()) {
            if (filterEntity != null) {
                boolean haveCorrectCondition = false;

                for (AbstractCondition condition : conditions) {
                    if ((condition.getParam()==null) || (condition.getParam().getValue() != null)) {
                        haveCorrectCondition = true;
                        break;
                    }
                }

                if (!haveCorrectCondition) {
                    if (!isNewWindow) {
                        App.getInstance().getWindowManager().showNotification
                                (MessageProvider.getMessage(mainMessagesPack, "filter.emptyConditions"), IFrame.NotificationType.ERROR);
                    }
                    return false;
                } else
                    applyDatasourceFilter();
            } else
                applyDatasourceFilter();
        } else {
            applyDatasourceFilter();
        }

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

        datasource.refresh();
        return true;
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
        newFilterEntity.setCode(filterEntity.getCode());
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
        List<AbstractCondition> visibleConditions = new ArrayList<AbstractCondition>();
        for (AbstractCondition condition : conditions) {
            if (!condition.isHidden())
                visibleConditions.add(condition);
        }

        if (visibleConditions.isEmpty()) {
            paramsLayout = new HorizontalLayout();
            return;
        }

        int columns = 3;
        int rows = visibleConditions.size() / columns;
        if (visibleConditions.size() % columns != 0)
            rows++;
        com.vaadin.ui.GridLayout grid = new com.vaadin.ui.GridLayout(columns, rows);
        grid.setMargin(true, false, false, false);
        boolean focusSetted=false;
        for (int i = 0; i < visibleConditions.size(); i++) {
            AbstractCondition condition = visibleConditions.get(i);
            HorizontalLayout paramLayout = new HorizontalLayout();
            paramLayout.setSpacing(true);
            boolean bottomMargin = (i / columns) < (rows - 1); // no bottom margin for the last row
            paramLayout.setMargin(false, true, bottomMargin, false);
            if (condition.getParam().getJavaClass() != null) {
                Label label = new Label(condition.getLocCaption());
                paramLayout.addComponent(label);

                ParamEditor paramEditor = new ParamEditor(condition, true);
                if (focusOnConditions && !focusSetted) {
                    paramEditor.setFocused();
                    focusSetted = true;
                }
                paramLayout.addComponent(paramEditor);
            }
            grid.addComponent(paramLayout, i % columns, i / columns);
            grid.setComponentAlignment(paramLayout, com.vaadin.ui.Alignment.MIDDLE_RIGHT);
        }

        paramsLayout = grid;
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
            buttons.removeButton(addToSetBtn);
        if (addToCurrSet != null) {
            table.removeAction(addToCurrSet);
        }
        if (addToCurSetBtn != null) {
            buttons.removeButton(addToCurSetBtn);
        }
        if (removeFromCurrSet != null) {
            table.removeAction(removeFromCurrSet);
        }
        if (removeFromCurSetBtn != null) {
            buttons.removeButton(removeFromCurSetBtn);
        }
        if ((filterEntity != null) && (BooleanUtils.isTrue(filterEntity.getIsSet()))) {
            addToCurrSet = new AddToCurrSetAction();

            addToCurSetBtn = new WebButton();
            addToCurSetBtn.setIcon("icons/join-to-set.png");
            addToCurSetBtn.setAction(addToCurrSet);
            addToCurSetBtn.setId("addToCurSetBtn");
            addToCurSetBtn.setCaption(MessageProvider.getMessage(MESSAGES_PACK, "addToCurSet"));
            buttons.addButton(addToCurSetBtn);

            removeFromCurrSet = new RemoveFromSetAction(table);
            removeFromCurSetBtn = new WebButton();
            removeFromCurSetBtn.setIcon("icons/delete-from-set.png");
            removeFromCurSetBtn.setAction(removeFromCurrSet);
            removeFromCurSetBtn.setId("removeFromCurSetBtn");
            removeFromCurSetBtn.setCaption(MessageProvider.getMessage(MESSAGES_PACK, "removeFromCurSet"));
            buttons.addButton(removeFromCurSetBtn);

            table.addAction(removeFromCurrSet);
        } else {
            addToSet = new AddToSetAction(table);
            addToSetBtn = new WebButton();
            addToSetBtn.setIcon("icons/insert-to-set.png");
            addToSetBtn.setAction(addToSet);
            addToSetBtn.setId("addToSetBtn");
            addToSetBtn.setCaption(MessageProvider.getMessage(MESSAGES_PACK, "addToSet"));

            table.addAction(addToSet);
            buttons.addButton(addToSetBtn);
        }
    }

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
                !clientConfig.getGenericFilterManualApplyRequired())
            apply(true);
    }

    public void loadFiltersAndApplyDefault() {
        loadFilterEntities();

        Window window = ComponentsHelper.getWindow(this);
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
                    Collection<FilterEntity> filters = (Collection<FilterEntity>) select.getItemIds();
                    for (FilterEntity filter : filters) {
                        if (defaultId.equals(filter.getId())) {
                            filter.setIsDefault(true);
                            filter.setApplyDefault(applyDefault);

                            Map<String, Object> params = window.getContext().getParams();
                            if (!BooleanUtils.isTrue((Boolean) params.get("disableAutoRefresh"))) {
                                applyingDefault = true;
                                try {
                                    select.setValue(filter);
                                    updateControls();
                                    if (clientConfig.getGenericFilterManualApplyRequired()) {
                                        if (filter.getApplyDefault()) {
                                            apply(true);
                                        }
                                    } else apply(true);
                                    if (filterEntity != null)
                                        if (filterEntity.getCode() != null) {
                                            window.setDescription(MessageProvider.getMessage(mainMessagesPack, filterEntity.getCode()));
                                        } else
                                            window.setDescription(filterEntity.getName());
                                    else
                                        window.setDescription(null);
                                } finally {
                                    applyingDefault = false;
                                }
                            }
                            else{

                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    public void setUseMaxResults(boolean useMaxResults) {
        this.useMaxResults = useMaxResults;
        maxResultsLayout.setVisible(useMaxResults
                && UserSessionProvider.getUserSession().isSpecificPermitted("cuba.gui.filter.maxResults"));
    }

    public boolean getUseMaxResults() {
        return useMaxResults;
    }

    public void editorCommitted() {
        changingFilter = true;
        try {
            saveFilterEntity();
            parseFilterXml();

            internalSetFilterEntity();

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
            if (filter == filterEntity) {
                captions.put(filter, getCurrentFilterCaption());
            } else {
                if (filter.getCode() == null)
                    captions.put(filter, filter.getName());
                else {
                    captions.put(filter, MessageProvider.getMessage(mainMessagesPack, filter.getCode()));
                }
            }
        }

        Collections.sort(
                list,
                new Comparator<FilterEntity>() {
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
        return Collections.unmodifiableList(conditions);
    }

    public void editorCancelled() {
        if (filterEntity.getXml() == null)
            filterEntity = null;

        switchToUse();
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
            if (filter.getCode() == null)
                captions.put(filter, filter.getName());
            else {
                captions.put(filter, MessageProvider.getMessage(mainMessagesPack, filter.getCode()));
            }
        }

        Collections.sort(
                filters,
                new Comparator<FilterEntity>() {
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

    public CollectionDatasource getDatasource() {
        return datasource;
    }

    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;
        this.dsQueryFilter = datasource.getQueryFilter();

        if (clientConfig.getGenericFilterManualApplyRequired()) {
            // set initial denying condition to get empty datasource before explicit filter applying
            QueryFilter queryFilter = new QueryFilter(new DenyingClause(), datasource.getMetaClass().getName());
            if (dsQueryFilter != null) {
                queryFilter = new QueryFilter(dsQueryFilter, queryFilter);
            }
            datasource.setQueryFilter(queryFilter);
        }

        if (datasource instanceof CollectionDatasource.Lazy) {
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

    public void add(Component component) {
        throw new UnsupportedOperationException();
    }

    public void remove(Component component) {
        throw new UnsupportedOperationException();
    }

    public <T extends Component> T getOwnComponent(String id) {
        List<AbstractCondition> list = editor == null ? conditions : editor.getConditions();

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

    public <T extends Component> T getComponent(String id) {
        String[] elements = ValuePathHelper.parse(id);
        if (elements.length == 1)
            return (T) getOwnComponent(id);
        else
            throw new UnsupportedOperationException("Filter contains only one level of subcomponents");
    }

    public Collection<Component> getOwnComponents() {
        return Collections.EMPTY_LIST;
    }

    public Collection<Component> getComponents() {
        return getOwnComponents();
    }

    private void parseFilterXml() {
        if (filterEntity == null) {
            conditions = new ArrayList<AbstractCondition>();
        } else {
            FilterParser parser =
                    new FilterParser(filterEntity.getXml(), getFrame().getMessagesPack(), getId(), datasource);
            conditions = parser.fromXml().getConditions();
        }
    }

    public void applySettings(Element element) {
        // logic moved to loadFiltersAndApplyDefault()
    }

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

    public Component getApplyTo() {
        return applyTo;
    }

    public void setApplyTo(Component component) {
        applyTo = component;
        if ((applyTo != null) && (Table.class.isAssignableFrom(applyTo.getClass()))) {
            Table table = (Table) applyTo;
            setActions(table);
        }
    }

    private void saveAsFolder(boolean isAppFolder) {
        final AbstractSearchFolder folder = isAppFolder ? (new AppFolder()) : (new SearchFolder());
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
            public void windowClose(com.vaadin.ui.Window.CloseEvent e) {
                App.getInstance().getAppWindow().removeWindow(window);
            }
        });
        App.getInstance().getAppWindow().addWindow(window);
    }

    private String submintParameters() {
        FilterParser parser = new FilterParser(filterEntity.getXml(), MESSAGES_PACK, filterEntity.getComponentId(), datasource);
        parser.fromXml();
        List<AbstractCondition> defaultConditions = parser.getConditions();
        Iterator<AbstractCondition> it = conditions.iterator();
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
        }
        Collection<FilterEntity> filters = (Collection<FilterEntity>) select.getItemIds();
        for (FilterEntity filter : filters) {
            if (!ObjectUtils.equals(filter, filterEntity))
                filter.setIsDefault(false);
        }
    }

    private class SelectListener implements Property.ValueChangeListener {

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

        public String getCaption() {
            return MessageProvider.getMessage(MESSAGES_PACK, getId());
        }

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

        private String wrapValueForLike(Object value,boolean before, boolean after) {
            return ParametersHelper.CASE_INSENSITIVE_MARKER + (before?"%":"") + value + (after?"%":"");
        }

        public void setValue(Object value) {
        }

        public void addListener(ValueListener listener) {
            param.addListener(listener);
        }

        public void removeListener(ValueListener listener) {
            param.removeListener(listener);
        }

        public boolean isEditable() {
            return false;
        }

        public void setEditable(boolean editable) {
        }

        public String getId() {
            return param.getName();
        }

        public void setId(String id) {
        }

        public String getDebugId() {
            return null;
        }

        public void setDebugId(String id) {
        }

        public boolean isEnabled() {
            return false;
        }

        public void setEnabled(boolean enabled) {
        }

        public boolean isVisible() {
            return false;
        }

        public void setVisible(boolean visible) {
        }

        public void requestFocus() {
        }

        public float getHeight() {
            return 0;
        }

        public int getHeightUnits() {
            return 0;
        }

        public void setHeight(String height) {
        }

        public float getWidth() {
            return 0;
        }

        public int getWidthUnits() {
            return 0;
        }

        public void setWidth(String width) {
        }

        public Alignment getAlignment() {
            return Alignment.TOP_LEFT;
        }

        public void setAlignment(Alignment alignment) {
        }

        public String getStyleName() {
            return null;
        }

        public void setStyleName(String name) {
        }

        public <A extends IFrame> A getFrame() {
            return null;
        }

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


        public void actionPerform(Component component) {

            if (!table.getSelected().isEmpty()) {
                String entityType = table.getDatasource().getMetaClass().getName();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("entityType", entityType);
                params.put("items", table.getSelected());
                params.put("componentPath", getComponentPath());
                params.put("componentId", WebFilter.this.getId());
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

        public void actionPerform(Component component) {
            IFrame frame = WebFilter.this.getFrame();
            String windowAlias = frame.getId();
            frame.openLookup(windowAlias, new Window.Lookup.Handler() {
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

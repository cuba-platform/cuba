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
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.UserSessionClient;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Window;
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
import com.haulmont.cuba.web.WebConfig;
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
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.vaadin.hene.popupbutton.PopupButton;

import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang.BooleanUtils.isTrue;

public class WebFilter
        extends WebAbstractComponent<VerticalLayout> implements Filter
{
    private static final String MESSAGES_PACK = "com.haulmont.cuba.web.gui.components.filter";

    private PersistenceManagerService persistenceManager;

    private CollectionDatasource datasource;
    private QueryFilter dsQueryFilter;
    private FilterEntity filterEntity;
    private List<Condition> conditions = Collections.EMPTY_LIST;

    private AbstractLayout paramsLayout;
    private AbstractOrderedLayout editLayout;
    private AbstractSelect select;
    private WebPopupButton actions;

    private Button applyBtn;
    private CheckBox defaultCb;

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

    private WebConfig config = ConfigProvider.getConfig(WebConfig.class);

    private static final String GLOBAL_FILTER_PERMISSION = "cuba.gui.filter.global";

    private String mainMessagesPack = AppConfig.getInstance().getMessagesPack();

    public WebFilter() {
        persistenceManager = ServiceLocator.lookup(PersistenceManagerService.NAME);

        component = new VerticalLayout();
        component.setMargin(true);
        component.setStyleName("generic-filter");

        foldersPane = App.getInstance().getAppWindow().getFoldersPane();

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setSpacing(true);

        select = new FilterSelect();
        select.setWidth(300, Sizeable.UNITS_PIXELS);
        select.setStyleName("generic-filter-select");
        select.setNullSelectionAllowed(true);
        select.setImmediate(true);
        select.addListener(new SelectListener());
        App.getInstance().getWindowManager().setDebugId(select, "genericFilterSelect");
        topLayout.addComponent(select);

        applyBtn = WebComponentsHelper.createButton("icons/search.png");
        applyBtn.setCaption(MessageProvider.getMessage(mainMessagesPack, "actions.Apply"));
        applyBtn.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                apply();
            }
        });
        App.getInstance().getWindowManager().setDebugId(applyBtn, "genericFilterApplyBtn");
        topLayout.addComponent(applyBtn);

        actions = new WebPopupButton();
        actions.setCaption(MessageProvider.getMessage(MESSAGES_PACK, "actionsCaption"));
        topLayout.addComponent((com.vaadin.ui.Component) actions.getComponent());

        defaultCb = new CheckBox();
        defaultCb.setCaption(MessageProvider.getMessage(MESSAGES_PACK, "defaultCb"));
        defaultCb.setImmediate(true);

        defaultCb.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                if (filterEntity != null) {
                    filterEntity.setIsDefault(isTrue((Boolean) defaultCb.getValue()));

                    Collection<FilterEntity> filters = select.getItemIds();
                    for (FilterEntity filter : filters) {
                        if (!filter.equals(filterEntity))
                            filter.setIsDefault(false);
                    }
                }

            }
        });
        topLayout.addComponent(defaultCb);

        initMaxResultsLayout();
        topLayout.addComponent(maxResultsLayout);

        component.addComponent(topLayout);

        createParamsLayout();
        component.addComponent(paramsLayout);

        updateControls();
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
                new IntegerValidator(MessageProvider.getMessage(mainMessagesPack, "validation.invalidNumber")));
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

        if (filterEntity == null)
            return;

        if (checkGlobalFilterPermission()) {
            if (filterEntity.getFolder() == null || filterEntity.getFolder() instanceof SearchFolder)
                actions.addAction(new EditAction());

            if (filterEntity.getCode() == null && filterEntity.getFolder() == null)
                actions.addAction(new DeleteAction());
        }

        if (filterEntity.getCode() == null && foldersPane != null && filterEntity.getFolder() == null)
            actions.addAction(new SaveAsFolderAction());
    }

    public void apply() {
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

        datasource.refresh();
    }

    private void applyDatasourceFilter() {
        if (filterEntity != null) {
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
        filterEntity.setUser(UserSessionClient.getUserSession().getCurrentOrSubstitutedUser());
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

    private void createParamsLayout() {
        List<Condition> visibleConditions = new ArrayList<Condition>();
        for (Condition condition : conditions) {
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

        for (int i = 0; i < visibleConditions.size(); i++) {
            Condition condition = visibleConditions.get(i);
            HorizontalLayout paramLayout = new HorizontalLayout();
            paramLayout.setSpacing(true);
            paramLayout.setMargin(false, true, true, false);
            if (condition.getParam().getJavaClass() != null) {
                Label label = new Label(condition.getLocCaption());
                paramLayout.addComponent(label);

                ParamEditor paramEditor = new ParamEditor(condition, true);
                paramLayout.addComponent(paramEditor);
            }
            grid.addComponent(paramLayout, i % columns, i / columns);
            grid.setComponentAlignment(paramLayout, com.vaadin.ui.Alignment.MIDDLE_RIGHT);
        }

        paramsLayout = grid;
    }

    public void setFilterEntity(FilterEntity filterEntity) {
        changingFilter = true;
        try {
            this.filterEntity = filterEntity;

            parseFilterXml();

            internalSetFilterEntity();

            updateControls();
            if (paramsLayout != null)
                component.removeComponent(paramsLayout);
            createParamsLayout();
            component.addComponent(paramsLayout);
        } finally {
            changingFilter = false;
        }
        apply();
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
            String defIdStr =  e.attributeValue("id");
            if (!StringUtils.isBlank(defIdStr)) {
                UUID defaultId = null;
                try {
                    defaultId = UUID.fromString(defIdStr);
                } catch (IllegalArgumentException ex) {
                    //
                }
                if (defaultId != null) {
                    Collection<FilterEntity> filters = select.getItemIds();
                    for (FilterEntity filter : filters) {
                        if (defaultId.equals(filter.getId())) {
                            filter.setIsDefault(true);

                            Map<String, Object> params = window.getContext().getParams();
                            if (!BooleanUtils.isTrue((Boolean) params.get("disableAutoRefresh"))) {
                                applyingDefault = true;
                                try {
                                    select.setValue(filter);
                                    updateControls();
                                    apply();
                                    if(filterEntity != null)
                                        if(filterEntity.getCode() != null){
                                            window.setDescription(MessageProvider.getMessage(mainMessagesPack, filterEntity.getCode()));
                                        } else
                                            window.setDescription(filterEntity.getName());
                                    else
                                        window.setDescription(null);
                                } finally {
                                    applyingDefault = false;
                                }
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
                && UserSessionClient.getUserSession().isSpecificPermitted("cuba.gui.filter.maxResults"));
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
                    captions.put(filter, MessageProvider.getMessage(mainMessagesPack,filter.getCode()));
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

        for (FilterEntity filter : list) {
            select.addItem(filter);
            select.setItemCaption(filter, captions.get(filter));
        }

        select.setValue(filterEntity);
    }

    private String getCurrentFilterCaption() {
        String name;
        if(filterEntity != null)
            if (filterEntity.getCode() == null)
                name = InstanceUtils.getInstanceName((Instance) filterEntity);
            else {
                name = MessageProvider.getMessage(mainMessagesPack, filterEntity.getCode());
            }
        else
            name = "";
        AbstractSearchFolder folder = filterEntity.getFolder();
        if (folder != null) {
            if(!StringUtils.isBlank(folder.getDoubleName()))
                name = folder.getDoubleName();
            else if(!StringUtils.isBlank(folder.getCode()))
                name = MessageProvider.getMessage(mainMessagesPack, folder.getCode()+".doubleName");
            name = MessageProvider.getMessage(MESSAGES_PACK, "folderPrefix") + " " + name;
        }
        return name;
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

        User user = UserSessionClient.getUserSession().getSubstitutedUser();
        if (user == null)
            user = UserSessionClient.getUserSession().getUser();

        ctx.setQueryString("select f from sec$Filter f " +
                "where f.componentId = :component and (f.user is null or f.user.id = :userId) order by f.name")
                .addParameter("component", getComponentPath())
                .addParameter("userId", user.getId());

        List<FilterEntity> filters = ds.loadList(ctx);
        for (FilterEntity filter : filters) {
            select.addItem(filter);
            if(filter.getCode() == null)
                select.setItemCaption(filter, filter.getName());
            else{
                select.setItemCaption(filter, MessageProvider.getMessage(mainMessagesPack,filter.getCode()));
            }
        }
    }

    private void saveFilterEntity() {
        if (filterEntity.getFolder() == null) {
            DataService ds = ServiceLocator.getDataService();
            CommitContext ctx = new CommitContext(Collections.singletonList(filterEntity));
            Map<Entity, Entity> result = ds.commit(ctx);
            filterEntity = (FilterEntity) result.get(filterEntity);

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
        Map<String, Locale> locales = config.getAvailableLocales();
        for (Object id : select.getItemIds()) {
            if (id != filterEntity){
                FilterEntity fe = (FilterEntity)id;
                if(fe.getCode() == null)
                    names.add(fe.getName());
                else{
                    for(Map.Entry<String,Locale> locale : locales.entrySet()){
                        names.add(MessageProvider.getMessage(mainMessagesPack,fe.getCode(),locale.getValue()));
                    }
                }
            }
        }

        editor = new FilterEditor(this, filterEntity, getXmlDescriptor(), names);
        editor.init();
        editLayout.addComponent(editor.getLayout());
    }

    public CollectionDatasource getDatasource() {
        return datasource;
    }

    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;
        this.dsQueryFilter = datasource.getQueryFilter();

        if (config.getGenericFilterManualApplyRequired()) {
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
        createParamsLayout();
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

        defaultCb.setVisible(filterEntity != null && !editing && filterEntity.getFolder() == null);
        if (filterEntity != null && !editing)
            defaultCb.setValue(isTrue(filterEntity.getIsDefault()));
        else
            defaultCb.setValue(false);
    }

    private boolean checkGlobalFilterPermission() {
        if (filterEntity == null || filterEntity.getUser() != null)
            return true;
        else
            return UserSessionClient.getUserSession().isSpecificPermitted(GLOBAL_FILTER_PERMISSION);
    }

    public void add(Component component) {
        throw new UnsupportedOperationException();
    }

    public void remove(Component component) {
        throw new UnsupportedOperationException();
    }

    public <T extends Component> T getOwnComponent(String id) {
        List<Condition> list = editor == null ? conditions : editor.getConditions();

        for (Condition condition : list) {
            if (condition.getParam() != null) {
                String paramName = condition.getParam().getName();
                String componentName = paramName.substring(paramName.lastIndexOf('.') + 1);
                if (id.equals(componentName)) {
                    ParamWrapper wrapper = new ParamWrapper(condition);
                    return (T) wrapper;
                }
            }
        }
        return null;
    }

    public <T extends Component> T getComponent(String id) {
        String[] elements = ValuePathHelper.parse(id);
        if (elements.length == 1)
            return (T)getOwnComponent(id);
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
            conditions = new ArrayList<Condition>();
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
        Element e = element.element("defaultFilter");
        if (e == null)
            e = element.addElement("defaultFilter");

        UUID defaultId = null;
        Collection<FilterEntity> filters = select.getItemIds();
        for (FilterEntity filter : filters) {
            if (isTrue(filter.getIsDefault())) {
                defaultId = filter.getId();
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
            return true;
        }
        return false;
    }

    public Component getApplyTo() {
        return applyTo;
    }

    public void setApplyTo(Component component) {
        applyTo = component;
    }

    private void saveAsFolder() {
        final SearchFolder folder = new SearchFolder();
        if(filterEntity.getCode() == null){
            folder.setName(filterEntity.getName());
            folder.setDoubleName(filterEntity.getName());
        }else{
            String name = MessageProvider.getMessage(mainMessagesPack, filterEntity.getCode());
            folder.setName(name);
            folder.setDoubleName(name);
        }
        folder.setFilterComponentId(filterEntity.getComponentId());
        folder.setFilterXml(filterEntity.getXml());
        if (UserSessionClient.getUserSession().isSpecificPermitted(GLOBAL_FILTER_PERMISSION))
            folder.setUser(filterEntity.getUser());
        else
            folder.setUser(UserSessionClient.getUserSession().getCurrentOrSubstitutedUser());

        Presentations presentations;
        if (applyTo != null && applyTo instanceof HasPresentations) {
            final HasPresentations presentationsOwner = (HasPresentations) applyTo;
            presentations = presentationsOwner.isUsePresentations()
                    ? presentationsOwner.getPresentations() : null;
        } else {
            presentations = null;
        }

        final FolderEditWindow window = new FolderEditWindow(false, folder,
                presentations,
                new Runnable() {
                    public void run() {
                        SearchFolder savedFolder = saveFolder(folder);
                        filterEntity.setFolder(savedFolder);
                        if (UserSessionClient.getUserSession().isSpecificPermitted(GLOBAL_FILTER_PERMISSION))
                            deleteFilterEntity();
                        select.setItemCaption(filterEntity, getCurrentFilterCaption());

                        // search for existing folders with the same name
//                        boolean found = false;
//                        Collection<SearchFolder> folders = foldersPane.getSearchFolders();
//                        for (final SearchFolder existingFolder : folders) {
//                            if (ObjectUtils.equals(existingFolder.getName(), folder.getName())) {
//                                found = true;
//                                App.getInstance().getWindowManager().showOptionDialog(
//                                        MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(), "dialogs.Confirmation"),
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
                }
        );

        window.addListener(new com.vaadin.ui.Window.CloseListener() {
            public void windowClose(com.vaadin.ui.Window.CloseEvent e) {
                App.getInstance().getAppWindow().removeWindow(window);
            }
        });
        App.getInstance().getAppWindow().addWindow(window);
    }

    private SearchFolder saveFolder(SearchFolder folder) {
        SearchFolder savedFolder = (SearchFolder) foldersPane.saveFolder(folder);
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


    private class SelectListener implements Property.ValueChangeListener {

        public void valueChange(Property.ValueChangeEvent event) {
            if (changingFilter)
                return;

            filterEntity = (FilterEntity) select.getValue();
            parseFilterXml();

            updateControls();
            component.removeComponent(paramsLayout);
            createParamsLayout();
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
                App.getInstance().getWindowManager().setCurrentWindowCaption(window.getCaption(), descr);
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

    private class SaveAsFolderAction extends AbstractAction {

        protected SaveAsFolderAction() {
            super("saveAsFolderAction");
        }

        @Override
        public String getCaption() {
            return MessageProvider.getMessage(MESSAGES_PACK, getId());
        }

        public void actionPerform(Component component) {
            saveAsFolder();
        }
    }

    public static final Pattern LIKE_PATTERN = Pattern.compile("\\slike\\s+" + ParametersHelper.QUERY_PARAMETERS_RE);

    private static class ParamWrapper implements HasValue {

        private final Condition condition;

        private ParamWrapper(Condition condition) {
            this.condition = condition;
        }

        public <T> T getValue() {
            Object value = condition.getParam().getValue();
            if (value instanceof String
                    && !StringUtils.isEmpty((String) value)
                    && !((String) value).contains("%")
                    && !((String) value).startsWith(ParametersHelper.CASE_INSENSITIVE_MARKER))
            {
                // try to wrap value for case-insensitive "like" search
                if (condition instanceof PropertyCondition) {
                    PropertyCondition.Op op = ((PropertyCondition) condition).getOperator();
                    if (PropertyCondition.Op.CONTAINS.equals(op) || op.equals(PropertyCondition.Op.DOES_NOT_CONTAIN)) {
                        value = wrapValueForLike(value);
                    }
                } else if (condition instanceof CustomCondition) {
                    String where = ((CustomCondition) condition).getWhere();
                    Matcher matcher = LIKE_PATTERN.matcher(where);
                    if (matcher.find()) {
                        value = wrapValueForLike(value);
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

        public void setValue(Object value) {
        }

        public void addListener(ValueListener listener) {
            condition.getParam().addListener(listener);
        }

        public void removeListener(ValueListener listener) {
            condition.getParam().removeListener(listener);
        }

        public boolean isEditable() {
            return false;
        }

        public void setEditable(boolean editable) {
        }

        public String getId() {
            return condition.getName();
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
}

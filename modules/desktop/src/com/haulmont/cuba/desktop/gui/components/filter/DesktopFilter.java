/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.entity.AppFolder;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.components.*;
import com.haulmont.cuba.desktop.sys.DesktopWindowManager;
import com.haulmont.cuba.desktop.sys.layout.LayoutAdapter;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.AbstractParam;
import com.haulmont.cuba.gui.components.filter.Op;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.filter.DenyingClause;
import com.haulmont.cuba.gui.filter.QueryFilter;
import com.haulmont.cuba.gui.settings.SettingsImpl;
import com.haulmont.cuba.gui.xml.ParametersHelper;
import com.haulmont.cuba.security.entity.FilterEntity;
import com.haulmont.cuba.security.entity.SearchFolder;
import com.haulmont.cuba.security.entity.User;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopFilter extends DesktopAbstractComponent<JPanel> implements Filter {
    private static final String MESSAGES_PACK = "com.haulmont.cuba.gui.components.filter";

    private PersistenceManagerService persistenceManager;
    private CollectionDatasource datasource;
    private QueryFilter dsQueryFilter;
    private String mainMessagesPack = AppConfig.getMessagesPack();

    private FilterEntity noFilter;
    private FilterEntity filterEntity;
    private List<AbstractCondition> conditions = Collections.EMPTY_LIST;

    private DesktopFilterSelect select;
    private JPanel maxResultsPanel;
    private JPanel paramsPanel;
    private JPanel editPanel;
    private JButton applyBtn;
    private FilterEditor editor;

    private boolean changingFilter;
    private boolean applyingDefault;
    private boolean editing = false;
    private boolean initialized = false;

    private boolean useMaxResults;
    private JCheckBox maxResultsCb;
    //private DesktopTextField maxResultsField;
    private MaxResultsField maxResultsField;

    private DesktopPopupButton actions;

    private GlobalConfig globalConfig = ConfigProvider.getConfig(GlobalConfig.class);
    private ClientConfig clientConfig = ConfigProvider.getConfig(ClientConfig.class);

    private Component applyTo;

    private static final String GLOBAL_FILTER_PERMISSION = "cuba.gui.filter.global";
    private static final String GLOBAL_APP_FOLDERS_PERMISSION = "cuba.gui.appFolder.global";


    public DesktopFilter() {
        persistenceManager = ServiceLocator.lookup(PersistenceManagerService.NAME);
        LC topLc = new LC();
        topLc.insets("0","5","0","5");
        if (LayoutAdapter.isDebug())
            topLc.debug(1000);

        MigLayout topLayout = new MigLayout(topLc);

        impl = new JPanel(topLayout);
        //todo foldersPane

        noFilter = new FilterEntity() {
            @Override
            public String toString() {
                return getName();
            }
        };
        noFilter.setName(MessageProvider.getMessage(mainMessagesPack, "filter.noFilter"));

        select = new DesktopFilterSelect();
        Dimension dimension = select.getMinimumSize();
        select.setMinimumSize(new Dimension(300, dimension.height));
        select.addItemListener(new SelectListener());

        impl.add(select);
        DesktopComponentsHelper.adjustSize(select);
        applyBtn = new JButton(MessageProvider.getMessage(mainMessagesPack, "actions.Apply"));
        applyBtn.setIcon(App.getInstance().getResources().getIcon("icons/search.png"));
        DesktopComponentsHelper.adjustSize(applyBtn);
        applyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apply(false);
            }
        });
        impl.add(applyBtn);

        actions = new DesktopPopupButton();
        actions.setVisible(true);
        actions.setPopupVisible(true);

        actions.setCaption(MessageProvider.getMessage(MESSAGES_PACK, "actionsCaption"));
        impl.add(actions.<java.awt.Component>getComponent());

        initMaxResultsPanel();
        impl.add(maxResultsPanel, new CC().wrap());

        createParamsPanel(false);
        impl.add(paramsPanel);

        updateControls();
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
            maxResultsField.setValue(maxResults);
            datasource.setMaxResults(maxResults);
        }
    }

    private void switchToEdit() {
        editing = true;
        updateControls();
        impl.remove(paramsPanel);
        createEditLayout();
        impl.add(editPanel, "span");
    }

    private void switchToUse() {
        editing = false;
        editor = null;
        updateControls();
        impl.remove(editPanel);

        createParamsPanel(true);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                impl.revalidate();
                impl.repaint();
            }
        });
    }

    private void setActions(Table table) {
        //todo
        /*ButtonsPanel buttons = table.getButtonsPanel();
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
        }    */
    }

    private void updateControls() {
        fillActions();
        actions.setVisible(!editing);
        actions.setPopupVisible(false);
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

    private void fillActions() {
        for (Action action : new ArrayList<Action>(actions.getActions())) {
            actions.removeAction(action);
        }

        if (editing)
            return;

        actions.addAction(new CreateAction());

        if (filterEntity == null){
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
        //todo
        /* if (filterEntity.getCode() == null && foldersPane != null && filterEntity.getFolder() == null)
            actions.addAction(new SaveAsFolderAction(false));
        if (checkGlobalAppFolderPermission()) {
            if (filterEntity.getCode() == null && foldersPane != null && filterEntity.getFolder() == null)
                actions.addAction(new SaveAsFolderAction(true));
        }*/
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
            if (paramsPanel != null)
                impl.remove(paramsPanel);
            createParamsPanel(false);
            impl.add(paramsPanel);
        } finally {
            changingFilter = false;
        }
        if (BooleanUtils.isTrue(filterEntity.getApplyDefault()) ||
                BooleanUtils.isTrue(filterEntity.getIsSet()) ||
                !clientConfig.getGenericFilterManualApplyRequired())
            apply(true);
    }

    private void createParamsPanel(boolean focusOnConditions) {
        if (paramsPanel != null) {
            impl.remove(paramsPanel);
        }
        LC paramsPanelLC = new LC();
        paramsPanelLC.insetsAll("0");
        if (LayoutAdapter.isDebug()) {
            paramsPanelLC.debug(1000);
        }
        paramsPanel = new JPanel(new MigLayout(paramsPanelLC));

        List<AbstractCondition> visibleConditions = new ArrayList<AbstractCondition>();
        for (AbstractCondition condition : conditions) {
            if (!condition.isHidden())
                visibleConditions.add(condition);
        }
        if (visibleConditions.isEmpty()) {
            impl.add(paramsPanel, new CC().spanX());
            return;
        }

        int columns = 3;
        LC paramsLC = new LC();
        paramsLC.insetsAll("0").wrapAfter(columns);
        if (LayoutAdapter.isDebug()) {
            paramsLC.debug(1000);
        }
        MigLayout paramsLayout = new MigLayout(paramsLC);
        paramsPanel.setLayout(paramsLayout);
        boolean focusSetted = false;
        for (AbstractCondition condition : visibleConditions) {
            JPanel paramPanel = new JPanel();

            if (condition.getParam().getJavaClass() != null) {
                JLabel label = new JLabel(condition.getLocCaption());
                paramPanel.add(label);

                ParamEditor paramEditor = new ParamEditor(condition, true);
                if (focusOnConditions && !focusSetted) {

                }
                paramPanel.add(paramEditor);


            }
            paramsPanel.add(paramPanel);
        }
        impl.add(paramsPanel, new CC().spanX());
    }

    private void internalSetFilterEntity() {
        List<FilterEntity> list = new ArrayList(select.getFilters());
        list.remove(filterEntity);

        select.removeAllItems();

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
        ItemWrapper<FilterEntity> noFilterWrapper = new ItemWrapper<FilterEntity>(noFilter, noFilter.toString());
        select.setNoFilter(noFilterWrapper);
        select.addItem(noFilterWrapper);
        for (FilterEntity filter : list) {
            ItemWrapper<FilterEntity> wrapper = new ItemWrapper<FilterEntity>(filter, captions.get(filter));
            select.addItem(wrapper);
            if (filter == filterEntity) {
                select.setSelectedItem(wrapper);
            }
        }
    }

    private void initMaxResultsPanel() {
        MigLayout layout = new MigLayout();
        maxResultsPanel = new JPanel(layout);

        maxResultsCb = new JCheckBox(MessageProvider.getMessage(mainMessagesPack, "filter.maxResults.label1"));
        maxResultsCb.setSelected(true);
        maxResultsCb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maxResultsField.setEnabled(BooleanUtils.isTrue(maxResultsCb.isSelected()));
            }
        }
        );
        maxResultsPanel.add(maxResultsCb);
        maxResultsField = new MaxResultsField(4);
        maxResultsField.setPreferredSize(new Dimension(42, DesktopComponentsHelper.FIELD_HEIGHT));
        maxResultsPanel.add(maxResultsField);

        JLabel maxResultsLabel2 = new JLabel(MessageProvider.getMessage(mainMessagesPack, "filter.maxResults.label2"));
        maxResultsPanel.add(maxResultsLabel2);
    }

    private String getCurrentFilterCaption() {
        String name;
        if (filterEntity != null)
            if (filterEntity.getCode() == null)
                name = InstanceUtils.getInstanceName(filterEntity);
            else {
                name = MessageProvider.getMessage(mainMessagesPack, filterEntity.getCode());
            }
        else
            name = "";
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
        return name;
    }

    public boolean apply(boolean isNewWindow) {
        if (clientConfig.getGenericFilterChecking()) {
            if (filterEntity != null) {
                boolean haveCorrectCondition = false;

                for (AbstractCondition condition : conditions) {
                    if ((condition.getParam() == null) || (condition.getParam().getValue() != null)) {
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
            if (BooleanUtils.isTrue(maxResultsCb.isSelected()))
                maxResults = maxResultsField.getValue();
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
                    Collection<FilterEntity> filters = select.getFilters();
                    for (FilterEntity filter : filters) {
                        if (defaultId.equals(filter.getId())) {
                            filter.setIsDefault(true);
                            filter.setApplyDefault(applyDefault);

                            Map<String, Object> params = window.getContext().getParams();
                            if (!BooleanUtils.isTrue((Boolean) params.get("disableAutoRefresh"))) {
                                applyingDefault = true;
                                try {
                                    int count = select.getItemCount();
                                    for (int i = 0; i < count; i++) {
                                        ItemWrapper<FilterEntity> wrapper = (ItemWrapper<FilterEntity>) select.getItemAt(i);
                                        if (wrapper.getItem().equals(filter)) {
                                            select.setSelectedItem(wrapper);
                                            break;
                                        }
                                    }

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
                            break;
                        }
                    }
                }
            }
        }
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
        ItemWrapper<FilterEntity> noFilterWrapper = new ItemWrapper<FilterEntity>(noFilter, noFilter.toString());
        select.setNoFilter(noFilterWrapper);
        select.addItem(noFilterWrapper);
        for (FilterEntity filter : filters) {
            select.addItem(new ItemWrapper<FilterEntity>(filter, captions.get(filter)));
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


        }
        //todo
        /*else if (filterEntity.getFolder() instanceof SearchFolder) {
            filterEntity.getFolder().setName(filterEntity.getName());
            filterEntity.getFolder().setFilterXml(filterEntity.getXml());
            SearchFolder folder = saveFolder((SearchFolder) filterEntity.getFolder());
            filterEntity.setFolder(folder);
        }*/
    }

    private void deleteFilterEntity() {
        DataService ds = ServiceLocator.getDataService();
        CommitContext ctx = new CommitContext();
        ctx.setRemoveInstances(Collections.singletonList(filterEntity));
        ds.commit(ctx);
    }

    private void createEditLayout() {
        List<String> names = new ArrayList<String>();
        Map<String, Locale> locales = globalConfig.getAvailableLocales();
        for (FilterEntity filter : select.getFilters()) {
            if (filter != filterEntity) {

                if (filter.getCode() == null)
                    names.add(filter.getName());
                else {
                    for (Map.Entry<String, Locale> locale : locales.entrySet()) {
                        names.add(MessageProvider.getMessage(mainMessagesPack, filter.getCode(), locale.getValue()));
                    }
                }
            }
        }

        editor = new FilterEditor(this, filterEntity, getXmlDescriptor(), names);
        editor.init();
        editPanel = editor.getPanel();

        editor.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (BooleanUtils.isTrue(filterEntity.getIsDefault())) {
                    Collection<FilterEntity> filters = select.getFilters();
                    for (FilterEntity filter : filters) {
                        if (!filter.equals(filterEntity))
                            filter.setIsDefault(false);
                    }
                }
            }
        });
    }

    public void setUseMaxResults(boolean useMaxResults) {
        this.useMaxResults = useMaxResults;
        maxResultsPanel.setVisible(useMaxResults
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

    public void setApplyTo(Component component) {
        applyTo = component;
        if ((applyTo != null) && (Table.class.isAssignableFrom(applyTo.getClass()))) {
            Table table = (Table) applyTo;
            setActions(table);
        }
    }

    public Component getApplyTo() {
        return applyTo;
    }

    public void add(Component component) {
    }

    public void remove(Component component) {
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
    }

    public boolean saveSettings(Element element) {
        Boolean changed = false;
        Element e = element.element("defaultFilter");
        if (e == null)
            e = element.addElement("defaultFilter");

        UUID defaultId = null;
        Boolean applyDefault = false;
        Collection<FilterEntity> filters = select.getFilters();
        for (FilterEntity filter : filters) {
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
                                select.removeItem(select.getSelectedItem());
                                if (!select.getFilters().isEmpty()) {
                                    select.setSelectedItem(select.getFilters().iterator().next());
                                } else {
                                    select.setSelectedItem(noFilter);
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
        Collection<FilterEntity> filters = select.getFilters();
        for (FilterEntity filter : filters) {
            if (!ObjectUtils.equals(filter, filterEntity))
                filter.setIsDefault(false);
        }
    }

    private class SelectListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (ItemEvent.SELECTED != e.getStateChange()) {
                return;
            }
            if (changingFilter)
                return;

            filterEntity = ((ItemWrapper<FilterEntity>) select.getSelectedItem()).getItem();
            if (filterEntity.equals(noFilter)) {
                filterEntity = null;
            }
            if ((filterEntity != null) && (applyTo != null) && (Table.class.isAssignableFrom(applyTo.getClass()))) {
                Table table = (Table) applyTo;
                setActions(table);
            }

            parseFilterXml();
            updateControls();
            createParamsPanel(true);
            impl.revalidate();
            impl.repaint();

            if (!applyingDefault) {
                Window window = ComponentsHelper.getWindow(DesktopFilter.this);
                String descr;
                if (filterEntity != null)
                    if (filterEntity.getCode() != null) {
                        descr = MessageProvider.getMessage(mainMessagesPack, filterEntity.getCode());
                    } else
                        descr = filterEntity.getName();
                else
                    descr = null;
                if (!initialized) {
                    window.setDescription(descr);
                    initialized = true;
                } else {
                    DesktopWindowManager wManager = (DesktopWindowManager) App.getInstance().getWindowManager();
                    wManager.setCurrentWindowCaption(window, window.getCaption(), descr);
                }
            }
            if (useMaxResults)
                maxResultsCb.setSelected(true);
            paramsPanel.revalidate();
            paramsPanel.repaint();
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
        public MakeDefaultAction(){
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

        private String wrapValueForLike(Object value, boolean before, boolean after) {
            return ParametersHelper.CASE_INSENSITIVE_MARKER + (before ? "%" : "") + value + (after ? "%" : "");
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

    private class MaxResultsField extends JTextField{
        private Integer value;
        private final int ENTER_CODE = 10;

        public MaxResultsField(int length) {
            TextComponentDocument doc = new TextComponentDocument();
            doc.setMaxLength(length);
            this.setDocument(doc);
            addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {

                }

                @Override
                public void focusLost(FocusEvent e) {
                    checkValue();
                }
            });
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (ENTER_CODE == e.getKeyCode())
                        checkValue();
                }
            });
        }

        private void checkValue() {
            try {
                Integer newValue = Integer.parseInt(getText());
                setValue(newValue);

            } catch (NumberFormatException ex) {
                setValue(value);
            }
        }

        public Integer getValue() {
            try {
                Integer newValue = Integer.parseInt(getText());
                setValue(newValue);
                return value;
            } catch (NumberFormatException e) {
                setValue(value);
                return value;
            }
        }

        public void setValue(Integer value) {
            this.value = value;
            setText(String.valueOf(value));
        }
    }
}

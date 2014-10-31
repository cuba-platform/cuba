/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.client.sys.PersistenceManagerClient;
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
import com.haulmont.cuba.desktop.sys.layout.MigLayoutHelper;
import com.haulmont.cuba.desktop.sys.vcl.CollapsiblePanel;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.AbstractParam;
import com.haulmont.cuba.gui.components.filter.ConditionsTree;
import com.haulmont.cuba.gui.components.filter.Op;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.data.ValueChangingListener;
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
import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.dom4j.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generic filter implementation for the desktop-client.
 *
 * @author krivopustov
 * @version $Id$
 */
public class DesktopFilter extends DesktopAbstractComponent<JPanel> implements Filter {

    protected static final String MESSAGES_PACK = "com.haulmont.cuba.gui.components.filter";

    protected static final String GLOBAL_FILTER_PERMISSION = "cuba.gui.filter.global";
    protected static final String GLOBAL_APP_FOLDERS_PERMISSION = "cuba.gui.appFolder.global";

    protected Messages messages = AppBeans.get(Messages.NAME);
    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);

    protected PersistenceManagerService persistenceManager = AppBeans.get(PersistenceManagerClient.NAME);

    protected CollectionDatasource datasource;
    protected QueryFilter dsQueryFilter;

    protected FilterEntity noFilter;
    protected ItemWrapper<FilterEntity> noFilterWrapper;
    protected FilterEntity filterEntity;
    protected ConditionsTree conditions = new ConditionsTree();

    protected DesktopLookupField select;
    protected JPanel maxResultsPanel;
    protected JPanel paramsPanel;
    protected JPanel editPanel;
    protected JButton applyBtn;
    protected FilterEditor editor;

    protected boolean defaultFilterEmpty = true;
    protected boolean changingFilter;
    protected boolean applyingDefault;
    protected boolean editing = false;

    protected boolean useMaxResults;
    protected JCheckBox maxResultsCb;

    protected MaxResultsField maxResultsField;
    protected Boolean manualApplyRequired;

    protected boolean editable = true;
    protected boolean required = false;
    protected boolean folderActionsEnabled = true;

    protected DesktopPopupButton actionsButton;

    protected GlobalConfig globalConfig;
    protected ClientConfig clientConfig;
    protected String defaultFilterCaption;

    protected Component applyTo;

    protected String initialWindowCaption;

    protected Metadata metadata = AppBeans.get(Metadata.NAME);

    public DesktopFilter() {
        Configuration configuration = AppBeans.get(Configuration.NAME);
        globalConfig = configuration.getConfig(GlobalConfig.class);
        clientConfig = configuration.getConfig(ClientConfig.class);

        LC topLc = new LC();
        topLc.hideMode(3);
        topLc.insetsAll("0");
        if (LayoutAdapter.isDebug())
            topLc.debug(1000);

        MigLayout topLayout = new MigLayout(topLc);

        impl = new JPanel(topLayout);
        //todo foldersPane

        defaultFilterCaption = messages.getMessage(MESSAGES_PACK, "defaultFilter");

        noFilter = new FilterEntity() {
            @Override
            public String toString() {
                return getName();
            }
        };
        noFilter.setName(messages.getMainMessage("filter.noFilter"));

        noFilterWrapper = new ItemWrapper<>(noFilter, noFilter.toString());

        select = new DesktopLookupField() {
            @Override
            public void updateMissingValueState() {
                // nothing
            }
        };
        select.setRequired(true);

        select.<JComponent>getComponent().setMinimumSize(new Dimension(300, DesktopComponentsHelper.FIELD_HEIGHT));
        select.addListener(new SelectListener());
        impl.add(select.<java.awt.Component>getComponent());
        applyBtn = new JButton(messages.getMainMessage("actions.Apply"));
        applyBtn.setIcon(App.getInstance().getResources().getIcon("icons/search.png"));
        DesktopComponentsHelper.adjustSize(applyBtn);
        applyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isVisible() && datasource != null && !editing) {
                    apply(false);
                }
            }
        });
        impl.add(applyBtn, new CC().hideMode(3));

        actionsButton = new DesktopPopupButton();
        actionsButton.setVisible(isEditFiltersPermitted());
        actionsButton.setPopupVisible(true);

        actionsButton.setCaption(messages.getMessage(MESSAGES_PACK, "actionsCaption"));
        impl.add(actionsButton.<java.awt.Component>getComponent(), new CC().hideMode(3));

        initMaxResultsPanel();
        impl.add(maxResultsPanel, new CC().wrap().hideMode(0));

        createParamsPanel(false);
        impl.add(paramsPanel);

        updateControls();

        if (App.getInstance().isTestMode()) {
            select.setId("filterSelect");

            if (applyBtn != null) {
                applyBtn.setName("applyBtn");
            }

            if (maxResultsPanel != null) {
                maxResultsField.setName("maxResultsField");
                maxResultsCb.setName("maxResultsCheckBox");
            }

            if (actionsButton != null) {
                actionsButton.setId("actionsBtn");
            }
        }
    }

    @Override
    public void setFrame(IFrame frame) {
        super.setFrame(frame);
        frame.addAction(new AbstractAction("applyFilter", clientConfig.getFilterApplyShortcut()) {
            @Override
            public void actionPerform(Component component) {
                apply(false);
            }
        });
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
        } else if (useMaxResults) {
            initMaxResults();
        }
    }

    protected void initMaxResults() {
        int maxResults = datasource.getMaxResults();
        if (maxResults == 0 || maxResults == persistenceManager.getMaxFetchUI(datasource.getMetaClass().getName()))
            maxResults = persistenceManager.getFetchUI(datasource.getMetaClass().getName());
        maxResultsField.setValue(maxResults);

        datasource.setMaxResults(maxResults);
    }

    protected void switchToEdit() {
        editing = true;
        updateControls();
        impl.remove(paramsPanel);
        createEditLayout();
        impl.add(editPanel, "span");
    }

    protected void switchToUse() {
        editing = false;
        editor = null;

        if (filterEntity == null) {
            ItemWrapper<FilterEntity> selected = select.getValue();
            filterEntity = selected == null ? null : selected.getItem();

            if (noFilter.equals(filterEntity)) {
                filterEntity = null;
            }
            if ((filterEntity != null) && (applyTo != null) && (Table.class.isAssignableFrom(applyTo.getClass()))) {
                Table table = (Table) applyTo;
                setActions(table);
            }

            parseFilterXml();
        }

        updateControls();
        impl.remove(editPanel);

        createParamsPanel(true);

        updateWindowCaption();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                impl.revalidate();
                impl.repaint();
            }
        });
    }

    protected void setActions(Table table) {
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

    protected void updateControls() {
        fillActions();
        actionsButton.setVisible(!editing);
        actionsButton.setPopupVisible(false);
        select.setEnabled(!editing);
        applyBtn.setVisible(!editing);

        actionsButton.setVisible(editable && isEditFiltersPermitted());
        actionsButton.setEnabled(actionsButton.getActions().size() > 0);
    }

    protected boolean checkGlobalAppFolderPermission() {
        return userSessionSource.getUserSession().isSpecificPermitted(GLOBAL_APP_FOLDERS_PERMISSION);
    }

    protected boolean checkGlobalFilterPermission() {
        if (filterEntity == null || filterEntity.getUser() != null) {
            return true;
        } else {
            return userSessionSource.getUserSession().isSpecificPermitted(GLOBAL_FILTER_PERMISSION);
        }
    }

    protected void fillActions() {
        for (Action action : new ArrayList<>(actionsButton.getActions())) {
            actionsButton.removeAction(action);
        }

        if (editing)
            return;

        actionsButton.addAction(new CreateAction());

        if (filterEntity == null) {
            if (!defaultFilterEmpty) {
                actionsButton.addAction(new MakeDefaultAction());
            }
            return;
        }


        if ((BooleanUtils.isNotTrue(filterEntity.getIsSet())))
            actionsButton.addAction(new CopyAction());

        if (checkGlobalFilterPermission()) {
            if ((BooleanUtils.isNotTrue(filterEntity.getIsSet())) &&
                    ((filterEntity.getFolder() == null && (filterEntity.getCode() == null)) ||
                            (filterEntity.getFolder() instanceof SearchFolder) ||
                            ((filterEntity.getFolder() instanceof AppFolder) && checkGlobalAppFolderPermission())))
                actionsButton.addAction(new EditAction());

            if (filterEntity.getCode() == null && filterEntity.getFolder() == null)
                actionsButton.addAction(new DeleteAction());
        } else {
            if (filterEntity.getFolder() instanceof SearchFolder) {
                if ((userSessionSource.getUserSession().getUser().equals(((SearchFolder) filterEntity.getFolder()).getUser())) &&
                        (BooleanUtils.isNotTrue(filterEntity.getIsSet())))
                    actionsButton.addAction(new EditAction());
            }
            if (filterEntity.getCode() == null && filterEntity.getFolder() == null &&
                    userSessionSource.getUserSession().getUser().equals(filterEntity.getUser()))
                actionsButton.addAction(new DeleteAction());
        }

        if (filterEntity != null && BooleanUtils.isNotTrue(filterEntity.getIsDefault())
                && filterEntity.getFolder() == null
                && filterEntity.getIsSet() == null) {
            actionsButton.addAction(new MakeDefaultAction());
        }
        //todo
        /* if (filterEntity.getCode() == null && foldersPane != null && filterEntity.getFolder() == null)
            actions.addAction(new SaveAsFolderAction(false));
        if (checkGlobalAppFolderPermission()) {
            if (filterEntity.getCode() == null && foldersPane != null && filterEntity.getFolder() == null)
                actions.addAction(new SaveAsFolderAction(true));
        }*/
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
            if (paramsPanel != null)
                impl.remove(paramsPanel);
            createParamsPanel(false);
            impl.add(paramsPanel);
        } finally {
            changingFilter = false;
        }
        if (BooleanUtils.isTrue(filterEntity.getApplyDefault()) ||
                BooleanUtils.isTrue(filterEntity.getIsSet()) ||
                !getResultingManualApplyRequired())
            apply(true);
    }

    protected void createParamsPanel(boolean focusOnConditions) {
        if (paramsPanel != null) {
            impl.remove(paramsPanel);
        }
        LC paramsPanelLC = new LC();
        paramsPanelLC.insetsAll("0");
        if (LayoutAdapter.isDebug()) {
            paramsPanelLC.debug(1000);
        }
        paramsPanel = new JPanel(new MigLayout(paramsPanelLC));

        boolean hasGroups = false;
        for (AbstractCondition condition : conditions.getRoots()) {
            if (condition.isGroup() && !condition.isHidden()) {
                hasGroups = true;
                break;
            }
        }
        if (hasGroups && conditions.getRootNodes().size() > 1) {
            JPanel groupBox = createParamsGroupBox(
                    messages.getMessage(AbstractCondition.MESSAGES_PACK, "GroupType.AND"));
            paramsPanel.add(groupBox);
            recursivelyCreateParamsPanel(focusOnConditions, conditions.getRootNodes(), groupBox, 0);
        } else {
            recursivelyCreateParamsPanel(focusOnConditions, conditions.getRootNodes(), paramsPanel, 0);
        }

        impl.add(paramsPanel, new CC().spanX());
    }

    protected void recursivelyCreateParamsPanel(
            boolean focusOnConditions, List<Node<AbstractCondition>> nodes, JComponent parentComponent, int level) {
        List<Node<AbstractCondition>> visibleConditionNodes = new ArrayList<>();
        for (Node<AbstractCondition> node : nodes) {
            AbstractCondition condition = node.getData();
            if (!condition.isHidden())
                visibleConditionNodes.add(node);
        }
        if (visibleConditionNodes.isEmpty()) {
            return;
        }

        int columns = level == 0 ? 3 : 2;
        LC paramsLC = new LC();
        paramsLC.insetsAll("0").wrapAfter(columns);
        if (LayoutAdapter.isDebug()) {
            paramsLC.debug(1000);
        }
        MigLayout paramsLayout = new MigLayout(paramsLC);
        parentComponent.setLayout(paramsLayout);

        boolean focusSet = false;

        for (Node<AbstractCondition> node : visibleConditionNodes) {
            AbstractCondition condition = node.getData();
            if (condition.isGroup()) {
                CollapsiblePanel groupBox = createParamsGroupBox(condition.getLocCaption());

                if (!node.getChildren().isEmpty()) {
                    recursivelyCreateParamsPanel(
                            focusOnConditions && !focusSet, node.getChildren(), groupBox.getComposition(), level++);
                }
                parentComponent.add(groupBox);
            } else {
                JPanel paramPanel = new JPanel();

                if (condition.getParam().getJavaClass() != null) {
                    JLabel label = new JLabel(condition.getLocCaption());
                    paramPanel.add(label);

                    final ParamEditor paramEditor = new ParamEditor(condition, true, true);
                    if (focusOnConditions && !focusSet) {
                        focusSet = true;
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                paramEditor.requestFocus();
                            }
                        });
                    }
                    paramPanel.add(paramEditor);
                }
                parentComponent.add(paramPanel);
            }
        }
    }

    protected CollapsiblePanel createParamsGroupBox(String caption) {
        JPanel composition = new JPanel();
        composition.setBorder(new EmptyBorder(0,0,0,0));

        CollapsiblePanel groupBox = new CollapsiblePanel(composition);
        groupBox.setCaption(caption);
        groupBox.setBorderVisible(true);
        return groupBox;
    }

    protected void internalSetFilterEntity() {
        List<ItemWrapper<FilterEntity>> list = select.getOptionsList();
        for (ItemWrapper<FilterEntity> wrapper : list) {
            if (wrapper.getItem().equals(filterEntity)) {
                list.remove(wrapper);
                break;
            }
        }
        String caption = getCurrentFilterCaption();
        if (BooleanUtils.isTrue(filterEntity.getIsDefault()) && filterEntity.getFolder() == null) {
            caption += " " + defaultFilterCaption;
        }
        list.add(new ItemWrapper<>(filterEntity, caption));
        final Map<FilterEntity, String> captions = new HashMap<>();
        for (ItemWrapper<FilterEntity> filterWrapper : list) {
            String filterCaption;
            if (filterWrapper.getItem() == filterEntity) {
                filterCaption = getCurrentFilterCaption();
                captions.put(filterWrapper.getItem(), getCurrentFilterCaption());
            } else {
                filterCaption = getFilterCaption(filterWrapper.getItem());
                if (BooleanUtils.isTrue(filterWrapper.getItem().getIsDefault())) {
                    filterCaption += " " + defaultFilterCaption;
                }
            }

            captions.put(filterWrapper.getItem(), filterCaption);
        }

        Collections.sort(
                list,
                new Comparator<ItemWrapper>() {
                    @Override
                    public int compare(ItemWrapper f1, ItemWrapper f2) {
                        return captions.get(f1.getItem()).compareTo(captions.get(f2.getItem()));
                    }
                }
        );

        select.setOptionsList(list);
        if (!required)
            select.setNullOption(noFilterWrapper);

        for (ItemWrapper filterWrapper : list) {
            if (filterWrapper.getItem() == filterEntity) {
                select.setValue(filterWrapper);
            }
        }
    }

    protected void initMaxResultsPanel() {
        MigLayout layout = new MigLayout();
        maxResultsPanel = new JPanel(layout);

        maxResultsCb = new JCheckBox(messages.getMainMessage("filter.maxResults.label1"));
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

        JLabel maxResultsLabel2 = new JLabel(messages.getMainMessage("filter.maxResults.label2"));
        maxResultsPanel.add(maxResultsLabel2);
    }

    protected String getCurrentFilterCaption() {
        String name;
        if (filterEntity != null)
            if (filterEntity.getCode() == null)
                name = InstanceUtils.getInstanceName(filterEntity);
            else {
                name = messages.getMainMessage(filterEntity.getCode());
            }
        else
            name = "";
        AbstractSearchFolder folder = filterEntity.getFolder();
        if (folder != null) {
            if (!StringUtils.isBlank(folder.getTabName()))
                name = messages.getMainMessage(folder.getTabName());
            else if (!StringUtils.isBlank(folder.getName())) {
                name = messages.getMainMessage(folder.getName());
            }
            if (BooleanUtils.isTrue(filterEntity.getIsSet()))
                name = messages.getMessage(MESSAGES_PACK, "setPrefix") + " " + name;
            else
                name = messages.getMessage(MESSAGES_PACK, "folderPrefix") + " " + name;
        }
        return name;
    }

    @Override
    public boolean apply(boolean isNewWindow) {
        if (clientConfig.getGenericFilterChecking()) {
            if (filterEntity != null) {
                boolean haveRequiredConditions = haveFilledRequiredConditions();
                if (!haveRequiredConditions) {
                    if (!isNewWindow) {
                        WindowManager wm = DesktopComponentsHelper.getTopLevelFrame(getComposition()).getWindowManager();
                        wm.showNotification(messages.getMainMessage("filter.emptyRequiredConditions"),
                                IFrame.NotificationType.HUMANIZED);
                    }
                    return false;
                }

                boolean haveCorrectCondition = hasCorrectCondition();
                if (!haveCorrectCondition) {
                    if (!isNewWindow) {
                        WindowManager wm = DesktopComponentsHelper.getTopLevelFrame(getComposition()).getWindowManager();
                        wm.showNotification(messages.getMainMessage("filter.emptyConditions"),
                                IFrame.NotificationType.HUMANIZED);
                    }
                    return false;
                }
            }
        }

        applyDatasourceFilter();

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
            if ((condition.getParam() != null) && (condition.getParam().getValue() != null)
                    || condition instanceof CustomCondition && condition.isHidden()) {
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

    protected void applyDatasourceFilter() {
        if (datasource != null) {
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
        } else {
            LogFactory.getLog(DesktopFilter.class).warn("Unable to apply datasource filter with null datasource");
        }
    }

    protected void createFilterEntity() {
        filterEntity = new FilterEntity();

        filterEntity.setComponentId(getComponentPath());
        filterEntity.setName(messages.getMessage(MESSAGES_PACK, "newFilterName"));
        filterEntity.setUser(userSessionSource.getUserSession().getCurrentOrSubstitutedUser());
    }

    protected void copyFilterEntity() {
        FilterEntity newFilterEntity = new FilterEntity();
        newFilterEntity.setComponentId(filterEntity.getComponentId());
        newFilterEntity.setName(messages.getMessage(MESSAGES_PACK, "newFilterName"));
        newFilterEntity.setUser(userSessionSource.getUserSession().getCurrentOrSubstitutedUser());
        //newFilterEntity.setCode(filterEntity.getCode());
        newFilterEntity.setXml(filterEntity.getXml());
        filterEntity = newFilterEntity;
    }

    protected String getComponentPath() {
        StringBuilder sb = new StringBuilder(getId() != null ? getId() : "filterWithoutId");
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

    @Override
    public void loadFiltersAndApplyDefault() {
        loadFilterEntities();

        Window window = ComponentsHelper.getWindow(this);

        Collection<ItemWrapper<FilterEntity>> filters = select.getOptionsList();
        FilterEntity defaultFilter = getDefaultFilter(filters, window);
        if (defaultFilter != null) {
            defaultFilterEmpty = false;

            for (ItemWrapper<FilterEntity> filterWrapper : filters) {
                if (ObjectUtils.equals(defaultFilter, filterWrapper.getItem())) {
                    filterWrapper.setCaption(getFilterCaption(filterWrapper.getItem()) + " " + defaultFilterCaption);

                    if (!WindowParams.DISABLE_AUTO_REFRESH.getBool(window.getContext())) {
                        applyingDefault = true;
                        try {
                            select.setValue(filterWrapper);
                            updateControls();
                            if (clientConfig.getGenericFilterManualApplyRequired()) {
                                if (BooleanUtils.isTrue(filterWrapper.getItem().getApplyDefault())) {
                                    apply(true);
                                }
                            } else
                                apply(true);
                            if (filterEntity != null)
                                window.setDescription(getFilterCaption(filterEntity));
                            else
                                window.setDescription(null);
                        } finally {
                            applyingDefault = false;
                        }
                    }
                    break;
                }
            }
        } else {
            noFilter.setIsDefault(true);
            defaultFilterEmpty = true;
            updateControls();
        }
    }

    public void editorCancelled() {
        if (filterEntity.getXml() == null)
            filterEntity = null;

        switchToUse();
    }

    protected String getFilterCaption(FilterEntity filter) {
        if (filter.getCode() == null)
            return filter.getName();
        else {
            return messages.getMainMessage(filter.getCode());
        }
    }

    protected void loadFilterEntities() {
        DataService ds = AppBeans.get(DataService.NAME);
        LoadContext ctx = new LoadContext(metadata.getExtendedEntities().getEffectiveMetaClass(FilterEntity.class));
        ctx.setView("app");

        User user = userSessionSource.getUserSession().getSubstitutedUser();
        if (user == null)
            user = userSessionSource.getUserSession().getUser();

        MetaClass effectiveMetaClass = metadata.getExtendedEntities().getEffectiveMetaClass(FilterEntity.class);
        ctx.setQueryString("select f from " +effectiveMetaClass.getName() + " f " +
                "where f.componentId = :component and (f.user is null or f.user.id = :userId) order by f.name")
                .setParameter("component", getComponentPath())
                .setParameter("userId", user.getId());

        List<FilterEntity> filters = new ArrayList(ds.loadList(ctx));
        final Map<FilterEntity, String> captions = new HashMap<>();
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
        List<ItemWrapper<FilterEntity>> wrappedList = new LinkedList<>();
        for (FilterEntity filter : filters) {
            wrappedList.add(new ItemWrapper<>(filter, getFilterCaption(filter)));
        }
        select.setOptionsList(wrappedList);
        if (!required) {
            select.setNullOption(noFilterWrapper);
            select.setValue(noFilterWrapper);
        } else {
            if (!wrappedList.isEmpty())
                select.setValue(wrappedList.iterator().next());
        }
    }

    protected FilterEntity getDefaultFilter(Collection<ItemWrapper<FilterEntity>> filterWrappers, Window window) {
        // First check if there is parameter with name equal to this filter component id, containing a filter code to apply
        Map<String, Object> params = window.getContext().getParams();
        String code = (String) params.get(getId());
        if (!StringUtils.isBlank(code)) {
            for (ItemWrapper<FilterEntity> filterWrapper : filterWrappers) {
                if (code.equals(filterWrapper.getItem().getCode()))
                    return filterWrapper.getItem();
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
                    for (ItemWrapper<FilterEntity> filterWrapper : filterWrappers) {
                        if (defaultId.equals(filterWrapper.getItem().getId())) {
                            FilterEntity filter = filterWrapper.getItem();
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

    protected void saveFilterEntity() {
        Boolean isDefault = filterEntity.getIsDefault();
        Boolean applyDefault = filterEntity.getApplyDefault();
        if (filterEntity.getFolder() == null) {
            DataService ds = AppBeans.get(DataService.NAME);
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

    protected void deleteFilterEntity() {
        DataService ds = AppBeans.get(DataService.NAME);
        CommitContext ctx = new CommitContext();
        ctx.setRemoveInstances(Collections.singletonList(filterEntity));
        ds.commit(ctx);
    }

    protected void createEditLayout() {
        List<String> names = new ArrayList<>();
        Map<String, Locale> locales = globalConfig.getAvailableLocales();
        for (ItemWrapper<FilterEntity> filterWrapper : (List<ItemWrapper<FilterEntity>>) select.getOptionsList()) {
            FilterEntity filter = filterWrapper.getItem();
            if (filter != filterEntity) {
                if (filter.getCode() == null)
                    names.add(filter.getName());
                else {
                    for (Map.Entry<String, Locale> locale : locales.entrySet()) {
                        names.add(messages.getMainMessage(filter.getCode(), locale.getValue()));
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
                    Collection<ItemWrapper<FilterEntity>> filterWrappers = select.getOptionsList();
                    for (ItemWrapper<FilterEntity> filterWrapper : filterWrappers) {
                        if (!filterWrapper.getItem().equals(filterEntity))
                            filterWrapper.getItem().setIsDefault(false);
                    }
                }
            }
        });
    }

    @Override
    public void setUseMaxResults(boolean useMaxResults) {
        this.useMaxResults = useMaxResults;
        maxResultsPanel.setVisible(useMaxResults
                && userSessionSource.getUserSession().isSpecificPermitted("cuba.gui.filter.maxResults"));

        if (datasource != null)
            initMaxResults();
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

    @Override
    public void setApplyTo(Component component) {
        applyTo = component;
        if ((applyTo != null) && (Table.class.isAssignableFrom(applyTo.getClass()))) {
            Table table = (Table) applyTo;
            setActions(table);
        }
    }

    @Override
    public Component getApplyTo() {
        return applyTo;
    }

    @Override
    public void add(Component component) {
    }

    @Override
    public void remove(Component component) {
    }

    @Override
    public void requestFocus() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                select.requestFocus();
            }
        });
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

    @Nullable
    @Override
    public <T extends Component> T getComponent(String id) {
        String[] elements = ValuePathHelper.parse(id);
        if (elements.length == 1) {
            return getOwnComponent(id);
        } else {
            throw new UnsupportedOperationException("Filter contains only one level of subcomponents");
        }
    }

    @Nonnull
    @Override
    public <T extends Component> T getComponentNN(String id) {
        T component = getComponent(id);
        if (component == null) {
            throw new IllegalArgumentException(String.format("Not found component with id '%s'", id));
        }
        return component;
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return Collections.emptyList();
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
    }

    @Override
    public boolean saveSettings(Element element) {
        Boolean changed = false;
        Element e = element.element("defaultFilter");
        if (e == null)
            e = element.addElement("defaultFilter");

        UUID defaultId = null;
        Boolean applyDefault = false;
        Collection<ItemWrapper<FilterEntity>> filterWrappers = select.getOptionsList();
        for (ItemWrapper<FilterEntity> filterWraper : filterWrappers) {
            if (BooleanUtils.isTrue(filterWraper.getItem().getIsDefault())) {
                defaultId = filterWraper.getItem().getId();
                applyDefault = filterWraper.getItem().getApplyDefault();
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

    protected void delete() {
        if (required && select.getOptionsList().size() == 1) {
            getFrame().showNotification(
                    messages.getMessage(MESSAGES_PACK, "deleteRequired.caption"),
                    messages.getMessage(MESSAGES_PACK, "deleteRequired.msg"),
                    IFrame.NotificationType.HUMANIZED);
            return;
        }
        getFrame().showOptionDialog(
                messages.getMessage(MESSAGES_PACK, "deleteDlg.title"),
                messages.getMessage(MESSAGES_PACK, "deleteDlg.msg"),
                IFrame.MessageType.CONFIRMATION,
                new Action[]{
                        new DialogAction(DialogAction.Type.YES) {
                            @Override
                            public void actionPerform(Component component) {
                                deleteFilterEntity();
                                filterEntity = null;
                                select.getOptionsList().remove(select.getValue());
                                select.setOptionsList(select.getOptionsList()); // to update DesktopLookupField properly
                                if (!select.getOptionsList().isEmpty()) {
                                    select.setValue(select.getOptionsList().iterator().next());
                                } else {
                                    select.setValue(noFilterWrapper);
                                }
                            }
                        },
                        new DialogAction(DialogAction.Type.NO)
                }
        );
    }

    protected void setDefaultFilter() {
        if (filterEntity != null) {
            filterEntity.setIsDefault(true);
            defaultFilterEmpty = false;
        } else defaultFilterEmpty = true;
        Collection<ItemWrapper<FilterEntity>> filterWrappers = select.getOptionsList();
        for (ItemWrapper<FilterEntity> filterWrapper : filterWrappers) {
            if (!ObjectUtils.equals(filterWrapper.getItem(), filterEntity)) {
                filterWrapper.getItem().setIsDefault(false);
                filterWrapper.setCaption(getFilterCaption(filterWrapper.getItem()));
            } else {
                filterWrapper.setCaption(getFilterCaption(filterWrapper.getItem()) + " " + defaultFilterCaption);
            }
        }
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
        actionsButton.setVisible(editable && isEditFiltersPermitted());
    }

    protected boolean isEditFiltersPermitted() {
        return userSessionSource.getUserSession().isSpecificPermitted("cuba.gui.filter.edit");
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setRequired(boolean required) {
        if (this.required != required)
            select.setRequired(required);

        this.required = required;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public void setFolderActionsEnabled(boolean enabled) {
        folderActionsEnabled = enabled;
    }

    @Override
    public boolean isFolderActionsEnabled() {
        return folderActionsEnabled;
    }

    @Override
    public void setMargin(boolean enable) {
        setMargin(enable, enable, enable, enable);
    }

    @Override
    public void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
        MigLayout layout = (MigLayout) impl.getLayout();
        LC lc = new LC();
        lc.setInsets(MigLayoutHelper.makeInsets(new boolean[] {topEnable, rightEnable, bottomEnable, leftEnable}));
        layout.setLayoutConstraints(lc);
    }

    protected boolean getResultingManualApplyRequired() {
        return manualApplyRequired != null ? manualApplyRequired : clientConfig.getGenericFilterManualApplyRequired();
    }

    protected void updateWindowCaption() {
        if (!applyingDefault) {
            Window window = ComponentsHelper.getWindow(DesktopFilter.this);
            String filterTitle;
            if (filterEntity != null) {
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

            DesktopWindowManager wm = DesktopComponentsHelper.getTopLevelFrame(
                    DesktopFilter.this.getComposition()).getWindowManager();

            wm.setWindowCaption(window, initialWindowCaption, filterTitle);
        }
    }

    protected class SelectListener implements ValueListener {

        @Override
        public void valueChanged(Object source, String property, Object prevValue, Object value) {
            if (changingFilter) {
                return;
            }

            ItemWrapper<FilterEntity> selected = select.getValue();

            filterEntity = selected == null ? null : selected.getItem();
            if (noFilter.equals(filterEntity)) {
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

            updateWindowCaption();

            if (useMaxResults) {
                maxResultsCb.setSelected(true);
            }
            paramsPanel.revalidate();
            paramsPanel.repaint();
        }
    }

    protected class CreateAction extends AbstractAction {

        protected CreateAction() {
            super("createAction");
        }

        @Override
        public String getCaption() {
            return messages.getMessage(MESSAGES_PACK, getId());
        }

        @Override
        public void actionPerform(Component component) {
            createFilterEntity();
            parseFilterXml();
            switchToEdit();
        }
    }

    protected class CopyAction extends AbstractAction {
        protected CopyAction() {
            super("copyAction");
        }

        @Override
        public String getCaption() {
            return messages.getMessage(MESSAGES_PACK, getId());
        }

        @Override
        public void actionPerform(Component component) {
            copyFilterEntity();
            parseFilterXml();
            switchToEdit();
        }
    }

    protected class EditAction extends AbstractAction {

        protected EditAction() {
            super("editAction");
        }

        @Override
        public String getCaption() {
            return messages.getMessage(MESSAGES_PACK, getId());
        }

        @Override
        public void actionPerform(Component component) {
            switchToEdit();
        }
    }

    protected class DeleteAction extends AbstractAction {

        protected DeleteAction() {
            super("deleteAction");
        }

        @Override
        public String getCaption() {
            return messages.getMessage(MESSAGES_PACK, getId());
        }

        @Override
        public void actionPerform(Component component) {
            delete();
        }
    }

    protected class MakeDefaultAction extends AbstractAction {
        public MakeDefaultAction() {
            super("makeDefault");
        }

        @Override
        public String getCaption() {
            return messages.getMessage(MESSAGES_PACK, getId());
        }

        @Override
        public void actionPerform(Component component) {
            setDefaultFilter();
            actionsButton.removeAction(MakeDefaultAction.this);
        }
    }

    public static final Pattern LIKE_PATTERN = Pattern.compile("\\slike\\s+" + ParametersHelper.QUERY_PARAMETERS_RE);

    protected static class ParamWrapper implements HasValue {
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

    protected class MaxResultsField extends JTextField {
        private Integer value;
        private static final int ENTER_CODE = 10;

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

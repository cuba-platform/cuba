/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.entity.AppFolder;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
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
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebWindowManager;
import com.haulmont.cuba.web.app.folders.AppFolderEditWindow;
import com.haulmont.cuba.web.app.folders.FolderEditWindow;
import com.haulmont.cuba.web.app.folders.FoldersPane;
import com.haulmont.cuba.web.gui.components.filter.*;
import com.haulmont.cuba.web.toolkit.VersionedThemeResource;
import com.haulmont.cuba.web.toolkit.ui.CubaCheckBox;
import com.haulmont.cuba.web.toolkit.ui.CubaComboBox;
import com.haulmont.cuba.web.toolkit.ui.CubaGroupBox;
import com.haulmont.cuba.web.toolkit.ui.CubaVerticalActionsLayout;
import com.haulmont.cuba.web.toolkit.ui.converters.SimpleStringToIntegerConverter;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.commons.lang.*;
import org.dom4j.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generic filter implementation for the web-client.
 *
 * @author krivopustov
 * @version $Id$
 */
public class WebFilter extends WebAbstractComponent<CubaVerticalActionsLayout> implements Filter {

    protected static final String MESSAGES_PACK = "com.haulmont.cuba.gui.components.filter";

    protected static final String GLOBAL_FILTER_PERMISSION = "cuba.gui.filter.global";
    protected static final String GLOBAL_APP_FOLDERS_PERMISSION = "cuba.gui.appFolder.global";

    public static final Pattern LIKE_PATTERN = Pattern.compile("\\slike\\s+" + ParametersHelper.QUERY_PARAMETERS_RE);

    protected Messages messages;
    protected UserSessionSource userSessionSource;

    protected PersistenceManagerService persistenceManager;

    protected CollectionDatasource datasource;
    protected QueryFilter dsQueryFilter;
    protected FilterEntity filterEntity;
    protected ConditionsTree conditions = new ConditionsTree();

    protected ComponentContainer paramsLayout;
    protected AbstractOrderedLayout editLayout;
    protected CubaComboBox select;
    protected WebPopupButton actionsButton;

    protected Button pinAppliedFilterBtn;
    protected Button applyBtn;

    protected boolean defaultFilterEmpty = true;
    protected boolean changingFilter;
    protected boolean applyingDefault;
    protected boolean editing;
    protected FilterEditor editor;
    protected FoldersPane foldersPane;

    protected boolean useMaxResults;
    protected CheckBox maxResultsCb;
    protected TextField maxResultsField;
    protected AbstractOrderedLayout maxResultsLayout;
    protected Boolean manualApplyRequired;

    protected boolean editable = true;
    protected boolean required = false;
    protected boolean folderActionsEnabled = true;

    protected Component applyTo;

    protected FilterEntity noFilter;
    protected FilterEntity filterEntityBeforeCopy;

    protected AppliedFilter lastAppliedFilter;
    protected LinkedList<AppliedFilterHolder> appliedFilters = new LinkedList<>();
    protected VerticalLayout appliedFiltersLayout;

    protected GlobalConfig globalConfig = AppBeans.get(Configuration.class).getConfig(GlobalConfig.class);
    protected ClientConfig clientConfig = AppBeans.get(Configuration.class).getConfig(ClientConfig.class);
    protected String defaultFilterCaption;

    protected HorizontalLayout topLayout = null;

    protected Metadata metadata = AppBeans.get(Metadata.class);

    protected String userStyleName = null;

    public WebFilter() {
        persistenceManager = AppBeans.get(PersistenceManagerService.NAME);
        component = new CubaVerticalActionsLayout();

        messages = AppBeans.get(Messages.class);
        userSessionSource = AppBeans.get(UserSessionSource.class);

        defaultFilterCaption = messages.getMessage(MESSAGES_PACK, "defaultFilter");

        // don't add margin because filter is usually placed inside a groupbox that adds margins to its content
        component.setMargin(false);
        component.setSpacing(true);
        component.setStyleName("cuba-generic-filter");

        foldersPane = App.getInstance().getAppWindow().getFoldersPane();

        topLayout = new HorizontalLayout();
        topLayout.setSpacing(true);

        noFilter = new FilterEntity() {
            @Override
            public String toString() {
                return getName();
            }
        };
        noFilter.setName(messages.getMainMessage("filter.noFilter"));

        select = new CubaComboBox();
        select.setWidth(300, Sizeable.Unit.PIXELS);
        select.setStyleName("cuba-generic-filter-select");
        select.setNullSelectionAllowed(true);
        select.setNullSelectionItemId(noFilter);
        select.setImmediate(true);
        select.setPageLength(20);
        select.addValueChangeListener(new SelectListener());
        select.setTextInputAllowed(false);

        topLayout.addComponent(select);

        applyBtn = WebComponentsHelper.createButton("icons/search.png");
        applyBtn.setCaption(messages.getMainMessage("actions.Apply"));
        applyBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                apply(false);
            }
        });
        topLayout.addComponent(applyBtn);

        if (globalConfig.getAllowQueryFromSelected()) {
            pinAppliedFilterBtn = WebComponentsHelper.createButton();
            pinAppliedFilterBtn.setCaption(messages.getMessage(MESSAGES_PACK, "pinAppliedFilterBtn.caption"));
            pinAppliedFilterBtn.setDescription(messages.getMessage(MESSAGES_PACK, "pinAppliedFilterBtn.description"));
            pinAppliedFilterBtn.setEnabled(false);
            pinAppliedFilterBtn.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    if (datasource instanceof CollectionDatasource.SupportsApplyToSelected) {
                        ((CollectionDatasource.SupportsApplyToSelected) datasource).pinQuery();
                        addApplied();
                    }
                }
            });
            topLayout.addComponent(pinAppliedFilterBtn);
        }

        actionsButton = new WebPopupButton();
        actionsButton.setCaption(messages.getMessage(MESSAGES_PACK, "actionsCaption"));
        topLayout.addComponent((com.vaadin.ui.Component) actionsButton.getComponent());

        initMaxResultsLayout();
        topLayout.addComponent(maxResultsLayout);
        topLayout.setComponentAlignment(maxResultsLayout, com.vaadin.ui.Alignment.MIDDLE_LEFT);

        component.addComponent(topLayout);

        createParamsLayout(false);
        if (paramsLayout.getComponentCount() > 0)
            component.addComponent(paramsLayout);

        updateControls();

        if (AppUI.getCurrent().isTestMode()) {
            select.setCubaId("filterSelect");

            if (applyBtn != null) {
                applyBtn.setCubaId("applyBtn");
            }

            if (maxResultsLayout != null) {
                maxResultsField.setCubaId("maxResultsField");
                maxResultsCb.setCubaId("maxResultsCheckBox");
            }

            if (actionsButton != null) {
                actionsButton.setId("actionsBtn");
            }

            if (pinAppliedFilterBtn != null) {
                pinAppliedFilterBtn.setCubaId("pinAppliedBtn");
            }
        }
    }

    protected void addApplied() {
        if (lastAppliedFilter == null)
            return;

        if (!appliedFilters.isEmpty() && appliedFilters.getLast().filter.equals(lastAppliedFilter))
            return;

        if (appliedFiltersLayout == null) {
            appliedFiltersLayout = new VerticalLayout();
            appliedFiltersLayout.setMargin(new MarginInfo(true, false, false, false));
            appliedFiltersLayout.setSpacing(true);

            component.addComponent(appliedFiltersLayout, component.getComponentIndex(paramsLayout));
        }

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);

        if (!appliedFilters.isEmpty()) {
            AppliedFilterHolder holder = appliedFilters.getLast();
            holder.layout.removeComponent(holder.button);
        }

        Label label = new Label(lastAppliedFilter.getText());
        layout.addComponent(label);

        Button button = new Button();
        button.setStyleName(BaseTheme.BUTTON_LINK);
        button.addStyleName("remove-applied-filter");
        button.setIcon(new VersionedThemeResource("icons/item-remove.png"));
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                removeLastApplied();
            }
        });
        layout.addComponent(button);
        layout.setComponentAlignment(button, com.vaadin.ui.Alignment.MIDDLE_LEFT);

        appliedFiltersLayout.addComponent(layout);

        appliedFilters.add(new AppliedFilterHolder(lastAppliedFilter, layout, button));
    }

    protected void removeLastApplied() {
        if (!appliedFilters.isEmpty()) {
            AppliedFilterHolder holder = appliedFilters.removeLast();
            appliedFiltersLayout.removeComponent(holder.layout);

            if (!appliedFilters.isEmpty()) {
                holder = appliedFilters.getLast();
                holder.layout.addComponent(holder.button);
                holder.layout.setComponentAlignment(holder.button, com.vaadin.ui.Alignment.MIDDLE_LEFT);
            }
        }
        ((CollectionDatasource.SupportsApplyToSelected) datasource).unpinLastQuery();
    }

    @Override
    public void requestFocus() {
        select.focus();
    }

    protected void initMaxResultsLayout() {
        maxResultsLayout = new HorizontalLayout();
        maxResultsLayout.setSpacing(true);
        maxResultsCb = new CubaCheckBox(messages.getMainMessage("filter.maxResults.label1"));
        maxResultsCb.setImmediate(true);
        maxResultsCb.setValue(true);
        maxResultsCb.addValueChangeListener(
                new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        maxResultsField.setEnabled(BooleanUtils.isTrue(maxResultsCb.getValue()));
                    }
                });

        maxResultsCb.setStyleName("cuba-filter-maxresults");
        maxResultsLayout.addComponent(maxResultsCb);
        maxResultsLayout.setComponentAlignment(maxResultsCb, com.vaadin.ui.Alignment.MIDDLE_LEFT);

        maxResultsField = new TextField();
        maxResultsField.setImmediate(true);
        maxResultsField.setMaxLength(4);
        maxResultsField.setWidth(50, Sizeable.Unit.PIXELS);
        maxResultsField.setInvalidAllowed(false);
        maxResultsField.addValidator(
                new IntegerRangeValidator(messages.getMainMessage("validation.invalidNumber"), 0, Integer.MAX_VALUE) {
                    @Override
                    public void validate(Object value) throws InvalidValueException {
                        try {
                            super.validate(value);
                        } catch (InvalidValueException e) {
                            maxResultsField.markAsDirty();
                            throw e;
                        }
                    }
                }
        );
        maxResultsField.setConverter(new SimpleStringToIntegerConverter());
        maxResultsLayout.addComponent(maxResultsField);

        Label maxResultsLabel2 = new Label(messages.getMainMessage("filter.maxResults.label2"));
        maxResultsLabel2.setSizeUndefined();
        maxResultsLayout.addComponent(maxResultsLabel2);
        maxResultsLayout.setComponentAlignment(maxResultsLabel2, com.vaadin.ui.Alignment.MIDDLE_LEFT);

        maxResultsLayout.setStyleName("cuba-filter-maxresults");
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
        updateFolderActions();
    }

    protected void updateFolderActions() {
        Action saveAsFolderAction = actionsButton.getAction(SaveAsFolderAction.SAVE_AS_FOLDER);
        Action saveAsAppFolderAction = actionsButton.getAction(SaveAsFolderAction.SAVE_AS_APP_FOLDER);

        if (filterEntity != null && isFolderActionsEnabled()) {
            if (filterEntity.getCode() == null && foldersPane != null
                    && filterEntity.getFolder() == null && saveAsFolderAction == null) {
                actionsButton.addAction(new SaveAsFolderAction(false));
            }
            if (checkGlobalAppFolderPermission()) {
                if (filterEntity.getCode() == null && foldersPane != null
                        && filterEntity.getFolder() == null && saveAsAppFolderAction == null)
                    actionsButton.addAction(new SaveAsFolderAction(true));
            }
        } else {
            if (saveAsAppFolderAction != null) {
                actionsButton.removeAction(saveAsAppFolderAction);
            }
            if (saveAsFolderAction != null) {
                actionsButton.removeAction(saveAsFolderAction);
            }
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
                        wm.showNotification(messages.getMainMessage("filter.emptyRequiredConditions"),
                                IFrame.NotificationType.HUMANIZED);
                    }
                    return false;
                }

                boolean haveCorrectCondition = hasCorrectCondition();
                if (!haveCorrectCondition) {
                    if (!isNewWindow) {
                        wm.showNotification(messages.getMainMessage("filter.emptyConditions"),
                                IFrame.NotificationType.HUMANIZED);
                    }
                    return false;
                }
            }
        }

        applyDatasourceFilter();

        if (useMaxResults) {
            int maxResults = 0;
            if (BooleanUtils.isTrue(maxResultsCb.getValue())) {
                String maxResultsFieldValue = maxResultsField.getValue();
                if (StringUtils.isNotBlank(maxResultsFieldValue)) {
                    try {
                        //noinspection ConstantConditions
                        maxResults = Datatypes.get(Integer.class).parse(maxResultsFieldValue, userSessionSource.getLocale());
                    } catch (ParseException e) {
                        throw new Validator.InvalidValueException("");
                    }
                } else
                    maxResults = persistenceManager.getMaxFetchUI(datasource.getMetaClass().getName());
            }
            datasource.setMaxResults(maxResults);
        }
        if (datasource instanceof CollectionDatasource.SupportsPaging)
            ((CollectionDatasource.SupportsPaging) datasource).setFirstResult(0);

        refreshDatasource();

        if (pinAppliedFilterBtn != null) {
            pinAppliedFilterBtn.setEnabled(filterEntity != null && filterEntity.getXml() != null);
        }
        lastAppliedFilter = new AppliedFilter(filterEntity, paramsLayout);

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
        if (datasource instanceof CollectionDatasource.Suspendable)
            ((CollectionDatasource.Suspendable) datasource).refreshIfNotSuspended();
        else
            datasource.refresh();
    }

    protected void applyDatasourceFilter() {
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

    protected void createFilterEntity() {
        filterEntity = metadata.create(FilterEntity.class);

        filterEntity.setComponentId(getComponentPath());
        filterEntity.setName(messages.getMessage(MESSAGES_PACK, "newFilterName"));
        filterEntity.setUser(userSessionSource.getUserSession().getCurrentOrSubstitutedUser());
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

    protected void copyFilterEntity() {

        FilterEntity newFilterEntity = metadata.create(FilterEntity.class);
        newFilterEntity.setComponentId(filterEntity.getComponentId());
        newFilterEntity.setName(messages.getMessage(MESSAGES_PACK, "newFilterName"));
        newFilterEntity.setUser(userSessionSource.getUserSession().getCurrentOrSubstitutedUser());
        //newFilterEntity.setCode(filterEntity.getCode());
        newFilterEntity.setXml(filterEntity.getXml());
        filterEntityBeforeCopy = filterEntity;
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

    protected void createParamsLayout(boolean focusOnConditions) {
        boolean hasGroups = false;
        for (AbstractCondition condition : conditions.getRoots()) {
            if (condition.isGroup() && !condition.isHidden()) {
                hasGroups = true;
                break;
            }
        }
        if (hasGroups && conditions.getRootNodes().size() > 1) {
            WebGroupBox groupBox = new WebGroupBox();
            groupBox.setWidth(Component.AUTO_SIZE);
            groupBox.setCaption(messages.getMessage(AbstractCondition.MESSAGES_PACK, "GroupType.AND"));
            ComponentContainer container = groupBox.getComponent();
            paramsLayout = container;
            recursivelyCreateParamsLayout(focusOnConditions, conditions.getRootNodes(), container, 0);
        } else {
            paramsLayout = recursivelyCreateParamsLayout(focusOnConditions, conditions.getRootNodes(), null, 0);
        }
        if (paramsLayout instanceof Layout.MarginHandler) {
            ((Layout.MarginHandler) paramsLayout).setMargin(new MarginInfo(false));
        }
        paramsLayout.setStyleName("cuba-generic-filter-paramslayout");
    }

    protected ComponentContainer recursivelyCreateParamsLayout(boolean focusOnConditions,
                                                             List<Node<AbstractCondition>> nodes,
                                                             ComponentContainer parentContainer,
                                                             int level) {

        List<Node<AbstractCondition>> visibleConditionNodes = new ArrayList<>();
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
        grid.setMargin(new MarginInfo(parentContainer == null, false, false, false));
        grid.setSpacing(true);

        boolean focusSet = false;

        // Test support
        boolean useTestIds = (parentContainer == null && getDebugId() != null) || (parentContainer != null && parentContainer.getId() != null);
        String baseTestId = null;
        TestIdManager testIdManager = null;
        if (useTestIds) {
            baseTestId = parentContainer == null ? getDebugId() : parentContainer.getId();
            testIdManager = AppUI.getCurrent().getTestIdManager();
        }

        for (int i = 0; i < visibleConditionNodes.size(); i++) {
            Node<AbstractCondition> node = visibleConditionNodes.get(i);
            AbstractCondition condition = node.getData();
            com.vaadin.ui.Component cellContent;
            if (condition.isGroup()) {
                CubaGroupBox groupBox = new CubaGroupBox();
                groupBox.setWidth(Component.AUTO_SIZE);
                groupBox.setCaption(condition.getLocCaption());

                if (!node.getChildren().isEmpty()) {
                    recursivelyCreateParamsLayout(
                            focusOnConditions && !focusSet, node.getChildren(), groupBox, level++);
                }

                if (useTestIds) {
                    groupBox.setId(testIdManager.getTestId(baseTestId + "_group_" + i));
                    groupBox.setCubaId("group_" + i);
                }

                cellContent = groupBox;
            } else {
                if (condition.getParam().getJavaClass() != null) {
                    ParamEditor paramEditor = new ParamEditor(condition, true, true, true);
                    if (focusOnConditions && !focusSet) {
                        paramEditor.setFocused();
                        focusSet = true;
                    }

                    if (useTestIds) {
                        paramEditor.setId(testIdManager.getTestId(baseTestId + "_param_" + i));
                        paramEditor.setCubaId("param_" + i);
                    }

                    cellContent = paramEditor;
                } else {
                    HorizontalLayout paramLayout = new HorizontalLayout();
                    paramLayout.setSpacing(true);
                    paramLayout.setMargin(false);
                    paramLayout.setSizeUndefined();
                    paramLayout.setStyleName("cuba-generic-filter-paramcell");

                    cellContent = paramLayout;
                }
            }
            grid.addComponent(cellContent, i % columns, i / columns);
            grid.setComponentAlignment(cellContent, com.vaadin.ui.Alignment.MIDDLE_RIGHT);
        }

        if (parentContainer != null) {
            parentContainer.addComponent(grid);
        }

        return grid;
    }

    protected void setActions(Table table) {
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
                addToCurSetBtn.setCaption(messages.getMessage(MESSAGES_PACK, "addToCurSet"));
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
                removeFromCurSetBtn.setCaption(messages.getMessage(MESSAGES_PACK, "removeFromCurSet"));
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
                addToSetBtn.setCaption(messages.getMessage(MESSAGES_PACK, "addToSet"));
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
            if (!WindowParams.DISABLE_AUTO_REFRESH.getBool(window.getContext())) {
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

        if (required)
            updateComponentRequired();
    }

    @Override
    public void setUseMaxResults(boolean useMaxResults) {
        this.useMaxResults = useMaxResults;
        maxResultsLayout.setVisible(useMaxResults
                && AppBeans.get(UserSessionSource.class).getUserSession().isSpecificPermitted("cuba.gui.filter.maxResults"));
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

    protected void internalSetFilterEntity() {
        List<FilterEntity> list = new ArrayList(select.getItemIds());
        list.remove(filterEntity);

        select.getContainerDataSource().removeAllItems();

        list.add(filterEntity);

        final Map<FilterEntity, String> captions = new HashMap<>();
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
                    name = messages.getMessage(MESSAGES_PACK, "setPrefix") + " " + name;
                else
                    name = messages.getMessage(MESSAGES_PACK, "folderPrefix") + " " + name;
            }
        } else
            name = "";
        return name;
    }

    public List<AbstractCondition> getConditions() {
        return Collections.unmodifiableList(conditions.toConditionsList());
    }

    public void editorCancelled() {
        filterEntity = filterEntityBeforeCopy;
        filterEntityBeforeCopy = null;
        if (filterEntity != null && filterEntity.getXml() == null)
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
        DataService ds = AppBeans.get(DataService.class);
        LoadContext ctx = new LoadContext(metadata.getExtendedEntities().getEffectiveMetaClass(FilterEntity.class));
        ctx.setView("app");

        UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.class);

        User user = userSessionSource.getUserSession().getSubstitutedUser();
        if (user == null)
            user = userSessionSource.getUserSession().getUser();
        MetaClass effectiveMetaClass = metadata.getExtendedEntities().getEffectiveMetaClass(FilterEntity.class);

        ctx.setQueryString("select f from " + effectiveMetaClass.getName() + " f " +
                "where f.componentId = :component and (f.user is null or f.user.id = :userId) order by f.name")
                .setParameter("component", getComponentPath())
                .setParameter("userId", user.getId());

        List<FilterEntity> filters = new ArrayList<>(ds.<FilterEntity>loadList(ctx));
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

        select.addItem(noFilter);
        for (FilterEntity filter : filters) {
            select.addItem(filter);
            select.setItemCaption(filter, captions.get(filter));
        }
    }

    protected FilterEntity getDefaultFilter(Collection<FilterEntity> filters, Window window) {
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

    protected void saveFilterEntity() {
        Boolean isDefault = filterEntity.getIsDefault();
        Boolean applyDefault = filterEntity.getApplyDefault();
        if (filterEntity.getFolder() == null) {
            DataService ds = AppBeans.get(DataService.class);
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

    protected void deleteFilterEntity() {
        DataService ds = AppBeans.get(DataService.class);
        CommitContext ctx = new CommitContext();
        ctx.setRemoveInstances(Collections.singletonList(filterEntity));
        ds.commit(ctx);
    }

    protected void createEditLayout() {
        List<String> names = new ArrayList<>();
        Map<String, Locale> locales = globalConfig.getAvailableLocales();
        for (Object id : select.getItemIds()) {
            if (id != filterEntity) {
                FilterEntity fe = (FilterEntity) id;
                if (fe.getCode() == null)
                    names.add(fe.getName());
                else {
                    for (Map.Entry<String, Locale> locale : locales.entrySet()) {
                        names.add(messages.getMainMessage(fe.getCode(), locale.getValue()));
                    }
                }
            }
        }

        editor = new FilterEditor(this, filterEntity, getXmlDescriptor(), names);
        editor.init();
        editor.getSaveButton().addClickListener(new Button.ClickListener() {
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
        editLayout = editor.getLayout();
    }

    @Override
    public void setFrame(IFrame frame) {
        super.setFrame(frame);
        ClientConfig clientConfig = AppBeans.get(Configuration.class).getConfig(ClientConfig.class);
        frame.addAction(new AbstractAction("applyFilter", clientConfig.getFilterApplyShortcut()) {
            @Override
            public void actionPerform(Component component) {
                apply(false);
            }
        });
    }

    @Override
    public void setDebugId(String id) {
        super.setDebugId(id);

        if (id != null && AppUI.getCurrent().isTestMode()) {
            TestIdManager testIdManager = AppUI.getCurrent().getTestIdManager();

            select.setId(testIdManager.getTestId(id + "_filterSelect"));

            if (applyBtn != null) {
                applyBtn.setId(testIdManager.getTestId(id + "_applyBtn"));
            }

            if (maxResultsLayout != null) {
                maxResultsField.setId(testIdManager.getTestId(id + "_maxResultsField"));
                maxResultsCb.setId(testIdManager.getTestId(id + "_maxResultsCheckBox"));
            }

            if (actionsButton != null) {
                actionsButton.setDebugId(testIdManager.getTestId(id + "_actionsBtn"));
            }

            if (pinAppliedFilterBtn != null) {
                pinAppliedFilterBtn.setId(testIdManager.getTestId(id + "_pinAppliedBtn"));
            }
        }
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

    protected void switchToUse() {
        editing = false;
        editor = null;

        if (filterEntity == null) {
            filterEntity = (FilterEntity) select.getValue();
            if ((filterEntity != null) && (applyTo != null) && (Table.class.isAssignableFrom(applyTo.getClass()))) {
                Table table = (Table) applyTo;
                setActions(table);
            }

            parseFilterXml();
        }

        updateControls();
        component.removeComponent(editLayout);
        createParamsLayout(true);
        if (paramsLayout.getComponentCount() > 0)
            component.addComponent(paramsLayout);
    }

    protected void switchToEdit() {
        editing = true;
        updateControls();
        component.removeComponent(paramsLayout);
        createEditLayout();
        component.addComponent(editLayout);
    }

    protected void updateControls() {
        fillActions();
        actionsButton.setVisible(!editing);
        ((org.vaadin.hene.popupbutton.PopupButton) actionsButton.getComponent()).setPopupVisible(false);
        ((org.vaadin.hene.popupbutton.PopupButton) actionsButton.getComponent()).setVisible(editable);
        ((org.vaadin.hene.popupbutton.PopupButton) actionsButton.getComponent()).setEnabled(actionsButton.getActions().size() > 0);

        select.setEnabled(!editing);
        applyBtn.setVisible(!editing);
        actionsButton.setVisible(editable && isEditFiltersPermitted());
        if (pinAppliedFilterBtn != null)
            pinAppliedFilterBtn.setEnabled(!editing && filterEntity != null && filterEntity.getXml() != null);
    }

    protected boolean checkGlobalAppFolderPermission() {
        return userSessionSource.getUserSession().isSpecificPermitted(GLOBAL_APP_FOLDERS_PERMISSION);
    }

    protected boolean checkGlobalFilterPermission() {
        if (filterEntity == null || filterEntity.getUser() != null)
            return true;
        else
            return userSessionSource.getUserSession().isSpecificPermitted(GLOBAL_FILTER_PERMISSION);
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
        return Collections.EMPTY_LIST;
    }

    @Override
    public Collection<Component> getComponents() {
        return getOwnComponents();
    }

    protected void parseFilterXml() {
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

        String newXml = submintParameters();

        folder.setFilterComponentId(filterEntity.getComponentId());
        folder.setFilterXml(newXml);
        if (!isAppFolder) {
            if (userSessionSource.getUserSession().isSpecificPermitted(GLOBAL_FILTER_PERMISSION))
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
                    //                                AppUI.getInstance().getWindowManager().showOptionDialog(
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
        window.addCloseListener(new com.vaadin.ui.Window.CloseListener() {
            @Override
            public void windowClose(com.vaadin.ui.Window.CloseEvent e) {
                App.getInstance().getAppUI().removeWindow(window);
            }
        });
        App.getInstance().getAppUI().addWindow(window);
    }

    protected String submintParameters() {
        FilterParser parser = new FilterParser(filterEntity.getXml(), MESSAGES_PACK, filterEntity.getComponentId(), datasource);
        parser.fromXml();
        List<AbstractCondition> defaultConditions = parser.getConditions().toConditionsList();
        Iterator<AbstractCondition> it = conditions.toConditionsList().iterator();
        Iterator<AbstractCondition> defaultIt = defaultConditions.iterator();
        while (it.hasNext()) {
            AbstractCondition current = it.next();
            AbstractCondition defCondition = defaultIt.next();
            AbstractParam param = current.getParam();
            if (param != null && param.getValue() != null) {
                defCondition.setParam(param);
            }
        }
        return parser.toXml().getXml();
    }

    protected SearchFolder saveFolder(SearchFolder folder) {
        SearchFolder savedFolder = (SearchFolder) foldersPane.saveFolder(folder);
        foldersPane.refreshFolders();
        return savedFolder;
    }

    protected AppFolder saveAppFolder(AppFolder folder) {
        AppFolder savedFolder = (AppFolder) foldersPane.saveFolder(folder);
        foldersPane.refreshFolders();
        return savedFolder;
    }

    protected void delete() {
        if (required) {
            int size = 0;
            for (Object itemId : select.getItemIds()) {
                if (itemId != noFilter)
                    size++;
            }
            if (size == 1) {
                getFrame().showNotification(
                        messages.getMessage(MESSAGES_PACK, "deleteRequired.caption"),
                        messages.getMessage(MESSAGES_PACK, "deleteRequired.msg"),
                        IFrame.NotificationType.HUMANIZED);
                return;
            }
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
                                select.removeItem(select.getValue());
                                if (required) {
                                    updateComponentRequired();
                                } else {
                                    if (!select.getItemIds().isEmpty()) {
                                        select.select(select.getItemIds().iterator().next());
                                    } else {
                                        select.select(null);
                                    }
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
        actionsButton.setVisible(editable && isEditFiltersPermitted());
    }

    protected boolean isEditFiltersPermitted() {
        return AppBeans.get(UserSessionSource.class).getUserSession().isSpecificPermitted("cuba.gui.filter.edit");
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setRequired(boolean required) {
        if (required)
            updateComponentRequired();

        if (this.required != required)
            select.setNullSelectionAllowed(!required);
        this.required = required;
    }

    protected void updateComponentRequired() {
        if (select.getValue() == null) {
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

    @Override
    public void setFolderActionsEnabled(boolean enabled) {
        this.folderActionsEnabled = enabled;
        updateFolderActions();
    }

    @Override
    public boolean isFolderActionsEnabled() {
        return folderActionsEnabled;
    }

    @Override
    public void setStyleName(String name) {
        if (userStyleName != null)
            getComposition().removeStyleName(userStyleName);
        this.userStyleName = name;
        if (name != null)
            getComposition().addStyleName(userStyleName);
    }

    @Override
    public String getStyleName() {
        return userStyleName;
    }

    @Override
    public void setMargin(boolean enable) {
        component.setMargin(enable);
    }

    @Override
    public void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
        component.setMargin(new MarginInfo(topEnable, rightEnable, bottomEnable, leftEnable));
    }

    protected boolean getResultingManualApplyRequired() {
        return manualApplyRequired != null ? manualApplyRequired : clientConfig.getGenericFilterManualApplyRequired();
    }

    protected class SelectListener implements Property.ValueChangeListener {
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

            if (paramsLayout.getComponentCount() > 0)
                component.addComponent(paramsLayout);

            if (!applyingDefault) {
                Window window = ComponentsHelper.getWindow(WebFilter.this);
                String descr;
                if (filterEntity != null)
                    if (filterEntity.getCode() != null) {
                        descr = messages.getMainMessage(filterEntity.getCode());
                    } else
                        descr = filterEntity.getName();
                else
                    descr = null;
                window.setDescription(descr);
                App.getInstance().getWindowManager().setCurrentWindowCaption(window, window.getCaption(), descr);
            }

            if (useMaxResults)
                maxResultsCb.setValue(true);

            if (pinAppliedFilterBtn != null)
                pinAppliedFilterBtn.setEnabled(false);
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

    protected class SaveAsFolderAction extends AbstractAction {

        public static final String SAVE_AS_APP_FOLDER = "saveAsAppFolderAction";
        public static final String SAVE_AS_FOLDER = "saveAsFolderAction";

        protected boolean isAppFolder;

        protected SaveAsFolderAction(boolean isAppFolder) {
            super(isAppFolder ? SAVE_AS_APP_FOLDER : SAVE_AS_FOLDER);
            this.isAppFolder = isAppFolder;
        }

        @Override
        public String getCaption() {
            return messages.getMessage(MESSAGES_PACK, getId());
        }

        @Override
        public void actionPerform(Component component) {
            saveAsFolder(isAppFolder);
        }
    }

    protected static class ParamWrapper implements HasValue {

        protected final AbstractCondition condition;
        protected final AbstractParam param;

        protected ParamWrapper(AbstractCondition condition, AbstractParam param) {
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

        protected String wrapValueForLike(Object value) {
            return ParametersHelper.CASE_INSENSITIVE_MARKER + "%" + value + "%";
        }

        protected String wrapValueForLike(Object value, boolean before, boolean after) {
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
            return messages.getMessage(MESSAGES_PACK, getId());
        }

        @Override
        public void actionPerform(Component component) {
            if (!table.getSelected().isEmpty()) {
                String entityType = table.getDatasource().getMetaClass().getName();
                Map<String, Object> params = new HashMap<>();
                params.put("entityType", entityType);
                params.put("items", table.getSelected());
                params.put("componentPath", getComponentPath());
                String[] strings = ValuePathHelper.parse(getComponentPath());
                String componentId = ValuePathHelper.format(Arrays.copyOfRange(strings, 1, strings.length));
                params.put("componentId", componentId);
                params.put("foldersPane", foldersPane);
                params.put("entityClass", datasource.getMetaClass().getJavaClass().getName());
                params.put("query", datasource.getQuery());
                WebFilter.this.getFrame().openWindow("saveSetInFolder",
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
            return messages.getMessage(MESSAGES_PACK, getId());
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

    protected class AddToCurrSetAction extends AbstractAction {

        protected AddToCurrSetAction() {
            super("addToCurSet");
        }

        @Override
        public String getCaption() {
            return messages.getMessage(MESSAGES_PACK, getId());
        }

        @Override
        public void actionPerform(Component component) {
            IFrame frame = WebFilter.this.getFrame();
            String[] strings = ValuePathHelper.parse(getComponentPath());
            String windowAlias = strings[0];
            StringBuilder lookupAlias = new StringBuilder(windowAlias);
            if (windowAlias.endsWith(".browse")) {
                int index = lookupAlias.lastIndexOf(".browse");
                lookupAlias.delete(index, lookupAlias.length());
                lookupAlias.append(".lookup");
            }
            frame.openLookup(lookupAlias.toString(), new Window.Lookup.Handler() {

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
            Set<String> set = new HashSet<>();
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
            Set<String> convertedSet = new HashSet<>();
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
            Set<String> convertedSet = new HashSet<>();
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

    protected static class AppliedFilterHolder {
        public final AppliedFilter filter;
        public final HorizontalLayout layout;
        public final Button button;

        protected AppliedFilterHolder(AppliedFilter filter, HorizontalLayout layout, Button button) {
            this.filter = filter;
            this.layout = layout;
            this.button = button;
        }
    }
}
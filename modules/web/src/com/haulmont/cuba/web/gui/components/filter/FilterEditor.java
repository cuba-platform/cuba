/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 14.10.2009 17:19:08
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.security.entity.FilterEntity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.WebFilter;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.UserSessionClient;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.vaadin.ui.*;
import com.vaadin.data.Property;

import java.util.*;
import java.util.regex.Pattern;

import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;
import static org.apache.commons.lang.BooleanUtils.isTrue;

public class FilterEditor {

    private FilterEntity filterEntity;
    private Element filterDescriptor;
    private MetaClass metaClass;
    private CollectionDatasource datasource;
    private List<ConditionDescriptor> descriptors = new ArrayList<ConditionDescriptor>();

    private List<Condition> conditions = new ArrayList<Condition>();
    private AbstractOrderedLayout layout;
    private TextField nameField;
    private Table table;
    private String messagesPack;
    private String filterComponentName;
    private AbstractSelect addSelect;

    private static final String EDITOR_WIDTH = "480px";
    private static List<String> defaultExcludedProps = Arrays.asList("version");
    private CheckBox globalCb;

    public FilterEditor(final WebFilter webFilter, FilterEntity filterEntity, Element filterDescriptor)
    {
        this.filterEntity = filterEntity;
        this.filterDescriptor = filterDescriptor;
        this.filterComponentName = filterDescriptor.attributeValue("id");
        this.datasource = webFilter.getDatasource();
        this.messagesPack = webFilter.getFrame().getMessagesPack();
        this.metaClass = datasource.getMetaClass();

        parseDescriptorXml();

        FilterParser parser = new FilterParser(this.filterEntity.getXml(), messagesPack, filterComponentName);
        this.conditions = parser.fromXml().getConditions();

        layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true, false, false, false);
        layout.setWidth(EDITOR_WIDTH);

        GridLayout grid = new GridLayout(2, 2);
        grid.setWidth("100%");
        grid.setSpacing(true);

        HorizontalLayout controlLayout = new HorizontalLayout();
        controlLayout.setSpacing(true);

        Button saveBtn = WebComponentsHelper.createButton();
        saveBtn.setCaption(getMessage("FilterEditor.saveBtn"));
        saveBtn.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                if (commit())
                    webFilter.editorCommitted();
            }
        });
        controlLayout.addComponent(saveBtn);

        Button cancelBtn = WebComponentsHelper.createButton();
        cancelBtn.setCaption(getMessage("FilterEditor.cancelBtn"));
        cancelBtn.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                webFilter.editorCancelled();
            }
        });
        controlLayout.addComponent(cancelBtn);

        grid.addComponent(controlLayout, 0, 0);

        globalCb = new CheckBox();
        globalCb.setCaption(getMessage("FilterEditor.global"));
        globalCb.setValue(filterEntity.getUser() == null);
        globalCb.setEnabled(UserSessionClient.getUserSession().isSpecificPermitted("cuba.gui.filter.global"));
        controlLayout.addComponent(globalCb);

        grid.addComponent(globalCb, 1, 0);
        grid.setComponentAlignment(globalCb, Alignment.MIDDLE_RIGHT);

        HorizontalLayout nameLayout = new HorizontalLayout();
        nameLayout.setSpacing(true);

        Label label = new Label(getMessage("FilterEditor.nameLab"));
        nameLayout.addComponent(label);

        nameField = new TextField();
        nameField.setValue(filterEntity.getName());
        nameField.setWidth("200px");
        nameLayout.addComponent(nameField);

        grid.addComponent(nameLayout, 0, 1);

        AbstractLayout addLayout = initAddSelect();

        grid.addComponent(addLayout, 1, 1);
        grid.setComponentAlignment(addLayout, Alignment.MIDDLE_RIGHT);

        layout.addComponent(grid);

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSpacing(true);

        initTable(mainLayout);

        layout.addComponent(mainLayout);
    }

    private AbstractLayout initAddSelect() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        
        Label label = new Label(getMessage("FilterEditor.addCondition"));
        layout.addComponent(label);

        addSelect = new NativeSelect();
        addSelect.setImmediate(true);
        addSelect.setNullSelectionAllowed(true);
        addSelect.setWidth("100px");
        for (ConditionDescriptor descriptor : descriptors) {
            addSelect.addItem(descriptor);
            addSelect.setItemCaption(descriptor, descriptor.getLocCaption());
        }

        if (UserSessionClient.getUserSession().isSpecificPermitted("cuba.gui.filter.customConditions")) {
            ConditionCreator conditionCreator = new ConditionCreator(filterComponentName, datasource);
            addSelect.addItem(conditionCreator);
            addSelect.setItemCaption(conditionCreator, conditionCreator.getLocCaption());
        }

        addSelect.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                if (addSelect.getValue() != null) {
                    addCondition((ConditionDescriptor) addSelect.getValue());
                    addSelect.select(null);
                }
            }
        });

        layout.addComponent(addSelect);

        return layout;
    }

    private void initTable(AbstractLayout layout) {
        table = new Table();
        table.setImmediate(true);
        table.setSelectable(false);
        table.setPageLength(0);
        table.setWidth(EDITOR_WIDTH);
        table.setHeight("200px");

        String nameCol = getMessage("FilterEditor.column.name");
        String opCol = getMessage("FilterEditor.column.op");
        String paramCol = getMessage("FilterEditor.column.param");
        String cntrCol = getMessage("FilterEditor.column.control");

        table.addContainerProperty(nameCol, NameEditor.class, null);
        table.setColumnWidth(nameCol, 160);

        table.addContainerProperty(opCol, OperationEditor.class, null);
        table.setColumnWidth(opCol, 80);

        table.addContainerProperty(paramCol, ParamEditor.class, null);
        table.setColumnWidth(paramCol, 160);

        table.addContainerProperty(cntrCol, Button.class, null);
        table.setColumnWidth(cntrCol, 80);

        for (final Condition condition : this.conditions) {
            NameEditor nameEditor = new NameEditor(condition);
            OperationEditor operationEditor = condition.createOperationEditor();
            ParamEditor paramEditor = new ParamEditor(condition, false);

            table.addItem(new Object[] {
                    nameEditor,
                    operationEditor,
                    paramEditor,
                    createDeleteConditionBtn(condition)
                    },
                    condition
            );
        }

        layout.addComponent(table);
    }

    private String getMessage(String key) {
        return MessageProvider.getMessage(getClass(), key);
    }

    private void addCondition(ConditionDescriptor descriptor) {
        Condition condition = descriptor.createCondition();
        conditions.add(condition);

        NameEditor nameEditor = new NameEditor(condition);
        OperationEditor operationEditor = condition.createOperationEditor();
        ParamEditor paramEditor = new ParamEditor(condition, false);

        table.addItem(new Object[] {
                nameEditor,
                operationEditor,
                paramEditor,
                createDeleteConditionBtn(condition)
                },
                condition
        );
    }

    private void deleteCondition(Condition condition) {
        conditions.remove(condition);
        table.removeItem(condition);
    }

    private Button createDeleteConditionBtn(final Condition condition) {
        Button delBtn = WebComponentsHelper.createButton();
        delBtn.setCaption("Delete");
        delBtn.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                deleteCondition(condition);
            }
        });
        return delBtn;
    }

    private void parseDescriptorXml() {
        for (Element element : Dom4j.elements(filterDescriptor)) {
            ConditionDescriptor conditionDescriptor;
            if ("properties".equals(element.getName())) {
                addMultiplePropertyDescriptors(element, filterComponentName);
            } else if ("property".equals(element.getName())) {
                conditionDescriptor = new PropertyConditionDescriptor(element, messagesPack, filterComponentName, datasource);
                descriptors.add(conditionDescriptor);
            } else if ("custom".equals(element.getName())) {
                conditionDescriptor = new CustomConditionDescriptor(element, messagesPack, filterComponentName, datasource);
                descriptors.add(conditionDescriptor);
            } else
                throw new UnsupportedOperationException("Element not supported: " + element.getName());
        }

        Collections.sort(descriptors, new Comparator<ConditionDescriptor>() {
            public int compare(ConditionDescriptor cd1, ConditionDescriptor cd2) {
                return cd1.getLocCaption().compareTo(cd2.getLocCaption());
            }
        });
    }

    private void addMultiplePropertyDescriptors(Element element, String filterComponentName) {
        List<String> includedProps = new ArrayList<String>();

        String inclRe = element.attributeValue("include");
        Pattern inclPattern = Pattern.compile(inclRe);

        for (MetaProperty property : metaClass.getProperties()) {
            if (property.getRange().getCardinality().isMany())
                continue;
            if (defaultExcludedProps.contains(property.getName()))
                continue;

            if (inclPattern.matcher(property.getName()).matches()) {
                includedProps.add(property.getName());
            }
        }

        String exclRe = element.attributeValue("exclude");
        Pattern exclPattern = null;
        if (!StringUtils.isBlank(exclRe))
            exclPattern = Pattern.compile(exclRe);

        for (String prop : includedProps) {
            if (exclPattern == null || !exclPattern.matcher(prop).matches()) {
                ConditionDescriptor conditionDescriptor =
                        new PropertyConditionDescriptor(prop, null, messagesPack, filterComponentName, datasource);
                descriptors.add(conditionDescriptor);
            }
        }
    }

    public AbstractOrderedLayout getLayout() {
        return layout;
    }

    public FilterEntity getFilterEntity() {
        return filterEntity;
    }

    public boolean commit() {
        StringBuilder sb = new StringBuilder();
        for (Condition condition : conditions) {
            String error = condition.getError();
            if (error != null)
                sb.append(error).append("\n");
        }
        if (sb.length() > 0) {
            App.getInstance().getMainWindow().showNotification(
                    getMessage("FilterEditor.commitError"),
                    sb.toString(),
                    Window.Notification.TYPE_HUMANIZED_MESSAGE);
            return false;
        }

        FilterParser parser = new FilterParser(conditions, messagesPack, filterComponentName);
        String xml = parser.toXml().getXml();

        filterEntity.setName((String) nameField.getValue());
        filterEntity.setXml(xml);

        if (isTrue((Boolean) globalCb.getValue()))
            filterEntity.setUser(null);
        else
            filterEntity.setUser(UserSessionClient.getUserSession().getUser());

        return true;
    }
}

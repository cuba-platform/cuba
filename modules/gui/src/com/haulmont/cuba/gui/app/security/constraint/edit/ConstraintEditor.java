/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.constraint.edit;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.filter.GroovyGenerator;
import com.haulmont.cuba.core.global.filter.JpqlGenerator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManagerProvider;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.autocomplete.JpqlSuggestionFactory;
import com.haulmont.cuba.gui.components.autocomplete.Suggestion;
import com.haulmont.cuba.gui.components.filter.ConditionsTree;
import com.haulmont.cuba.gui.components.filter.FakeFilterSupport;
import com.haulmont.cuba.gui.components.filter.FilterParser;
import com.haulmont.cuba.gui.components.filter.Param;
import com.haulmont.cuba.gui.components.filter.edit.FilterEditor;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.Constraint;
import com.haulmont.cuba.security.entity.ConstraintOperationType;
import com.haulmont.cuba.security.entity.FilterEntity;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Arrays.asList;

/**
 * @author Chevelev
 * @version $Id$
 */
public class ConstraintEditor extends AbstractEditor {

    @Inject
    protected LookupField entityName;

    @Inject
    protected SourceCodeEditor joinClause;

    @Inject
    protected SourceCodeEditor whereClause;

    @Inject
    protected SourceCodeEditor groovyScript;

    @Inject
    private Label groovyScriptLabel;

    @Inject
    private Label joinClauseLabel;

    @Inject
    private Label whereClauseLabel;

    @Inject
    private LinkButton whereClauseHelp;

    @Inject
    private LinkButton joinClauseHelp;

    @Inject
    private LinkButton groovyScriptHelp;

    @Inject
    private TextField code;

    @Inject
    private Label codeLabel;

    @Inject
    private Datasource<Constraint> constraint;

    @Inject
    protected Metadata metadata;

    @Inject
    protected ExtendedEntities extendedEntities;

    @Inject
    protected WindowManagerProvider windowManagerProvider;

    @Inject
    protected WindowConfig windowConfig;

    protected Map<Object, String> entities;

    @Override
    public void postInit() {
        Map<String, Object> options = new TreeMap<>();
        entities = new HashMap<>();
        for (MetaClass metaClass : metadata.getTools().getAllPersistentMetaClasses()) {
            if (extendedEntities.getExtendedClass(metaClass) == null) {
                MetaClass mainMetaClass = extendedEntities.getOriginalOrThisMetaClass(metaClass);
                String originalName = mainMetaClass.getName();
                options.put(metaClass.getName(), originalName);
                entities.put(originalName, metaClass.getName());
            }
        }
        entityName.setOptionsMap(options);

        joinClause.setHighlightActiveLine(false);
        joinClause.setShowGutter(false);
        joinClause.setShowPrintMargin(false);
        joinClause.setSuggester((source, text, cursorPosition) -> requestHint(joinClause, text, cursorPosition));

        whereClause.setHighlightActiveLine(false);
        whereClause.setShowGutter(false);
        whereClause.setShowPrintMargin(false);
        whereClause.setSuggester((source, text, cursorPosition) -> requestHint(whereClause, text, cursorPosition));

        groovyScript.setHighlightActiveLine(false);
        groovyScript.setShowGutter(false);
        groovyScript.setShowPrintMargin(false);

        setupVisibility();
        constraint.addItemPropertyChangeListener(e -> {
            if ("checkType".equals(e.getProperty()) || "operationType".equals(e.getProperty())) {
                setupVisibility();
            }
        });
    }

    private void setupVisibility() {
        Constraint item = (Constraint) getItem();
        asList(groovyScript, groovyScriptLabel, groovyScriptHelp)
                .forEach(component -> component.setVisible(item.getCheckType().memory()));
        asList(joinClause, joinClauseLabel, joinClauseHelp, whereClause, whereClauseLabel, whereClauseHelp)
                .forEach(component -> component.setVisible(item.getCheckType().database()));
        asList(code, codeLabel)
                .forEach(component -> component.setVisible(item.getOperationType() == ConstraintOperationType.CUSTOM));
    }

    protected List<Suggestion> requestHint(SourceCodeEditor sender, String text, int cursorPosition) {
        String joinStr = joinClause.getValue();
        String whereStr = whereClause.getValue();

        // the magic entity name!  The length is three character to match "{E}" length in query
        String entityNameAlias = "a39";

        int position = 0;

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append(entityNameAlias);
        queryBuilder.append(" from ");
        queryBuilder.append(entities.get(entityName.getValue()));
        queryBuilder.append(" ");
        queryBuilder.append(entityNameAlias);
        queryBuilder.append(" ");
        if (joinStr != null && !joinStr.equals("")) {
            if (sender == joinClause) {
                position = queryBuilder.length() + cursorPosition - 1;
            }
            queryBuilder.append(joinStr);
        }

        if (whereStr != null && !whereStr.equals("")) {
            if (sender == whereClause) {
                position = queryBuilder.length() + " WHERE ".length() + cursorPosition - 1;
            }
            queryBuilder.append(" WHERE ").append(whereStr);
        }

        String query = queryBuilder.toString();
        query = query.replace("{E}", entityNameAlias);

        List<Suggestion> suggestions
                = JpqlSuggestionFactory.requestHint(query, position, sender.getAutoCompleteSupport(), cursorPosition);
        addSpecificSuggestions(sender, text, cursorPosition, suggestions);
        return suggestions;
    }

    protected void addSpecificSuggestions(SourceCodeEditor sender, String text, int cursorPosition, List<Suggestion> suggestions) {
        if (cursorPosition >= 1 && ":".equals(text.substring(cursorPosition - 1, cursorPosition))) {
            suggestions.add(new Suggestion(sender.getAutoCompleteSupport(), "session$userLogin", "session$userLogin",
                    "", cursorPosition, cursorPosition));
            suggestions.add(new Suggestion(sender.getAutoCompleteSupport(), "session$userId", "session$userId",
                    "", cursorPosition, cursorPosition));
            suggestions.add(new Suggestion(sender.getAutoCompleteSupport(), "session$userGroupId", "session$userGroupId",
                    "", cursorPosition, cursorPosition));
        }
    }

    public void getJoinClauseHelp() {
        getDialogParams().setModal(false).setWidth(600);
        showMessageDialog(getMessage("joinClause"), getMessage("joinClauseHelp"), MessageType.CONFIRMATION_HTML);
    }

    public void getWhereClauseHelp() {
        getDialogParams().setModal(false).setWidth(600);
        showMessageDialog(getMessage("whereClause"), getMessage("whereClauseHelp"), MessageType.CONFIRMATION_HTML);
    }

    public void getGroovyScriptHelp() {
        getDialogParams().setModal(false).setWidth(600);
        showMessageDialog(getMessage("groovyScript"), getMessage("groovyScriptHelp"), MessageType.CONFIRMATION_HTML);
    }

    public void openWizard() {
        String entityNameValue = entityName.<String>getValue();
        if (StringUtils.isBlank(entityNameValue)) {
            showNotification(getMessage("notification.entityIsEmpty"), NotificationType.HUMANIZED);
            entityName.requestFocus();
            return;
        }

        FakeFilterSupport fakeFilterSupport = new FakeFilterSupport(this, metadata.getSession().getClass(entityNameValue));

        WindowInfo windowInfo = windowConfig.getWindowInfo("filterEditor");

        Map<String, Object> params = new HashMap<>();
        Constraint constraint = (Constraint) getItem();
        final Filter fakeFilter = fakeFilterSupport.createFakeFilter();
        final FilterEntity filterEntity = fakeFilterSupport.createFakeFilterEntity(constraint.getFilterXml());
        final ConditionsTree conditionsTree = fakeFilterSupport.createFakeConditionsTree(fakeFilter, filterEntity);

        params.put("filter", fakeFilter);
        params.put("filterEntity", filterEntity);
        params.put("conditions", conditionsTree);
        params.put("useShortConditionForm", true);

        FilterEditor filterEditor = (FilterEditor) windowManagerProvider.get().openWindow(windowInfo, WindowManager.OpenType.DIALOG, params);
        filterEditor.addCloseListener(actionId -> {
            //todo eude rename com.haulmont.cuba.gui.components.filter.FilterParser
            filterEntity.setXml(FilterParser.getXml(filterEditor.getConditions(), Param.ValueProperty.DEFAULT_VALUE));
            if (filterEntity.getXml() != null) {
                Element element = Dom4j.readDocument(filterEntity.getXml()).getRootElement();
                com.haulmont.cuba.core.global.filter.FilterParser filterParser = new com.haulmont.cuba.core.global.filter.FilterParser(element);

                Constraint item = (Constraint) getItem();
                if (item.getCheckType().database()) {
                    String jpql = new JpqlGenerator().generateJpql(filterParser.getRoot());
                    constraint.setWhereClause(jpql);
                }

                if (item.getCheckType().memory()) {
                    String groovy = new GroovyGenerator().generateGroovy(filterParser.getRoot());
                    constraint.setGroovyScript(groovy);
                }
                constraint.setFilterXml(filterEntity.getXml());
            }
        });
    }
}
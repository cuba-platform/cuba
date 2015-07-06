/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.constraint.edit;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.SourceCodeEditor;
import com.haulmont.cuba.gui.components.autocomplete.AutoCompleteSupport;
import com.haulmont.cuba.gui.components.autocomplete.JpqlSuggestionFactory;
import com.haulmont.cuba.gui.components.autocomplete.Suggester;
import com.haulmont.cuba.gui.components.autocomplete.Suggestion;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    protected Metadata metadata;

    protected Map<Object, String> entities;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        Map<String, Object> options = new TreeMap<>();
        entities = new HashMap<>();
        for (MetaClass metaClass : metadata.getTools().getAllPersistentMetaClasses()) {
            if (metadata.getExtendedEntities().getExtendedClass(metaClass) == null) {
                MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
                String originalName = originalMetaClass == null ? metaClass.getName() : originalMetaClass.getName();
                options.put(metaClass.getName(), originalName);
                entities.put(originalName, metaClass.getName());
            }
        }
        entityName.setOptionsMap(options);

        joinClause.setHighlightActiveLine(false);
        joinClause.setShowGutter(false);
        joinClause.setShowPrintMargin(false);
        joinClause.setSuggester(new Suggester() {
            @Override
            public List<Suggestion> getSuggestions(AutoCompleteSupport source, String text, int cursorPosition) {
                return requestHint(joinClause, text, cursorPosition);
            }
        });

        whereClause.setHighlightActiveLine(false);
        whereClause.setShowGutter(false);
        whereClause.setShowPrintMargin(false);
        whereClause.setSuggester(new Suggester() {
            @Override
            public List<Suggestion> getSuggestions(AutoCompleteSupport source, String text, int cursorPosition) {
                return requestHint(whereClause, text, cursorPosition);
            }
        });
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
}
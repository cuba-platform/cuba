package com.haulmont.cuba.web.app.ui.security.constraint.edit;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.autocomplete.AutoCompleteSupport;
import com.haulmont.cuba.gui.autocomplete.JpqlSuggestionFactory;
import com.haulmont.cuba.gui.autocomplete.Suggester;
import com.haulmont.cuba.gui.autocomplete.Suggestion;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.AutoCompleteTextField;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.TextField;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Author: Alexander Chevelev
 * Date: 23.12.2010
 * Time: 13:41:17
 */
public class ConstraintEditor extends AbstractEditor {
    private TextField entityName;
    private AutoCompleteTextField joinClause;
    private AutoCompleteTextField whereClause;

    private static volatile Collection<MetaClass> metaClasses;

    public ConstraintEditor(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        super.init(params);
        entityName = getComponent("entityName");
        // casts to concrete classes !
        joinClause = getComponent("joinClause");
        whereClause = getComponent("whereClause");

        joinClause.setSuggester(new Suggester() {
            public List<Suggestion> getSuggestions(AutoCompleteSupport source, String text, int cursorPosition) {
                return requestHint(joinClause, text, cursorPosition);
            }
        });

        whereClause.setSuggester(new Suggester() {
            public List<Suggestion> getSuggestions(AutoCompleteSupport source, String text, int cursorPosition) {
                return requestHint(whereClause, text, cursorPosition);
            }
        });

    }

    private List<Suggestion> requestHint(AutoCompleteTextField sender, String text, int cursorPosition) {
        String joinStr = (String) joinClause.getValue();
        String whereStr = (String) whereClause.getValue();

        // the magic entity name!  The length is three character to match "{E}" length in query
        String entityNameAlias = "a39";

        int position = -1;

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append(entityNameAlias);
        queryBuilder.append(" from ");
        queryBuilder.append(entityName.getValue());
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

        return JpqlSuggestionFactory.requestHint(query, position, sender.getAutoCompleteSupport(), cursorPosition);
    }
}

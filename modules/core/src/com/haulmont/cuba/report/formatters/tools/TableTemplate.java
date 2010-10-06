/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Vasiliy Fontanenko
 * Created: 23.06.2010 19:44:23
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters.tools;

import com.sun.star.text.XText;

import java.util.HashMap;
import java.util.regex.Pattern;

public class TableTemplate {
    private HashMap<Integer, String> columnsTemplates;
    private String tableName;
    private int templateRow;

    public TableTemplate() {
        columnsTemplates = new HashMap<Integer, String>();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void addColumnTemplate(Integer tableColumnIndex, String columnTemplate) {
        columnsTemplates.put(tableColumnIndex, columnTemplate);
    }

    public boolean isEmpty() {
        return columnsTemplates.isEmpty();
    }

    public int getTemplateRow() {
        return templateRow;
    }

    public void setTemplateRow(int templateRow) {
        this.templateRow = templateRow;
    }

    public HashMap<Integer, String> getColumnsTemplates() {
        return columnsTemplates;
    }
    
    public String getColumnTemplate(int col) {
        return columnsTemplates.get(col);
    }

    public boolean haveValueExpressions() {
        boolean haveValueExpressions = false;
        for(String columnTemplate : columnsTemplates.values()) {
            String templateText = columnTemplate;
            if (Pattern.compile("\\$\\{[^\\.]+?\\}").matcher(templateText).find()) {//ED - only table aliases are matched there - if there is a point - this is not a table alias 
                haveValueExpressions = true;
            }
        }
        return haveValueExpressions;
    }
}

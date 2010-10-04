package com.haulmont.cuba.report.formatters.tools;

import com.sun.star.text.XText;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: fontanenko
 * Date: 23.06.2010
 * Time: 19:44:23
 * To change this template use File | Settings | File Templates.
 */
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

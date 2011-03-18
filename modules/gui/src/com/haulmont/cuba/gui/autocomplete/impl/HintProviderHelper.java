package com.haulmont.cuba.jpql.impl;

/**
 * Author: Alexander Chevelev
 * Date: 17.10.2010
 * Time: 0:59:50
 */
public class HintProviderHelper {
    public static EntityPath parseEntityPath(String lastWord) {
        String[] parts = lastWord.split("\\.");
        EntityPath result = new EntityPath();
        result.topEntityVariableName = parts[0];
        int consumedPartsCount = 1;
        if (lastWord.endsWith(".") || parts.length == 1) {
            result.lastEntityFieldPattern = "";
        } else {
            result.lastEntityFieldPattern = parts[parts.length - 1];
            consumedPartsCount = 2;
        }
        if (parts.length == 1) {
            result.traversedFields = new String[0];
        } else {
            result.traversedFields = new String[parts.length - consumedPartsCount];
            System.arraycopy(parts, 1, result.traversedFields, 0, parts.length - consumedPartsCount);
        }
        return result;
    }

    public static String getLastWord(String queryString, int caretPosition) {
        // todo ������ �� ������� ����� ��������?
        if (queryString.charAt(caretPosition) == ' ') {
            return "";
        }
        int lastWordStart = queryString.lastIndexOf(' ', caretPosition);
        return queryString.substring(lastWordStart + 1, caretPosition + 1);
    }
}

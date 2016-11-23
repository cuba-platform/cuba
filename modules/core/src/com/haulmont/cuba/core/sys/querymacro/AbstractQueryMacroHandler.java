package com.haulmont.cuba.core.sys.querymacro;

import com.haulmont.cuba.core.sys.QueryMacroHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractQueryMacroHandler implements QueryMacroHandler {
    protected int count;
    private final Pattern macroPattern;

    protected AbstractQueryMacroHandler(Pattern macroPattern) {
        this.macroPattern = macroPattern;
    }

    @Override
    public String expandMacro(String queryString) {
        count = 0;
        Matcher matcher = macroPattern.matcher(queryString);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, doExpand(matcher.group(1)));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    protected abstract String doExpand(String macro);
}

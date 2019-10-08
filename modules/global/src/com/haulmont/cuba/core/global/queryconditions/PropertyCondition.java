package com.haulmont.cuba.core.global.queryconditions;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;

/**
 * Condition that contains parts of a query. The parts are stored as named values and can be obtained by
 * {@link #getValue(String)} method.
 * <p>
 * {@link #getParameters()} method returns parameters parsed from the query parts by the {@link #parseParameters()}
 * method.
 */
public abstract class PropertyCondition implements Condition {

    public static class Entry implements Serializable {
        public final String name;
        public final String value;

        public Entry(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString() {
            return name + ": " + value;
        }
    }

    protected List<Entry> entries;

    protected List<String> parameters = new ArrayList<>();

    public PropertyCondition(List<Entry> entries) {
        this.entries = new ArrayList<>(entries);
        parseParameters();
    }

    protected abstract void parseParameters();

    @Nullable
    public String getValue(String name) {
        for (Entry entry : entries) {
            if (entry.name.equals(name))
                return entry.value;
        }
        return null;
    }

    @Override
    public Collection<String> getParameters() {
        return parameters;
    }

    @Nullable
    @Override
    public Condition actualize(Set<String> actualParameters) {
        return actualParameters.containsAll(getParameters()) ? this : null;
    }

    @Override
    public String toString() {
        return entries.toString();
    }
}

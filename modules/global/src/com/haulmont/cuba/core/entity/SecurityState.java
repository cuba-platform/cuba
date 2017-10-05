package com.haulmont.cuba.core.entity;

import com.google.common.collect.Multimap;

import java.io.Serializable;
import java.util.*;

/**
 * Stores information about:
 * filtered data by row level security
 * hidden, readonly and required attributes for entity
 */
public class SecurityState implements Serializable {

    private static final long serialVersionUID = 6613320540189701505L;

    protected transient Multimap<String, Object> filteredData = null;

    protected String[] inaccessibleAttributes;

    protected String[] filteredAttributes;

    protected String[] readonlyAttributes;

    protected String[] requiredAttributes;

    protected String[] hiddenAttributes;

    protected byte[] securityToken;

    public Collection<String> getReadonlyAttributes() {
        return readonlyAttributes != null ? Collections.unmodifiableList(Arrays.asList(readonlyAttributes))
                : Collections.emptyList();
    }

    public Collection<String> getRequiredAttributes() {
        return requiredAttributes != null ? Collections.unmodifiableList(Arrays.asList(requiredAttributes))
                : Collections.emptyList();
    }

    public Collection<String> getHiddenAttributes() {
        return hiddenAttributes != null ? Collections.unmodifiableList(Arrays.asList(hiddenAttributes))
                : Collections.emptyList();
    }
}

package com.haulmont.chile.core.model;

import com.haulmont.chile.core.common.ValueBuffer;
import com.haulmont.chile.core.common.ValueListener;

import java.util.UUID;

/**
 * Interface of domain objects belonging to a metamodel.
 * Can be implicitly implemented by entities as a result of class enchancing.
 */
public interface Instance extends ValueBuffer {

    UUID getUuid();
    MetaClass getMetaClass();
    
    /**
     * Instance name as defined by {@link com.haulmont.chile.core.annotations.NamePattern}
     * or <code>toString()</code>
     */
    String getInstanceName();
    
    void addListener(ValueListener listener);
    void removeListener(ValueListener listener);
}

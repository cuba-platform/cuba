/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 17.09.2010 12:20:09
 *
 * $Id$
 */
package com.haulmont.cuba.gui.presentations;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.security.entity.Presentation;
import org.dom4j.Element;

import java.util.Collection;
import java.util.Map;

public interface Presentations {

    Presentation getCurrent();

    void setCurrent(Presentation p);

    Element getSettings(Presentation p);
    void setSettings(Presentation p, Element e);

    Presentation getPresentation(Object id);

    String getCaption(Object id);

    Collection<Object> getPresentationIds();

    void setDefault(Presentation p);

    Presentation getDefault();

    void add(Presentation presentation);

    void remove(Presentation p);

    void modify(Presentation p);

    boolean isAutoSave(Presentation p);

    boolean isGlobal(Presentation p);

    void commit();

    void commited(Map<Entity, Entity> map);

    Presentation getPresentationByName(String name);

    void addListener(PresentationsChangeListener listener);
    void removeListener(PresentationsChangeListener listener);
}

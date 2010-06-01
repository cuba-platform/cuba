/*
* Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
* Haulmont Technology proprietary and confidential.
* Use is subject to license terms.

* Author: Gennady Pavlov
* Created: 19.04.2010 14:13:26
*
* $Id$
*/
package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.WindowManager;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class ActionsFieldHelper {
    private ActionsField component;
    private String entityName;
    private MetaProperty metaProperty;

    public ActionsFieldHelper(ActionsField component) {
        this.component = component;
        metaProperty = component.getMetaProperty();
        entityName = metaProperty.getRange().asClass().getName();
    }

    public void createLookupAction() {
        createLookupAction(entityName + ".browse", WindowManager.OpenType.THIS_TAB, Collections.<String, Object>emptyMap());
    }

    public void createLookupAction(WindowManager.OpenType openType) {
        createLookupAction(entityName + ".browse", openType, Collections.<String, Object>emptyMap());
    }

    public void createLookupAction(String windowAlias) {
        createLookupAction(windowAlias, WindowManager.OpenType.THIS_TAB, Collections.<String, Object>emptyMap());
    }

    public void createLookupAction(final String windowAlias, final WindowManager.OpenType openType, final Map<String, Object> params) {
        Action action = new AbstractAction(ActionsField.LOOKUP) {
            public void actionPerform(Component componend) {
                component.getFrame().openLookup(windowAlias,
                        new Window.Lookup.Handler() {
                            public void handleLookup(Collection items) {
                                Entity entity;
                                if (items != null && items.size() > 0) {
                                    entity = (Entity) items.iterator().next();
                                    getItem().setValue(metaProperty.getName(), entity);
                                    component.getValue();
                                }
                            }
                        }, openType, params);
            }

            @Override
            public String getCaption() {
                return "";
            }
        };
        component.addAction(action);
    }

    public void createOpenAction() {
        Action action = new AbstractAction(ActionsField.OPEN) {
            public void actionPerform(Component componend) {
                Entity entity = getItem().getValue(metaProperty.getName());
                if (entity != null) {
                    component.getFrame().openEditor(((Instance)entity).getMetaClass().getName() + ".edit", entity, WindowManager.OpenType.THIS_TAB);
                }
            }

            @Override
            public String getCaption() {
                return "";
            }
        };
        component.addAction(action);
    }

    private Instance getItem() {
        return (Instance) component.getDatasource().getItem();
    }

}


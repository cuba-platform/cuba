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
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class ActionsFieldHelper {
    private ActionsField component;
    private String entityName;

    public ActionsFieldHelper(ActionsField component) {
        this.component = component;
        MetaProperty metaProperty = component.getMetaProperty();
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
                                if (items != null && items.size() > 0) {
                                    component.setValue(items.iterator().next());
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
        createOpenAction(WindowManager.OpenType.THIS_TAB);
    }

    public void createOpenAction(final WindowManager.OpenType openType) {
        Action action = new AbstractAction(ActionsField.OPEN) {
            public void actionPerform(Component componend) {
                Entity entity = component.getValue();
                if (entity != null) {
                    String windowAlias = ((Instance) entity).getMetaClass().getName() + ".edit";
                    final Window.Editor editor = component.getFrame().openEditor(windowAlias, entity, openType);
                    editor.addListener(new Window.CloseListener() {
                        public void windowClosed(String actionId) {
                            if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                                Entity item = editor.getItem();

                                CollectionDatasource optionsDatasource = component.getOptionsDatasource();
                                if (optionsDatasource != null && optionsDatasource.containsItem(item.getId())) {
                                    optionsDatasource.updateItem(item);
                                }

                                boolean modified  = component.getDatasource().isModified();
                                component.setValue(null);
                                component.setValue(item);
                                ((DatasourceImplementation) component.getDatasource()).setModified(modified);
                            }
                        }
                    });
                }
            }

            @Override
            public String getCaption() {
                return "";
            }
        };
        component.addAction(action);
    }

}


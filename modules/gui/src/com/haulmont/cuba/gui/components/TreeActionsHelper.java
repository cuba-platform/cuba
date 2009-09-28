package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.security.entity.EntityOp;

import java.util.Map;

public class TreeActionsHelper extends ListActionsHelper<Tree>{
    public TreeActionsHelper(IFrame frame, Tree tree) {
        super(frame, tree);
    }

    public Action createCreateAction(final ValueProvider valueProvider, final WindowManager.OpenType openType) {
        final AbstractAction action = new AbstractAction("create") {
            public String getCaption() {
                final String messagesPackage = AppConfig.getInstance().getMessagesPack();
                return MessageProvider.getMessage(messagesPackage, "actions.Create");
            }

            public boolean isEnabled() {
                return userSession.isEntityOpPermitted(metaClass, EntityOp.CREATE);
            }

            public void actionPerform(Component component) {
                final CollectionDatasource datasource = TreeActionsHelper.this.component.getDatasource();
                final String hierarchyProperty = TreeActionsHelper.this.component.getHierarchyProperty();
                final DataService dataservice = datasource.getDataService();

                final Entity parentItem = datasource.getItem();
                if (parentItem == null) return;

                final Entity item = dataservice.<Entity>newInstance(datasource.getMetaClass());
                ((Instance) item).setValue(hierarchyProperty, parentItem);

                final String windowID = datasource.getMetaClass().getName() + ".edit";
                for (Map.Entry<String, Object> entry : valueProvider.getValues().entrySet()) {
                    ((Instance) item).setValue(entry.getKey(), entry.getValue());
                }
                final Window window = frame.openEditor(windowID, item, openType, valueProvider.getParameters());
                window.addListener(new Window.CloseListener() {
                    public void windowClosed(String actionId) {
                        if (window instanceof Window.Editor) {
                            Object item = ((Window.Editor) window).getItem();
                            if (item instanceof Entity)
                                datasource.addItem((Entity) item);
                        }
                    }
                });
            }
        };
        
        TreeActionsHelper.this.component.addAction(action);
        return action;
    }
}

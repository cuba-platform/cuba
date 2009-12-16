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
import java.util.Collections;

public class TreeActionsHelper extends ListActionsHelper<Tree>{
    public TreeActionsHelper(IFrame frame, Tree tree) {
        super(frame, tree);
    }

    public Action createCreateAction(final ValueProvider valueProvider, final WindowManager.OpenType openType) {
        AbstractAction action = new CreateAction(valueProvider, openType);
        component.addAction(action);
        return action;
    }

    private class CreateAction extends AbstractAction {
        private final ValueProvider valueProvider;
        private final WindowManager.OpenType openType;

        public CreateAction(ValueProvider valueProvider, WindowManager.OpenType openType) {
            super("create");
            this.valueProvider = valueProvider;
            this.openType = openType;
        }

        public String getCaption() {
            final String messagesPackage = AppConfig.getInstance().getMessagesPack();
            return MessageProvider.getMessage(messagesPackage, "actions.Create");
        }

        public boolean isEnabled() {
            return super.isEnabled() && userSession.isEntityOpPermitted(metaClass, EntityOp.CREATE);
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
            if (valueProvider.getValues() != null) {
                for (Map.Entry<String, Object> entry : valueProvider.getValues().entrySet()) {
                    ((Instance) item).setValue(entry.getKey(), entry.getValue());
                }
            }

            Map<String, Object> params = valueProvider.getParameters() != null ?
                    valueProvider.getParameters() : Collections.<String, Object>emptyMap();

            final Window window = frame.openEditor(windowID, item, openType, params);
            window.addListener(new Window.CloseListener() {
                public void windowClosed(String actionId) {
                    if (Window.COMMIT_ACTION_ID.equals(actionId) && window instanceof Window.Editor) {
                        Object item = ((Window.Editor) window).getItem();
                        if (item instanceof Entity)
                            datasource.addItem((Entity) item);
                    }
                }
            });
        }
    }
}

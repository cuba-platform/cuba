/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.bali.datastruct.Tree;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManagerProvider;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.filter.addcondition.AddConditionWindow;
import com.haulmont.cuba.gui.components.filter.addcondition.ConditionDescriptorsTreeBuilder;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.descriptor.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.descriptor.CustomConditionCreator;
import com.haulmont.cuba.gui.components.filter.descriptor.DynamicAttributesConditionCreator;
import com.haulmont.cuba.gui.components.filter.edit.CustomConditionEditor;
import com.haulmont.cuba.gui.components.filter.edit.DynamicAttributesConditionEditor;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class that does a sequence of steps for adding new condition:
 * <ol>
 *     <li>opens add condition dialog</li>
 *     <li>if necessary opens new condition editor</li>
 *     <li>invokes a handler passing new condition to it</li>
 * </ol>
 * @author gorbunkov
 * @version $Id$
 */
public class AddConditionHelper {

    public static final int PROPERTIES_HIERARCHY_DEPTH = 2;

    protected WindowManager windowManager;
    protected WindowConfig windowConfig;
    protected Filter filter;
    protected Handler handler;
    protected Tree<AbstractConditionDescriptor> descriptorsTree;

    public AddConditionHelper(Filter filter, Handler handler) {
        this.filter = filter;
        this.handler = handler;
        windowManager = AppBeans.get(WindowManagerProvider.class).get();
        windowConfig = AppBeans.get(WindowConfig.class);
    }

    public interface Handler {
        void handle(AbstractCondition condition);
    }

    /**
     * Opens AddCondition window. When condition is selected/created a {@code Handler#handle} method
     * will be called
     * @param conditionsTree conditions tree is necessary for custom condition editing. It is used
     *                       for suggestion of other component names in 'param where' field.
     */
    public void addCondition(final ConditionsTree conditionsTree) {
        Map<String, Object> params = new HashMap<>();
        if (descriptorsTree == null) {
            descriptorsTree = new ConditionDescriptorsTreeBuilder(filter, PROPERTIES_HIERARCHY_DEPTH).build();
        }
        params.put("descriptorsTree", descriptorsTree);
        WindowInfo windowInfo = windowConfig.getWindowInfo("addCondition");
        final AddConditionWindow window = windowManager.openWindow(windowInfo, WindowManager.OpenType.DIALOG, params);
        window.addListener(new Window.CloseListener() {
            @Override
            public void windowClosed(String actionId) {
                if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                    Collection<AbstractConditionDescriptor> descriptors = window.getDescriptors();
                    if (descriptors != null) {
                        for (AbstractConditionDescriptor descriptor : descriptors) {
                            _addCondition(descriptor, conditionsTree);
                        }
                    }
                }
            }
        });
    }

    protected void _addCondition(AbstractConditionDescriptor descriptor, ConditionsTree conditionsTree) {
        final AbstractCondition condition = descriptor.createCondition();

        if (descriptor instanceof CustomConditionCreator) {
            WindowInfo windowInfo = windowConfig.getWindowInfo("customConditionEditor");
            Map<String, Object> params = new HashMap<>();
            params.put("condition", condition);
            params.put("conditionsTree", conditionsTree);
            final CustomConditionEditor window = windowManager.openWindow(windowInfo, WindowManager.OpenType.DIALOG, params);
            window.addListener(new Window.CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                        handler.handle(condition);
                    }
                }
            });
        } else if (descriptor instanceof DynamicAttributesConditionCreator) {
            WindowInfo windowInfo = windowConfig.getWindowInfo("dynamicAttributesConditionEditor");
            Map<String, Object> params = new HashMap<>();
            params.put("condition", condition);
            final DynamicAttributesConditionEditor window = windowManager.openWindow(windowInfo, WindowManager.OpenType.DIALOG, params);
            window.addListener(new Window.CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                        handler.handle(condition);
                    }
                }
            });
        } else {
            handler.handle(condition);
        }
    }
}

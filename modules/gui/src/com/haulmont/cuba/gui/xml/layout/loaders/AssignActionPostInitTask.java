/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;

/**
 * @author abramov
 * @version $Id$
 */
public class AssignActionPostInitTask implements ComponentLoader.PostInitTask {

    protected Component.ActionOwner component;
    protected String actionName;
    private Frame frame;

    public AssignActionPostInitTask(Component.ActionOwner component, String actionName, Frame frame) {
        this.component = component;
        this.actionName = actionName;
        this.frame = frame;
    }

    @Override
    public void execute(ComponentLoader.Context context, Frame window) {
        String[] elements = ValuePathHelper.parse(actionName);
        if (elements.length > 1) {
            final String id = elements[elements.length - 1];
            String[] subPath = (String[]) ArrayUtils.subarray(elements, 0, elements.length - 1);

            // using this.frame to look up the component inside the actual frame
            Component holder = this.frame.getComponent(ValuePathHelper.format(subPath));
            if (holder == null) {
                throw new GuiDevelopmentException(
                        "Can't find component: " + Arrays.toString(subPath) + " for action: " + actionName,
                        context.getFullFrameId(), "Component ID", Arrays.toString(subPath));
            }

            if (!(holder instanceof Component.ActionsHolder)) {
                throw new GuiDevelopmentException(String.format(
                        "Component '%s' can't contain actions", holder.getId()), context.getFullFrameId(),
                        "Holder ID", holder.getId());
            }

            Action action = ((Component.ActionsHolder) holder).getAction(id);
            if (action == null) {
                throw new GuiDevelopmentException(String.format(
                        "Can't find action '%s' in '%s'", id, holder.getId()), context.getFullFrameId(),
                        "Holder ID", holder.getId());
            }

            this.component.setAction(action);
        } else if (elements.length == 1) {
            final String id = elements[0];
            final Action action = window.getAction(id);

            if (action == null) {
                String message = "Can't find action " + id;
                if (Window.Editor.WINDOW_COMMIT.equals(id) || Window.Editor.WINDOW_COMMIT_AND_CLOSE.equals(id))
                    message += ". This may happen if you are opening an AbstractEditor-based screen by openWindow() method, " +
                            "for example from the main menu. Use openEditor() method or give the screen a name ended " +
                            "with '.edit' to open it as editor from the main menu.";
                throw new GuiDevelopmentException(message, context.getFullFrameId());
            }

            this.component.setAction(action);
        } else {
            throw new GuiDevelopmentException("Empty action name", context.getFullFrameId());
        }
    }
}
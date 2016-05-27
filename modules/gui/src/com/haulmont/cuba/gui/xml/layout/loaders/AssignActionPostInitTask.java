/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;

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
            Action action = getActionRecursively(frame, id);

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

    protected Action getActionRecursively(Frame frame, String actionId) {
        Action action = frame.getAction(actionId);
        if (action == null) {
            Frame parentFrame = frame.getFrame();
            if (parentFrame != frame) {
                return getActionRecursively(parentFrame, actionId);
            }
        }
        return action;
    }
}
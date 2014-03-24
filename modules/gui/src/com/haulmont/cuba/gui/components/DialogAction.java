/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;

public class DialogAction extends AbstractAction {

    public enum Type {
        OK("ok", "actions.Ok", "icons/ok.png"),
        CANCEL("cancel", "actions.Cancel", "icons/cancel.png"),
        YES("yes", "actions.Yes", "icons/ok.png"),
        NO("no", "actions.No", "icons/cancel.png"),
        CLOSE("close", "actions.Close", "");

        private String id;
        private String msgKey;
        private String icon;

        Type(String id, String msgKey, String icon) {
            this.id = id;
            this.msgKey = msgKey;
            this.icon = icon;
        }
    }

    private Type type;

    public DialogAction(Type type) {
        super(type.id);
        this.type = type;
    }

    @Override
    public String getCaption() {
        return AppBeans.get(Messages.class).getMainMessage(type.msgKey);
    }

    @Override
    public String getIcon() {
        return type.icon;
    }

    public Type getType() {
        return type;
    }

    public void actionPerform(Component component) {
    }
}

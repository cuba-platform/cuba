/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 15.10.2009 11:22:30
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;

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
        return MessageProvider.getMessage(AppConfig.getMessagesPack(), type.msgKey);
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

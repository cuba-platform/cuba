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
        OK("ok", "actions.Ok"),
        CANCEL("cancel", "actions.Cancel"),
        YES("yes", "actions.Yes"),
        NO("no", "actions.No"),
        CLOSE("close", "actions.Close");

        private String id;
        private String msgKey;

        Type(String id, String msgKey) {
            this.id = id;
            this.msgKey = msgKey;
        }
    }

    private Type type;

    public DialogAction(Type type) {
        super(type.id);
        this.type = type;
    }

    @Override
    public String getCaption() {
        return MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(), type.msgKey);
    }

    public void actionPerform(Component component) {
    }
}

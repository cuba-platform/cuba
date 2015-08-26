/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DialogAction extends AbstractAction {

    public enum Type {
        OK("ok", "actions.Ok", "actions.dialog.Ok.icon"),
        CANCEL("cancel", "actions.Cancel", "actions.dialog.Cancel.icon"),
        YES("yes", "actions.Yes", "actions.dialog.Yes.icon"),
        NO("no", "actions.No", "actions.dialog.No.icon"),
        CLOSE("close", "actions.Close", "actions.dialog.Close.icon");

        private String id;
        private String msgKey;
        private String iconMsgKey;

        Type(String id, String msgKey, String iconMsgKey) {
            this.id = id;
            this.msgKey = msgKey;
            this.iconMsgKey = iconMsgKey;
        }

        public String getId() {
            return id;
        }

        public String getMsgKey() {
            return msgKey;
        }

        public String getIconMsgKey() {
            return iconMsgKey;
        }
    }

    private Type type;

    public DialogAction(Type type) {
        super(type.id);
        this.type = type;
    }

    public DialogAction(Type type, boolean primary) {
        this(type);
        this.primary = primary;
    }

    public DialogAction(Type type, Status status) {
        this(type);

        this.primary = status == Status.PRIMARY;
    }

    @Override
    public String getCaption() {
        return messages.getMainMessage(type.msgKey);
    }

    @Override
    public String getIcon() {
        return messages.getMainMessage(type.iconMsgKey);
    }

    public Type getType() {
        return type;
    }

    @Override
    public void actionPerform(Component component) {
    }
}
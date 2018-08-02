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
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;

/**
 * Standard action for option dialogs.
 * <br>
 * You can use fluent API to create instances of DialogAction and assign handlers to them:
 * <pre>{@code
 *     showOptionDialog(
 *             "Select options",
 *             "Do you want to print all rows?",
 *             MessageType.CONFIRMATION,
 *             new Action[]{
 *                     new DialogAction(Type.YES).withHandler(event -> {
 *                         // add action logic here
 *                     }),
 *                     new DialogAction(Type.NO)
 *                             .withCaption("Print selected")
 *                             .withIcon("icons/print-selected.png")
 *                             .withHandler(event -> {
 *                         // add action logic here
 *                     }),
 *                     new DialogAction(Type.CANCEL)
 *             });
 * }</pre>
 *
 * @see Window#showOptionDialog(String, String, Frame.MessageType, Action[])
 */
public class DialogAction extends BaseAction {

    public enum Type {
        OK("ok", "actions.Ok", CubaIcon.DIALOG_OK),
        CANCEL("cancel", "actions.Cancel", CubaIcon.DIALOG_CANCEL),
        YES("yes", "actions.Yes", CubaIcon.DIALOG_YES),
        NO("no", "actions.No", CubaIcon.DIALOG_NO),
        CLOSE("close", "actions.Close", CubaIcon.DIALOG_CLOSE);

        private String id;
        private String msgKey;
        private Icons.Icon iconKey;

        Type(String id, String msgKey, Icons.Icon iconKey) {
            this.id = id;
            this.msgKey = msgKey;
            this.iconKey = iconKey;
        }

        public String getId() {
            return id;
        }

        public String getMsgKey() {
            return msgKey;
        }

        public Icons.Icon getIconKey() {
            return iconKey;
        }
    }

    protected Type type;

    public DialogAction(Type type) {
        super(type.id);
        this.type = type;
        this.caption = messages.getMainMessage(type.msgKey);

        this.icon = AppBeans.get(Icons.class)
                .get(type.iconKey);
    }

    public DialogAction(Type type, boolean primary) {
        this(type);
        this.primary = primary;
    }

    public DialogAction(Type type, Status status) {
        this(type);
        this.primary = status == Status.PRIMARY;
    }

    public Type getType() {
        return type;
    }
}
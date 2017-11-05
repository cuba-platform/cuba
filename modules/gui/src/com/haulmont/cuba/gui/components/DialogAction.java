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
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;

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
 *                             .withIcon("icons/print-seletcted.png")
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
        OK("ok", "actions.Ok", "actions.dialog.Ok.icon"),
        CANCEL("cancel", "actions.Cancel", "actions.dialog.Cancel.icon"),
        YES("yes", "actions.Yes", "actions.dialog.Yes.icon"),
        NO("no", "actions.No", "actions.dialog.No.icon"),
        CLOSE("close", "actions.Close", "actions.dialog.Close.icon");

        private String id;
        private String msgKey;
        private String iconKey;

        Type(String id, String msgKey, String iconKey) {
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

        public String getIconKey() {
            return iconKey;
        }
    }

    protected Type type;

    public DialogAction(Type type) {
        super(type.id);
        this.type = type;
        this.caption = messages.getMainMessage(type.msgKey);

        ThemeConstantsManager thCM = AppBeans.get(ThemeConstantsManager.NAME);
        this.icon = thCM.getThemeValue(type.iconKey);
    }

    public DialogAction(Type type, boolean primary) {
        this(type);
        this.primary = primary;;
    }

    public DialogAction(Type type, Status status) {
        this(type);
        this.primary = status == Status.PRIMARY;
    }

    public Type getType() {
        return type;
    }
}
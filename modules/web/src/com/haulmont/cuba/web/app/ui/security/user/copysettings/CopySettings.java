/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.app.ui.security.user.copysettings;

import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.app.UserSettingService;
import com.haulmont.cuba.security.entity.User;
import java.util.Map;
import java.util.Set;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class CopySettings extends AbstractWindow {

    private Datasource<User> usersDs;
    private Set<User> users;
    private Button copyBtn;

    public CopySettings(IFrame frame) {
        super(frame);
    }

    protected void init(Map<String, Object> params) {
        usersDs = getDsContext().get("users");
        users =(Set<User>) params.get("users");
        copyBtn = getComponent("copyBtn");
        copyBtn.setAction(new AbstractAction("deployBtn") {
            public void actionPerform(Component component) {
                if (usersDs.getItem()==null){
                    showNotification(
                            getMessage("selectUser"),NotificationType.HUMANIZED);
                }
                else{

                showOptionDialog(
                        getMessage("confirmCopy.title"),
                        getMessage("confirmCopy.msg"),
                        MessageType.CONFIRMATION,
                        new Action[]{
                                new DialogAction(DialogAction.Type.YES){
                                    public void actionPerform(Component component){
                                          copySettings();
                                    }
                                },
                                new DialogAction(DialogAction.Type.NO)
                        }
                );
                }
            }
        });

        Button cancelBtn = getComponent("cancelBtn");
        cancelBtn.setAction(new AbstractAction("cancelBtn") {
            public void actionPerform(Component component) {
                close("cancel");
            }
        });
    }

    private void copySettings(){
        UserSettingService settingsService = ServiceLocator.lookup(UserSettingService.NAME);
        User fromUser = usersDs.getItem();
        for (User user : users) {
            if (!user.equals(fromUser))
            settingsService.copySettings(fromUser, user);
        }
        close("ok");
    }

}

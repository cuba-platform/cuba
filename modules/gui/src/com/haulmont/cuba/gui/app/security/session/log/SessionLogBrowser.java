package com.haulmont.cuba.gui.app.security.session.log;

import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.DialogAction;

import javax.inject.Inject;
import java.util.Map;

public class SessionLogBrowser extends AbstractLookup {

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected Button enableBtn;

    @Override
    public void init(Map<String, Object> params) {
        enableBtn.setCaption(globalConfig.getUserSessionLogEnabled() ?
                getMessage("disableLogging") : getMessage("enableLogging"));
    }

    public void enableLogging() {
        if (globalConfig.getUserSessionLogEnabled()) {
            showOptionDialog(getMessage("dialogs.Confirmation"), getMessage("confirmDisable"), MessageType.CONFIRMATION,
                    new Action[] {
                            new DialogAction(DialogAction.Type.YES, true).withHandler(actionPerformedEvent -> {
                                globalConfig.setUserSessionLogEnabled(false);
                                enableBtn.setCaption(getMessage("enableLogging"));
                            }),
                            new DialogAction(DialogAction.Type.NO)
                    });
        } else {
            globalConfig.setUserSessionLogEnabled(true);
            enableBtn.setCaption(getMessage("disableLogging"));
        }
    }
}

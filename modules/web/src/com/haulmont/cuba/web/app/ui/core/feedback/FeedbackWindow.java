/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Vitaly Timofeev
 * Created: 29.06.2010 16:43:20
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.core.feedback;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.app.EmailService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.UserSessionClient;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.web.AppWindow;
import com.haulmont.cuba.web.app.UserSettingHelper;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

public class FeedbackWindow extends AbstractWindow {

    protected final String bugReportReason;
    protected final String featureRequestReason;
    protected final String otherReason;

    public FeedbackWindow(IFrame frame) {
        super(frame);
        bugReportReason = MessageProvider.getMessage(getClass(),  "bugReport");
        featureRequestReason = MessageProvider.getMessage(getClass(),  "featureRequest");
        otherReason = MessageProvider.getMessage(getClass(),  "other");
    }

    protected Boolean validateAndSend() {
        Boolean result = true;
        if (StringUtils.isBlank((String) ((TextField) getComponent("mainBody")).getValue())) {
            showNotification(MessageProvider.getMessage(getClass(),  "emptyBodyErr"), NotificationType.ERROR);
            result = false;
        }
        if (StringUtils.isBlank((String) ((LookupField) getComponent("reason")).getValue()) || (otherReason.equals((String) ((LookupField) getComponent("reason")).getValue()) && StringUtils.isBlank((String) ((TextField) getComponent("reasonFreeText")).getValue()))) {
            showNotification(MessageProvider.getMessage(getClass(),  "emptyReasonErr"), NotificationType.ERROR);
            result = false;
        }
        if (result) {
            try {
                EmailService emailService = ServiceLocator.lookup(EmailService.NAME);
                String infoHeader = "";
                infoHeader += (MessageProvider.getMessage(getClass(),  "supportEmail") + ".\n");
                infoHeader += (MessageProvider.getMessage(getClass(),  "systemID") + ": " + (ConfigProvider.getConfig(GlobalConfig.class).getSystemID() == null ? "none" : ConfigProvider.getConfig(GlobalConfig.class).getSystemID()) + "\n");
                infoHeader += (MessageProvider.getMessage(getClass(),  "userLogin") + ": " + (UserSessionClient.getUserSession().getUser().getLogin() == null ? "none" : UserSessionClient.getUserSession().getUser().getLogin()) + "\n");
                infoHeader += (MessageProvider.getMessage(getClass(),  "userEmail") + ": " + (UserSessionClient.getUserSession().getUser().getEmail() == null ? "none" : UserSessionClient.getUserSession().getUser().getEmail()) + "\n");
                infoHeader += (MessageProvider.getMessage(getClass(),  "userFirstName") + ": " + (UserSessionClient.getUserSession().getUser().getFirstName() == null ? "none" : UserSessionClient.getUserSession().getUser().getFirstName()) + "\n");
                infoHeader += (MessageProvider.getMessage(getClass(),  "userMiddleName") + ": " + (UserSessionClient.getUserSession().getUser().getMiddleName() == null ? "none" : UserSessionClient.getUserSession().getUser().getMiddleName()) + "\n");
                infoHeader += (MessageProvider.getMessage(getClass(),  "userLastName") + ": " + (UserSessionClient.getUserSession().getUser().getLastName() == null ? "none" : UserSessionClient.getUserSession().getUser().getLastName()) + "\n");
                infoHeader += (MessageProvider.getMessage(getClass(),  "timestamp") + ": " + (Datatypes.getInstance().get(Date.class).format(TimeProvider.currentTimestamp())) + "\n");
                infoHeader += (MessageProvider.getMessage(getClass(),  "reason") + ": " + (otherReason.equals((String) ((LookupField) getComponent("reason")).getValue()) ? (String) ((TextField) getComponent("reasonFreeText")).getValue() : (String) ((LookupField) getComponent("reason")).getValue()) + "\n");
                infoHeader += (MessageProvider.getMessage(getClass(),  "mailBody") + ": \n");
                infoHeader += ((String) ((TextField) getComponent("mainBody")).getValue());
                EmailInfo emailInfo = new EmailInfo(
                        ConfigProvider.getConfig(GlobalConfig.class).getSupportEmail(),
                        "[Feedback Form][" + ConfigProvider.getConfig(GlobalConfig.class).getSystemID() + "][" + UserSessionClient.getUserSession().getUser().getLogin() + "][" + Datatypes.getInstance().get(Date.class).format(TimeProvider.currentTimestamp()) + "] " + (otherReason.equals((String) ((LookupField) getComponent("reason")).getValue()) ? (String) ((TextField) getComponent("reasonFreeText")).getValue() : (String) ((LookupField) getComponent("reason")).getValue()),
                        null,
                        null,
                        null,
                        infoHeader
                        );
                emailService.sendEmail(emailInfo);
                showNotification(MessageProvider.getMessage(getClass(),  "emailSent"), NotificationType.HUMANIZED);
            } catch (Exception e) {
                showNotification(MessageProvider.getMessage(getClass(),  "emailSentErr"), NotificationType.ERROR);
                result = false;
            }
        }
        return result;
    }

    protected void init(Map<String, Object> params) {

        ((LookupField) getComponent("reason")).setOptionsList(Arrays.asList(bugReportReason, featureRequestReason, otherReason));
        getComponent("reasonFreeText").setVisible(false);
        ((LookupField) getComponent("reason")).addListener(new ValueListener<LookupField>() {
            public void valueChanged(LookupField source, String property, Object prevValue, Object value) {
                if (otherReason.equals((String) value)) {
                    getComponent("reasonFreeText").setVisible(true);
                } else {
                    getComponent("reasonFreeText").setVisible(false);
                }
            }
        });

        Button okBtn = getComponent("ok");
        okBtn.setAction(
                new AbstractAction("ok") {
                    public void actionPerform(Component component) {
                        if (validateAndSend()) {
                            close("ok");
                        }
                    }
                }
        );

        Button cancelBtn = getComponent("cancel");
        cancelBtn.setAction(
                new AbstractAction("cancel") {
                    public void actionPerform(Component component) {
                        close("cancel");
                    }
                }
        );

    }

}

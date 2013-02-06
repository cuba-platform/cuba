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
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.WebWindow;

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
        StringBuffer sb = new StringBuffer();
        TextField mainBody = (TextField) getComponent("mainBody");
        if (mainBody.isRequired() && mainBody.isVisible()) {
            try {
                mainBody.validate();
            } catch (ValidationException ve) {
                sb.append(ve.getMessage() + "<br>");
            }
        }
        LookupField reason = (LookupField) getComponent("reason");
        if (reason.isRequired() && reason.isVisible()) {
            try {
                reason.validate();
            } catch (ValidationException ve) {
                sb.append(ve.getMessage() + "<br>");
            }
        }
        TextField reasonFreeText = (TextField) getComponent("reasonFreeText");
        if (reasonFreeText.isRequired() && reasonFreeText.isVisible()) {
            try {
                reasonFreeText.validate();
            } catch (ValidationException ve) {
                sb.append(ve.getMessage() + "<br>");
            }
        }
        if(sb.length() > 0){
//            showNotification(MessageProvider.getMessage(WebWindow.class, "validationFail.caption"),
//                    MessageProvider.getMessage(WebWindow.class,  "validationFail") + "<br>" +
//                    sb.toString(), NotificationType.TRAY);
            showNotification(MessageProvider.getMessage(WebWindow.class, "validationFail.caption"),
                    sb.toString(), NotificationType.TRAY);
            result = false;
        }
        if (result) {
            try {
                WebConfig webConfig = ConfigProvider.getConfig(WebConfig.class);
                EmailService emailService = ServiceLocator.lookup(EmailService.NAME);
                String infoHeader = "";
                infoHeader += (MessageProvider.getMessage(getClass(),  "supportEmail") + ".\n");
                infoHeader += (MessageProvider.getMessage(getClass(),  "systemID") + ": " + (webConfig.getSystemID() == null ? "none" : webConfig.getSystemID()) + "\n");
                User user = UserSessionProvider.getUserSession().getUser();
                infoHeader += (MessageProvider.getMessage(getClass(),  "userLogin") + ": " + (user.getLogin() == null ? "none" : user.getLogin()) + "\n");
                infoHeader += (MessageProvider.getMessage(getClass(),  "userEmail") + ": " + (user.getEmail() == null ? "none" : user.getEmail()) + "\n");
                infoHeader += (MessageProvider.getMessage(getClass(),  "userFirstName") + ": " + (user.getFirstName() == null ? "none" : user.getFirstName()) + "\n");
                infoHeader += (MessageProvider.getMessage(getClass(),  "userMiddleName") + ": " + (user.getMiddleName() == null ? "none" : user.getMiddleName()) + "\n");
                infoHeader += (MessageProvider.getMessage(getClass(),  "userLastName") + ": " + (user.getLastName() == null ? "none" : user.getLastName()) + "\n");
                infoHeader += (MessageProvider.getMessage(getClass(),  "timestamp") + ": " + (Datatypes.get(Date.class).format(TimeProvider.currentTimestamp())) + "\n");
                infoHeader += (MessageProvider.getMessage(getClass(),  "reason") + ": "
                        + (otherReason.equals(((LookupField) getComponent("reason")).getValue())
                                ? (String) ((TextField) getComponent("reasonFreeText")).getValue()
                                : (String) ((LookupField) getComponent("reason")).getValue()) + "\n");
                infoHeader += (MessageProvider.getMessage(getClass(),  "mailBody") + ": \n");
                infoHeader += ((String) ((TextField) getComponent("mainBody")).getValue());
//                EmailInfo emailInfo = new EmailInfo(
//                        webConfig.getSupportEmail(),
//                        "[Feedback Form][" + webConfig.getSystemID() + "]["
//                                + user.getLogin() + "]["
//                                + Datatypes.get(Date.class).format(TimeProvider.currentTimestamp()) + "] "
//                                + (otherReason.equals(((LookupField) getComponent("reason")).getValue())
//                                    ? (String) ((TextField) getComponent("reasonFreeText")).getValue()
//                                    : (String) ((LookupField) getComponent("reason")).getValue()),
//                        infoHeader
//                        );
//                emailService.sendEmail(emailInfo);
                showNotification(MessageProvider.getMessage(getClass(),  "emailSent"), NotificationType.HUMANIZED);
            } catch (Exception e) {
                showNotification(MessageProvider.getMessage(getClass(),  "emailSentErr"), NotificationType.ERROR);
                result = false;
            }
        }
        return result;
    }

    public void init(Map<String, Object> params) {

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

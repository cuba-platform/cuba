/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.app.ui.core.feedback;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.app.EmailService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.WebWindow;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * @author timofeev
 * @version $Id$
 */
public class FeedbackWindow extends AbstractWindow {

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected Configuration configuration;

    @Inject
    protected EmailService emailService;

    @Inject
    protected TextArea mainBody;

    @Named("reason")
    protected LookupField reason;

    @Inject
    protected TextField reasonFreeText;

    @Inject
    protected Button okBtn;

    @Inject
    protected Button cancelBtn;

    protected String otherReason;

    protected Boolean validateAndSend() {
        Boolean result = true;
        StringBuilder sb = new StringBuilder();
        if (mainBody.isRequired() && mainBody.isVisible()) {
            try {
                mainBody.validate();
            } catch (ValidationException ve) {
                sb.append(ve.getMessage()).append("<br>");
            }
        }
        if (reason.isRequired() && reason.isVisible()) {
            try {
                reason.validate();
            } catch (ValidationException ve) {
                sb.append(ve.getMessage()).append("<br>");
            }
        }
        if (reasonFreeText.isRequired() && reasonFreeText.isVisible()) {
            try {
                reasonFreeText.validate();
            } catch (ValidationException ve) {
                sb.append(ve.getMessage()).append("<br>");
            }
        }
        if(sb.length() > 0){
            showNotification(messages.getMessage(WebWindow.class, "validationFail.caption"),
                    sb.toString(), NotificationType.TRAY);
            result = false;
        }
        if (result) {
            try {
                WebConfig webConfig = configuration.getConfig(WebConfig.class);
                String infoHeader = "";
                infoHeader += (getMessage("supportEmail") + ".\n");
                infoHeader += (getMessage("systemID") + ": " + (webConfig.getSystemID() == null ? "none" : webConfig.getSystemID()) + "\n");
                User user = userSessionSource.getUserSession().getUser();
                infoHeader += (getMessage("userLogin") + ": " + (user.getLogin() == null ? "none" : user.getLogin()) + "\n");
                infoHeader += (getMessage("userEmail") + ": " + (user.getEmail() == null ? "none" : user.getEmail()) + "\n");
                infoHeader += (getMessage("userFirstName") + ": " + (user.getFirstName() == null ? "none" : user.getFirstName()) + "\n");
                infoHeader += (getMessage("userMiddleName") + ": " + (user.getMiddleName() == null ? "none" : user.getMiddleName()) + "\n");
                infoHeader += (getMessage("userLastName") + ": " + (user.getLastName() == null ? "none" : user.getLastName()) + "\n");
                infoHeader += (getMessage("timestamp") + ": " + (Datatypes.getNN(Date.class).format(timeSource.currentTimestamp())) + "\n");
                infoHeader += (getMessage("reason") + ": "
                        + (otherReason.equals(reason.getValue())
                                ? reasonFreeText.getValue()
                                : reason.getValue()) + "\n");
                infoHeader += (getMessage("mailBody") + ": \n");
                infoHeader += mainBody.getValue();
                EmailInfo emailInfo = new EmailInfo(
                        webConfig.getSupportEmail(),
                        "[Feedback Form][" + webConfig.getSystemID() + "]["
                                + user.getLogin() + "]["
                                + Datatypes.getNN(Date.class).format(timeSource.currentTimestamp()) + "] "
                                + (otherReason.equals(reason.getValue())
                                    ? reasonFreeText.getValue()
                                    : reason.getValue()),
                        infoHeader
                        );
                emailService.sendEmail(emailInfo);
                showNotification(getMessage("emailSent"), NotificationType.HUMANIZED);
            } catch (Exception e) {
                showNotification(getMessage("emailSentErr"), NotificationType.ERROR);
                result = false;
            }
        }
        return result;
    }

    public void init(Map<String, Object> params) {
        otherReason = getMessage("other");
        reason.setOptionsList(Arrays.asList(getMessage("bugReport"), getMessage("featureRequest"), otherReason));
        reasonFreeText.setVisible(false);
        reason.addListener(new ValueListener<LookupField>() {
            public void valueChanged(LookupField source, String property, Object prevValue, Object value) {
                if (otherReason.equals(value)) {
                    reasonFreeText.setVisible(true);
                } else {
                    reasonFreeText.setVisible(false);
                }
            }
        });

        okBtn.setAction(
                new AbstractAction("ok") {
                    @Override
                    public void actionPerform(Component component) {
                        if (validateAndSend()) {
                            close("ok");
                        }
                    }
                }
        );

        cancelBtn.setAction(
                new AbstractAction("cancel") {
                    @Override
                    public void actionPerform(Component component) {
                        close("cancel");
                    }
                }
        );
    }
}
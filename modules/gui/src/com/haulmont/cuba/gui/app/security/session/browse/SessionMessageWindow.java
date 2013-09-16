/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.session.browse;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class SessionMessageWindow extends AbstractWindow {

    @Inject
    private TextArea messageField;

    @Inject
    private OptionsGroup whomOptionsGroup;

    @Inject
    private UserSessionService uss;

    @Inject
    protected Messages messages;

    private String result;

    private Set<UserSessionEntity> allSessions;
    private Set<UserSessionEntity> selectedSessions;

    private String TO_ALL;
    private String TO_SELECTED;

    @Override
    public void init(Map<String, Object> params) {
        getDialogParams().setWidth(500);

        TO_ALL = messages.getMessage(getClass(), "messageWindow.toAll");
        TO_SELECTED = messages.getMessage(getClass(), "messageWindow.toSelected");

        allSessions = (Set) params.get("allSessions");
        selectedSessions = (Set) params.get("selectedSessions");
        Objects.requireNonNull(allSessions, "allSessions window parameter is not set");
        Objects.requireNonNull(selectedSessions, "selectedSessions window parameter is not set");

        List<String> whomOptions = new ArrayList<>(2);
        if (!selectedSessions.isEmpty()) {
            whomOptions.add(TO_SELECTED);
        }
        whomOptions.add(TO_ALL);
        whomOptionsGroup.setOptionsList(whomOptions);
        whomOptionsGroup.setValue(whomOptions.get(0));
    }

    public void send() {
        String text = (String) messageField.getValue();
        Set<UserSessionEntity> sessions = TO_ALL.equals(whomOptionsGroup.getValue()) ? allSessions : selectedSessions;

        if (!sessions.isEmpty() && !StringUtils.isBlank(text)) {
            List<UUID> sessionIds = new ArrayList<>(sessions.size());
            for (UserSessionEntity session : sessions) {
                sessionIds.add(session.getId());
            }
            uss.postMessage(sessionIds, text);

            result = messages.formatMessage(getClass(), "messageWindow.report", sessions.size());
        }
        close();
    }

    public void close() {
        close(Window.CLOSE_ACTION_ID);
    }

    public String getResult() {
        return result;
    }
}

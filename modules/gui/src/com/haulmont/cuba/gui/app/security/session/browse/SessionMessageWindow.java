/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.session.browse;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.theme.Theme;
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
    protected TextArea messageField;

    @Inject
    protected OptionsGroup whomOptionsGroup;

    @Inject
    protected UserSessionService uss;

    @Inject
    protected Label sendToAllLabel;

    @Inject
    protected Theme theme;

    protected String result;

    protected Set<UserSessionEntity> allSessions;
    protected Set<UserSessionEntity> selectedSessions;

    protected String TO_ALL;
    protected String TO_SELECTED;

    @Override
    public void init(Map<String, Object> params) {
        getDialogParams().setWidth(theme.getInt("cuba.gui.SessionMessageWindow.width"));

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
        if (whomOptions.size() == 1) {
            whomOptionsGroup.setVisible(false);
            sendToAllLabel.setValue(TO_ALL);
            sendToAllLabel.setVisible(true);
        }
        whomOptionsGroup.setValue(whomOptions.get(0));
    }

    public void send() {
        String text = messageField.getValue();
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

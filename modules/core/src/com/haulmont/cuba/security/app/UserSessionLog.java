/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.security.app;

import com.haulmont.cuba.security.entity.SessionAction;
import com.haulmont.cuba.security.entity.SessionLogEntry;
import com.haulmont.cuba.security.global.UserSession;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * API for user sessions logging.
 */
public interface UserSessionLog {

    String NAME = "cuba_SessionHistory";

    /**
     * Create log entry for session with custom params.
     *
     * @param userSession log entry is created for this session
     * @param action      session action, like login, logout, etc
     * @param params      additional params for log entry, like client's address and other info.
     *                    Use id property of SessionParams entry to pass in params map.
     * @return created log entry or null if session is not logged
     * @see com.haulmont.cuba.security.global.SessionParams
     */
    @Nullable
    SessionLogEntry createSessionLogRecord(UserSession userSession, SessionAction action, Map<String, Object> params);

    /**
     * Create log entry for session with custom params.
     *
     * @param userSession        log entry is created for this session
     * @param action             session action, like login, logout, etc
     * @param substitutedSession session that was substituted by new session from {@code userSession} param
     * @param params             additional params for log entry, like client's address and other info.
     *                           Use id property of SessionParams entry to pass in params map
     * @return created log entry or null if session is not logged
     * @see com.haulmont.cuba.security.global.SessionParams
     */
    @Nullable
    SessionLogEntry createSessionLogRecord(UserSession userSession, SessionAction action, UserSession substitutedSession, Map<String, Object> params);

    /**
     * Updates params on provided session log record.
     *
     * @param userSession update log record for this session
     * @param action      last session action
     * @return null in case of session is not logged
     */
    @Nullable
    SessionLogEntry updateSessionLogRecord(UserSession userSession, SessionAction action);

    /**
     * Get latest session log record. Session could have multiple log records in case of user substitution.
     *
     * @param userSessionId id of user session
     * @return latest log record
     */
    SessionLogEntry getLastSessionLogRecord(UUID userSessionId);

    /**
     * Get all session log records. Session could have multiple log records in case of user substitution.
     *
     * @param userSessionId id of user session
     * @return list of log records sorted by <code>startedTs</code> asc
     */
    List<SessionLogEntry> getAllSessionLogRecords(UUID userSessionId);
}

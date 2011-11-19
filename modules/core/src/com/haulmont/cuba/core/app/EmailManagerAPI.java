/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.SendingMessage;

import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author ovchinnikov
 */
public interface EmailManagerAPI {
    String NAME = "cuba_EmailManager";

    List<SendingMessage> addEmailsToQueue(List<SendingMessage> sendingMessageList);

    void queueEmailsToSend();
}

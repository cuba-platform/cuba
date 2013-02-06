/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.SendingMessage;

import java.util.List;

/**
 * @author ovchinnikov
 * @version $Id$
 */
public interface EmailManagerAPI {

    String NAME = "cuba_EmailManager";

    List<SendingMessage> addEmailsToQueue(List<SendingMessage> sendingMessageList);

    /**
     * @return short message describing  how many emails were sent
     */
    String queueEmailsToSend();
}
